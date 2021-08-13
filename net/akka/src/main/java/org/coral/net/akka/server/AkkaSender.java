package org.coral.net.akka.server;

import akka.actor.ActorContext;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.pattern.AskableActorSelection;
import akka.util.Timeout;
import org.coral.net.akka.api.AppMessage;
import org.coral.net.akka.api.ITransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.runtime.AbstractFunction1;
import scala.util.Try;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class AkkaSender {
	private static final Logger LOGGER = LoggerFactory.getLogger(AkkaSender.class);

	private static final Timeout REPLY_TIMEOUT = Timeout.apply(5, TimeUnit.SECONDS);
	private static final Timeout RESOLVE_TIMEOUT = Timeout.apply(1, TimeUnit.SECONDS);

	private final ActorSystem actorSystem;

	public AkkaSender(ActorSystem akka) {
		this.actorSystem = akka;
	}

	protected ActorSelection select(AkkaNode node, AppMessage msg) {
		String remoteAddress = "akka.tcp://cluster@" + node.getAkkaAddr() + "/user/" + msg.getMethod();
		return this.actorSystem.actorSelection(remoteAddress);
	}


	protected ActorSelection select(String method) {
		return this.actorSystem.actorSelection(
				"akka://" + this.actorSystem.name() + "/user/" + method);
	}

	public ITransformer asker(Duration timeout) {
		return new Asker(timeout);
	}

	public ITransformer teller(ActorRef sender) {
		return new Teller(sender);
	}

	public ITransformer forwarder(ActorContext context) {
		return new Forwarder(context);
	}

	public ITransformer resolver(String sender) {
		return new Resolver(sender);
	}

	protected abstract class Transformer implements ITransformer {
		@Override
		public CompletableFuture<Object> send(AkkaNode node, String cluster, AppMessage msg) {
			return this.send(select(node, msg), msg);
		}

		protected abstract CompletableFuture<Object> send(ActorSelection actor, AppMessage msg);
	}

	protected class Asker extends Transformer {
		private final Timeout timeout;

		public Asker(Duration timeout) {
			this.timeout = timeout == null ? REPLY_TIMEOUT : Timeout.apply(timeout.toMillis(), TimeUnit.MILLISECONDS);
		}

		@Override
		protected CompletableFuture<Object> send(ActorSelection actor, AppMessage msg) {
			final CompletableFuture<Object> c = new CompletableFuture<>();
			new AskableActorSelection(actor).ask(msg, this.timeout)
					.onComplete(new AbstractFunction1<Try<Object>, Void>() {
						@Override
						public Void apply(Try<Object> arg) {
							if (arg.isSuccess()) {
								c.complete(arg.get());
							}
							if (arg.isFailure()) {
								c.completeExceptionally(arg.failed().get());
							}
							return null;
						}
					}, actorSystem.dispatcher());
			return c;
		}

	}

	protected class Teller extends Transformer {
		private final ActorRef sender;

		public Teller(ActorRef sender) {
			this.sender = sender;
		}

		@Override
		protected CompletableFuture<Object> send(ActorSelection actor, AppMessage msg) {
			actor.tell(msg, this.sender);
			return CompletableFuture.completedFuture(null);
		}
	}

	protected class Forwarder extends Transformer {
		private final ActorContext context;

		public Forwarder(ActorContext context) {
			this.context = context;
		}

		@Override
		protected CompletableFuture<Object> send(ActorSelection actor, AppMessage msg) {
			actor.forward(msg, this.context);
			return CompletableFuture.completedFuture(null);
		}
	}

	protected class Resolver extends Transformer {

		private final String method;

		public Resolver(String method) {
			this.method = method;
		}

		@Override
		protected CompletableFuture<Object> send(ActorSelection actor, AppMessage msg) {
			final CompletableFuture<Object> c = new CompletableFuture<>();
			select(this.method).resolveOne(RESOLVE_TIMEOUT)
					.onComplete(new AbstractFunction1<Try<ActorRef>, Void>() {
						@Override
						public Void apply(Try<ActorRef> arg) {
							if (arg.isSuccess()) {
								actor.tell(msg, arg.get());
								c.complete(null);
							}
							if (arg.isFailure()) {
								final Throwable e = arg.failed().get();
								LOGGER.debug("[cluster]Deployed? Cannot find actor for sender method: {}", method, e);
								c.completeExceptionally(e);
							}
							return null;
						}
					}, actorSystem.dispatcher());
			return c;
		}
	}


}

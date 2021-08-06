package org.coral.net.akka.config;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.ActorSystem.Settings;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.dispatch.*;
import akka.remote.*;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Option;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

public class AkkaProfiler {

	private static final Logger LOGGER = LoggerFactory.getLogger(AkkaProfiler.class);

	private AkkaProfiler() {
	}

	public static ActorSystem createSystem(String name, Config config) {

		return watch(ActorSystem.create(name, inject(config), AkkaProfiler.class.getClassLoader()));
	}

	public static Config inject(Config config) {
		return ConfigFactory.parseMap(new HashMap<String, Object>() {
			private static final long serialVersionUID = 1L;

			{
				put("akka.actor.default-dispatcher.executor", Executor.Configurator.class.getName());
				put("akka.actor.default-mailbox.mailbox-type", Mailbox.QueueUnbounded.class.getName());
			}
		}).withFallback(config);
	}

	public static ActorSystem watch(ActorSystem akka) {
		ActorRef ref = akka.actorOf(Props.create(Watcher.class), "remoting-profiler");
		akka.eventStream().subscribe(ref, RemotingLifecycleEvent.class);
		return akka;
	}

	public static class Watcher extends UntypedActor {
		private final Set<String> _conns;

		public Watcher() {
			this._conns = ConcurrentHashMap.newKeySet();
		}

		@Override
		public void onReceive(Object event) throws Exception {
			LOGGER.error("onReceive:{}", event);

			if (event instanceof AssociationEvent) {
				final AssociationEvent conn = (AssociationEvent) event;
				if (event instanceof AssociationErrorEvent) {
					//this._error.mark();
				} else if (conn.localAddress() != null && conn.remoteAddress() != null) {
					final String pair = conn.localAddress() + "-" + conn.remoteAddress();
					if (conn instanceof AssociatedEvent) {
						if (this._conns.add(pair)) {
							//this._count.inc();
						}
					} else if (conn instanceof DisassociatedEvent) {
						if (this._conns.remove(pair)) {
							//this._count.dec();
						}
					}
				}
			} else if (event instanceof RemotingErrorEvent) {
				//this._error.mark();
			}
		}
	}

	public static class Executor {
		private Executor() {
		}

		public static class Configurator extends DefaultExecutorServiceConfigurator {
			private static ExecutorServiceConfigurator fallback(Config config, DispatcherPrerequisites preq) {
				final String executor = config.getString("default-executor.fallback");
				switch (executor) {
					case "fork-join-executor":
						return new ForkJoinExecutorConfigurator(config.getConfig(executor), preq);
					case "thread-pool-executor":
						return new ThreadPoolExecutorConfigurator(config.getConfig(executor), preq);
					default:
						throw new RuntimeException("unkown executor: " + executor);
				}
			}

			public Configurator(Config config, DispatcherPrerequisites preq) {
				super(config, preq, fallback(config, preq));
			}

			@Override
			public ExecutorServiceFactory createExecutorServiceFactory(String id, ThreadFactory factory) {
				return new Factory(super.createExecutorServiceFactory(id, factory));
			}
		}

		public static class Factory implements ExecutorServiceFactory {
			private final ExecutorServiceFactory _factory;

			public Factory(ExecutorServiceFactory factory) {
				this._factory = factory;
			}

			@Override
			public ExecutorService createExecutorService() {
				return new Service(this._factory.createExecutorService());
			}
		}

		public static class Service extends AbstractExecutorService {
			private final ExecutorService _executor;
			//private final PerfCounter.Timer _timer;

			public Service(ExecutorService executor) {
				this._executor = executor;
				//	this._timer = PerfCounter.timer("akka", "executor", "running");
			}

			@Override
			public void execute(Runnable command) {
				this._executor.execute(command);
			}

			@Override
			public void shutdown() {
				this._executor.shutdown();
			}

			@Override
			public List<Runnable> shutdownNow() {
				return this._executor.shutdownNow();
			}

			@Override
			public boolean isShutdown() {
				return this._executor.isShutdown();
			}

			@Override
			public boolean isTerminated() {
				return this._executor.isTerminated();
			}

			@Override
			public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
				return this._executor.awaitTermination(timeout, unit);
			}
		}
	}

	public static class Mailbox {
		private Mailbox() {
		}

		public static class MBox implements MailboxType {
			private final MailboxType _mbox;

			public MBox(MailboxType mbox) {
				this._mbox = mbox;
			}

			@Override
			public MessageQueue create(Option<ActorRef> owner, Option<ActorSystem> system) {
				return new MQueue(this._mbox.create(owner, system));
			}
		}

		public static class MQueue implements MessageQueue {
			private final MessageQueue _queue;
//			private final Counter _counter;
//			private final Meter _enqueue;
//			private final Meter _dequeue;
//			private final Meter _failure;

			public MQueue(MessageQueue queue) {
				this._queue = queue;
//				this._counter = PerfCounter.counter("akka", "mailbox", "message");
//				this._enqueue = PerfCounter.meter("akka", "mailbox", "enqueue");
//				this._dequeue = PerfCounter.meter("akka", "mailbox", "dequeue");
//				this._failure = PerfCounter.meter("akka", "mailbox", "failure");
			}

			@Override
			public void cleanUp(ActorRef actor, MessageQueue dead) {
				if (dead instanceof MQueue) {
					dead = ((MQueue) dead)._queue;
				}
				this._queue.cleanUp(actor, dead);
			}

			@Override
			public Envelope dequeue() {
				Envelope msg = this._queue.dequeue();
				if (msg != null) {
//					this._counter.dec();
//					this._dequeue.mark();
				}
				return msg;
			}

			@Override
			public void enqueue(ActorRef actor, Envelope msg) {
				try {
					this._queue.enqueue(actor, msg);
//					this._counter.inc();
//					this._enqueue.mark();
				} catch (Exception e) {
					LOGGER.error("enqueue:{}", msg, e);
					throw e;
				}
			}

			@Override
			public boolean hasMessages() {
				return this._queue.hasMessages();
			}

			@Override
			public int numberOfMessages() {
				return this._queue.numberOfMessages();
			}
		}

		public static class QueueBounded extends MBox {
			public QueueBounded(Settings settings, Config config) {
				super(new BoundedMailbox(settings, config));
			}
		}

		public static class QueueUnbounded extends MBox {
			public QueueUnbounded(Settings settings, Config config) {
				super(new UnboundedMailbox(settings, config));
			}
		}

		public static class DequeBounded extends MBox {
			public DequeBounded(Settings settings, Config config) {
				super(new BoundedDequeBasedMailbox(settings, config));
			}
		}

		public static class DequeUnbounded extends MBox {
			public DequeUnbounded(Settings settings, Config config) {
				super(new UnboundedDequeBasedMailbox(settings, config));
			}
		}
	}
}

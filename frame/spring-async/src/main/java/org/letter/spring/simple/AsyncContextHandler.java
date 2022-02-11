package org.letter.spring.simple;

import javax.servlet.AsyncContext;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author wuhao
 * @createTime 2022-01-26 17:48:00
 */
public class AsyncContextHandler implements Runnable {
	private AsyncContext asyncContext = null;
	private String args = null;

	public AsyncContextHandler(AsyncContext asyncContext) {
		this.asyncContext = asyncContext;
	}

	@Override
	public void run(){
		try {
			Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(1000);
						System.out.println("内部线程 OK：" + Thread.currentThread().getName());
						asyncContext.getResponse().setCharacterEncoding("utf-8");
						asyncContext.getResponse().setContentType("text/html;charset=UTF-8");
						asyncContext.getResponse().getWriter().println("这是异步的请求返回");
						//异步请求完成通知
						//此时整个请求才完成
						asyncContext.complete();
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			});
			thread.start();

		} catch (Exception e) {
			System.out.println("异常：" + e);
		}

	}
}

package org.letter.spring.simple;

import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import java.io.IOException;

/**
 * @author wuhao
 * @createTime 2022-01-26 18:03:00
 */
public class AsyncListenerHandler implements AsyncListener {
	@Override
	public void onTimeout(AsyncEvent event) throws IOException {
		System.out.println("onTimeout...");
	}

	@Override
	public void onStartAsync(AsyncEvent event) throws IOException {
		System.out.println("onStartAsync...");
	}

	@Override
	public void onError(AsyncEvent event) throws IOException {
		System.out.println("onError：" + event.getThrowable());
	}

	@Override
	public void onComplete(AsyncEvent event) throws IOException {
		System.out.println("onComplete...");
	}
}

package org.coral.netty.threadlocal;

import io.netty.util.concurrent.FastThreadLocal;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author wuhao
 * @createTime 2021-08-31 17:24:00
 */
public class FastThreadLocalTest {
	public static ExecutorService  executorService = Executors.newFixedThreadPool(3);
	public static void main(String[] args) throws InterruptedException {
		FastThreadLocalTest fastThreadLocalTest = new FastThreadLocalTest();
		Thread thread1 = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					fastThreadLocalTest.testMultiThreadLocal();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		thread1.start();
		Thread.sleep(1000000);

//		Thread thread2 = new Thread(new Runnable() {
//			@Override
//			public void run() {
//				try {
//					fastThreadLocalTest.testSignalThreadLocal();
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
//		});
//		thread2.start();

//		Thread thread3 = new Thread(new Runnable() {
//			@Override
//			public void run() {
//				try {
//					fastThreadLocalTest.testSimpleThreadLocal();
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
//		});
//		thread3.start();
	}
	public void testMultiThreadLocal() throws InterruptedException {
		for (int i = 0; i < 100000; i++){
			executorService.execute(new Runnable() {
				@Override
				public void run() {
					testFastThreadLocal("multi");
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			});

		}
	}

	public void testSimpleThreadLocal() throws InterruptedException {
		for (int i = 0; i < 100000; i++){
			Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					testFastThreadLocal("multi");
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			});
			thread.start();

		}
	}
	public void testSignalThreadLocal() throws InterruptedException {
		for (int i = 0; i < 100000; i++){
			testFastThreadLocal("signal");
			Thread.sleep(100);
		}
	}
	public void testFastThreadLocal(String sign){
		FastThreadLocal fastThreadLocal = new FastThreadLocal();
		fastThreadLocal.set(sign);
	}
}

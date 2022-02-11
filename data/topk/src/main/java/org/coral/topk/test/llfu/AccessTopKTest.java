package org.coral.topk.test.llfu;

import java.util.List;
import java.util.Random;

/**
 * @author wuhao
 * @createTime 2022-02-11 18:30:00
 */
public class AccessTopKTest {
	public static void main(String[] args) {
		//testWithQueue();
		testNoQueue();
	}

	public static void testWithQueue(){
		AccessTopK topCount = new AccessTopK();
		topCount.init();
		long timeOld = System.currentTimeMillis();
		for (int i = 0; i < 1000000000; i++) {
			Random random = new Random();
			int count = random.nextInt(10);
			topCount.accessAsyn(i % 10 + "");
			if (count % 5 == 0) {
				topCount.accessAsyn((i % 5 + 5) + "");
			}
			if (i % 1000000 == 0){
				System.out.println("time use:" + (System.currentTimeMillis() - timeOld));
				timeOld = System.currentTimeMillis() ;
			}
		}

	}
	public static void testNoQueue(){
		AccessTopK topCount = new AccessTopK();
		topCount.init();
		long timeOld = System.currentTimeMillis();
		for (int i = 0; i < 1000000000; i++) {
			Random random = new Random();
			int count = random.nextInt(10);
			topCount.access(i % 10 + "");
			if (count % 5 == 0) {
				topCount.access((i % 5 + 5) + "");
			}
			if (i % 1000000 == 0){
				System.out.println("time use:" + (System.currentTimeMillis() - timeOld));
				timeOld = System.currentTimeMillis() ;
			}
		}


	}
}

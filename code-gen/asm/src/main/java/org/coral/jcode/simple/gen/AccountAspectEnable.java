package org.coral.jcode.simple.gen;

import org.coral.jcode.simple.aspectj.AccountDoWork;

/**
 * @author wuhao
 * @createTime 2021-07-26 14:57:00
 */
public class AccountAspectEnable {
	public static void main(String[] args) {
		System.out.println("AccountAspectEnable Start");
		long count = 1;
		doAccountAspectj(count);
		System.out.println("AccountAspectEnable End");
	}

	public static void doAccountAspectj(long count) {
		long times = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			AccountDoWork.pay();
		}
		System.out.println("doAccountAspectj:" + (System.currentTimeMillis() - times));
	}
}

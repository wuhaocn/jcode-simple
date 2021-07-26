package org.coral.jcode.simple.gen;

import org.coral.jcode.simple.aspectj.AccountDoB;

/**
 * @author wuhao
 * @createTime 2021-07-26 14:00:00
 */
public class AccountAspectDisable {
    public static void main(String[] args) {
		System.out.println("AccountAspectDisable Start");
        AccountDoB accountDoB = new AccountDoB();
        accountDoB.getAccount().pay(1);
        System.out.println("AccountAspectDisable End");
    }
}

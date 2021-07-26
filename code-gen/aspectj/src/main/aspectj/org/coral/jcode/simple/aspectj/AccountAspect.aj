package org.coral.jcode.simple.aspectj;

import org.coral.jcode.simple.bean.Account;

/**
 * @author wuhao
 * @createTime 2021-07-26 11:11:00
 */

public aspect AccountAspect {

    pointcut callPay(int amount, Account account):
            call(boolean org.coral.jcode.simple.bean.Account.pay(int)) && args(amount) && target(account);

    before(int amount, Account account): callPay(amount, account) {
        System.out.println("[AccountAspect]Before Pay Total: " + account.balance);
        System.out.println("[AccountAspect]Require Pay: " + amount);
    }

    boolean around(int amount, Account account): callPay(amount, account) {
        if (account.balance < amount) {
            System.out.println("[AccountAspect]not pay!");
            return false;
        }
        return proceed(amount, account);
    }

    after(int amount, Account balance): callPay(amount, balance) {
        System.out.println("[AccountAspect]After Pay Total：" + balance.balance);
    }

}
package org.coral.jcode.simple.aspectj;

import org.coral.jcode.simple.bean.Account;

/**
 * @author wuhao
 * @createTime 2021-07-26 10:47:00
 */
public class AccountDoWork {
    public static void pay() {
        Account account = new Account();
        account.pay(1);
    }
}

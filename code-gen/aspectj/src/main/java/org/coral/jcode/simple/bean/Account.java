package org.coral.jcode.simple.bean;

public class Account {

    public int balance = 20;

    public boolean pay(int amount) {
        if (balance < amount) {
            return false;
        }
        balance -= amount;
        return true;
    }
}
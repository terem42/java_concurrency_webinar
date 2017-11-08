package fr.terem.training.concurrency;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Account {

    private int balance;
    private int id;
    //private int failCounter;
    private final LongAdder failCounter = new LongAdder();


    private final Lock lock = new ReentrantLock();

    public Account(int id,int initialBalance) {
        this.id = id;
        this.balance = initialBalance;
    }

    public void deposit(final int amount) {
        balance += amount;
    }

    public void withdraw(final int amount) {
        balance -= amount;
    }

    public int getBalance() {
        return balance;
    }
    public int getId() {
        return id;
    }

    public Lock getLock() {
        return lock;
    }
    public void incFailedTransferCount() {
        failCounter.increment();
    }
    public long getFailCount() {
        return failCounter.sum();
    }



}

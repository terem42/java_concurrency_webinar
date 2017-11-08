package fr.terem.training.concurrency;

import java.util.concurrent.TimeUnit;

public class OperationsWithLocks {
    private static final int LOCK_WAIT_SEC = 5;

    public static void main(String[] args) throws InterruptedException {
        final Account acc1 = new Account(1, 1000);
        final Account acc2 = new Account(2, 1000);
        System.out.println("accounts created, transfer funds");

        new Thread(new Runnable() {
            public void run() {
                try {
                    transfer(acc1, acc2, 500);
                } catch (Exception e) {
                    System.out.println("Unhandled exception has occured while acc1 => acc2 transfer: " + e.getClass().getSimpleName());
                    e.printStackTrace();
                }
            }
        }).start();
        System.out.println("First thread started");

        try {
            transfer(acc2, acc1, 500);
        } catch (Exception w) {
            System.out.println("Unhandled exception has occured while acc2 => acc1 transfer");
            w.printStackTrace();
        }
        System.out.println("Program done");

    }

    static void transfer(Account acc1, Account acc2, int amount) throws InsufficientFundsException, AccountLockFailedException, InterruptedException {
        System.out.println("Trying to lock acc id=" + acc1.getId()+" Thread="+Thread.currentThread().getName());
        if (acc1.getLock().tryLock(LOCK_WAIT_SEC, TimeUnit.SECONDS)) {
            try {
                if (acc1.getBalance() < amount) throw new InsufficientFundsException();
                Thread.sleep(1000);
                System.out.println("Trying to lock acc id=" + acc2.getId()+" Thread="+Thread.currentThread().getName());
                if (acc2.getLock().tryLock(LOCK_WAIT_SEC, TimeUnit.SECONDS)) {
                    try {
                        System.out.println("locked both accounts, do the transfer");
                        acc1.withdraw(amount);
                        acc2.deposit(amount);
                    } finally {
                        acc2.getLock().unlock();
                        System.out.println("Unlocked acc id=" + acc2.getId()+" Thread="+Thread.currentThread().getName());
                    }
                } else {
                    //do some compensatory actions
                    acc2.incFailedTransferCount();
                    throw new AccountLockFailedException("error, failed to lock account id=" + acc2.getId() + " in " + LOCK_WAIT_SEC + " seconds"+" Thread="+Thread.currentThread().getName());
                }

            } finally {
                acc1.getLock().unlock();
                System.out.println("Unlocked acc id=" + acc1.getId()+" Thread="+Thread.currentThread().getName());
            }
        } else {
            //do some compensatory actions
            acc1.incFailedTransferCount();
            throw new AccountLockFailedException("error, failed to lock account id=" + acc1.getId() + " in " + LOCK_WAIT_SEC + " seconds"+" Thread="+Thread.currentThread().getName());
        }

    }

}

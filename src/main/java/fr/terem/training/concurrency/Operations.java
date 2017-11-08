package fr.terem.training.concurrency;

public class Operations {
    public static void main(String[] args) throws InterruptedException {
        final Account acc1 = new Account(1,1000);
        final Account acc2 = new Account(2,1000);
        System.out.println("accounts created, transfer funds");

        new Thread(new Runnable() {
            public void run() {
                try {
                    transfer(acc1, acc2, 500);
                } catch (Exception e){
                    System.out.println("Unhandled exception has occured while acc1 => acc2 transfer: "+ e.getMessage());
                }
            }
        }).start();
        System.out.println("First thread started");

        try {
            transfer(acc2, acc1, 500);
        } catch (Exception w){
            System.out.println("Unhandled exception has occured while acc2 => acc1 transfer");
        }
        System.out.println("Program done");

    }

    static void transfer(Account acc1, Account acc2, int amount) throws Exception {
        System.out.println("trying to lock "+acc1.getId());
        synchronized (acc1) {
            System.out.println("locked "+acc1.getId());
            if (acc1.getBalance() < amount) throw new Exception("insufficient funds");
            Thread.sleep(1000);
            System.out.println("trying to lock "+acc2.getId());
            synchronized (acc2) {
                System.out.println("locked "+acc2.getId());
                acc1.withdraw(amount);
                acc2.deposit(amount);
            }
        }

    }

}

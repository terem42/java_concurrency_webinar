package fr.terem.training.concurrency;

public class AccountLockFailedException extends Exception {
    AccountLockFailedException() {
        super();
    }
    AccountLockFailedException(String msg) {
        super(msg);
    }
}

package fr.terem.training.concurrency;

public class InsufficientFundsException extends Exception {
    InsufficientFundsException() {
        super();
    }
    InsufficientFundsException(String msg) {
        super(msg);
    }
}

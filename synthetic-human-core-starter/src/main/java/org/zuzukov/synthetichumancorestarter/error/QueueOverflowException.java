package org.zuzukov.synthetichumancorestarter.error;

public class QueueOverflowException extends RuntimeException {
    public QueueOverflowException(String message) {
        super(message);
    }
}

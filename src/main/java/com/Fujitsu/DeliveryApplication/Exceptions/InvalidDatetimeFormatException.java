package com.Fujitsu.DeliveryApplication.Exceptions;

public class InvalidDatetimeFormatException extends RuntimeException {
    public InvalidDatetimeFormatException(String message) {
        super(message);
    }
}

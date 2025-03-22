package com.Fujitsu.DeliveryApplication.Exceptions;

public class InvalidVehicleTypeException extends RuntimeException {
    public InvalidVehicleTypeException(String message) {
        super(message);
    }
}

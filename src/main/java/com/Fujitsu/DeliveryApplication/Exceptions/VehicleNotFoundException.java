package com.Fujitsu.DeliveryApplication.Exceptions;

public class VehicleNotFoundException extends RuntimeException{
    public VehicleNotFoundException(String message) {
        super(message);
    }
}

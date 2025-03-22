package com.Fujitsu.DeliveryApplication.Exceptions;

public class WeatherNotFoundException extends RuntimeException{
    public WeatherNotFoundException(String message) {
        super(message);
    }
}

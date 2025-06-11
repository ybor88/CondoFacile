package com.condofacile.service;

public interface SmsService {
    boolean sendSms(String toPhoneNumber, String message);
}

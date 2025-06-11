package com.condofacile.service;

public interface EmailService {
    boolean sendEmail(String to, String subject, String text);
}
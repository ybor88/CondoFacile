package com.condofacile.service;

public interface ResetService {
    boolean sendResetLink(String email, String link);
    boolean updatePassword(String email, String newPassword);
}
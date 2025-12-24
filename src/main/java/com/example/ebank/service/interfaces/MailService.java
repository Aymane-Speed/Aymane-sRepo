package com.example.ebank.service.interfaces;

public interface MailService {
    public void sendCredentials(String toEmail, String login, String rawPassword);

    }

package com.agh.librario.Mail;

public interface MailSender {
    void sendEmail(String to, String subject, String text);
}


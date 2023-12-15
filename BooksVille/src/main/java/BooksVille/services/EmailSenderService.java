package BooksVille.services;

import BooksVille.infrastructure.events.event.ForgotPasswordEvent;

public interface EmailSenderService {
    void sendNotificationEmail(String url, String email, String firstName, String subject, String description);
    void sendRegistrationEmailVerification(String url, String email, String firstName);
}

package BooksVille.infrastructure.events.publisher;

import BooksVille.infrastructure.events.event.CompleteRegistrationEvent;
import BooksVille.infrastructure.events.event.ForgotPasswordEvent;
import BooksVille.infrastructure.events.event.NotificationMailEvent;
import BooksVille.utils.AuthenticationUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventPublisher {
    private final ApplicationEventPublisher eventPublisher;

    public void completeRegistrationEventPublisher(String email, String firstName, HttpServletRequest request) {
        eventPublisher.publishEvent(new CompleteRegistrationEvent(email, firstName, AuthenticationUtils.applicationUrl(request)));
    }

    public void forgotPasswordEventPublisher(String email, HttpServletRequest request) {
        eventPublisher.publishEvent(new ForgotPasswordEvent(email, AuthenticationUtils.applicationUrl(request)));
    }

    public void notificationMailEventPublisher(String email, String firstName, String subject, String description, HttpServletRequest request) {
        eventPublisher.publishEvent(new NotificationMailEvent(email, firstName, subject, description, AuthenticationUtils.applicationUrl(request)));
    }
}

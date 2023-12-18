package BooksVille.infrastructure.events.listeners;

import BooksVille.infrastructure.events.event.ForgotPasswordEvent;
import BooksVille.infrastructure.security.JWTGenerator;
import BooksVille.services.EmailSenderService;
import BooksVille.utils.SecurityConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Async
@Component
@RequiredArgsConstructor
public class ForgotPasswordEventListener implements ApplicationListener<ForgotPasswordEvent> {
    private final EmailSenderService emailSenderService;
    private final JWTGenerator tokenProvider;

    @Override
    public void onApplicationEvent(ForgotPasswordEvent event) {
        // Create a verification token for the user
        String verificationToken = tokenProvider.generateSignUpVerificationToken(event.getEmail(), SecurityConstants.JWT_REFRESH_TOKEN_EXPIRATION);

        // Build the verification url to be sent to the user
        String url = "http://127.0.0.1:1123/reset-forgot-password?token=" + verificationToken;

        // Send the email to the user
        emailSenderService.sendForgotPasswordEmailVerification(url, event);

        log.info("Click the link to verify your email and change ur password : {}", url);
    }
}

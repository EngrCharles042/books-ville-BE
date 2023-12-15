package BooksVille.services.implementation;

import BooksVille.services.EmailSenderService;
import BooksVille.utils.HelperClass;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EmailSenderServiceImpl implements EmailSenderService {
    private final JavaMailSender mailSender;
    private final HelperClass helperClass;


    @Value("${spring.mail.username}")
    private String senderMail;

    @Override
    public void sendNotificationEmail(String url,
                                      String email,
                                      String firstName,
                                      String subject,
                                      String description) {

        String action = "Contact Us";
        String serviceProvider = "BooksVille Customer Service";

        helperClass.sendNotificationEmail(
                firstName,
                url,
                mailSender,
                senderMail,
                email,
                action,
                serviceProvider,
                subject,
                description
        );
    }

    @Override
    public void sendRegistrationEmailVerification(String url, String email, String firstName) {
        String action = "Verify Email";
        String serviceProvider = "BooksVille Registration Portal Service";
        String subject = "Email Verification";
        String description = "Please follow the link below to complete your registration.";

        helperClass.sendEmail(
                firstName,
                url,
                mailSender,
                senderMail,
                email,
                action,
                serviceProvider,
                subject,
                description
            );
    }
}

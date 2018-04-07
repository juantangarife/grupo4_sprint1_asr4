package services;

import classes.AttachmentWrapper;
import play.libs.mailer.Email;
import play.libs.mailer.MailerClient;
import javax.inject.Inject;

public class MailService {
    private final static String FROM_EMAIL = "jc.tangarife1927@uniandes.edu.co";

    @Inject
    private  MailerClient mailerClient;

    public void sendEmail(String subject, String[] recipients, String body, AttachmentWrapper[] attachments) {
        Email email = new Email()
                .setSubject(subject)
                .setFrom(FROM_EMAIL)
                .setBodyHtml(body);

        for(String recipient: recipients) {
            email.addTo(recipient);
        }

        for(AttachmentWrapper attachment: attachments){
            email.addAttachment(attachment.getName(), attachment.getData(), attachment.getMimeType());
        }
        mailerClient.send(email);
    }
}

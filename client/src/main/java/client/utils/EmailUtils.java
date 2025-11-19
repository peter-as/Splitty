package client.utils;

import commons.Participant;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.email.EmailPopulatingBuilder;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.api.mailer.config.TransportStrategy;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;


public class EmailUtils {
    String from;
    String password;
    String splitty;
    String host;

    /**
     * Constructor, reads credentials from file
     */
    public EmailUtils() {
        this(Paths.get("client/src/main/resources/client/email/config.txt"));
    }

    /**
     * Constructor with custom path for testing purposes
     * @param path The custom path
     */
    public EmailUtils(Path path) {
        try (BufferedReader br =
                     Files.newBufferedReader(
                             path)) {
            this.from = br.readLine().replace("email ", "");
            this.password = br.readLine().replace("password ", "");
            this.host = br.readLine().replace("host ", "");
            this.splitty = br.readLine().replace("splitty ", "");
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Send email with given credentials
     * @param recipients addresses of people who receive the email
     * @param subject subject of the email
     * @param body content of the email
     */
    private void emailFunction(List<String> recipients, String subject, String body) {
        EmailPopulatingBuilder builder = EmailBuilder.startingBlank();
        for (String s : recipients) {
            builder = builder.to("Recipient", s);
        }
        builder = builder.from("Splitty", from);
        builder = builder.ccWithFixedName("Sender", from);
        builder = builder.withSubject(subject);
        builder = builder.withPlainText(body);
        Email email = builder.buildEmail();
        Mailer mailer = MailerBuilder
                .withSMTPServer(host, 465, from, password)
                .withTransportStrategy(TransportStrategy.SMTPS)
                .buildMailer();
        mailer.sendMail(email);
    }

    /**
     * Send emails method
     * @param recipients List of emails for recipients
     * @param inviteCode Invite code to send
     */
    public void sendEmails(List<String> recipients, String inviteCode) {
        emailFunction(recipients, "Splitty invitation",
                "You are invited to join a Splitty event. You can join the event at "
                        + splitty + " with invite code " + inviteCode);
    }

    /**
     * Init email
     * @return true if credentials can be used, false otherwise
     */
    public boolean initEmail() {
        try {
            emailFunction(List.of(from), "Test email",
                    "The configured email works!");
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * Send reminder to a person to pay
     * @param p Participant
     * @param email Email of recipient
     * @param inviteCode Invite code of event
     */
    public void sendReminder(Participant p, String email, String inviteCode) {
        try {
            emailFunction(List.of(email), "Reminder of payment",
                    "Greetings,\nYou owe money to " + p.getName()
                            + ". You can open the Splitty app on " + splitty
                            + " on event with invite code " + inviteCode);
        } catch (Exception ex) {
            return;
        }
    }
}

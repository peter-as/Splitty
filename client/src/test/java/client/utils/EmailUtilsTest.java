package client.utils;

import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EmailUtilsTest {

    @Test
    void sendEmail() {
        EmailUtils eu = new EmailUtils(Paths.get("src/main/resources/client/email/config.txt"));
        assertTrue(eu.initEmail());
        eu.sendEmails(List.of("splitty47@gmail.com"), "code");
    }
}
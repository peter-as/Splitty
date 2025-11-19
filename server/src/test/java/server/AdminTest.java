package server;

import org.junit.jupiter.api.Test;
import server.scenes.AdminCtrl;

import java.io.ByteArrayOutputStream;

import static org.junit.jupiter.api.Assertions.*;

class AdminTest {

    @Test
    void checkPassword() {
        ByteArrayOutputStream sw = new ByteArrayOutputStream();
        AdminCtrl admin = new AdminCtrl(sw);
        String s = sw.toString();
        System.out.println(s);
        assertEquals(s.toString().substring(0,18),"Your password is: ");
        s = s.replace("\r","");
        s = s.replace("\n","");
        s = s.substring(18,28);
        assertSame(true,admin.checkPassword(s));
        assertNotSame(true,admin.checkPassword("asdfasdfji"));
    }
}
package server.scenes;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Random;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javax.inject.Inject;


public class AdminCtrl {

    private String password;

    @FXML
    private PasswordField loginField;

    private PrimaryCtrl pc;

    /**
     * Constructor, which generates and outputs password to console
     */
    public AdminCtrl(OutputStream output) {
        Random r = new Random();
        password = "";
        String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        for (int i = 0; i < 10; i++) {
            password += chars.charAt(r.nextInt(chars.length()));
        }
        PrintStream ps = new PrintStream(output);
        ps.println("Your password is: " + password);
    }

    /**
     * Empty constructor
     */
    @Inject
    public AdminCtrl(PrimaryCtrl pc) {
        this(System.out);
        this.pc = pc;
    }

    /**
     * Check password method
     * @param password password to be checked
     * @return true if the password is correct, false otherwise
     */
    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    /**
     * Enter method, for when password is entered
     */
    public void enter() {
        System.out.println("Login attempt");
        if (checkPassword(loginField.getText())) {
            pc.showLogin();
        } else {
            System.out.println("Failure :(");
        }
    }

    /**
     * Getter
     * @return admin password
     */
    public String getPassword() {
        return password;
    }
}

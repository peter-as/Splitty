package client.scenes;

import client.utils.EmailUtils;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import commons.Participant;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;


public class SendInvitesCtrl implements Initializable {

    @FXML
    private Label eventTitle;
    @FXML
    private Label inviteCode;
    @FXML
    private TextArea input;
    private String eventID;
    private ServerUtils server;
    private MainCtrl mainCtrl;
    private EmailUtils emailUtils;
    boolean isEmailConfigured;
    private Event event;
    @FXML
    private Label giveInvite;
    @FXML
    private Label invitePeople;
    @FXML
    private Button backB;
    @FXML
    private Button sendButton;

    /**
     * Constructor for Send invites
     * @param server The server for the overview
     * @param mainCtrl The main control
     */
    @Inject
    public SendInvitesCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * Initialize method for Send invites controller
     * @param location
     * The location used to resolve relative paths for the root object, or
     * {@code null} if the location is not known.
     *
     * @param resources
     * The resources used to localize the root object, or {@code null} if
     * the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.emailUtils = new EmailUtils();
        this.isEmailConfigured = emailUtils.initEmail();
    }

    /**
     * Send invites to emails given
     */
    public void sendInvites() {
        if (input.getText().isEmpty() || input.getText().isBlank()) {
            mainCtrl.showOverview(eventID);
            return;
        }
        String text = input.getText();
        String[] lines = text.split("\n", -1);
        List<String> emails = List.of(lines);
        try {
            emailUtils.sendEmails(emails, event.getInviteCode());
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Problem");
            alert.setContentText("One or more emails are invalid!");
            alert.showAndWait();
            return;
        }
        List<Participant> addition = new ArrayList<>();
        int counter = 100 * event.getPrtList().size();
        for (String s : emails) {
            Participant p = new Participant();
            p.setEmail(s);
            String name = "Participant" + counter;
            p.setName(name);
            counter += 10;
            server.addParticipant(p, eventID);
        }
        mainCtrl.showOverview(eventID);
    }

    /**
     * Set event id for the controller
     * @param eventID The id of the event
     */
    public void setEventID(String eventID) {
        this.eventID = eventID;
        event = server.getEvent(eventID);
        this.eventTitle.setText(event.getName());
        this.inviteCode.setText(event.getInviteCode());
    }

    /**
     * Getter
     * @return Is the email configured
     */
    public boolean isEmailConfigured() {
        return isEmailConfigured;
    }

    /**
     * Setter
     * @param emailUtils email utils
     */
    public void setEmailUtils(EmailUtils emailUtils) {
        this.emailUtils = emailUtils;
    }

    /**
     * Setter
     * @param event event
     */
    public void setEvent(Event event) {
        this.event = event;
    }

    /**
     * Getter
     * @return event title
     */
    public Label getEventTitle() {
        return eventTitle;
    }

    /**
     * Getter
     * @return invite code
     */
    public Label getInviteCode() {
        return inviteCode;
    }

    /**
     * Setter
     * @param eventTitle event title
     */
    public void setEventTitle(Label eventTitle) {
        this.eventTitle = eventTitle;
    }

    /**
     * Setter
     * @param inviteCode invite code
     */
    public void setInviteCode(Label inviteCode) {
        this.inviteCode = inviteCode;
    }

    /**
     * Setter
     * @param input input label
     */
    public void setInput(TextArea input) {
        this.input = input;
    }

    /**
     * Getter
     * @return event utils
     */
    public EmailUtils getEmailUtils() {
        return emailUtils;
    }

    /**
     * Sends the user to the overview
     */
    public void back() {
        mainCtrl.showOverview(eventID);
    }

    /**
     * Fills out the text on the scene with the corresponding strings
     */
    public void fillText(HashMap<String, String> languageMap) {
        giveInvite.setText(languageMap.get("SendInvitesCode"));
        invitePeople.setText(languageMap.get("SendInvitesEmails"));
        backB.setText(languageMap.get("BackButton"));
        sendButton.setText(languageMap.get("SendInvitesButton"));
    }
}

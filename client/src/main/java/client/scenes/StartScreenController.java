package client.scenes;

import client.LanguageNode;
import client.LanguageSwitcher;
import client.utils.ServerUtils;
import commons.Event;
import commons.Expense;
import commons.Participant;
import commons.Tag;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javax.inject.Inject;

public class StartScreenController {
    private List<String> recentEventNames;
    private List<Event> recentEvents;
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private Hyperlink hyp1;
    @FXML
    private Hyperlink hyp2;
    @FXML
    private Hyperlink hyp3;
    @FXML
    private Hyperlink hyp4;
    @FXML
    private TextField eventCode;
    @FXML
    private TextField newEvent;
    @FXML
    private ComboBox<LanguageNode> cb;

    @FXML
    private Label createLabel;
    @FXML
    private Button createButton;
    @FXML
    private Label joinLabel;
    @FXML
    private Button joinButton;
    @FXML
    private Label recentLabel;


    /**
     * Constructor for the StartScreenController
     * @param server the server to use
     * @param ctrl the ctrl to use
     */
    @Inject
    public StartScreenController(ServerUtils server, MainCtrl ctrl) {
        this.server = server;
        this.mainCtrl = ctrl;
    }

    /**
     * Initializes the ComboBox
     */
    public void initialize() {
        fillEvents(Paths.get("client/src/main/resources/client/recentEvents.txt"));
    }

    /**
     * Creates an Event with a name
     */
    public void addEvent() {
        String eventName = newEvent.getText();
        if (eventName.isEmpty()) {
            return;
        }
        Event event = new Event(new Date(), new Date(),
                eventName, new ArrayList<Participant>(),
                new ArrayList<Expense>(), new ArrayList<Tag>());
        Event e = server.postEvent(event);
        newEvent.setText("");
        mainCtrl.confirmationPopUp(mainCtrl.getLanguages().get("EventCreated"));
        open(4, e);
    }

    /**
     * Checks if an event with that code exists, if it does, it opens its overview page
     */
    public void joinEvent() {
        String code = eventCode.getText();
        if (code.isEmpty()) {
            return;
        }

        List<Event> events = server.getEvents();
        for (Event e : events) {
            if (e.getInviteCode().equals(code)) {
                eventCode.setText("");
                open(4, e);
                return;
            }
        }
        mainCtrl.showAlert(mainCtrl.getLanguages().get("EventNotFound"));
    }

    /**
     * Opens an event's overview page, and rearranges the recent list
     * @param numb the number of the event on the list
     */
    public void open(int numb, Event e) {
        boolean isContained = false;
        for (Event event : recentEvents) {
            if (event != null && e.getId().equals(event.getId())) {
                isContained = true;
                break;
            }
        }
        if (isContained) {
            for (int i = recentEvents.indexOf(e) - 1; i >= 0; i--) {
                recentEvents.set(i + 1, recentEvents.get(i));
                recentEventNames.set(i + 1, recentEventNames.get(i));
            }
        } else {
            for (int i = numb - 1; i >= 0; i--) {
                recentEvents.set(i + 1, recentEvents.get(i));
                recentEventNames.set(i + 1, recentEventNames.get(i));
            }
        }
        recentEventNames.set(0, e.getName());
        recentEvents.set(0, e);
        writeEvents();
        mainCtrl.showOverview(Long.toString(recentEvents.get(0).getId()));

    }

    /**
     * Writes the recent events to recentEvents.txt
     */
    public void writeEvents() {

        try (FileWriter writer =
                     new FileWriter("client/src/main/resources/client/recentEvents.txt")) {
            for (int i = 0; i < 4; i++) {
                if (recentEvents.get(i) == null) {
                    writer.close();
                    break;
                }
                String text = recentEvents.get(i).getId() + "\n";
                writer.write(text);
            }
            writer.close();
        } catch (Exception e) {
            System.out.println("Writing error");
        }
        displayEvents();
    }

    /**
     * Writes all the recent events to recentEvents.txt
     */
    public void fillEvents(Path path) {
        recentEvents = new ArrayList<>();
        recentEventNames = new ArrayList<>();
        int i = 0;
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            for (; i < 4; i++) {
                String eventID = reader.readLine();
                Event e = server.getEvent(eventID);
                recentEvents.add(e);
                recentEventNames.add(e.getName());
            }
        } catch (Exception e) {
            System.out.println("Reading Error");
        }
        for (; i < 5; i++) {
            recentEvents.add(null);
            recentEventNames.add("");
        }
        displayEvents();
    }

    /**
     * Displays all the recent events under the recentEvents label
     */
    public void displayEvents() {
        hyp1.setText(recentEventNames.get(0));
        hyp2.setText(recentEventNames.get(1));
        hyp3.setText(recentEventNames.get(2));
        hyp4.setText(recentEventNames.get(3));
    }

    /**
     * Calls the open method from the first recently viewed event
     */
    public void openFirst() {
        hyp1.setVisited(false);
        open(0, recentEvents.get(0));
    }

    /**
     * Calls the open method from the second recently viewed event
     */
    public void openSec() {
        hyp2.setVisited(false);
        open(1, recentEvents.get(1));
    }

    /**
     * Calls the open method from the third recently viewed event
     */
    public void openThird() {
        hyp3.setVisited(false);
        open(2, recentEvents.get(2));
    }

    /**
     * Calls the open method from the fourth recently viewed event
     */
    public void openFourth() {
        hyp4.setVisited(false);
        open(3, recentEvents.get(3));
    }


    /**
     * Changes all the text on the page to match the selected language
     */
    public void fillText(HashMap<String, String> languageMap) {
        createLabel.setText(languageMap.get("StartCreate"));
        createButton.setText(languageMap.get("StartCreateButton"));
        joinLabel.setText(languageMap.get("StartJoin"));
        joinButton.setText(languageMap.get("StartJoinButton"));
        recentLabel.setText(languageMap.get("StartRecentEvents"));
    }

    /**
     * Getter for the combobox of the startscreen
     * @return the combobox
     */
    public ComboBox<LanguageNode> getComboBox() {
        return cb;
    }

    /**
     * Sets the text of the create textfield (for testing)
     * @param text the text to be set to
     */
    public void setCreateText(String text) {
        newEvent = new TextField(text);
        recentEvents = new ArrayList<>();
        recentEventNames = new ArrayList<>();
        hyp1 = new Hyperlink();
        hyp2 = new Hyperlink();
        hyp3 = new Hyperlink();
        hyp4 = new Hyperlink();
        for (int i = 0; i < 5; i++) {
            recentEvents.add(null);
            recentEventNames.add("");
        }
    }

    /**
     * Sets the text of the join textfield (for testing)
     * @param text the text to be set to
     */
    public void setJoinText(String text) {
        eventCode = new TextField(text);
    }

    /**
     * Sets the hyperlinks for an event (for testing)
     * @param text the id of the event
     */
    public void setHyperLinks(String text) {
        hyp1 = new Hyperlink(text);
        hyp2 = new Hyperlink(text);
        hyp3 = new Hyperlink(text);
        hyp4 = new Hyperlink(text);
        recentEvents = new ArrayList<>();
        recentEventNames = new ArrayList<>();
        Event e = server.getEvent(text);
        for (int i = 0; i < 5; i++) {
            recentEvents.add(e);
            recentEventNames.add(text);
        }

    }
}

package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import commons.Participant;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.checkerframework.checker.units.qual.A;


public class EditRemoveParticipantCtrl implements Initializable {

    private Participant selectedParticipant;
    private AddParticipantCtrl addParticipantCtrl;
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private String eventID = "";
    private String pname = "";
    @FXML
    private TextField name = new TextField();
    @FXML
    private ObservableList<Participant> participantData;
    @FXML
    private ChoiceBox<String> participantList;
    @FXML
    private Label titleL;
    @FXML
    private Button proceedB;
    @FXML
    private Button cancelB;

    /**
     * Constructor for overview of participant add page
     * @param server The server for the overview
     * @param mainCtrl The main control
     */

    @Inject
    public EditRemoveParticipantCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * Set the event id for the event that is being shown.
     * @param eventID the event id.
     */
    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    /**
     * Set the selected participant
     * @param participant the selected participant
     */
    public void setSelectedParticipant(Participant participant) {
        this.selectedParticipant = participant;
    }

    /**
     * Create new FXML objects (for testing purposes only)
     */
    public void notNull() {
        addParticipantCtrl = new AddParticipantCtrl(server, mainCtrl);
        participantList = new ChoiceBox<>();
        Participant participant1 = new Participant("John", "j@gmail.com", "IBAN000", "BIC000");
        List<Participant> participants = new ArrayList<>();
        participants.add(participant1);
        participantData = FXCollections.observableList(participants);
    }

    /**
     * Method to initialize addParticipant instance
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
    }

    /**
     * Proceeds to the Add Participant page
     */
    public void proceed() {
        addParticipantCtrl.setSelectedParticipant(selectedParticipant);
        mainCtrl.showAddParticipantFilled(eventID, selectedParticipant);
    }

    /**
     * Refresh method that sets the participant data into the choice box
     */
    public void refresh() {
        var participants = server.getParticipants(eventID);
        participantData = FXCollections.observableList(participants);
        participantList.getItems().clear();
        for (Participant participant : participantData) {
            participantList.getItems().add(participant.getName());
        }
        participantList.getSelectionModel().selectedItemProperty()
                .addListener(((observable, oldValue, newValue) -> {
                    for (Participant participant : participantData) {
                        if (participant.getName().equals(participantList.getSelectionModel()
                                .getSelectedItem())) {
                            selectedParticipant = participant;
                        }
                    }
                }));
    }

    /**
     * Set the addParticipant control
     * @param addParticipantCtrl the addParticipantCtrl
     */
    public void setAddParticipantCtrl(AddParticipantCtrl addParticipantCtrl) {
        this.addParticipantCtrl = addParticipantCtrl;
    }

    /**
     * Cancel method, sends the user back to the Overview page of the event
     */
    public void cancel() {
        mainCtrl.showOverview(eventID);
    }

    /**
     * Fills out the text on the scene with the corresponding strings
     */
    public void fillText(HashMap<String, String> languageMap) {
        titleL.setText(languageMap.get("EditParticipantSelect"));
        proceedB.setText(languageMap.get("EditParticipantProceed"));
        cancelB.setText(languageMap.get("CancelButton"));
    }
}

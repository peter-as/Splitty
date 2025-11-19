package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Tag;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;


public class EditRemoveTagCtrl implements Initializable {

    private Tag selectedTag;
    private AddEditTagCtrl addEditTagCtrl;
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private String eventID = "";
    private ObservableList<Tag> tagData;
    @FXML
    private ChoiceBox<String> tagList;
    @FXML
    private Label tagSelect;
    @FXML
    private Button cancelB;
    @FXML
    private Button editB;

    /**
     * Constructor for overview of participant add page
     * @param server The server for the overview
     * @param mainCtrl The main control
     */

    @Inject
    public EditRemoveTagCtrl(ServerUtils server, MainCtrl mainCtrl) {
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
        mainCtrl.showAddTagFilled(eventID, selectedTag);
        addEditTagCtrl.setSelectedTag(selectedTag);
    }

    /**
     * Refresh method that sets the participant data into the choice box
     */
    public void refresh() {
        var tags = server.getTags(eventID);
        tagData = FXCollections.observableList(tags);
        tagList.getItems().clear();
        for (Tag tag : tagData) {
            tagList.getItems().add(tag.getName());
        }
        tagList.getSelectionModel().selectedItemProperty()
                .addListener(((observable, oldValue, newValue) -> {
                    for (Tag tag : tagData) {
                        if (tag.getName().equals(tagList.getSelectionModel()
                                .getSelectedItem())) {
                            selectedTag = tag;
                        }
                    }
                }));
    }

    /**
     * Set the addParticipant control
     * @param addEditTagCtrl the addEditTagCtrl
     */
    public void setAddTagCtrl(AddEditTagCtrl addEditTagCtrl) {
        this.addEditTagCtrl = addEditTagCtrl;
    }

    /**
     * Cancel method, sends the user back to the Overview page of the event
     */
    public void cancel() {
        mainCtrl.showAddExpense(eventID);
    }

    /**
     * Fills out the text on the scene with the corresponding strings
     */
    public void fillText(HashMap<String, String> languageMap) {
        tagSelect.setText(languageMap.get("EditTagSelectTag"));
        cancelB.setText(languageMap.get("CancelButton"));
        editB.setText(languageMap.get("ParticipantEdit"));
    }
}

package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Participant;
import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javax.swing.*;


public class AddParticipantCtrl implements Initializable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private String eventID = "";
    private Participant selectedParticipant;

    @FXML
    private Label title;
    @FXML
    private TextField name = new TextField();

    @FXML
    private TextField email = new TextField();

    @FXML
    private TextField iban = new TextField();

    @FXML
    private TextField bic = new TextField();
    @FXML
    private Button removeB;
    @FXML
    private Button add;
    @FXML
    private Label nameL;
    @FXML
    private Label emailL;
    @FXML
    private Label ibanL;
    @FXML
    private Label bicL;
    @FXML
    private Button cancelB;

    /**
     * Constructor for overview of participant add page
     * @param server The server for the overview
     * @param mainCtrl The main control
     */
    @Inject
    public AddParticipantCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * Get the label with the title of the page
     * @return the title label
     */
    public Label getTitle() {
        return title;
    }

    /**
     * Get the add button
     * @return the add button
     */
    public Button getAdd() {
        return add;
    }


    /**
     * Set the selected participant
     * @param selectedParticipant the selected participant
     */
    public void setSelectedParticipant(Participant selectedParticipant) {
        this.selectedParticipant = selectedParticipant;
    }

    /**
     * Set the event id for the event that is being shown.
     * @param eventID the event id.
     */
    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    /**
     * Set the text field to show the name of a participant
     * @param pname the name of a participant
     */
    public void setName(String pname) {

        name.setText(pname);
    }

    /**
     * Set the text field to show the email of a participant
     * @param pemail the email of a participant
     */
    public void setEmail(String pemail) {

        email.setText(pemail);
    }

    /**
     * Set the text field to show the IBAN of a participant
     * @param piban the iban of a participant
     */
    public void setIban(String piban) {

        iban.setText(piban);
    }

    /**
     * Set the text field to show the BIC of a participant
     * @param pbic the bic of a participant
     */
    public void setBic(String pbic) {

        bic.setText(pbic);
    }

    /**
     * Create new FXML objects (for testing purposes only)
     */
    public void notNull() {
        removeB = new Button();
        add = new Button();
        title = new Label();
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
     * Show remove button and change the title to "Edit/Remove"
     */
    public void showButton() {
        removeB.setVisible(true);
        title.setText(mainCtrl.getLanguages().get("EditParticipantTitle"));
        add.setOnAction(e -> edit());
        add.setText(mainCtrl.getLanguages().get("ParticipantEdit"));

    }

    /**
     * Show add button and change the title to "Add"
     */
    public void hideButton() {
        removeB.setVisible(false);
        title.setText(mainCtrl.getLanguages().get("AddParticipantTitle"));
        add.setOnAction(q -> add());
        add.setText(mainCtrl.getLanguages().get("AddButton"));
    }

    /**
     * Cancel method, sends the user back to the Overview page of the event
     */
    public void cancel() {
        clearFields();
        mainCtrl.showOverview(eventID);
    }

    /**
     * Add method for participant
     */
    public void add() {
        Participant pnew = getParticipant();
        if (pnew.getName().equals("")) {
            mainCtrl.showAlert(mainCtrl.getLanguages().get("ParticipantNameAlert"));
            return;
        }
        List<Participant> existing = server.getEvent(eventID).getPrtList();
        List<String> names = new ArrayList<>();
        for (Participant p : existing) {
            names.add(p.getName());
        }
        if (names.contains(pnew.getName())) {
            mainCtrl.showAlert(mainCtrl.getLanguages().get("ParticipantNameExists"));
            return;
        }
        server.addParticipant(pnew, eventID);

        clearFields();
        mainCtrl.confirmationPopUp(mainCtrl.getLanguages().get("ParticipantAdded"));
        mainCtrl.showOverview(eventID);
    }

    /**
     * Edit method for participant
     */
    @SuppressWarnings("checkstyle:VariableDeclarationUsageDistance")
    public void edit() {
        if (name.getText().equals("")) {
            mainCtrl.showAlert(mainCtrl.getLanguages().get("ParticipantNameAlert"));
            return;
        }
        String oldName = selectedParticipant.getName();
        selectedParticipant.setName(name.getText());
        selectedParticipant.setEmail(email.getText());
        selectedParticipant.setIban(iban.getText());
        selectedParticipant.setBic(bic.getText());
        selectedParticipant.setId(server.editParticipant(selectedParticipant).getId());

        clearFields();
        mainCtrl.confirmationPopUp(mainCtrl.getLanguages().get("ParticipantEdited"));
        mainCtrl.showOverview(eventID);
    }

    /**
     * Remove method for participant
     */
    public void remove() {
        if (selectedParticipant.getNetDebt() != 0) {
            mainCtrl.showAlert(mainCtrl.getLanguages().get("ParticipantDelete"));
            return;
        }
        java.util.List<Participant> participants = new ArrayList<>();
        participants.addAll(mainCtrl.getOverviewCtrl()
                .getExpenses().stream().map(x -> x.getWhoPaid()).toList());
        mainCtrl.getOverviewCtrl()
                .getExpenses().stream()
                .map(x -> x.getParticipants()).forEach(x -> participants.addAll(x));
        List<Long> parts = participants.stream().map(x -> x.getId()).toList();
        if (parts.contains(selectedParticipant.getId())) {
            mainCtrl.showAlert(mainCtrl.getLanguages()
                    .get(mainCtrl.getLanguages().get("ParticipantInvolved")));
            return;
        }
        server.removeParticipant(selectedParticipant, eventID);
        clearFields();
        mainCtrl.confirmationPopUp(mainCtrl.getLanguages().get("ParticipantDeleted"));
        mainCtrl.showOverview(eventID);
    }

    /**
     * Asks the user for confirmation before removing a participant
     */
    public void removeConf() {
        Frame frame = new JFrame();
        JPanel panel = new JPanel();
        LayoutManager layout = new FlowLayout();
        panel.setLayout(layout);
        frame.add(panel);

        int result = JOptionPane.showConfirmDialog(null,
                mainCtrl.getLanguages().get("DeleteConfirm"),
                "Delete confirmation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        if (result == JOptionPane.YES_OPTION) {
            remove();
        } else if (result == JOptionPane.NO_OPTION) {
            frame.dispose();
        }
    }

    /**
     * Participant fetch method
     * @return The new participant
     */
    private Participant getParticipant() {
        String pname = name.getText();
        String pemail = email.getText();
        String piban = iban.getText();
        String pbic = bic.getText();
        return new Participant(pname, pemail, piban, pbic);
    }

    /**
     * Participant clear fields
     */
    private void clearFields() {
        if (name != null) {
            name.clear();
        }
        if (email != null) {
            email.clear();
        }
        if (bic != null) {
            bic.clear();
        }
        if (iban != null) {
            iban.clear();
        }
    }

    /**
     * Fills out the text on the scene with the corresponding strings
     */
    public void fillText(HashMap<String, String> languageMap) {
        title.setText(languageMap.get("AddParticipantTitle"));
        nameL.setText(languageMap.get("AddParticipantName"));
        emailL.setText(languageMap.get("AddParticipantEmail"));
        ibanL.setText(languageMap.get("AddParticipantIBAN"));
        bicL.setText(languageMap.get("AddParticipantBIC"));
        add.setText(languageMap.get("AddButton"));
        cancelB.setText(languageMap.get("CancelButton"));
        removeB.setText(languageMap.get("RemoveButton"));
    }

}

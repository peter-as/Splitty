package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Expense;
import commons.Tag;
import java.awt.*;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javax.swing.*;


public class AddEditTagCtrl implements Initializable {

    @FXML
    private Label title;
    @FXML
    private TextField nameTextField;
    @FXML
    private ColorPicker colorPicker;
    @FXML
    private javafx.scene.control.Button addEdit;
    @FXML
    private Button remove;
    private ServerUtils server;
    private MainCtrl mainCtrl;
    private String eventId;
    private Tag selectedTag;
    @FXML
    private Label tagName;
    @FXML
    private Label tagColor;
    @FXML
    private Button backB;

    /**
     * Constructor for event overview
     *
     * @param server   The server for the overview
     * @param mainCtrl The main control
     */
    @Inject
    public AddEditTagCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * Getter for event id
     *
     * @return event id
     */
    public String getEventId() {
        return eventId;
    }


    /**
     * setter for event id
     *
     * @param eventId - event id
     */
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    /**
     * Fill the fields with the information of the tag you want to edit.
     * @param tag the tag of which to use the information.
     */
    public void fillFields(Tag tag) {
        this.nameTextField.setText(tag.getName());

        double r = (tag.getColor().getRed() / 255.0);
        double g = (tag.getColor().getGreen() / 255.0);
        double b = (tag.getColor().getBlue() / 255.0);
        double a = (tag.getColor().getAlpha() / 255.0);
        this.colorPicker.setValue(new Color(r, g, b, a));
    }

    /**
     * Set the selected tag.
     * @param tag The tag to set as selected tag.
     */
    public void setSelectedTag(Tag tag) {
        selectedTag = tag;
    }

    /**
     * Show remove button and change the title to "Edit/Remove"
     */
    public void showEdit() {
        remove.setVisible(true);
        title.setText(mainCtrl.getLanguages().get("EditTagTitle"));
        addEdit.setOnAction(e -> edit());
        addEdit.setText(mainCtrl.getLanguages().get("ParticipantEdit"));

    }

    /**
     * Show add button and change the title to "Add"
     */
    public void showAdd() {
        remove.setVisible(false);
        title.setText(mainCtrl.getLanguages().get("AddTagTitle"));
        addEdit.setOnAction(e -> add());
        addEdit.setText(mainCtrl.getLanguages().get("AddButton"));
    }


    /**
     * Initialize method
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  {@code null} if the location is not known.
     * @param resources The resources used to localize the root object, or {@code null} if
     *                  the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    /**
     * Gets input from user input fields
     *
     * @return a Tag object from the input, or null if filled improperly
     */
    public Tag getInput() {
        String name = nameTextField.getText();
        Color color = colorPicker.getValue();

        if (!validate(name, color)) {
            return null;
        }

        int r = (int) (color.getRed() * 255);
        int g = (int) (color.getGreen() * 255);
        int b = (int) (color.getBlue() * 255);
        int a = (int) (color.getOpacity() * 255);

        return new Tag(new java.awt.Color(r, g, b, a), name);
    }

    /**
     * Validates input that it is not null
     * @param name - name of tag
     * @param color - color of tag
     * @return boolean if valid input
     */
    public boolean validate(String name, Color color) {
        if (name.isEmpty() || color == null) {
            mainCtrl.showAlert(mainCtrl.getLanguages().get("FillFields"));
            return false;
        }
        //if server.getTags has the same name,
        // displayPopUp("Tag with name already exists!"), return false;
        return true;
    }


    /**
     * Method called when add button is pressed
     */
    public void add() {
        Tag tag = getInput();
        System.out.println(tag);
        if (tag != null) {
            server.addTag(tag, eventId);
            mainCtrl.confirmationPopUp(mainCtrl.getLanguages().get("TagAdded"));
            mainCtrl.showAddExpense(eventId);
        }
    }

    /**
     * Method to edit the selected Tag.
     */
    public void edit() {
        Tag tag = getInput();
        selectedTag.setColor(tag.getColor());
        selectedTag.setName(tag.getName());
        server.editTag(eventId, selectedTag);
        mainCtrl.confirmationPopUp(mainCtrl.getLanguages().get("TagEdited"));
        clearFields();
        mainCtrl.showAddExpense(eventId);
    }

    /**
     * Method to remove the selected Tag.
     */
    public void delete() {
        java.util.List<Long> tags = mainCtrl.getOverviewCtrl()
                .getExpenses().stream().map(x -> x.getTag().getId()).toList();
        if (tags.contains(selectedTag.getId())) {
            mainCtrl.showAlert(mainCtrl.getLanguages().get("TagRemoveError"));
            return;
        }
        server.removeTag(eventId, selectedTag);

        clearFields();
        mainCtrl.showOverview(eventId);
    }

    /**
     * Asks the user for confirmation before removing a tag
     */
    public void removeConf() {
        Frame frame = new JFrame();
        JPanel panel = new JPanel();
        LayoutManager layout = new FlowLayout();
        panel.setLayout(layout);
        frame.add(panel);

        int result = JOptionPane.showConfirmDialog(null,
                mainCtrl.getLanguages().get("DeletionConfirm"),
                "Delete confirmation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        if (result == JOptionPane.YES_OPTION) {
            delete();
        } else if (result == JOptionPane.NO_OPTION) {
            frame.dispose();
        }
    }


    /**
     * Mehotd called when back button pressed
     */
    public void back() {
        mainCtrl.showAddExpense(eventId);
    }

    private void clearFields() {
        if (nameTextField != null) {
            nameTextField.clear();
        }
        if (colorPicker != null) {
            colorPicker.setValue(new Color(0, 0, 0, 0));
        }
    }

    /**
     * Fills out the text on the scene with the corresponding strings
     */
    public void fillText(HashMap<String, String> languageMap) {
        tagName.setText(languageMap.get("AddTagName"));
        tagColor.setText(languageMap.get("AddTagColor"));
        backB.setText(languageMap.get("BackButton"));
        remove.setText(languageMap.get("RemoveButton"));
    }
}

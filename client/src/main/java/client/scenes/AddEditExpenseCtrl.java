package client.scenes;

import client.Exchange;
import client.utils.CommonUtils;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Expense;
import commons.Participant;
import commons.Tag;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.net.URL;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javax.swing.*;
import org.w3c.dom.Text;

public class AddEditExpenseCtrl implements Initializable {
    @FXML
    private Label title;
    @FXML
    private ListView<CheckBox> checkboxListView;
    private ObservableList<CheckBox> checkboxList;
    @FXML
    private ChoiceBox<Participant> whoPaidChoiceBox;
    @FXML
    private TextField whatForTextField;
    @FXML
    private TextField howMuchTextField;
    @FXML
    private ChoiceBox<String> currencyChoiceBox;
    @FXML
    private DatePicker whenDatePicker;
    @FXML
    private RadioButton equalSplitRadioButton;
    @FXML
    private RadioButton specialSplitRadioButton;
    @FXML
    private ToggleGroup toggleGroup;
    @FXML
    private Button addButton;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button addTagButton;
    @FXML
    private Button editTagButton;
    @FXML
    private ComboBox<Tag> tagComboBox;
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private String eventId;
    private List<Participant> participants;
    private String tempId;
    @FXML
    private Label whoPaid;
    @FXML
    private Label whatFor;
    @FXML
    private Label howMuch;
    @FXML
    private Label whenL;
    @FXML
    private Label expType;
    @FXML
    private Label splitHow;
    @FXML
    private Button cancelB;

    /**
     * Constructor for event overview
     *
     * @param server   The server for the overview
     * @param mainCtrl The main control
     */
    @Inject
    public AddEditExpenseCtrl(ServerUtils server, MainCtrl mainCtrl) {
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
     * Method to initialize addexpense instance
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  {@code null} if the location is not known.
     * @param resources The resources used to localize the root object, or {@code null} if
     *                  the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        currencyChoiceBox.setItems(FXCollections.observableList(
                List.of("EUR", "USD", "CHF")));
        currencyChoiceBox.getSelectionModel().selectFirst();

    }

    /**
     * resets information in necessary fields. refreshes page
     */
    public void refresh() {
        title.setText(mainCtrl.getLanguages().get("AddExpenseTitle"));

        addButton.setVisible(true);
        editButton.setVisible(false);
        deleteButton.setVisible(false);

        this.participants = server.getParticipants(eventId);

        setWhoPaidList();
        whoPaidChoiceBox.getSelectionModel().selectFirst();

        setTagsList();
        tagComboBox.getSelectionModel().selectFirst();

        checkboxList = FXCollections.observableArrayList();
        checkboxListView.setItems(checkboxList);
        checkboxListView.setVisible(false);

        setSplitList();
        appearSplitList();

    }

    /**
     * Sets drop down menu with participants of the events, but only displays their names
     */
    private void setWhoPaidList() {
        whoPaidChoiceBox.setItems(FXCollections.observableList(participants));

        //This makes sure I display the names of participants instead of their object pointers
        whoPaidChoiceBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Participant participant) {
                return participant != null ? participant.getName() : "";
            }

            @Override
            public Participant fromString(String string) {
                return null;
            }
        });

    }

    /**
     * Sets drop down menu with participants of the events, but only displays their names
     */
    private void setTagsList() {
        List<Tag> tags = server.getTags(eventId);
        tagComboBox.setItems(FXCollections.observableList(tags));

        //This makes sure I display the names of tags instead of their object pointers
        tagComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Tag tag) {
                return tag != null ? tag.getName() : "";
            }

            @Override
            public Tag fromString(String string) {
                return null;
            }
        });

    }

    /**
     * Sets list of participants in checkbox list for those who can be selected to pay back
     */
    private void setSplitList() {
        for (Participant participant : participants) {
            addCheckbox(participant.getName(), participant.getId().toString());
        }
    }

    /**
     * Adds one checkbox to the list of checkboxes
     *
     * @param label - the name of the person, will be displayed on the checkbox
     * @param id    - the id of the person, will be used to access the person
     */
    private void addCheckbox(String label, String id) {
        CheckBox checkbox = new CheckBox(label);
        checkbox.setId(id);
        checkboxList.add(checkbox);
    }

    /**
     * Makes the special split list appear when the correct radio button is selected
     */
    private void appearSplitList() {
        toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == specialSplitRadioButton) {
                checkboxListView.setVisible(true);
            } else if (newValue == equalSplitRadioButton) {
                checkboxListView.setVisible(false);
            }
        });
    }

    /**
     * get input method to receive all the input from all input fields
     *
     * @return and Expense object that will be added to the database
     */
    public Expense getInput() {

        try {
            whenDatePicker.getConverter().fromString(
                    whenDatePicker.getEditor().getText());
        } catch (Exception ex) {
            mainCtrl.showAlert(mainCtrl.getLanguages().get("DateFormat"));
            return null;
        }

        String whatFor = whatForTextField.getText();
        String howMuch = howMuchTextField.getText();
        LocalDate localDate = whenDatePicker.getValue();

        if (localDate == null) {
            mainCtrl.showAlert(mainCtrl.getLanguages().get("FillFields"));
            return null;
        }

        if (localDate.isAfter(LocalDate.now())) {
            mainCtrl.showAlert(mainCtrl.getLanguages().get("NoRates"));
            return null;
        }

        List<Participant> participantsOweing = new ArrayList<>();

        if (!validate(whatFor, howMuch, localDate)) {
            return null;
        }

        howMuch = howMuch.replace(".", "");
        int amtPaid = Integer.parseInt(howMuch);

        if (equalSplitRadioButton.isSelected()) {
            participantsOweing = participants;
        }
        if (specialSplitRadioButton.isSelected()) {
            participantsOweing = getSelectedPayers();
        }

        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        String currency = currencyChoiceBox.getValue();
        Tag tag = tagComboBox.getValue();
        Participant whoPaid = whoPaidChoiceBox.getValue();

        amtPaid = mainCtrl.getExchanger().exchangeCurrencies(currency, "USD",
                amtPaid, date);


        return new Expense(whatFor, date, tag, whoPaid, amtPaid, currency, participantsOweing);

    }

    /**
     * Gets participants selected in the special split selection
     *
     * @return a List of participants to pay the expense back
     */
    public List<Participant> getSelectedPayers() {
        List<Participant> result = new ArrayList<>();
        for (CheckBox checkbox : checkboxList) {
            if (checkbox.isSelected()) {
                result.add(getParticipant(Long.parseLong(checkbox.getId())));
            }
        }
        return result;
    }

    /**
     * Gets a participant from the participants list of the event using their id
     *
     * @param id - the id of the participant being sought after
     * @return the participant
     */
    public Participant getParticipant(Long id) {
        for (int i = 0; i < participants.size(); i++) {
            if (participants.get(i).getId().equals(id)) {
                return participants.get(i);
            }
        }
        return null;
    }


    /**
     * validates input
     *
     * @param whatFor   - expense title
     * @param howMuch   - expense cost
     * @param localDate - date
     * @return boolean if valid
     */
    public boolean validate(String whatFor, String howMuch, LocalDate localDate) {
        if (whatFor == null || whatFor.isEmpty() || localDate == null
                || howMuch == null || howMuch.isEmpty()
                || !(equalSplitRadioButton.isSelected() || specialSplitRadioButton.isSelected())
                || (specialSplitRadioButton.isSelected() && getSelectedPayers().isEmpty())
                || tagComboBox.getValue() == null) {
            mainCtrl.showAlert(mainCtrl.getLanguages().get("FillFields"));
            return false;
        }
        if (howMuch.length() < 4 || !howMuch.contains(".")
                || howMuch.indexOf(".") != howMuch.lastIndexOf(".")
                || howMuch.charAt(howMuch.length() - 3) != '.') {
            System.out.println(mainCtrl.getLanguages().get("CorrectFormat"));
            mainCtrl.showAlert(mainCtrl.getLanguages().get("MoneyFormat"));
            return false;
        }
        try {
            howMuch = howMuch.replace(".", "");
            //getting rid of cent marker so that it is hopefully an int in cents now
            int amt = Integer.parseInt(howMuch);
        } catch (NumberFormatException ex) {
            return false;
        }
        return true;
    }


    /**
     * Method to abort adding an expense.
     */
    public void abort() {
        clearFields();
        mainCtrl.showOverview(eventId);
    }

    /**
     * Method to add an expense, called on add button
     */
    public void add() {
        Expense expense = getInput();
        if (expense != null) {
            System.out.println(expense.getWhoPaid().getName());
            Expense exp = server.postExpense(expense, eventId);
            expense.setId(exp.getId());
            mainCtrl.confirmationPopUp(mainCtrl.getLanguages().get("ExpenseAdded"));
            clearFields();
            mainCtrl.showOverview(eventId);
        }
    }

    /**
     * Method to edit an expense, called on edit button
     */
    public void edit() {
        Expense expense = getInput();
        if (expense != null) {
            server.editExpense(expense, tempId, eventId);
            mainCtrl.confirmationPopUp(mainCtrl.getLanguages().get("ExpenseEdited"));
            clearFields();
            mainCtrl.showOverview(eventId);
        }
    }

    /**
     * Method to delete an expense, called on delete button
     */
    public void delete() {
        Expense expense = getInput();
        if (expense != null) {
            expense.setId(Long.parseLong(tempId));
            server.deleteExpense(expense, eventId);
            clearFields();
            mainCtrl.showOverview(eventId);
        }
    }

    /**
     * Asks the user for confirmation before removing an expense
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
            delete();
        } else if (result == JOptionPane.NO_OPTION) {
            frame.dispose();
        }
    }

    /**
     * Go to the selection screen of which tag to edit.
     */
    public void editTag() {
        mainCtrl.showEditRemoveTag(eventId);
    }

    /**
     * Method to add tag, called on add button
     */
    public void addTag() {
        mainCtrl.showAddTag(eventId);
    }

    /**
     * clears all fields
     */
    public void clearFields() {
        whoPaidChoiceBox.getSelectionModel().selectFirst();
        whatForTextField.setText("");
        howMuchTextField.setText("");
        currencyChoiceBox.getSelectionModel().selectFirst();
        whenDatePicker.setValue(null);
        toggleGroup.selectToggle(null);
        tagComboBox.getSelectionModel().selectFirst();

    }

    /**
     * Method to fill in all fields with the information of the expense being editted
     *
     * @param expense - the expense to be editted/deleted
     */
    public void editExpense(Expense expense) {
        title.setText(mainCtrl.getLanguages().get("EditExpense"));
        editButton.setVisible(true);
        deleteButton.setVisible(true);
        addButton.setVisible(false);

        tempId = Long.toString(expense.getId());
        String currentCurrency = CommonUtils.readCurrency(
                Paths.get("client/src/main/resources/client/config.txt"));
        whoPaidChoiceBox.setValue(expense.getWhoPaid());
        whatForTextField.setText(expense.getName());
        int cost = mainCtrl.getExchanger().exchangeCurrencies("USD", currentCurrency,
                expense.getAmountPaid(), expense.getDate());
        String amtPaid = CommonUtils.moneyToText(cost);
        howMuchTextField.setText(amtPaid);
        if (expense.getDate() != null) {
            whenDatePicker.setValue(expense.getDate().toInstant().atZone(ZoneId.systemDefault())
                    .toLocalDate());
        }
        currencyChoiceBox.setValue(currentCurrency);
        if (expense.getParticipants().equals(participants)) {
            equalSplitRadioButton.setSelected(true);
        } else {
            specialSplitRadioButton.setSelected(true);
            List<Long> participantIds = new ArrayList<>();
            for (Participant p : expense.getParticipants()) {
                participantIds.add(p.getId());
            }
            for (CheckBox checkbox : checkboxList) {
                if (participantIds.contains(Long.parseLong(checkbox.getId()))) {
                    checkbox.setSelected(true);
                }
            }
        }
    }

    /**
     * Fills out the text on the scene with the corresponding strings
     */
    public void fillText(HashMap<String, String> languageMap) {
        whoPaid.setText(languageMap.get("AddExpenseWhoPaid"));
        whatFor.setText(languageMap.get("AddExpenseWhatFor"));
        howMuch.setText(languageMap.get("AddExpenseHowMuch"));
        whenL.setText(languageMap.get("AddExpenseWhen"));
        expType.setText(languageMap.get("AddExpenseType"));
        splitHow.setText(languageMap.get("AddExpenseSplit"));
        equalSplitRadioButton.setText(languageMap.get("AddExpenseSplitEqual"));
        specialSplitRadioButton.setText(languageMap.get("AddExpenseSplitSome"));
        addTagButton.setText(languageMap.get("AddExpenseCreateTag"));
        editTagButton.setText(languageMap.get("AddExpenseEditTag"));
        cancelB.setText(languageMap.get("CancelButton"));
        addButton.setText(languageMap.get("AddButton"));
        deleteButton.setText(languageMap.get("EditExpenseDelete"));
        editButton.setText(languageMap.get("ParticipantEdit"));
    }

}

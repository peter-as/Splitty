package client.scenes;

import client.Exchange;
import client.LanguageNode;
import client.LanguageSwitcher;
import client.Main;
import client.utils.CommonUtils;
import client.utils.Refreshable;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import commons.Expense;
import commons.Participant;
import commons.Tag;
import java.awt.Color;
import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.nio.file.Paths;
import java.util.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;

public class OverviewCtrl implements Initializable, Refreshable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    private ObservableList<Participant> dataParticipants;
    private ObservableList<Expense> dataExpenses;
    private ObservableList<Tag> dataTags;

    private Participant selectedParticipant;
    private Tag selectedTag;

    @FXML
    private ChoiceBox<String> participantList;
    @FXML
    private ComboBox<Tag> tagList;
    @FXML
    private Accordion expenseAccordion;

    @FXML
    private FlowPane participantsPane;
    private String eventID;

    @FXML
    private ComboBox<LanguageNode> cb;

    @FXML
    private Button tagButton;
    @FXML
    private Button back;
    @FXML
    private Button sendInv;
    @FXML
    private Label participantLabel;
    @FXML
    private Label expenseLabel;
    @FXML
    private Button addExpenseButton;
    @FXML
    private Button allButton;
    @FXML
    private Button fromButton;
    @FXML
    private Button includingButton;
    @FXML
    private Button debtsButton;
    @FXML
    private ComboBox<String> currency;
    @FXML
    private Button eventTitleButton;
    @FXML
    private Label eventCodeLabel;
    @FXML
    private Button statisticsB;
    @FXML
    private Label getExpenseLabel;

    private Refreshable refreshable;

    /**
     * Constructor for event overview
     * @param server The server for the overview
     * @param mainCtrl The main control
     */
    @Inject
    public OverviewCtrl(ServerUtils server, MainCtrl mainCtrl) {
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
     * Method to initialize an overview instance.
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
        ArrayList<String> currencies = new ArrayList<>();
        currencies.add("EUR");
        currencies.add("USD");
        currencies.add("CHF");
        String currentCurrency = CommonUtils.readCurrency(
                Paths.get("client/src/main/resources/client/config.txt"));
        currency.getItems().addAll(currencies);
        currency.getSelectionModel().select(currencies.indexOf(currentCurrency));

        this.refreshable = this;
        server.registerForUpdates(this, e -> this.doRefresh(e));
    }

    /**
     * Method to send invites for the event.
     */
    public void sendInvites() {
        mainCtrl.showSendInvites(eventID);
    }

    /**
     * Method to go to the addExpense scene
     */
    public void addExpense() {
        mainCtrl.showAddExpense(eventID);
    }

    /**
     * Method to go to the addParticipant scene
     */
    public void addParticipant() {
        mainCtrl.showAddParticipant(eventID);
    }

    /**
     * Method to go to the editParticipant scene
     */
    public void editParticipant() {
        mainCtrl.showEditRemoveParticipant(eventID);
    }

    /**
     * Call the editExpense method from the mainCtrl to switch to editExpense scene.
     * @param expense the expense to edit.
     */
    public void editExpense(Expense expense) {
        mainCtrl.editExpense(eventID, expense);
    }


    /**
     * Method to get all expenses on the event.
     */
    public void getAllExpenses() {
        expenseAccordion.getPanes().clear();

        for (Expense expense : dataExpenses) {
            addAccordion(expense);
        }
    }

    /**
     * Method to get all the expenses from a certain participant on this event.
     */
    public void getFromExpenses() {
        expenseAccordion.getPanes().clear();

        for (Expense expense : dataExpenses) {
            if (expense.getWhoPaid().equals(selectedParticipant)) {
                addAccordion(expense);
            }
        }
    }

    /**
     * Method to get all the expenses including a certain participant on this event.
     */
    public void getIncludingExpenses() {
        expenseAccordion.getPanes().clear();

        for (Expense expense : dataExpenses) {
            var participants = expense.getParticipants();
            for (Participant p : participants) {
                if (p.equals(selectedParticipant)) {
                    addAccordion(expense);
                }
            }
        }
    }

    /**
     * Method to sort the expenses based on the tag they have.
     */
    public void getTagExpenses() {
        expenseAccordion.getPanes().clear();
        var tag = tagList.getValue();
        for (Expense expense : dataExpenses) {
            if (tag.equals(expense.getTag())) {
                addAccordion(expense);
            }
        }
    }

    private void addAccordion(Expense expense) {
        Map<String, String> languageMap = mainCtrl.getLanguages();
        String participants = "";
        int size = participantList.getItems().size();

        for (Participant participant : expense.getParticipants()) {
            if (size <= 1) {
                participants = languageMap.get("OverviewAll"); // text for all
                break;
            }
            participants = participants.concat(participant.getName() + " ");
            size--;
        }

        Button edit = new Button(languageMap.get("OverviewEdit")); // text for edit
        edit.setOnAction(e -> editExpense(expense));
        Label tag = new Label(expense.getTag().getName());

        double r = (expense.getTag().getColor().getRed() / 255.0);
        double g = (expense.getTag().getColor().getGreen() / 255.0);
        double b = (expense.getTag().getColor().getBlue() / 255.0);
        double a = (expense.getTag().getColor().getAlpha() / 255.0);

        javafx.scene.paint.Color color = new javafx.scene.paint.Color(r, g, b, a);
        tag.setBackground(new Background(new BackgroundFill(color, null, null)));

        int paid = expense.getAmountPaid();
        int inCurrency = mainCtrl.getExchanger().exchangeCurrencies("USD", currency.getValue(),
                paid, expense.getDate());
        String value = CommonUtils.moneyToText(inCurrency);

        HBox content = new HBox(new Label(expense.getWhoPaid().getName() + " "
                + languageMap.get("OverviewPaid") + " "
                + value + " " + currency.getValue() + " "
                + languageMap.get("OverviewFor") + " "
                + expense.getName() + "\n"
                + "(" + participants + ")"), tag, edit);

        content.setAlignment(Pos.TOP_LEFT);
        content.setSpacing(10);
        content.setPadding(new Insets(5));
        TitledPane pane = new TitledPane(expense.getDate() + " " + expense.getName(), content);
        expenseAccordion.getPanes().add(pane);
    }

    /**
     * Refreshes the overview
     */
    public void refresh() {
        if (eventID == null) {
            return;
        }
        var participants = server.getParticipants(eventID);
        var expenses = server.getExpenses(eventID);
        var allTags = server.getTags(eventID);

        dataParticipants = FXCollections.observableList(participants);
        dataExpenses = FXCollections.observableList(expenses);
        dataTags = FXCollections.observableList(allTags);

        tagList.getItems().clear();
        participantList.getItems().clear();
        participantsPane.getChildren().clear();

        tagList.setItems(dataTags);

        tagList.setConverter(new StringConverter<>() {
            @Override
            public String toString(Tag tag) {
                return tag != null ? tag.getName() : "";
            }

            @Override
            public Tag fromString(String string) {
                return null;
            }
        });

        for (Participant participant : dataParticipants) {
            participantList.getItems().add(participant.getName());
            Label label = new Label(participant.getName());
            participantsPane.getChildren().add(label);
        }

        participantList.getSelectionModel().selectedItemProperty()
                .addListener(((observable, oldValue, newValue) -> {
                    for (Participant participant : dataParticipants) {
                        if (participant.getName().equals(participantList.getSelectionModel()
                                .getSelectedItem())) {
                            selectedParticipant = participant;
                        }
                    }
                }));


        Event event = server.getEvent(eventID);
        eventTitleButton.setText(event.getName());
        eventCodeLabel.setText(mainCtrl.getLanguages().get("InviteCode") + event.getInviteCode());
        getAllExpenses();

        var tagsNames = new ArrayList<>();

        for (Tag tag : dataTags) {
            tagsNames.add(tag.getName());
        }

        if (!tagsNames.contains("food")) {
            server.addTag(new Tag(new Color(0, 255, 0, 255), "food"), eventID);
        }

        if (!tagsNames.contains("entrance fees")) {
            server.addTag(new Tag(new Color(0, 0, 255, 255), "entrance fees"), eventID);
        }

        if (!tagsNames.contains("travel")) {
            server.addTag(new Tag(new Color(255, 0, 0, 255), "travel"), eventID);
        }
    }

    /**
     * Returns to the start screen
     */
    public void goBack() {
        mainCtrl.showStart();
    }

    /**
     * Getter method for the language selector combobox on the scene
     * @return the combobox
     */
    public ComboBox<LanguageNode> getComboBox() {
        return cb;
    }

    /**
     * Fills out the text on the scene with the corresponding strings
     */
    public void fillText(HashMap<String, String> languageMap) {
        back.setText(languageMap.get("BackButton"));
        sendInv.setText(languageMap.get("SendInvitesButton"));
        participantLabel.setText(languageMap.get("Participants"));
        expenseLabel.setText(languageMap.get("Expenses"));
        addExpenseButton.setText(languageMap.get("AddExpenseButton"));
        allButton.setText(languageMap.get("AllButton"));
        fromButton.setText(languageMap.get("FromButton"));
        includingButton.setText(languageMap.get("IncludingButton"));
        debtsButton.setText(languageMap.get("SettleDebtsButton"));
        statisticsB.setText(languageMap.get("StatisticsButton"));
        getExpenseLabel.setText(languageMap.get("GetExpenseLabel"));
    }

    /**
     * Changes the displayed currency. First edits the config file, then calls refresh to reload
     * everything according to the new preferred currency
     */
    public void changeDisplayedCurrency() {
        String newLang = currency.getValue();
        File f = new File("client/src/main/resources/client/config.txt");
        ArrayList<String> lines = new ArrayList<>();
        try {
            Scanner reader = new Scanner(f);
            while (reader.hasNextLine()) {
                lines.add(reader.nextLine());
            }
            reader.close();

            lines.set(1, "currency:" + newLang);
            FileWriter writer = new FileWriter("client/src/main/resources/client/config.txt");
            for (String l : lines) {
                writer.write(l + "\n");
            }
            writer.close();
        }  catch (Exception e) {
            refresh();
            return;
        }
        refresh();
    }

    /**
     * Switches to the settle debts menu
     */
    public void goSettleDebts() {
        mainCtrl.showSettleDebts(eventID);
    }

    /**
     * Switches to the Statistics page
     */
    public void statistics() {
        mainCtrl.showStatistics(eventID);
    }

    /**
     * Getter
     * @return list of expenses in the event
     */
    public List<Expense> getExpenses() {
        return dataExpenses.stream().toList();
    }

    /**
     * Method to edit the title
     * Called when the title button is clicked upon.
     */
    public void editTitle() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Change Event Title");
        dialog.setHeaderText("Please enter new event title:");
        dialog.setContentText("Event title:");

        // Show the dialog and wait for user input
        Optional<String> result = dialog.showAndWait();
        String name = result.orElse(null);
        System.out.println(name);

        if (name == null || name.isEmpty()) {
            mainCtrl.showAlert("Name cannot be null.");
            return;
        }
        Event event = server.getEvent(eventID);
        event.setName(name);

        server.editEvent(eventID, event);

        eventTitleButton.setText(name);
    }

    /**
     * Do refresh for refreshable
     * @param event event to refresh with
     */
    public void doRefresh(Event event) {
        if (!(refreshable == null)) {
            refreshable.refresh();
        }
    }

    /**
     * Stop method for long polling
     */
    public void stop() {
        server.stop();
    }

    /**
     * Getter
     * @return event id
     */
    public String getEventID() {
        return eventID;
    }

    /**
     * Set the expense data.
     * @param expenses List of data to set for the expense data.
     */
    public void setDataExpenses(List<Expense> expenses) {
        dataExpenses = FXCollections.observableList(expenses);
    }

    /**
     * Setter
     * @param refreshable refreshable to be refreshed for long polling
     */
    public void setRefreshable(Refreshable refreshable) {
        this.refreshable = refreshable;
    }

}

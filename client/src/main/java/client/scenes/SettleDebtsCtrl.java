package client.scenes;

import client.utils.CommonUtils;
import client.utils.Refreshable;
import client.utils.ServerUtils;
import commons.Debt;
import commons.Event;
import commons.Participant;
import java.io.BufferedReader;
import java.io.FileReader;
import java.net.URL;
import java.nio.file.Paths;
import java.util.*;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javax.inject.Inject;


public class SettleDebtsCtrl implements Initializable, Refreshable {

    private String eventID;
    private final MainCtrl mainCtrl;
    private final ServerUtils serverUtils;
    private List<Debt> dataDebts;
    @FXML
    private Accordion accordion;
    @FXML
    private Label information;
    @FXML
    private Label debtTitle;
    @FXML
    private Button backB;

    /**
     * Constructor
     * @param mc Main controller
     * @param su Server utils for client
     */
    @Inject
    public SettleDebtsCtrl(MainCtrl mc, ServerUtils su) {
        this.mainCtrl = mc;
        this.serverUtils = su;
    }

    /**
     * Getter
     * @return the id of the event
     */
    public String getEventID() {
        return eventID;
    }

    /**
     * Setter
     * @param eventID the id for the specific event
     */
    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    /**
     * Initialize method for settle debts controller
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
        accordion.setMinWidth(556);
        accordion.setMaxWidth(556);
    }

    /**
     * Refreshes the debts in the menu
     */
    public void refresh() {
        List<Debt> debts = serverUtils.getDebts(eventID);
        Event ev = serverUtils.getEvent(eventID);
        List<Participant> pts = ev.getPrtList();
        String currentCurrency = CommonUtils.readCurrency(
                Paths.get("client/src/main/resources/client/config.txt"));
        String s = "";
        for (Participant p : pts) {
            int amt = p.getNetDebt();
            s += p.getName() + ": ";
            if (currentCurrency != null) {
                amt = mainCtrl.getExchanger().exchangeCurrencies("USD", currentCurrency,
                        amt, new Date());
            }
            s += amt / 100 + "." + Math.abs(amt) % 100  + " " + currentCurrency + "        ";
        }
        if (this.information != null) {
            this.information.setText(s);
        }
        this.dataDebts = debts;
        updatePaneList(debts, accordion.getPanes());
    }

    /**
     * Send reminder to email
     * @param p Participant who is owed
     * @param email Email of recipient
     */
    public void sendReminder(Participant p, String email) {
        mainCtrl.getSendInvitesCtrl().getEmailUtils().sendReminder(p, email,
                serverUtils.getEvent(eventID).getInviteCode());
    }

    /**
     * Get invite button with action
     * @param p Participant who is owed
     * @param email email of recipient
     * @return a box with label and button
     */
    public HBox getInviteButton(Participant p, String email) {
        Button btn = new Button(mainCtrl.getLanguages().get("SendReminder"));
        btn.setOnAction(e -> sendReminder(p, email));
        HBox vb = new HBox(
                new Label(mainCtrl.getLanguages().get("EmailConfigured") + " "),
                btn
        );
        vb.setAlignment(Pos.CENTER_LEFT);
        return vb;
    }

    /**
     * Update pane list method
     * @param debts The list of debts which are displayed
     * @param panes The panes list which has to be updated
     */
    public void updatePaneList(List<Debt> debts, ObservableList<TitledPane> panes) {
        panes.clear();
        for (int i = 0; i < debts.size(); i++) {
            final int j = i;
            Debt d = debts.get(i);
            BorderPane bp = getBorderPane(d, j);
            TitledPane tp = new TitledPane("", null);
            bp.prefWidthProperty().bind(tp.widthProperty().subtract(30));
            tp.setGraphic(bp);
            VBox vertical;
            if (!mainCtrl.getSendInvitesCtrl().isEmailConfigured()
                    || d.getPerson1().getEmail() == null
                    || d.getPerson1().getEmail().isEmpty()) {
                vertical = new VBox(getLabel(d.getPerson2()));
            } else {
                vertical = new VBox(getLabel(d.getPerson2()),
                        getInviteButton(d.getPerson2(), d.getPerson1().getEmail()));
            }
            tp.setContent(vertical);
            panes.add(tp);
        }
    }

    /**
     * Get label for bank information
     * @param p Person with bank information
     * @return The label explaining it
     */
    public VBox getLabel(Participant p) {
        VBox ans = new VBox();
        if (p.getIban() != null && !Objects.equals(p.getIban(), "")) {
            ans = new VBox(ans,
                    new Label(mainCtrl.getLanguages().get("BankAvailable") + " "));
            ans = new VBox(ans,
                    new Label(mainCtrl.getLanguages().get("AccountHolder") + " " + p.getName()));
            ans = new VBox(ans,
                    new Label(mainCtrl.getLanguages().get("IBANDebts") + " " + p.getIban()));
            if (p.getBic() != null && !Objects.equals(p.getBic(), "")) {
                ans = new VBox(ans,
                        new Label(mainCtrl.getLanguages().get("BICDebts") + " " + p.getBic()));
            }
        } else {
            ans = new VBox(ans, new Label(mainCtrl.getLanguages().get("NoInformation")));
        }
        return ans;
    }

    /**
     * Get border pane for debt and index
     * @param d The debt to get
     * @param j The index
     * @return The new border pane
     */
    public BorderPane getBorderPane(Debt d, int j) {
        String currentCurrency = CommonUtils.readCurrency(
                Paths.get("client/src/main/resources/client/config.txt"));
        int amountOwned = d.getAmt1Owes();
        if (currentCurrency != null) {
            amountOwned = mainCtrl.getExchanger().exchangeCurrencies("USD", currentCurrency,
                    amountOwned, new Date());
        }
        BorderPane bp = new BorderPane();
        Button btn = new Button(mainCtrl.getLanguages().get("MarkReceived"));
        Spinner<Double> selector =
                new Spinner(0, amountOwned / 100.0,
                        amountOwned / 100.0);
        btn.setOnAction(event -> settleDebt(j, selector.getValue()));
        HBox hb = new HBox(selector, btn);
        bp.setRight(hb);
        Text name1 = new Text(d.getPerson1().getName());
        name1.setStyle("-fx-font-weight: bold");
        final Text gives = new Text(mainCtrl.getLanguages().get("Gives") + " ");
        String str = "";
        int firstg = amountOwned / 100;
        int secondg = amountOwned % 100;
        str += firstg;
        str += ".";
        str += secondg;
        if (currentCurrency != null) {
            str += Currency.getInstance(currentCurrency).getSymbol();
        }
        Text money = new Text(str);
        money.setStyle("-fx-font-weight: bold");
        Text to = new Text(mainCtrl.getLanguages().get("To") + " ");
        Text name2 = new Text(d.getPerson2().getName());
        name2.setStyle("-fx-font-weight: bold");
        TextFlow tf = new TextFlow();
        tf.getChildren().addAll(name1, gives, money, to, name2);
        bp.setCenter(tf);
        return bp;
    }

    /**
     * Settle debt
     *
     * @param ind   the index of the debt in the list
     * @param value value to settle
     */
    public void settleDebt(int ind, Double value) {
        //System.out.println("Button " + ind + " was clicked!");
        Debt dd;
        if (value == null) {
            dd = dataDebts.get(ind);
        } else {
            String currentCurrency = CommonUtils.readCurrency(
                    Paths.get("client/src/main/resources/client/config.txt"));
            int d = (int) Math.round(value * 100);
            if (currentCurrency != null) {
                d = mainCtrl.getExchanger().exchangeCurrencies(currentCurrency, "USD",
                        d, new Date());
            }
            Debt cur = dataDebts.get(ind);
            if (Math.abs(d - cur.getAmt1Owes()) < 3) {
                d = cur.getAmt1Owes();
            }
            dd = new Debt(cur.getPerson1(), cur.getPerson2(), d, -d, cur.getDatePaid());

        }

        serverUtils.removeDebt(this.eventID, dd);
        refresh();
    }

    /**
     * Go back to event overview
     */
    public void back() {
        mainCtrl.showOverview(eventID);
    }

    /**
     * Reads the currently used currency from the config file
     * @return the current currency
     */
    public String readCurrency() {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(
                    "client/src/main/resources/client/config.txt"));
            reader.readLine();
            return reader.readLine().split(":")[1];

        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Setter
     * @param accordion accordion of page
     */
    public void setAccordion(Accordion accordion) {
        this.accordion = accordion;
    }

    /**
     * Fills out the text on the scene with the corresponding strings
     */
    public void fillText(HashMap<String, String> languageMap) {
        debtTitle.setText(languageMap.get("SettleDebtsButton"));
        backB.setText(languageMap.get("BackButton"));
    }

    /**
     * Getter
     * @return accordion
     */
    public Accordion getAccordion() {
        return accordion;
    }
}


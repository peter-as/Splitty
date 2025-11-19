package client.scenes;

import client.Exchange;
import client.utils.CommonUtils;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import commons.Expense;
import commons.Participant;
import commons.Tag;
import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

public class StatisticsCtrl implements Initializable {

    @FXML
    private Label eventCost;
    @FXML
    private PieChart pieChart = new PieChart();
    @FXML
    private PieChart spendingPieChart = new PieChart();
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private String eventID = "";
    private int cost;
    private String currentCurrency;
    @FXML
    private Label staisticsTitle;
    @FXML
    private Label totalCost;
    @FXML
    private Button backB;
    @FXML
    private Button showParticipant;
    @FXML
    private Button showTag;

    private HashMap<Participant, Integer> participantSpendingMap;

    /**
     * new
     * Called to initialize a controller after its root element has been
     * completely processed.
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
     * Constructor for overview of participant add page
     *
     * @param server   The server for the overview
     * @param mainCtrl The main control
     */
    @Inject
    public StatisticsCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * Set the event id for the event that is being shown.
     *
     * @param eventID the event id.
     */
    public void setEventID(String eventID) {
        this.eventID = eventID;
    }


    /**
     * Cancel method, sends the user back to the Overview page of the event
     */
    public void cancel() {
        mainCtrl.showOverview(eventID);
    }

    /**
     * Calculates the cost of each tag per event and adds both into a map
     *
     * @return tagPairs a map with the cost and the tag
     */
    public MultivaluedMap<Integer, Tag> calculateStatistics() {

        Event event = server.getEvent(eventID);
        //Map to store the name and the cost of each tag
        //The key represents the cost, the value is the actual tag
        MultivaluedMap<Integer, Tag> tagPairs = new MultivaluedHashMap<Integer, Tag>();

        List<Tag> tagsEvent = event.getTagList();

        //For each tag name, go through each expense
        for (Tag tt : tagsEvent) {
            String tagName = tt.getName();
            int tagCost = 0;
            for (Expense expense : event.getExpList()) {
                //For each expense, check if the tag name is found,
                // and increment cost to match expense amount
                if (tagName.equals(expense.getTag().getName())) {
                    tagCost += expense.getAmountPaid();
                }
            }
            //After a tag name has been checked in all the expenses,
            //if it has any cost, add it to the map
            if (tagCost != 0) {
                tagPairs.add(tagCost, tt);
            }
        }
        return tagPairs;
    }

    /**
     * Displays the piechart
     */
    public void display() {
        spendingPieChart.setVisible(false);
        pieChart.setVisible(true);

        MultivaluedMap<Integer, Tag> tagPairs = calculateStatistics();

        List<PieChart.Data> finalist = pieChart.getData();
        finalist.clear();
        List<String> text = new ArrayList<>();
        for (MultivaluedMap.Entry<Integer, List<Tag>> entry : tagPairs.entrySet()) {
            for (Tag t : entry.getValue()) {
                int exchangedTagCost = mainCtrl.getExchanger().exchangeCurrencies("USD",
                        currentCurrency, Integer.parseInt(entry.getKey().toString()), new Date());
                double percentage = Math.floor(100.0 * exchangedTagCost / cost * 100) / 100;
                String nameTag = t.getName();

                PieChart.Data slice = new PieChart.Data(nameTag, exchangedTagCost);
                finalist.add(slice);

                String tagValue = CommonUtils.moneyToText(exchangedTagCost);
                String currency = Currency.getInstance(currentCurrency).getSymbol();
                String legendItem = nameTag + ", " + tagValue + currency + ", " + percentage + "%";

                text.add(legendItem);
            }

        }

        Set<Node> items = pieChart.lookupAll("Label.chart-legend-item");
        int i = 0;
        for (Node item : items) {
            Label label = (Label) item;
            label.setText(text.get(i));
            i++;
        }

    }

    /**
     * Populates map of participants and their total spending
     */
    public void populateSpendingMap() {
        participantSpendingMap = new HashMap<>();
        List<Expense> expenses = server.getExpenses(eventID);
        for (Expense ex : expenses) {
            if (participantSpendingMap.containsKey(ex.getWhoPaid())) {
                int spent = participantSpendingMap.get(ex.getWhoPaid());
                participantSpendingMap.replace(ex.getWhoPaid(), ex.getAmountPaid() + spent);
            } else {
                participantSpendingMap.put(ex.getWhoPaid(), ex.getAmountPaid());
            }
        }
    }

    /**
     * Displays the spending per participant pie chart
     */
    public void displaySpendingPieChart() {
        pieChart.setVisible(false);
        spendingPieChart.setVisible(true);
        populateSpendingMap();
        List<PieChart.Data> finalist = spendingPieChart.getData();
        finalist.clear();
        List<String> text = new ArrayList<>();

        for (HashMap.Entry<Participant, Integer> entry : participantSpendingMap.entrySet()) {
            int exchangedSpending = mainCtrl.getExchanger().exchangeCurrencies("USD",
                    currentCurrency, entry.getValue(), new Date());
            double percentage = Math.floor(100.00 * exchangedSpending / cost * 100) / 100;
            String participantName = entry.getKey().getName();

            PieChart.Data slice = new PieChart.Data(participantName, exchangedSpending);
            finalist.add(slice);

            String value = CommonUtils.moneyToText(exchangedSpending);
            String currency = Currency.getInstance(currentCurrency).getSymbol();
            String legendItem = participantName + ", " + value + currency + ", " + percentage + "%";

            text.add(legendItem);

        }

        Set<Node> items = spendingPieChart.lookupAll("Label.chart-legend-item");
        int i = 0;
        for (Node item : items) {
            Label label = (Label) item;
            label.setText(text.get(i));
            i++;
        }
    }

    /**
     * Calculates the total event cost as well as sets everything up.
     *
     * @param path - path of currency api
     */
    public void calculateEventCost(Path path) {
        cost = 0;
        currentCurrency = CommonUtils.readCurrency(
                path);
        String currency = Currency.getInstance(currentCurrency).getSymbol();

        Event event = server.getEvent(eventID);
        List<Expense> expenses = event.getExpList();
        for (Expense expense : expenses) {
            int exchangedCost = mainCtrl.getExchanger().exchangeCurrencies("USD", currentCurrency,
                    expense.getAmountPaid(), expense.getDate());
            cost += exchangedCost;
        }
        String finalString = CommonUtils.moneyToText(cost);

        eventCost.setText(finalString + currency);
    }

    /**
     * Fills out the text on the scene with the corresponding strings
     */
    public void fillText(HashMap<String, String> languageMap) {
        staisticsTitle.setText(languageMap.get("StatisticsTitle"));
        totalCost.setText(languageMap.get("StatisticsCosts"));
        backB.setText(languageMap.get("BackButton"));
        showParticipant.setText(languageMap.get("ShowParticipant"));
        showTag.setText(languageMap.get("ShowTag"));
    }


}


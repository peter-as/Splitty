/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package client.scenes;

import client.Exchange;
import client.LanguageSwitcher;
import client.utils.EmailUtils;
import client.utils.ServerUtils;
import commons.Expense;
import commons.Participant;
import commons.Tag;
import java.awt.*;
import java.nio.file.Paths;
import java.util.Map;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.util.Pair;
import javax.swing.*;



public class MainCtrl {

    private Stage primaryStage;

    private StartScreenController startScreenCtrl;
    private OverviewCtrl overviewCtrl;
    private AddEditExpenseCtrl addEditExpenseCtrl;
    private AddParticipantCtrl addParticipantCtrl;
    private EditRemoveParticipantCtrl editRemoveParticipantCtrl;
    private SettleDebtsCtrl settleDebtsCtrl;
    private StatisticsCtrl statisticsCtrl;
    private EditRemoveTagCtrl editRemoveTagCtrl;

    private AddEditTagCtrl addEditTagCtrl;
    private SendInvitesCtrl sendInvitesCtrl;
    private Scene startScreen;
    private Scene overview;
    private Scene addEditExpense;
    private Scene addParticipant;
    private Scene editRemoveParticipant;
    private Scene settleDebts;
    private Scene statistics;
    private Scene addEditTag;
    private Scene editRemoveTag;
    private LanguageSwitcher languageSwitcher;
    private static String currentLanguage;
    private static String currentCurrency;
    private static Map<String, String> languages;
    private EmailUtils emailUtils;
    private boolean isEmailConfigured;
    private Exchange exchanger;
    private Scene sendInvites;


    /**
     * Empty constructor
     */
    public MainCtrl() {
    }


    /**
     * Main control constructor
     * @param primaryStage Primary stage for main control
     * @param overview Overview for main control
     */

    @SuppressWarnings("all")
    public void initialize(Stage primaryStage,
                           Pair<OverviewCtrl, Parent> overview,
                           Pair<AddEditExpenseCtrl, Parent> addEditExpense,
                           Pair<AddParticipantCtrl, Parent> addParticipant,
                           Pair<EditRemoveParticipantCtrl, Parent> editRemoveParticipant,
                           Pair<StartScreenController, Parent> startScreen,
                           Pair<SettleDebtsCtrl, Parent> settleDebts,
                           Pair<StatisticsCtrl, Parent> statistics,
                           Pair<AddEditTagCtrl, Parent> addEditTag,
                           Pair<EditRemoveTagCtrl, Parent> editRemoveTag,
                           Pair<SendInvitesCtrl, Parent> sendInvites) {


        this.primaryStage = primaryStage;
        this.overviewCtrl = overview.getKey();
        this.overview = new Scene(overview.getValue());
        this.addEditExpenseCtrl = addEditExpense.getKey();
        this.addEditExpense = new Scene(addEditExpense.getValue());
        this.addParticipantCtrl = addParticipant.getKey();
        this.addParticipant = new Scene(addParticipant.getValue());
        this.editRemoveParticipantCtrl = editRemoveParticipant.getKey();
        this.editRemoveParticipant = new Scene(editRemoveParticipant.getValue());
        this.startScreenCtrl = startScreen.getKey();
        this.startScreen = new Scene(startScreen.getValue());
        this.settleDebtsCtrl = settleDebts.getKey();
        this.settleDebts = new Scene(settleDebts.getValue());
        this.statisticsCtrl = statistics.getKey();
        this.statistics = new Scene(statistics.getValue());
        this.addEditTagCtrl = addEditTag.getKey();
        this.addEditTag = new Scene(addEditTag.getValue());
        this.editRemoveTagCtrl = editRemoveTag.getKey();
        this.editRemoveTag = new Scene(editRemoveTag.getValue());
        exchanger = new Exchange(new ServerUtils());
        this.sendInvitesCtrl = sendInvites.getKey();
        this.sendInvites = new Scene(sendInvites.getValue());
        languageSwitcher = new LanguageSwitcher(this.startScreen, this.overview, overviewCtrl, addEditExpenseCtrl,
                addParticipantCtrl, editRemoveParticipantCtrl, startScreenCtrl,
                settleDebtsCtrl, statisticsCtrl, addEditTagCtrl, editRemoveTagCtrl, sendInvitesCtrl);
        languageSwitcher.setUp();
        
        showStart();
        primaryStage.show();
    }

    /**
     * Shows startScreen in main control
     */
    public void showStart() {
        primaryStage.setTitle("Events");
        primaryStage.setScene(startScreen);
        startScreenCtrl.initialize();
    }

    /**
     * Shows overview in main control
     */
    public void showOverview(String eventID) {
        primaryStage.setTitle("Event: Overview");
        primaryStage.setScene(overview);
        overviewCtrl.setEventID(eventID);
        overviewCtrl.setRefreshable(overviewCtrl);
        overviewCtrl.refresh();
    }

    /**
     * Shows addExpense in main control
     */
    public void showAddExpense(String eventId) {
        primaryStage.setTitle("Event: Add Expense");
        primaryStage.setScene(addEditExpense);
        addEditExpenseCtrl.setEventId(eventId);
        addEditExpenseCtrl.refresh();

    }

    /**
     * Shows addParticipant with empty fields in main control
     */
    public void showAddParticipant(String eventID) {
        addParticipantCtrl.hideButton();
        primaryStage.setTitle("Event: Add Participant");
        primaryStage.setScene(addParticipant);
        addParticipantCtrl.setEventID(eventID);
    }

    /**
     * Shows addParticipant with filled in fields in main control
     */
    public void showAddParticipantFilled(String eventID, Participant p) {
        primaryStage.setTitle("Event: Edit / Remove Participant");
        addParticipantCtrl.setName(p.getName());
        addParticipantCtrl.setEmail(p.getEmail());
        addParticipantCtrl.setIban(p.getIban());
        addParticipantCtrl.setBic(p.getBic());
        addParticipantCtrl.setEventID(eventID);
        addParticipantCtrl.showButton();

        primaryStage.setScene(addParticipant);
    }

    /**
     * Show the add Tag screen but with the information filled of the tag you want to edit.
     * @param eventID the event id.
     * @param t the tag to edit.
     */
    public void showAddTagFilled(String eventID, Tag t) {
        primaryStage.setTitle("Event: Edit / Remove Tag");

        addEditTagCtrl.fillFields(t);
        addEditTagCtrl.setEventId(eventID);
        addEditTagCtrl.showEdit();

        primaryStage.setScene(addEditTag);
    }

    /**
     * Shows editRemoveParticipant in main control
     */
    public void showEditRemoveParticipant(String eventID) {
        primaryStage.setTitle("Event: Edit/Remove Participant");
        primaryStage.setScene(editRemoveParticipant);
        editRemoveParticipantCtrl.setEventID(eventID);
        editRemoveParticipantCtrl.refresh();
        editRemoveParticipantCtrl.setAddParticipantCtrl(addParticipantCtrl);

    }

    /**
     * Show the screen where you can select the tag to edit.
     * @param eventID the event ID of the tag to edit.
     */
    public void showEditRemoveTag(String eventID) {
        primaryStage.setTitle("Event: Edit/Remove Tag");
        primaryStage.setScene(editRemoveTag);
        editRemoveTagCtrl.setEventID(eventID);
        editRemoveTagCtrl.refresh();
        editRemoveTagCtrl.setAddTagCtrl(addEditTagCtrl);

    }

    /**
     * Show the settle debts menu
     * @param eventID id of event to show
     */
    public void showSettleDebts(String eventID) {
        primaryStage.setTitle("Settle Debts");
        primaryStage.setScene(settleDebts);
        settleDebtsCtrl.setEventID(eventID);
        overviewCtrl.setRefreshable(settleDebtsCtrl);
        settleDebtsCtrl.refresh();
    }

    /**
     * Method to go to the add expense screen but made to edit an existing expense.
     * @param eventID event id of the event the expense is attached to.
     * @param expense the expense which is to be edited.
     */
    public void editExpense(String eventID, Expense expense) {
        primaryStage.setTitle("Event: Edit Expense");
        primaryStage.setScene(addEditExpense);
        addEditExpenseCtrl.setEventId(eventID);
        addEditExpenseCtrl.refresh();
        addEditExpenseCtrl.editExpense(expense);
    }

    /**
     * Shows Statistics in main control
     */
    public void showStatistics(String eventID) {
        statisticsCtrl.setEventID(eventID);
        primaryStage.setTitle("Statistics");
        primaryStage.setScene(statistics);
        statisticsCtrl.calculateEventCost(Paths.get("client/src/main/resources/client/config.txt"));
        statisticsCtrl.display();

    }


    /**
     * Shows add tag page
     * @param eventId - event id
     */
    public void showAddTag(String eventId) {
        addEditTagCtrl.showAdd();
        primaryStage.setTitle("Create Tag");
        primaryStage.setScene(addEditTag);
        addEditTagCtrl.setEventId(eventId);

    }

    /**
     * Getter for the exchanger
     * @return the exchanger
     */
    public Exchange getExchanger() {
        return exchanger;
    }

    /**
     * Method to display pop up with confirmation message
     *
     */
    public void confirmationPopUp(String text) {
        Frame frame = new JFrame();
        JPanel panel = new JPanel();
        LayoutManager layout = new FlowLayout();
        panel.setLayout(layout);
        frame.add(panel);
        JOptionPane.showMessageDialog(null, text);

    }

    /** Getter for the current language
     * Show alert
     * @param message message to show the user
     */
    public void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Problem");
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Getter for the current language
     * @return the current language
     */
    public static String getCurrentLanguage() {
        return currentLanguage;
    }

    /**
     * Get the current currency
     * @return the current currency
     */
    public static String getCurrentCurrency() {
        return currentCurrency;
    }

    /**
     * Setter for the current language
     * @param currentLanguage the current language
     */
    public static void setCurrentLanguage(String currentLanguage) {
        MainCtrl.currentLanguage = currentLanguage;
    }

    /**
     * Setter for the current language
     * @param currentCurrency the current language
     */
    public static void setCurrentCurrency(String currentCurrency) {
        MainCtrl.currentCurrency = currentCurrency;
    }

    /**
     * Getter for the current map of languages
     * @return the languagemap
     */
    public Map<String, String> getLanguages() {
        return languageSwitcher.getLanguageMap();
    }

    /**
     * Show send invites menu
     * @param eventID Event id for which to show
     */
    public void showSendInvites(String eventID) {
        if (!sendInvitesCtrl.isEmailConfigured()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Problem");
            alert.setContentText(getLanguages().get("EmailConfigured"));
            alert.showAndWait();
        } else {
            sendInvitesCtrl.setEventID(eventID);
            primaryStage.setTitle("Send invites");
            primaryStage.setScene(sendInvites);
        }
    }

    /**
     * Getter
     * @return overview controller
     */
    public OverviewCtrl getOverviewCtrl() {
        return overviewCtrl;
    }

    /**
     * Getter
     * @return Send invites controller
     */
    public SendInvitesCtrl getSendInvitesCtrl() {
        return sendInvitesCtrl;
    }


}

package client.scenes;

import client.MyFXML;
import client.MyModule;
import client.utils.ServerUtils;
import com.google.inject.Injector;
import commons.Expense;
import commons.Participant;
import commons.Tag;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.awt.*;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.google.inject.Guice.createInjector;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(ApplicationExtension.class)
public class OverviewCtrlTest {

    private static final Injector INJECTOR = createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);
    @Mock
    ServerUtils su;
    @Mock
    MainCtrl mc;
    @InjectMocks
    OverviewCtrl oc;

    @BeforeAll
    public static void setupSpec() {
        System.setProperty("java.awt.headless", "true");
        System.setProperty("testfx.robot", "glass");
        System.setProperty("testfx.headless", "true");
        System.setProperty("prism.order", "sw");
        System.setProperty("prism.text", "t2k");
    }

    @Start
    private void start(Stage stage) {
        MockitoAnnotations.initMocks(this);
        var start = FXML.load(StartScreenController.class, "client", "scenes", "StartScreen.fxml");
        var overview = FXML.load(OverviewCtrl.class, "client", "scenes", "Overview.fxml");
        var addEditExpense = FXML.load(AddEditExpenseCtrl.class, "client", "scenes",
                "AddEditExpense.fxml");
        var addParticipant = FXML.load(AddParticipantCtrl.class, "client", "scenes",
                "addParticipant.fxml");
        var editRemoveParticipant = FXML.load(EditRemoveParticipantCtrl.class, "client", "scenes",
                "editRemoveParticipant.fxml");
        var settleDebts = FXML.load(SettleDebtsCtrl.class, "client", "scenes",
                "SettleDebts.fxml");
        var statistics = FXML.load(StatisticsCtrl.class, "client", "scenes",
                "Statistics.fxml");
        var addEditTag = FXML.load(AddEditTagCtrl.class, "client", "scenes",
                "AddEditTag.fxml");
        var editRemoveTag = FXML.load(EditRemoveTagCtrl.class, "client", "scenes",
                "editRemoveTag.fxml");
        var sendInvites = FXML.load(SendInvitesCtrl.class, "client", "scenes",
                "SendInvites.fxml");
        var mainCtrl = INJECTOR.getInstance(MainCtrl.class);
        mainCtrl.initialize(stage, overview,
                addEditExpense, addParticipant, editRemoveParticipant,
                start, settleDebts, statistics, addEditTag, editRemoveTag,
                sendInvites);
    }

    @Test
    public void testBack() {
        oc.goBack();
        verify(mc, times(1)).showStart();
    }

    @Test
    public void testSettleDebts() {
        oc.setEventID("4");
        oc.goSettleDebts();
        verify(mc, times(1)).showSettleDebts("4");
    }

    @Test
    public void testStatistics() {
        oc.setEventID("7");
        oc.statistics();
        verify(mc, times(1)).showStatistics("7");
    }

    @Test
    public void testSendInvites() {
        oc.setEventID("9");
        oc.sendInvites();
        verify(mc, times(1)).showSendInvites("9");
    }

    @Test
    public void testAddExpense() {
        oc.setEventID("10");
        oc.addExpense();
        verify(mc, times(1)).showAddExpense("10");
    }

    @Test
    public void testAddParticipant() {
        oc.setEventID("13");
        oc.addParticipant();
        verify(mc, times(1)).showAddParticipant("13");
    }

    @Test
    public void testEditParticipant() {
        oc.setEventID("17");
        oc.editParticipant();
        verify(mc, times(1)).showEditRemoveParticipant("17");
    }

    @Test
    public void testEditExpense() {
        oc.setEventID("17");
        Expense expense = new Expense("Food", new Date(2023, Calendar.APRIL, 13),
                new Tag(new Color(255, 255, 255, 255), "Food"),
                new Participant("Bob", "bob@gmail.com", "IBAN123", "BIC123"),
                1200, "EUR", Arrays.asList(
                new Participant("Rick", "rick@gmail.com", "IBAN123", "BIC123"),
                new Participant("Sara", "sara@gmail.com", "IBAN123", "BIC123")));
        oc.editExpense(expense);
        verify(mc, times(1)).editExpense("17", expense);
    }

    @Test
    public void testGetEventID() {
        oc.setEventID("2");
        assertEquals("2", oc.getEventID());
    }

    @Test
    public void testGetExpenses() {
        List<Expense> expenseListTest = Arrays.asList(
                new Expense("Food", new Date(2023, Calendar.APRIL, 13),
                        new Tag(new Color(255, 255, 255, 255), "Food"),
                        new Participant("Bob", "bob@gmail.com", "IBAN123", "BIC123"),
                        1200, "EUR", Arrays.asList(
                        new Participant("Rick", "rick@gmail.com", "IBAN123", "BIC123"),
                        new Participant("Sara", "sara@gmail.com", "IBAN123", "BIC123"))),

                new Expense("Drinks", new Date(2023, Calendar.APRIL, 14),
                        new Tag(new Color(255, 255, 255, 255), "Drinks"),
                        new Participant("Bob", "bob@gmail.com", "IBAN123", "BIC123"),
                        1200, "EUR", Arrays.asList(
                        new Participant("Rick", "rick@gmail.com", "IBAN123", "BIC123"),
                        new Participant("Sara", "sara@gmail.com", "IBAN123", "BIC123"))),

                new Expense("Travel", new Date(2023, Calendar.APRIL, 15),
                        new Tag(new Color(255, 255, 255, 255), "Travel"),
                        new Participant("Bob", "bob@gmail.com", "IBAN123", "BIC123"),
                        1200, "EUR", Arrays.asList(
                        new Participant("Rick", "rick@gmail.com", "IBAN123", "BIC123"),
                        new Participant("Sara", "sara@gmail.com", "IBAN123", "BIC123")
                ))
        );
        oc.setDataExpenses(expenseListTest);
        assertEquals(expenseListTest, oc.getExpenses());
    }
}

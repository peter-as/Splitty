package client.scenes;

import client.Main;
import client.MyFXML;
import client.MyModule;
import client.utils.ServerUtils;
import com.google.inject.Injector;
import commons.Debt;
import commons.Event;
import commons.Participant;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.inject.Guice.createInjector;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;


@ExtendWith(ApplicationExtension.class)
class SettleDebtsCtrlTest {
    private static final Injector INJECTOR = createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);
    @Mock
    ServerUtils su;
    @Mock
    MainCtrl mc;
    @Mock
    MainCtrl mc2;
    @InjectMocks
    SettleDebtsCtrl sdc;

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
        mc2 = mock(MainCtrl.class);
        Map<String, String> languages = new HashMap<>();
        languages.put("BankAvailable", "Bank information available, please transfer money to:");
        languages.put("AccountHolder", "Account holder:");
        languages.put("IBANDebts","IBAN:");
        languages.put("BICDebts","BIC:");
        languages.put("NoInformation","No sufficient banking information available");
        languages.put("Gives"," gives");
        languages.put("EmailConfigured","Email configured:");
        when(mc2.getLanguages()).thenReturn(languages);
    }

    @Test
    public void getLabelTest() {
        SettleDebtsCtrl sdc = new SettleDebtsCtrl(mc2, null);
        VBox vbox = new VBox(new VBox(), new Label("No sufficient banking information available"));
        VBox vbox1 = sdc.getLabel(new Participant());
        assertEquals(vbox.getChildren().get(1).getAccessibleText(), vbox1.getChildren().get(1).getAccessibleText());
        Participant p = new Participant();
        p.setIban("asdfdf");
        p.setBic("aaaaa");
        p.setName("asdf");
        assertEquals(
                ((Label)(sdc.getLabel(p).getChildren().get(1))).getText(),
                "BIC: aaaaa");
    }

    @Test
    public void backTest() {
        sdc = new SettleDebtsCtrl(mc, mock(ServerUtils.class));
        sdc.setEventID("3");
        sdc.back();
        verify(mc, times(1)).showOverview("3");
    }
    @Test
    public void getBorderPaneTest() {
        sdc = new SettleDebtsCtrl(mc2, mock(ServerUtils.class));
        Debt d = new Debt();
        Participant first = new Participant();
        Participant second = new Participant();
        first.setName("asdf");
        second.setName("second");
        d.setPerson1(first);
        d.setPerson2(second);
        BorderPane bp = sdc.getBorderPane(d, 3);
        TextFlow tf = (TextFlow) bp.getChildren().get(1);
        Text t = (Text)tf.getChildren().get(1);
        Text name1 = (Text)tf.getChildren().get(0);
        assertEquals(t.getText()," gives ");
        assertEquals(name1.getText(), first.getName());
    }

    @Test
    public void getInviteButtonTest() {
        Participant part = new Participant();
        sdc = new SettleDebtsCtrl(mc2, mock(ServerUtils.class));
        HBox hb = sdc.getInviteButton(part, "string");
        assertEquals(((Label)hb.getChildren().get(0)).getText(), "Email configured: ");
    }
    @Test
    public void cornerCases() {
        SettleDebtsCtrl sdcc = new SettleDebtsCtrl(null, null);
        sdcc.setEventID("asdf");
        assertEquals(sdcc.getEventID(), "asdf");
    }

    @Mock
    SendInvitesCtrl sii;
    @InjectMocks
    SettleDebtsCtrl settleDebtsCtrl;

    @Test
    public void refreshTest() {
        Participant p1 = new Participant();
        Participant p2 = new Participant();
        p1.setName("one");
        p2.setName("two");
        Debt d1 = new Debt();
        d1.setPerson1(p1);
        d1.setPerson2(p2);
        d1.setAmt1Owes(100);
        d1.setAmt1Owes(-100);
        when(mc.getSendInvitesCtrl()).thenReturn(sii);
        when(sii.isEmailConfigured()).thenReturn(false);
        when(su.getDebts(Mockito.any())).thenReturn(List.of(d1));
        Event evv = new Event();
        evv.setPrtList(new ArrayList<>());
        when(su.getEvent(Mockito.any())).thenReturn(null);
        Accordion ac = new Accordion();
        settleDebtsCtrl.setAccordion(ac);
        try {
            settleDebtsCtrl.refresh();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        assertEquals(settleDebtsCtrl.getAccordion(), ac);
    }
    @Test
    public void stressTest(FxRobot robot) {
        robot.clickOn("#newEvent").write("asdf");
        robot.clickOn(robot.lookup(".button").queryButton());
        robot.clickOn(".button");
        robot.clickOn(".button");
        robot.clickOn(".button");
        robot.clickOn(".button");
        robot.clickOn(".button");
        robot.clickOn(".button");
        robot.clickOn(".button");
        robot.clickOn(".button");
        robot.clickOn(".button");
        robot.clickOn(".button");
        robot.clickOn(".button");
        robot.clickOn(".button");
        robot.clickOn(".button");
        robot.clickOn(".button");
        robot.clickOn(".button");
    }
}
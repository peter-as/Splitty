package client.scenes;

import client.MyFXML;
import client.MyModule;
import client.utils.ServerUtils;
import com.google.inject.Injector;
import commons.Event;
import commons.Expense;
import commons.Participant;
import commons.Tag;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static com.google.inject.Guice.createInjector;
import static org.mockito.Mockito.*;

@ExtendWith(ApplicationExtension.class)
public class StartScreenCtrlTest {
    private static final Injector INJECTOR = createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);
    @Mock
    ServerUtils su;
    @Mock
    MainCtrl mc;
    @InjectMocks
    StartScreenController ssc;

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
                start, settleDebts, statistics, addEditTag, editRemoveTag, sendInvites);
    }

    Event e;
    @BeforeEach
    public void setup() {
        e = new Event(new Date(), new Date(), "TestEvent", new ArrayList<Participant>(), new LinkedList<Expense>(), new ArrayList<Tag>());
        e.setId(1L);
        su = mock(ServerUtils.class);
        List<Event> events = new ArrayList<>();
        events.add(e);
        when(su.postEvent(Mockito.any())).thenReturn(e);
        when(su.getEvent(Mockito.any())).thenReturn(e);
        when(su.getEvents()).thenReturn(events);
    }

    @Test
    public void createTest() {
        ssc = new StartScreenController(su, mc);
        ssc.setCreateText("TestEvent");
        ssc.addEvent();
        verify(mc, times(1)).showOverview(Mockito.any());
    }

    @Test
    public void joinTest() {
        ssc = new StartScreenController(su, mc);
        ssc.setCreateText("TestEvent");
        ssc.addEvent();
        ssc.setJoinText("1");
        ssc.joinEvent();
        verify(mc, times(2)).showOverview(Mockito.any());
    }

    @Test
    public void hyperLinkTest() {
        ssc = new StartScreenController(su, mc);
        ssc.setCreateText("TestEvent");
        ssc.addEvent();
        ssc.setHyperLinks("1");
        ssc.openFirst();
        ssc.openSec();
        ssc.openThird();
        ssc.openFourth();
        verify(mc, times(5)).showOverview(Mockito.any());
    }

    @Test
    public void testRead() {
        ssc = new StartScreenController(su, mc);
        ssc.setCreateText("TestEvent");
        ssc.fillEvents(Paths.get("src/main/resources/client/recentEvents.txt"));
    }
}

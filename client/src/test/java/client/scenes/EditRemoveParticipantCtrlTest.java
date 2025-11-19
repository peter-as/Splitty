package client.scenes;

import client.MyFXML;
import client.MyModule;
import client.utils.ServerUtils;
import com.google.inject.Injector;
import commons.Participant;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import static com.google.inject.Guice.createInjector;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(ApplicationExtension.class)
public class EditRemoveParticipantCtrlTest {
    private static final Injector INJECTOR = createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);
    @Mock
    ServerUtils su;
    @Mock
    MainCtrl mc;
    @InjectMocks
    EditRemoveParticipantCtrl apc;

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
    public void cancelTest() {
        apc.setEventID("1");
        apc.cancel();
        verify(mc, times(1)).showOverview("1");
    }

    @Test
    public void proceedTest() {
        apc.notNull();
        apc.refresh();
        apc.setEventID("1");
        Participant participant1 = new Participant("John", "j@gmail.com","IBAN000","BIC000");
        apc.setSelectedParticipant(participant1);
        apc.proceed();
        verify(mc, times(1)).showAddParticipantFilled("1",participant1);
    }
}

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

import java.util.HashMap;
import java.util.Map;

import static com.google.inject.Guice.createInjector;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(ApplicationExtension.class)
public class AddParticipantCtrlTest {
    private static final Injector INJECTOR = createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);
    @Mock
    ServerUtils su;
    @Mock
    MainCtrl mc;
    @Mock
    MainCtrl mainCtrl;
    @InjectMocks
    AddParticipantCtrl apc;

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
        mainCtrl = mock(MainCtrl.class);
        Map<String, String> languages = new HashMap<>();
        languages.put("EditParticipantTitle", "Edit / Remove Participant");
        languages.put("ParticipantEdit", "Edit");
        languages.put("AddParticipantTitle", "Add Participant");
        languages.put("AddButton","Add");
        when(mainCtrl.getLanguages()).thenReturn(languages);
    }

//    @Test
//    public void addTestFull() {
//        apc.notNull();
//        apc.setEventID("1");
//        apc.setName("name");
//        apc.setEmail("name");
//        apc.setBic("name");
//        apc.setIban("name");
//        apc.add();
//        verify(mc, times(1)).confirmationPopUp("Participant was added!");
//        verify(mc, times(1)).showOverview("1");
//    }

//    @Test
//    public void editTestFull() {
//        su = mock(ServerUtils.class);
//        apc = new AddParticipantCtrl(su, mc);
//        apc.notNull();
//        Participant participant1 = new Participant("John", "j@gmail.com","IBAN000","BIC000");
//        apc.setSelectedParticipant(participant1);
//        apc.setEventID("1");
//        apc.setName("name");
//        apc.setEmail("name");
//        apc.setBic("name");
//        apc.setIban("name");
//        apc.edit();
//        verify(mc, times(1)).confirmationPopUp("Participant was edited!");
//        verify(mc, times(1)).showOverview("1");
//    }

//    @Test
//    public void editTestEmpty() {
//        apc.notNull();
//        apc.setEventID("1");
//        apc.edit();
//        verify(mc, times(1)).confirmationPopUp("Participant must have a name! Please add one.");
//    }

    @Test
    public void cancelTest() {
        apc = new AddParticipantCtrl(mock(ServerUtils.class), mc);
        apc.notNull();
        apc.setEventID("1");
        apc.cancel();
        verify(mc, times(1)).showOverview("1");
    }

//    @Test
//    public void deleteTest() {
//        apc.notNull();
//        apc.setEventID("1");
//        Participant participant1 = new Participant("John", "j@gmail.com","IBAN000","BIC000");
//        apc.setSelectedParticipant(participant1);
//        apc.remove();
//        verify(mc, times(1)).confirmationPopUp("Participant was deleted!");
//        verify(mc, times(1)).showOverview("1");
//    }

    @Test
    public void showButtonTest(){
        apc = new AddParticipantCtrl(mock(ServerUtils.class), mainCtrl);
        apc.notNull();
        apc.showButton();
        assertEquals(apc.getTitle().getText(),"Edit / Remove Participant");
        assertEquals(apc.getAdd().getText(),"Edit");
    }

    @Test
    public void hideButtonTest(){
        apc = new AddParticipantCtrl(mock(ServerUtils.class), mainCtrl);
        apc.notNull();
        apc.hideButton();
        assertEquals(apc.getTitle().getText(),"Add Participant");
        assertEquals(apc.getAdd().getText(),"Add");
    }

}

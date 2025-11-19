package client.scenes;

import client.utils.EmailUtils;
import client.utils.ServerUtils;
import commons.Event;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(ApplicationExtension.class)
class SendInvitesCtrlTest {

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
    }
    @Mock
    ServerUtils su;
    @Mock
    MainCtrl mainCtrl;
    @InjectMocks
    SendInvitesCtrl sic;
    @Mock
    EmailUtils eu;
    @Mock
    Event event;
    @Test
    public void testInit() {
        sic.setEventTitle(new Label());
        sic.setInviteCode(new Label());
        sic.setEvent(event);
        sic.setEmailUtils(eu);
        when(event.getInviteCode()).thenReturn("asdf");
        when(event.getName()).thenReturn("anEvent");
        when(su.getEvent(Mockito.any())).thenReturn(event);
        sic.setEventID("3");
        assertEquals(sic.getEventTitle().getText(), "anEvent");
        assertEquals(sic.getInviteCode().getText(), "asdf");
        assertEquals(sic.getEmailUtils(), eu);
    }
    @Test
    public void sendInvitesTest() {
        sic.setInput(new TextArea("first\nsecond\nthird"));
        sic.setEvent(new Event());
        sic.setEvent(event);
        sic.setEmailUtils(eu);
        doNothing().when(eu).sendEmails(Mockito.any(), Mockito.any());
        sic.sendInvites();
        verify(su, times(3)).addParticipant(Mockito.any(), Mockito.any());
        sic.setInput(new TextArea());
        sic.sendInvites();
        verify(mainCtrl, times(2)).showOverview(Mockito.any());
        assertSame(sic.isEmailConfigured(), false);
    }
}
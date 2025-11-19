package server.scenes;

import com.google.inject.Guice;
import com.google.inject.Injector;
import commons.Event;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testfx.api.FxRobot;
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import server.utils.ServerUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(ApplicationExtension.class)
class MainViewTest {
    private static final Injector INJECTOR = Guice.createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);
    @BeforeAll
    public static void setupSpec() {
        System.setProperty("java.awt.headless", "true");
        System.setProperty("testfx.robot", "glass");
        System.setProperty("testfx.headless", "true");
        System.setProperty("prism.order", "sw");
        System.setProperty("prism.text", "t2k");
    }
    String str = "";
    @Start
    private void start(Stage stage) {
        MockitoAnnotations.initMocks(this);
        var admin = FXML.load(AdminCtrl.class, "server", "scenes", "admin-password.fxml");
        str = admin.getKey().getPassword();
        var login = FXML.load(LoginCtrl.class, "server", "scenes", "success.fxml");
        var events = FXML.load(EventOverviewCtrl.class, "server", "scenes", "event-overview.fxml");
        var pc = INJECTOR.getInstance(PrimaryCtrl.class);
        pc.init(stage, admin, login, events);
    }

    @Test
    public void testLogin(FxRobot robot) {
        robot.clickOn("#loginField").write(str);
        robot.clickOn(".button");
        Assertions.assertThat(robot.lookup(".button").queryAs(Button.class)).hasText("Go to: Event Overview");
    }
    @Mock
    PrimaryCtrl pcc;
    @Mock
    ServerUtils su;
    @InjectMocks
    LoginCtrl lc;
    @Mock
    TableView<Event> table;
    @InjectMocks
    EventOverviewCtrl eoc;
    @Mock
    TableView.TableViewSelectionModel<Event> smodel;
    @Test
    public void loginCtrlTest() {
        lc.goTo();
        verify(pcc, times(1)).showEventOverview();
    }
    @Test
    public void deleteRefreshTest() {
        eoc.setTable(table);
        eoc.refresh();
        verify(table, times(1)).setItems(Mockito.any());
        List<Event> events = List.of(new Event(), new Event());

        when(table.getSelectionModel()).thenReturn(smodel);
        when(smodel.getSelectedItems()).thenReturn(FXCollections.observableList(events));
        eoc.delete();
        verify(su, times(2)).send(Mockito.any(), Mockito.any());
    }

    PrimaryCtrl pc = new PrimaryCtrl();
    @Mock
    EventOverviewCtrl eocc;
    @Mock
    Stage ps;
    @Test
    public void primaryCtrlTest() {
        pc.setEventOverviewCtrl(eocc);
        pc.setPrimaryStage(ps);
        pc.showEventOverview();
        verify(ps, times(1)).setScene(Mockito.any());
        verify(ps, times(1)).setTitle(Mockito.any());
        verify(eocc, times(1)).refresh();
    }

}
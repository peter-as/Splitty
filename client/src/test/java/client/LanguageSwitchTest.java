package client;

import client.scenes.*;
import client.utils.ServerUtils;
import com.google.inject.Injector;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testfx.api.FxRobot;
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import javafx.stage.Stage;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.google.inject.Guice.createInjector;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(ApplicationExtension.class)
public class LanguageSwitchTest {

    private static final Injector INJECTOR = createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);
    @Mock
    ServerUtils su;
    @Mock
    MainCtrl mc;
    @InjectMocks
    StartScreenController ssc;
    LanguageSwitcher languageSwitcher;

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

    @BeforeEach
    public void setup() {
        //when(MainCtrl.getCurrentLanguage()).thenReturn("English");
        StartScreenController ssc = mock(StartScreenController.class);
        OverviewCtrl oc = mock(OverviewCtrl.class);
        when(ssc.getComboBox()).thenReturn(new ComboBox<LanguageNode>());
        when(oc.getComboBox()).thenReturn(new ComboBox<LanguageNode>());
        languageSwitcher = new LanguageSwitcher(mock(Scene.class), mock(Scene.class), oc, mock(AddEditExpenseCtrl.class),
                mock(AddParticipantCtrl.class), mock(EditRemoveParticipantCtrl.class),
                ssc, mock(SettleDebtsCtrl.class), mock(StatisticsCtrl.class),
                mock(AddEditTagCtrl.class), mock(EditRemoveTagCtrl.class), mock(SendInvitesCtrl.class));
        languageSwitcher.setUp();
    }

    @Test
    public void test() {
        ComboBox<LanguageNode> cb = languageSwitcher.getStartCb();
        languageSwitcher.displayLanguages("English",cb, Paths.get("src/main/resources/client/flags"));
        languageSwitcher.cbEventListener(cb);
        languageSwitcher.changeAllLangOverview(cb, Paths.get("src/main/resources/client/config.txt"));

    }

    @Test
    public void readTest() {
        Path file = Paths.get("src/main/resources/client/config.txt");
        String lang = languageSwitcher.readLang(file);
        List<String> possible = new ArrayList<>();
        possible.add("English");
        possible.add("Dutch");
        possible.add("Bulgarian");
        assertTrue(possible.contains(lang));
    }

    @Test
    public void readingTest() {
        languageSwitcher.changeTextLang("English", "src/main/resources/client/languages/");
        List<String> template = languageSwitcher.readTemplate("src/main/resources/client/languages/");
        assertEquals("StartCreate-",template.get(0));
        Map<String, String> map = languageSwitcher.getLanguageMap();
        assertEquals("Create New Event",map.get("StartCreate"));
    }

    @Test
    public void renameTest() {
        ComboBox<LanguageNode> cb = languageSwitcher.getStartCb();
        languageSwitcher.renameAllUsed("English");
        assertEquals(1,cb.getItems().size());
    }
}

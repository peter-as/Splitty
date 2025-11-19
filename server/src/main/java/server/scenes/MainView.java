package server.scenes;

import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class MainView extends Application {
    private static final Injector INJECTOR = Guice.createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);

    /**
     * MainView main method - server side controls
     * @param args arguments
     */
    public static void main(String [] args) {
        launch(args);
    }

    /**
     * Start method for the scenes
     * @param primaryStage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     * @throws Exception if something goes wrong
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        var admin = FXML.load(AdminCtrl.class, "server", "scenes", "admin-password.fxml");
        var login = FXML.load(LoginCtrl.class, "server", "scenes", "success.fxml");
        var events = FXML.load(EventOverviewCtrl.class, "server", "scenes", "event-overview.fxml");
        var pc = INJECTOR.getInstance(PrimaryCtrl.class);
        pc.init(primaryStage, admin, login, events);
    }
}

package server.scenes;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;
import server.database.EventRepository;

public class PrimaryCtrl {
    private Stage primaryStage;
    private Scene passwordScene;

    private Scene eventOverviewScene;

    private Scene loginScene;
    private EventOverviewCtrl eventOverviewCtrl;

    /**
     * Init method for primary control
     * @param primaryStage Primary stage from argument
     * @param admin Admin controller
     */
    public void init(Stage primaryStage, Pair<AdminCtrl, Parent> admin,
                     Pair<LoginCtrl, Parent> login,
                     Pair<EventOverviewCtrl, Parent> events) {
        this.primaryStage = primaryStage;
        this.passwordScene = new Scene(admin.getValue());
        this.loginScene = new Scene(login.getValue());
        this.eventOverviewScene = new Scene(events.getValue());
        this.eventOverviewCtrl = events.getKey();
        showAdmin();
        getPrimaryStage().show();
    }

    /**
     * Show admin password scene
     */
    public void showAdmin() {
        primaryStage.setTitle("Enter password");
        primaryStage.setScene(getPasswordScene());
    }

    /**
     * Show login scene
     */
    public void showLogin() {
        primaryStage.setTitle("Successful login!");
        primaryStage.setScene(getLoginScene());
    }

    /**
     * Show event overview scene
     */
    public void showEventOverview() {
        eventOverviewCtrl.register();
        eventOverviewCtrl.refresh();
        primaryStage.setTitle("Event Overview");
        primaryStage.setScene(getEventOverviewScene());
    }

    /**
     * Getter
     * @return Primary control's primary stage
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Getter
     * @return Password scene
     */
    public Scene getPasswordScene() {
        return passwordScene;
    }

    /**
     * Getter
     * @return Event Overview scene
     */
    public Scene getEventOverviewScene() {
        return eventOverviewScene;
    }

    /**
     * Getter
     * @return Login scene
     */
    public Scene getLoginScene() {
        return loginScene;
    }

    /**
     * Setter
     * @param primaryStage primary stage
     */
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**
     * Setter
     * @param eventOverviewCtrl event overview controller
     */
    public void setEventOverviewCtrl(EventOverviewCtrl eventOverviewCtrl) {
        this.eventOverviewCtrl = eventOverviewCtrl;
    }
}
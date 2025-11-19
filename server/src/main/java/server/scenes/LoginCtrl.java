package server.scenes;

import com.google.inject.Inject;
import server.utils.ServerUtils;

public class LoginCtrl {

    private PrimaryCtrl primaryCtrl;

    /**
     * Constructor for login control
     * @param primaryCtrl The primary control
     */
    @Inject
    public LoginCtrl(PrimaryCtrl primaryCtrl) {
        this.primaryCtrl = primaryCtrl;
    }

    /**
     * Switches to the Event Overview Scene
     */
    public void goTo() {
        primaryCtrl.showEventOverview();
    }
}

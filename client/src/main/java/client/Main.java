/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package client;

import static com.google.inject.Guice.createInjector;

import client.scenes.*;
import com.google.inject.Injector;
import java.io.IOException;
import java.net.URISyntaxException;
import javafx.application.Application;
import javafx.stage.Stage;


public class Main extends Application {

    private static final Injector INJECTOR = createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);

    /**
     * Main method
     * @param args Arguments for main
     * @throws URISyntaxException  If something goes wrong with the URI
     * @throws IOException If something goes wrong with IO
     */
    public static void main(String[] args) throws URISyntaxException, IOException {
        launch(args);
    }

    /**
     * Start method
     * @param primaryStage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
      * @throws IOException When something goes wrong with IO
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
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
        mainCtrl.initialize(primaryStage, overview,
                addEditExpense, addParticipant, editRemoveParticipant,
                start, settleDebts, statistics, addEditTag, editRemoveTag,
                sendInvites);

        primaryStage.setOnCloseRequest(e -> {
            overview.getKey().stop();
        });
    }
}
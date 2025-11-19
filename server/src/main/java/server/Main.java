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

package server;

import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import server.scenes.*;

@SpringBootApplication
@EntityScan(basePackages = { "commons", "server" })
public class Main extends Application {

    private static final Injector INJECTOR = Guice.createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);

    /**
     * Main method
     * @param args args for main
     */
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
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
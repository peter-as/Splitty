package server.scenes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import commons.*;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import org.checkerframework.checker.units.qual.A;
import server.utils.ServerUtils;



public class EventOverviewCtrl implements Initializable {
    private final ServerUtils serverUtils;
    private final PrimaryCtrl primaryCtrl;
    private ObservableList<Event> data = FXCollections.observableArrayList();

    @FXML
    private TableView<Event> table;

    @FXML
    private TableColumn<Event, Long> colId;
    @FXML
    private TableColumn<Event, String> colName;
    @FXML
    private TableColumn<Event, Date> colDateOfCreation;
    @FXML
    private TableColumn<Event, Date> colLastUpdate;
    @FXML
    private TableColumn<Event, String> colInviteCode;
    @FXML
    private Button importEventButton;


    /**
     * Constructor for event overview
     * @param primaryCtrl The primary control
     */
    @Inject
    public EventOverviewCtrl(ServerUtils serverUtils, PrimaryCtrl primaryCtrl) {
        this.serverUtils = serverUtils;
        this.primaryCtrl = primaryCtrl;

    }

    /**
     * Init method that associates the data with the table columns
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        colId.setCellValueFactory(new PropertyValueFactory<>("Id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        colDateOfCreation.setCellValueFactory(new PropertyValueFactory<>("DateOfCreation"));
        colLastUpdate.setCellValueFactory(new PropertyValueFactory<>("LastUpdate"));
        colInviteCode.setCellValueFactory(new PropertyValueFactory<>("InviteCode"));
    }

    /**
     * Refreshes the event overview
     */
    public void refresh() {
        var events = serverUtils.getEvents();
        data = FXCollections.observableList(events);
        table.setItems(data);
    }

    /**
     * Register for websocket
     */
    public void register() {
        serverUtils.registerForMessages("/topic/events", event -> {
            System.out.println("Delivery");
            this.refresh();
        });
    }

    /**
     * Deletes the selected event(s). Hold CTRL and click for selecting multiple events
     */
    public void delete() {
        ObservableList<Event> selectedItems = table.getSelectionModel().getSelectedItems();
        for (Event e : selectedItems) {
            serverUtils.send("/app/events", new EventDTO(true, e));
        }
    }

    /**
     * Imports event(s) from json; file used is the one selected from the popup window.
     * Hold CTRL and click for selecting multiple events
     */
    public void eventImport() {
        FileChooser fc = new FileChooser();
        FileChooser.ExtensionFilter filter =
                new FileChooser.ExtensionFilter("Json files", "*.json");
        fc.getExtensionFilters().add(filter);
        fc.setTitle("Select event json");
        File file = fc.showOpenDialog(primaryCtrl.getEventOverviewScene().getWindow());
        if (file == null) {
            System.out.println("File not found");
            return;
        }
        String s;
        try {
            s = Files.lines(Paths.get(file.getPath())).findFirst().get();
        } catch (IOException e) {
            System.out.println("File not found");
            return;
        }
        ObjectMapper om = new ObjectMapper();
        List<Event> ev;
        try {
            ev = om.readValue(s, new TypeReference<List<Event>>(){});
        } catch (JsonProcessingException e) {
            System.out.println("Cannot map file to events!");
            return;
        }
        for (Event e : ev) {
            serverUtils.send("/app/events", new EventDTO(false, e));
        }
    }

    /**
     * Exports selected event(s) to json.
     * Hold CTRL and click for selecting multiple events
     */
    public void eventExport() {
        List<Event> events = new ArrayList<>();
        ObservableList<Event> selectedItems = table.getSelectionModel().getSelectedItems();
        for (Event ev : selectedItems) {
            if (ev == null) {
                System.out.println("No events selected");
                return;
            }

            events.add(ev);
        }

        ObjectMapper om = new ObjectMapper();
        FileChooser fc = new FileChooser();
        FileChooser.ExtensionFilter filter =
                new FileChooser.ExtensionFilter("Json files", "*.json");
        fc.getExtensionFilters().add(filter);
        fc.setTitle("Select (or create) json file");
        File file = fc.showSaveDialog(primaryCtrl.getEventOverviewScene().getWindow());
        try {
            om.writeValue(file, events);
        } catch (Exception e) {
            System.out.println("File writing unsuccessful!");
        }
    }

    /**
     * Getter
     * @return the table
     */
    public TableView<Event> getTable() {
        return table;
    }

    /**
     * Setter
     * @param table table to set
     */
    public void setTable(TableView<Event> table) {
        this.table = table;
    }
}
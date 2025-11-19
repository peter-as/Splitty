package client;

import client.scenes.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.util.Callback;

public class LanguageSwitcher {

    private HashMap<String, String> languageMap;
    private boolean eventEnabled = true;
    private StartScreenController startCtrl;
    private OverviewCtrl overviewCtrl;
    private AddEditExpenseCtrl addExpenseCtrl;
    private AddParticipantCtrl addParticipantCtrl;
    private EditRemoveParticipantCtrl editRemoveParticipantCtrl;
    private SettleDebtsCtrl settleDebtsCtrl;
    private StatisticsCtrl statisticsCtrl;
    private AddEditTagCtrl addEditTagCtrl;
    private EditRemoveTagCtrl editRemoveTagCtrl;
    private SendInvitesCtrl sendInvitesCtrl;
    private Scene startscreen;
    private Scene overview;
    private int lastLanguage;
    private ComboBox<LanguageNode> startCb;
    private ComboBox<LanguageNode> overCb;
    private final Path path = Paths.get("client/src/main/resources/client/flags");
    private final Path langPath = Paths.get("client/src/main/resources/client/config.txt");
    private final String mapPath =
            "client/src/main/resources/client/languages/";

    /**
     * Constructor for the language switcher
     * @param overview the overview controller
     * @param addEditExpense the addEditExpense controller
     * @param addParticipant the addParticipant controller
     * @param editRemoveParticipant the editRemoveParticipant controller
     * @param startScreen the startScreen controller
     * @param settleDebts the settleDebts controller
     * @param statistics the statistics controller
     * @param addEditTag the addEditTag controller
     * @param editRemoveTag the editRemoveTag controller
     * @param sendInvitesCtrl the sendInvites controller
     */
    public LanguageSwitcher(Scene startscreenScene, Scene overviewScene, OverviewCtrl overview,
                            AddEditExpenseCtrl addEditExpense,
                            AddParticipantCtrl addParticipant,
                            EditRemoveParticipantCtrl editRemoveParticipant,
                            StartScreenController startScreen,
                            SettleDebtsCtrl settleDebts,
                            StatisticsCtrl statistics,
                            AddEditTagCtrl addEditTag,
                            EditRemoveTagCtrl editRemoveTag,
                            SendInvitesCtrl sendInvitesCtrl) {
        this.startscreen = startscreenScene;
        this.overview = overviewScene;
        this.overviewCtrl = overview;
        this.addExpenseCtrl = addEditExpense;
        this.addParticipantCtrl = addParticipant;
        this.editRemoveParticipantCtrl = editRemoveParticipant;
        this.startCtrl = startScreen;
        this.settleDebtsCtrl = settleDebts;
        this.statisticsCtrl = statistics;
        this.addEditTagCtrl = addEditTag;
        this.editRemoveTagCtrl = editRemoveTag;
        this.sendInvitesCtrl = sendInvitesCtrl;

        startCb = startCtrl.getComboBox();
        overCb = overviewCtrl.getComboBox();
    }

    /**
     * Sets up the comboboxes for the startscreen and overview
     */
    public void setUp() {
        cbSetUpStartScreen(startCb);
        cbSetUpOverview(overCb);
        callAllChange(readLang(langPath));
        String current = readLang(langPath);
        displayLanguages(current, startCb, path);
        displayLanguages(current, overCb, path);
    }

    /**
     * sets up the combobox for the overview
     * @param cb the combobox
     */
    public void cbSetUpOverview(ComboBox<LanguageNode> cb) {
        cbSetUp(cb);
        cb.setOnAction(e -> {
            if (eventEnabled) {
                changeAllLangOverview(cb, langPath);
            }
        });
        updateButton(cb);

    }

    /**
     * Sets up the combobox for the startscreen
     * @param cb the combobox
     */
    public void cbSetUpStartScreen(ComboBox<LanguageNode> cb) {
        cbSetUp(cb);
        cbEventListener(cb);
        updateButton(cb);
    }

    /**
     * Sets up a combobox for language switching
     * @param cb the combobox
     */
    public void cbSetUp(ComboBox<LanguageNode> cb) {
        //I set a custom CellFactory for the combobox
        //A CellFactory renders each node in the ComboBox
        cb.setCellFactory(new Callback<>() {
            //Override the call method from the Callback interface
            //This will create a new ListCell for every node in the ComboBox
            @Override
            public ListCell<LanguageNode> call(ListView<LanguageNode> param) {
                return new ListCell<>() {
                    private final ImageView imageView = new ImageView();

                    //Here I configure the ListCell to contain the elements of a LanguageNode
                    @Override
                    protected void updateItem(LanguageNode item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            setText(item.getText());
                            imageView.setImage(item.getImage());
                            setGraphic(imageView);
                        }
                    }
                };
            }
        });
    }

    /**
     * sets up the event listener for a combobox
     * @param cb the combobox
     */
    public void cbEventListener(ComboBox<LanguageNode> cb) {
        cb.setOnAction(e -> {
            if (eventEnabled) {
                changeAllLang(cb, langPath);
            }
        });
    }

    /**
     * Updates the current selected element for the combobox to display it properly
     * @param cb the combobox
     */
    public void updateButton(ComboBox<LanguageNode> cb) {
        cb.setButtonCell(new ListCell<>() {
            private final ImageView imageView = new ImageView();

            //Here I configure the main node of the CellFactory
            //However, I only display the image
            @Override
            protected void updateItem(LanguageNode item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    //Only show the image
                    setText(null);
                    imageView.setImage(item.getImage());
                    setGraphic(imageView);
                }
            }
        });
    }

    /**
     * Adds every language to the combobox
     * @param current the currently selected language
     */
    public void displayLanguages(String current, ComboBox<LanguageNode> cb, Path path) {
        ArrayList<LanguageNode> languages = new ArrayList<>();
        int index = -1;
        List<File> files = new ArrayList<>();
        try {
            files = Files.walk(path)
                    .filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            ;
        }

        if (!files.isEmpty()) {
            for (File child : files) {
                String name = child.getName().substring(0, child.getName().length() - 4);
                Image img = new Image("client/flags/" + name + ".jpg");
                if (name.equals(current)) {
                    name += " (Active)";
                    index = languages.size();
                }
                languages.add(new LanguageNode(name, img));
            }
        }
        languages.add(new LanguageNode("Download Template",
                new Image("client/icons/download.jpg")));
        lastLanguage = index;
        cb.getItems().addAll(languages);
        //I add the specific element as the selected node
        cb.getSelectionModel().select(index);
    }

    /**
     * Removes (Active) from the inactive language, and adds it to the active one
     * @param newLang The new active language
     */
    public void renameUsed(String newLang, ComboBox<LanguageNode> cb) {
        eventEnabled = false;
        cb.getItems().clear();
        displayLanguages(newLang, cb, path);
        eventEnabled = true;
    }

    /**
     * Reads in the currently used language from the config file
     * @return the language
     */
    public String readLang(Path path) {
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            MainCtrl.setCurrentLanguage(reader.readLine().split(":")[1]);
            return MainCtrl.getCurrentLanguage();

        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Changes the selected language in the config file and in the code
     */
    public void changeAllLang(ComboBox<LanguageNode> cb, Path path) {
        String newLang = cb.getValue().getText();
        if (newLang.equals("Download Template")) {
            downTemplate(cb);
            return;
        }
        ArrayList<String> lines = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line = reader.readLine();
            while (line != null) {
                lines.add(line);
                line = reader.readLine();
            }
            reader.close();

            lines.set(0, "language:" + newLang);
            FileWriter writer = new FileWriter("client/src/main/resources/client/config.txt");
            for (String l : lines) {
                writer.write(l + "\n");
            }
            writer.close();
            MainCtrl.setCurrentLanguage(newLang);
            renameAllUsed(newLang);
            callAllChange(newLang);

        } catch (Exception e) {
            return;
        }

    }

    /**
     * Puts the content of the language files in a map,
     * then fills out the scenes with their corresponding text
     * @param newLang the new language we switched to
     */
    public void callAllChange(String newLang) {
        changeTextLang(newLang, mapPath);
        startCtrl.fillText(languageMap);
        overviewCtrl.fillText(languageMap);
        statisticsCtrl.fillText(languageMap);
        addParticipantCtrl.fillText(languageMap);
        editRemoveParticipantCtrl.fillText(languageMap);
        sendInvitesCtrl.fillText(languageMap);
        addExpenseCtrl.fillText(languageMap);
        addEditTagCtrl.fillText(languageMap);
        editRemoveTagCtrl.fillText(languageMap);
        settleDebtsCtrl.fillText(languageMap);
    }

    /**
     * Renames all the elements in all the comboboxes
     * @param newLang the new language we switched to
     */
    public void renameAllUsed(String newLang) {
        renameUsed(newLang, startCtrl.getComboBox());
        renameUsed(newLang, overviewCtrl.getComboBox());

    }

    /**
     * This method turns the language txt file into a hashmap, then calls the text changing method
     * @param lang the language we want to change the page to
     */
    public void changeTextLang(String lang, String path) {

        languageMap = new HashMap<>();
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(path + lang + ".txt"))) {
            String pair;
            while ((pair = reader.readLine()) != null) {
                languageMap.put(pair.split("-")[0], pair.split("-")[1]);
            }

        } catch (Exception e) {
            return;
        }
    }

    /**
     * Reads the template file, and fills the line into a list
     * @return the list of the lines of the template
     */
    public List<String> readTemplate(String path) {
        BufferedReader reader;
        List<String> fileContent = new ArrayList<>();
        try {
            reader = Files.newBufferedReader(Paths.get(path + "LanguageTemplate" + ".txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                fileContent.add(line);
            }
            reader.close();

        } catch (Exception e) {
            return fileContent;
        }
        return fileContent;
    }

    /**
     * This method writes the template file to the downloads folder
     * and resets the combobox to the original language
     * If the file already exists, it will give a popup
     * @param cb the combobox
     */
    public void downTemplate(ComboBox<LanguageNode> cb) {
        eventEnabled = false;
        cb.getSelectionModel().select(lastLanguage);
        eventEnabled = true;
        ObjectMapper om = new ObjectMapper();
        FileChooser fc = new FileChooser();
        FileChooser.ExtensionFilter filter =
                new FileChooser.ExtensionFilter("TEXT files (*.txt)", "*.txt");
        fc.getExtensionFilters().add(filter);
        fc.setTitle("Select (or create) txt file");
        File file;
        if (cb.equals(startCb)) {
            file = fc.showSaveDialog(startscreen.getWindow());
        } else {
            file = fc.showSaveDialog(overview.getWindow());
        }
        List<String> template = readTemplate(mapPath);
        try {
            FileWriter writer = new FileWriter(file);
            for (String line : template) {
                writer.write(line + "\n");
            }
            writer.close();

        } catch (Exception e) {
            return;
        }


    }

    /**
     * Getter for the language map
     * @return the language map used
     */
    public HashMap<String, String> getLanguageMap() {
        return languageMap;
    }

    /**
     * Changes all the languages, and refreshes overview
     * @param cb the combobox
     */
    public void changeAllLangOverview(ComboBox<LanguageNode> cb, Path langPath) {
        changeAllLang(cb, langPath);
        overviewCtrl.refresh();

    }

    /**
     * Getter for the combobox of the startscreen
     * @return returns the startscreen's combobox
     */
    public ComboBox<LanguageNode> getStartCb() {
        return startCb;
    }
}

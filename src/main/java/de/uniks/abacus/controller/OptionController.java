package de.uniks.abacus.controller;

import de.uniks.abacus.App;
import de.uniks.abacus.model.History;
import de.uniks.abacus.model.Player;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static de.uniks.abacus.Constant.*;
import static de.uniks.abacus.model.AppService.currentTime;


public class OptionController implements Controller{
    private final App app;
    private final Player player;
    private TextField originField = null;
    private TextField boundField = null;
    private MenuButton optMenuButton = null;
    private final List<ChangeListener<Number>> listenerList = new ArrayList<>();

    public OptionController( App app, Player player ) {
        this.app = app;
        this.player = player;
    }

    @Override
    public String getTitle() {
        return OPTION_TITLE;
    }

    @Override
    public void init() {

    }

    @Override
    public Parent render() throws IOException {
        // Load FXML
        final Parent parent = FXMLLoader.load(
                Objects.requireNonNull(Controller.class.getResource("/de/uniks/abacus/views/OptionsPanel.fxml")));
        // Lookup
        originField = (TextField) parent.lookup("#originField");
        boundField = (TextField) parent.lookup("#boundField");
        final Button continueButton = (Button) parent.lookup("#continueButton");
        final Button mainMenuButton = (Button) parent.lookup("#mainMenuButton");
        final Button overViewButton = (Button) parent.lookup("#overViewButton");
        optMenuButton = (MenuButton) parent.lookup("#optMenuButton");

        app.setLimitOriginBound(originField, boundField,listenerList);

        // Set button onAction
        continueButton.setOnAction(event -> {
            History history = new History().setTime(currentTime());
            player.withHistories(history);
            app.toCalculation(player, originField, boundField, optMenuButton);
        });
        mainMenuButton.setOnAction(e -> app.show(new HomepageController(this.app)));
        overViewButton.setOnAction(e -> {
            char oldOpt = optMenuButton.getText().toCharArray()[0];
            app.setStandardInputControl(oldOpt, optMenuButton, originField, boundField,
                                        Integer.parseInt(originField.getText()), Integer.parseInt(boundField.getText()));
            app.show(new OverviewController(this.app,player));
        });
        app.menuItemsSetOnAction(optMenuButton);

        app.setStandardInputControlDefault(optMenuButton,originField,boundField);

        return parent;
    }

    @Override
    public void destroy() {
        originField.lengthProperty().removeListener(listenerList.get(0));
        boundField.lengthProperty().removeListener(listenerList.get(1));
    }

    @Override
    public void keyboardListener( KeyEvent e ) {
        if(e.getCode() == KeyCode.ENTER){
            History history = new History().setTime(currentTime());
            player.withHistories(history);
            app.toCalculation(player, originField, boundField, optMenuButton);
        }
    }
}

package de.uniks.abacus.controller;

import de.uniks.abacus.App;
import de.uniks.abacus.model.Player;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import static de.uniks.abacus.Constant.*;


public class OptionController implements Controller{
    private final App app;
    private final Player player;
    private static final AtomicBoolean wasInOverview = new AtomicBoolean();
    private TextField originField = null;
    private TextField boundField = null;
    private MenuButton optMenuButton = null;

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

        app.setLimitOriginBound(originField, boundField);

        // Set button onAction
        continueButton.setOnAction(event -> app.toCalculation(player, originField, boundField, optMenuButton));
        mainMenuButton.setOnAction(e -> app.show(new HomepageController(this.app)));
        overViewButton.setOnAction(e -> {
            char oldOpt = optMenuButton.getText().toCharArray()[0];
            app.setStandardInputControl(oldOpt, optMenuButton, originField, boundField,
                                        Integer.parseInt(originField.getText()), Integer.parseInt(boundField.getText()));
            wasInOverview.set(true);
            app.show(new OverviewController(this.app,player));
        });
        app.menuItemsSetOnAction(optMenuButton);

        if(player.getResults().size() != 0) {
            char oldOpt = player.getResults().get(0).getOperation();
            /*
            // we want to take the origin and bound value from the Result Pane.
            // because the value has been saved in app
            // we can set the bound and origin to 0 in order to get the old value of origin and bound
             */
            app.setStandardInputControl(oldOpt,optMenuButton,originField,boundField,0,0);
        } else if(wasInOverview.get()){
            // we want the value that has been saved in the app, that is why this if-condition
            // is needed
            app.setStandardInputControl('0',optMenuButton,originField,boundField,0,0);
            wasInOverview.set(false);
        } else {
            app.setStandardInputControl('+',optMenuButton,originField,boundField,0,10000);
        }
        return parent;
    }

    @Override
    public void destroy() {

    }

    @Override
    public void keyboardListener( KeyEvent e ) {
        if(e.getCode() == KeyCode.ENTER){
            app.toCalculation(player, originField, boundField, optMenuButton);
        }
    }
}

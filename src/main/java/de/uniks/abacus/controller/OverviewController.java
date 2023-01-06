package de.uniks.abacus.controller;

import de.uniks.abacus.App;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import java.io.IOException;
import java.util.Objects;

public class OverviewController implements Controller{

    private final App app;

    OverviewController( App app) {
        this.app = app;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public void init() {

    }

    @Override
    public Parent render() throws IOException {
        // Load FXML
        final Parent parent = FXMLLoader.load(
                Objects.requireNonNull(Controller.class.getResource("/de/uniks/abacus/views/OverviewPanel.fxml")));
        // Lookup Start button
        final Button continueButton = (Button) parent.lookup("#ContinuePlayButton");
        final Button mainMenuButton = (Button) parent.lookup("#mainMenuButton");
        final Button deletePlayerButton = (Button) parent.lookup("#deletePlayerButton");
        // Set Start button onAction
        continueButton.setOnAction(event -> {
            app.show(new OptionController(this.app));
        });
        mainMenuButton.setOnAction(e -> {
            app.show(new HomepageController(this.app));
        });
        deletePlayerButton.setOnAction(e -> {
            app.show(new HomepageController(this.app));
        });
        return parent;
    }

    @Override
    public void destroy() {

    }

    @Override
    public void keyboardListener( KeyEvent e ) {

    }
}

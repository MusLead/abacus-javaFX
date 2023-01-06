package de.uniks.abacus.controller;

import de.uniks.abacus.App;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import java.io.IOException;
import java.util.Objects;

public class HomepageController implements Controller {
    private final App app;

    public HomepageController( App app ) {
        this.app = app;
    }

    @Override
    public String getTitle() {
        return "Homepage";
    }

    @Override
    public void init() {

    }

    @Override
    public Parent render() throws IOException {
        // Load FXML
        final Parent parent = FXMLLoader.load(
                Objects.requireNonNull(Controller.class.getResource("/de/uniks/abacus/views/Homepage.fxml")));
        // Lookup Start button
        TextField userName = (TextField) parent.lookup("#nameInput");
        final Button startButton = (Button) parent.lookup("#startButton");
        // Set Start button onAction
        startButton.setOnAction(event -> {
            app.show(new OverviewController(this.app));
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

package de.uniks.abacus.controller;

import de.uniks.abacus.App;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;

import java.io.IOException;
import java.util.Objects;

public class CalculationController implements Controller {
    private final App app;

    public CalculationController( App app ) {
        this.app = app;
    }

    @Override
    public String getTitle() {
        return "Calculation Panel";
    }

    @Override
    public void init() {

    }

    @Override
    public Parent render() throws IOException {
        // Load FXML
        final Parent parent = FXMLLoader.load(
                Objects.requireNonNull(Controller.class.getResource("/de/uniks/abacus/views/CalculationPanel.fxml")));
        // Lookup Start button
        final Button continueButton = (Button) parent.lookup("#continueButton");
        // Set Start button onAction
        continueButton.setOnAction(event -> {
            app.show(new ResultController(this.app));
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

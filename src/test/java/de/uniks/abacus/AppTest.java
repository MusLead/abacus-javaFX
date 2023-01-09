package de.uniks.abacus;

import javafx.scene.control.*;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.junit.Test;

import org.testfx.framework.junit.ApplicationTest;

import java.util.Random;

import static org.junit.Assert.*;
import static org.testfx.assertions.api.Assertions.*;

public class AppTest extends ApplicationTest
{
    private Stage stage;

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        App app = new App();
        app.start(stage);
    }

    @Test
    public void changeView() {

        final String startTitle = "PMon - Main Menu";
        final String battleTitle = "PMon - Encounter";

        //• Initialen Fenstertitel prüfen
        assertEquals(startTitle,stage.getTitle());

        //• Spielernamen „Alice“ in das dafür vorgesehenen Eingabefeld eingeben.
        clickOn("#nameInput");
        final String name = "Alice";
        write(name);
        TextField userName = lookup("#nameInput").queryAs(TextField.class);
        assertEquals(name, userName.getText());

        //• Start-Button klicken, um ein Encounter zu starten
        clickOn("#startButton");

        //• Neuen Fenstertitel prüfen
        assertEquals(battleTitle,stage.getTitle());

        //• Leave-Button klicken
        clickOn("#leaveButton");

        //• Fenstertitel erneut prüfen
        assertEquals(startTitle,stage.getTitle());

        //• Prüfen, dass das Eingabefeld für den Spielernamen leer ist
        userName = lookup("#nameInput").queryAs(TextField.class);
        assertEquals("", userName.getText());
    }


}

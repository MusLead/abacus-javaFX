package de.uniks.abacus;

import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit.ApplicationTest;

import static org.junit.Assert.assertEquals;


class AppTest extends ApplicationTest {

    private Stage stage;
    private App app;

    @Override
    public void start( Stage stage ) {
        this.stage = stage;
        app = new App();
        app.start(stage); //to App.java
    }

    @Test
    public void changeView(){
        //• Initialen Fenstertitel prüfen
        assertEquals(Constant.HOMEPAGE_TITLE,stage.getTitle());

        //• Spielernamen „Alice“ in das dafür vorgesehenen Eingabefeld eingeben.
        clickOn("#nameInput");
        final String name = "Alice";
        write(name);
        TextField userName = lookup("#nameInput").queryAs(TextField.class);
        assertEquals(name, userName.getText());

        //• Start-Button klicken, um ein Encounter zu starten
        clickOn("#startButton");

        //• Neuen Fenstertitel prüfen
        assertEquals(Constant.OPTION_TITLE,stage.getTitle());

        //• Leave-Button klicken
        clickOn("#mainMenuButton");

        //• Fenstertitel erneut prüfen
        assertEquals(Constant.HOMEPAGE_TITLE,stage.getTitle());

        //• Prüfen, dass das Eingabefeld für den Spielernamen leer ist
        userName = lookup("#nameInput").queryAs(TextField.class);
        assertEquals("", userName.getText());
    }
}
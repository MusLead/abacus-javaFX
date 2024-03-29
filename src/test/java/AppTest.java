import de.uniks.abacus.App;
import javafx.scene.control.*;
import javafx.stage.Stage;

import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import static org.junit.Assert.*;

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

        final String startTitle = "Homepage";
        final String optionTitle = "Option";

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
        assertEquals(optionTitle,stage.getTitle());

        //• Leave-Button klicken
        clickOn("#mainMenuButton");

        //• Fenstertitel erneut prüfen
        assertEquals(startTitle,stage.getTitle());

        //• Prüfen, dass das Eingabefeld für den Spielernamen leer ist
        userName = lookup("#nameInput").queryAs(TextField.class);
        assertEquals("", userName.getText());
    }
    
}

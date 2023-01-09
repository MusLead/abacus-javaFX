package de.uniks.abacus;

import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;

class AppTest extends ApplicationTest {

    private Stage stage;
    private App app;

    @Override
    public void start( Stage stage ) throws Exception {
        this.stage = stage;
        app = new App();
        app.start(stage); //to App.java
    }

    @Test
    public void changeView(){

    }
}
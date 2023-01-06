package de.uniks.abacus;

import de.uniks.abacus.controller.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    private Stage stage;
    private Controller controller;

    @Override
    public void start(Stage primaryStage)
    {
        this.stage = primaryStage;
        primaryStage.setScene(new Scene(new Label("Loading...")));
        primaryStage.setTitle("Abacus");

        show(new HomepageController(this));
        primaryStage.show();

        primaryStage.setOnCloseRequest(e -> controller.destroy());
    }

    public void show(Controller controller)
    {
        controller.init();
        try
        {
            Scene scene = stage.getScene();
            scene.setRoot(controller.render());
            //https://youtu.be/tq_0im9qc6E
            scene.setOnKeyPressed(controller::keyboardListener);

			/*
			because the size of battle Scene and login Scene are different on size
			it has to adapt with the current size of the scene!
			https://stackoverflow.com/questions/13986475/automatically-resize-stage-if-content-is-changed
			 */
            stage.sizeToScene();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            return;
        }

        /*
         * delete the old controller! change to the new one!
         * */
        if (this.controller != null)
        {
            this.controller.destroy();
        }
        this.controller = controller;
        stage.setTitle(controller.getTitle());
    }
}

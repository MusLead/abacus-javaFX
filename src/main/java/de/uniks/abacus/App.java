package de.uniks.abacus;

import de.uniks.abacus.controller.*;
import de.uniks.abacus.model.AppService;
import de.uniks.abacus.model.Player;
import de.uniks.abacus.model.Result;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Random;

import static de.uniks.abacus.Constant.*;

public class App extends Application {
    private Stage stage;
    private Controller controller;
    private int origin;
    private int bound;
    private char operation;

    private final AppService appService = new AppService();

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

    public void menuItemsSetOnAction( MenuButton optMenuButton ) {
        if(optMenuButton == null){
            throw new NullPointerException("optionMenuButton not found");
        }
        final MenuItem plusMenuItem = optMenuButton.getItems().get(0);
        final MenuItem minusMenuItem = optMenuButton.getItems().get(1);
        final MenuItem multiplicationMenuItem = optMenuButton.getItems().get(2);
        final MenuItem divisionMenuItem = optMenuButton.getItems().get(3);

        plusMenuItem.setOnAction(e -> {
            optMenuButton.setText(plusMenuItem.getText());
        });

        minusMenuItem.setOnAction(e -> {
            optMenuButton.setText(minusMenuItem.getText());
        });

        multiplicationMenuItem.setOnAction(e -> {
            optMenuButton.setText(multiplicationMenuItem.getText());
        });

        divisionMenuItem.setOnAction(e -> {
            optMenuButton.setText(divisionMenuItem.getText());
        });

    }

    public void setStandardInputControl( char operation, MenuButton optMenuButton,
                                         TextField originField, TextField boundField,
                                         int origin, int bound ) {
        if(bound != 0 || bound > origin ) {
            optMenuButton.setText(String.valueOf(operation));
            originField.setText(String.valueOf(origin));
            boundField.setText(String.valueOf(bound));
            this.origin = origin;
            this.bound = bound;
            this.operation = operation;
        } else if (origin == 0 || operation == '0'){
            originField.setText(String.valueOf(this.origin));
            boundField.setText(String.valueOf(this.bound));
            optMenuButton.setText(String.valueOf(this.operation));
        }

    }

    public void toCalculation( Player player, TextField originField, TextField boundField, MenuButton optMenuButton ) {
        Random random = new Random();
        int origin = Integer.parseInt(originField.getText());
        int bound = Integer.parseInt(boundField.getText());
        int firstValue = random.nextInt(origin, bound);
        int secondValue = random.nextInt(origin, bound);
        char operation = optMenuButton.getText().toCharArray()[0];
        this.operation = operation;
        if(operation == '/') {
            Result result = appService.checkDivisionNew(origin, bound, firstValue, secondValue);
            if(result.getResultStatus().contains(TEMP_STATUS)){
                firstValue = result.getFirstVal();
                secondValue = result.getSecondVal();
            }
        }

        show(new CalculationController(this,player,firstValue,operation,secondValue,origin,bound));
    }

    public void setLimitOriginBound( TextField originField, TextField boundField ) {
        https://stackoverflow.com/questions/22714268/how-to-limit-the-amount-of-characters-a-javafx-textfield
        originField.lengthProperty().addListener(( observable, oldValue, newValue ) -> {
            if (newValue.intValue() > oldValue.intValue()) {
                // Check if the new character is greater than LIMIT
                if (originField.getText().length() >= MAX_INT_LENGTH) {
                    // if it's MAX_INT_LENGTH (10th) character then just setText to previous one
                    originField.setText(originField.getText().substring(0, MAX_INT_LENGTH));
                }
            }
        });

        boundField.lengthProperty().addListener(( observable, oldValue, newValue ) -> {
            if (newValue.intValue() > oldValue.intValue()) {
                // Check if the new character is greater than LIMIT
                if (boundField.getText().length() >= MAX_INT_LENGTH) {
                    // if it's MAX_INT_LENGTH (10th) character then just setText to previous one
                    boundField.setText(boundField.getText().substring(0, MAX_INT_LENGTH));
                }
            }
        });
    }

}

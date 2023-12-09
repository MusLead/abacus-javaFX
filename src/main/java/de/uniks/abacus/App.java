package de.uniks.abacus;

import de.uniks.abacus.controller.*;
import de.uniks.abacus.model.*;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

import static de.uniks.abacus.Constant.*;
import static de.uniks.abacus.model.AppService.*;

public class App extends Application {
    private Stage stage;
    private Controller controller;
    private int origin;
    private int bound;
    private char operation;
    private final AppService appService = new AppService();
    private final Game coreData = appService.load();
    Player currentPlayer = null;

    public Game getCoreData() {
        return coreData;
    }

    @Override
    public void start(Stage primaryStage)
    {
        this.stage = primaryStage;
        primaryStage.setScene(new Scene(new Label("Loading...")));
        primaryStage.setTitle("Abacus");

        show(new HomepageController(this));
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception
    {
        if(currentPlayer != null){
            int indexHistory = currentPlayer.getHistories().size() - 1;
            History currentHistory = currentPlayer.getHistories().get(indexHistory);
            String currentTime = currentHistory.getTime() + currentTimeFinish();
            currentHistory.setTime(currentTime);
        }
        appService.save(coreData);
        controller.destroy();
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public void show( Controller controller)
    {
        controller.init();
        try
        {
            Scene scene = stage.getScene();
            scene.setRoot(controller.render());
            //https://youtu.be/tq_0im9qc6E KeyboardListener Tutorial
            scene.setOnKeyPressed(controller::keyboardListener);
			//https://stackoverflow.com/questions/13986475/automatically-resize-stage-if-content-is-changed
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

    public void menuItemsSetOnAction( MenuButton optMenuButton )
    {
        if(optMenuButton == null){
            throw new NullPointerException("optionMenuButton not found");
        }
        final MenuItem plusMenuItem = optMenuButton.getItems().get(0);
        final MenuItem minusMenuItem = optMenuButton.getItems().get(1);
        final MenuItem multiplicationMenuItem = optMenuButton.getItems().get(2);
        final MenuItem divisionMenuItem = optMenuButton.getItems().get(3);

        plusMenuItem.setOnAction(e -> optMenuButton.setText(plusMenuItem.getText()));

        minusMenuItem.setOnAction(e -> optMenuButton.setText(minusMenuItem.getText()));

        multiplicationMenuItem.setOnAction(e -> optMenuButton.setText(multiplicationMenuItem.getText()));

        divisionMenuItem.setOnAction(e -> optMenuButton.setText(divisionMenuItem.getText()));

    }
    
    public void setStandardInputControl( char operation, MenuButton optMenuButton,
                                         TextField originField, TextField boundField,
                                         int origin, int bound )
    {
        if(bound != 0 || bound > origin ) {
            this.origin = origin;
            this.bound = bound;
            this.operation = operation;
        }
        setStandardInputControlDefault(optMenuButton,originField,boundField);
    }

    /**
     * this function shows a default value if the bound is 0 and the origin also 0
     * otherwise, it will give a value that has been saved by previous operation
     * @param optMenuButton Operation button
     * @param originField under bound value for random
     * @param boundField upper bound value for random
     */
    public void setStandardInputControlDefault(MenuButton optMenuButton,
                                         TextField originField, TextField boundField)
    {
        if(this.bound == 0 && this.origin == 0){
            //if the bound and origin still 0 set the default value.
            // by default origin = 0
            this.bound = 10000;
            this.operation = '+';
        }
        originField.setText(String.valueOf(this.origin));
        boundField.setText(String.valueOf(this.bound));
        optMenuButton.setText(String.valueOf(this.operation));

    }

    /**
     * This Function is being used especially in OptionController.
     * But we want to make sure
     * that the value of the option remains the same after a game (continue after submit the value)
     * so it is being set here.
     * @param player the player who plays the game
     * @param originField the lower bound
     * @param boundField the upper bound
     * @param optMenuButton the operation
     */
    public void toCalculation( Player player, TextField originField, TextField boundField, MenuButton optMenuButton )
    {
        this.currentPlayer = player;
        Random random = new Random();
        try {
            int origin = Integer.parseInt(originField.getText());
            int bound = Integer.parseInt(boundField.getText());
            int firstValue = random.nextInt(origin, bound);
            int secondValue = random.nextInt(origin, bound);
            char operation = optMenuButton.getText().toCharArray()[0];
            this.operation = operation;
            if (operation == '/' || operation == '*') {
                Result result = null;
                switch (operation){
                    case '/'-> result = appService.checkDivision(origin, bound, firstValue, secondValue);
                    case '*'-> result = appService.checkMultiplicationLimit(origin, bound, firstValue, secondValue);
                }
                if (result.getResultStatus().contains(TEMP_STATUS)) {
                    firstValue = result.getFirstVal();
                    secondValue = result.getSecondVal();
                }
            }
            show(new CalculationController(this, player, firstValue, operation, secondValue, origin, bound));
        } catch (Exception e){
            showDialog("Ups... Something is wrong",e.getMessage());
            if(e instanceof NumberFormatException){
                /*
                TODO update
                 if you want you could put setLimitOriginBounds for multiplication only lower...
                 that will increase the overhead of the listeners, but it could maybe help users only input
                 sufficient value.
                 Remember to change the setLimitOriginBounds to other listeners,
                 if the operations change (delete the old listener and add new listeners)
                 */
                // this occurs because the bounds are too high. suppose it is for multiplication
                // the set the number below
                boundField.setText("10000");
                originField.setText("9999");
            }
        }
    }

    /**
     * Limit the input text while typing! <br> <br>
     * WARNING!
     * DO NOT FORGET! <br>
     * This listener will be used in another class, if the scene from the class is closed,
     * then delete the listener too!
     * <a href="https://stackoverflow.com/questions/22714268/how-to-limit-the-amount-of-characters-a-javafx-textfield"> more information </a>
     * @param originField the lower bound
     * @param boundField the upper bound
     * @param listenerList the listener to make sure the digits are within the bound!
     */
    public void setLimitOriginBound( TextField originField, TextField boundField,
                                     List<ChangeListener<Number>> listenerList)
    {
        listenerList.add(origin_bound_listener(originField));
        listenerList.add(origin_bound_listener(boundField));
    }

    protected ChangeListener<Number> origin_bound_listener( TextField field )
    {
        ChangeListener<Number> originListener = ( observable, oldValue, newValue ) -> {
            if (newValue.intValue() > oldValue.intValue()) {
                // Check if the new character is greater than the LIMIT
                if (field.getText().length() >= MAX_INT_LENGTH) {
                    // if it's MAX_INT_LENGTH (10th) character then just setText to the previous one
                    field.setText(field.getText().substring(0, MAX_INT_LENGTH));
                }
            }
        };
        field.lengthProperty().addListener(originListener);
        return originListener;
    }

    public void deletePlayer (Player player)
    {
        coreData.withoutPlayers(player);
    }

    public void updateStageSize()
    {
        stage.sizeToScene();
    }

    public Alert showDialog(String header, String text)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(header);
        alert.setContentText(text);
        alert.initOwner(stage);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.show();
        return alert;
    }
}

package de.uniks.abacus.controller;

import de.uniks.abacus.App;
import de.uniks.abacus.model.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.Objects;

import static de.uniks.abacus.Constant.*;


public class CalculationController implements Controller {
    private final App app;
    private final Player player;
    private final int firstValue;
    private final char operation;
    private final int secondValue;
    private final int origin;
    private final int bound;
    private TextField answerField;

    public CalculationController( App app, Player player,
                                  int firstValue, char operation, int secondValue,
                                  int origin, int bound ) {
        this.app = app;
        this.player = player;
        this.firstValue = firstValue;
        this.operation = operation;
        this.secondValue = secondValue;
        this.origin = origin;
        this.bound = bound;
    }

    @Override
    public String getTitle() {
        return CALCULATION_TITLE;
    }

    @Override
    public void init() {

    }

    @Override
    public Parent render() throws IOException {
        // Load FXML
        final Parent parent = FXMLLoader.load(
                Objects.requireNonNull(Controller.class.getResource("/de/uniks/abacus/views/CalculationPanel.fxml")));
        // Lookup
        answerField = (TextField) parent.lookup("#answerField");
        // https://stackoverflow.com/questions/22714268/how-to-limit-the-amount-of-characters-a-javafx-textfield
        answerField.lengthProperty().addListener(( observable, oldValue, newValue ) -> {
            if (newValue.intValue() > oldValue.intValue()) {
                // Check if the new character is greater than LIMIT
                if (answerField.getText().length() >= MAX_INT_LENGTH) {
                    // if it's MAX_INT_LENGTH (10th) character then just setText to previous one
                    answerField.setText(answerField.getText().substring(0, MAX_INT_LENGTH));
                }
            }
        });
        final Text taskText = (Text) parent.lookup("#taskText");
        final Button continueButton = (Button) parent.lookup("#continueButton");
        // Set Start button onAction
        taskText.setText(firstValue + " " + operation + " "+ secondValue + " =");
        continueButton.setOnAction(event -> toResultScene(answerField));

        return parent;
    }

    private void toResultScene( TextField answerField ) {
        AppService appService = new AppService();
        int userInput;
        try {
            userInput = Integer.parseInt(answerField.getText());
            Result result = appService.creatNewResult(player, firstValue, operation,
                                                      secondValue, userInput);
            if(!app.getCoreData().getPlayers().contains(result.getPlayer())) {
                //if app DOES NOT CONTAIN Player, then add them to the app!
                app.getCoreData().withPlayers(result.getPlayer());
            }
            app.show(new ResultController(this.app,result, origin, bound));
        } catch(NumberFormatException e){
            System.out.println("invalid input!");
            System.err.println("because " + e.getMessage() + " or more than 10 digits");
            //FIXME ALERT is not showing!
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("The value is invalid!");
        }
    }

    @Override
    public void destroy() {

    }

    @Override
    public void keyboardListener( KeyEvent e ) {
        if(e.getCode() == KeyCode.ENTER){
            toResultScene(answerField);
        }
    }

}

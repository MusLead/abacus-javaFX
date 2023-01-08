package de.uniks.abacus.controller;

import de.uniks.abacus.App;
import de.uniks.abacus.model.Result;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.*;

import static de.uniks.abacus.Constant.CORRECT;
import static de.uniks.abacus.Constant.RESULT_TITLE;
import static de.uniks.abacus.model.AppService.currentTimeFinish;

public class ResultController implements Controller{
    private final App app;
    private final Result result;
    private final int origin;
    private final int bound;
    private final List<Node> resultsSettings = new ArrayList<>();
    private Parent parent;
    private Accordion accordion;

    public ResultController( App app, Result result, int origin, int bound ) {
        this.app = app;
        this.result = result;
        this.origin = origin;
        this.bound = bound;
    }

    @Override
    public String getTitle() {
        return RESULT_TITLE;
    }

    @Override
    public void init() {

    }

    @Override
    public Parent render() throws IOException {
        // Load FXML
        parent = FXMLLoader.load(
                Objects.requireNonNull(Controller.class.getResource("/de/uniks/abacus/views/ResultPanel.fxml")));

        if(result == null){
            //FIXME the alert is not showing on the screen!
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Program Error!");
            alert.setContentText("Result is empty, please contact developer!");
            alert.setOnCloseRequest(e -> System.exit(1));
        }
        assert result != null;

        // Lookup
        final Text statusText = (Text) parent.lookup("#StatusText");
        final Button mainMenuButton = (Button) parent.lookup("#mainMenuButton");
        final Button continueButton = (Button) parent.lookup("#continueButton");
        final Button overViewButton = (Button) parent.lookup("#overViewButton");
        accordion = (Accordion) parent.lookup("#accordionSetting");

        getSettingsNode(accordion, resultsSettings, parent);

        //set default value for the settings!
        try{
            app.menuItemsSetOnAction((MenuButton) resultsSettings.get(0));
            app.setStandardInputControl(result.getOperation(),
                                        (MenuButton) resultsSettings.get(0),
                                        (TextField) resultsSettings.get(1),
                                        (TextField) resultsSettings.get(2), origin, bound);
        } catch (Exception e){
            System.err.println(e.getMessage());
        }

        //Wrong or False Status (Header)
        statusText.setText(result.getResultStatus());

        //Show the correct or wrong answer!
        HBox answerContainer = (HBox) parent.lookup("#answerContainer");
        answerContainer.getChildren().clear();
        answerContainer.setSpacing(30);
        answerContainer.setAlignment(Pos.CENTER);
        String inputAnswerString = result.getFirstVal() + " " + result.getOperation() + " " +
                result.getSecondVal() + " = " + result.getResultVal();
        VBox inputsAnswer;
        inputsAnswer = new VBox(new Text("Your Answer:"),
                                new Text(inputAnswerString));
        int spacingVBox = 20;
        inputsAnswer.setAlignment(Pos.CENTER);
        inputsAnswer.setSpacing(spacingVBox);
        if(result.getResultStatus().contains(CORRECT)){
            answerContainer.getChildren().add(inputsAnswer);
        } else {
            String computerAnswerString = result.getFirstVal() + " " + result.getOperation() + " " +
                    result.getSecondVal() + " = " + result.getRightVal();
            VBox computerAnswer = new VBox(new Text("Correct Answer:"),
                                         new Text(computerAnswerString));
            computerAnswer.setAlignment(Pos.CENTER);
            computerAnswer.setSpacing(spacingVBox);
            answerContainer.getChildren().addAll(inputsAnswer,computerAnswer);
        }


        // Set button onAction
        continueButton.setOnAction(event -> {
            getSettingsNode(accordion, resultsSettings, parent);
            app.toCalculation(result.getPlayer(),
                              (TextField) Objects.requireNonNull(resultsSettings.get(1)),
                              (TextField) Objects.requireNonNull(resultsSettings.get(2)),
                              (MenuButton) Objects.requireNonNull(resultsSettings.get(0)));
        });
        mainMenuButton.setOnAction(e -> {
            result.getHistory().setTime(result.getHistory().getTime() + currentTimeFinish());
            app.show(new HomepageController(this.app));
        });
        overViewButton.setOnAction(e -> {
            result.getHistory().setTime(result.getHistory().getTime() + currentTimeFinish());
            app.show(new OverviewController(this.app, result.getPlayer()));
        });
        return parent;
    }

    private void getSettingsNode( Accordion accordion, List<Node> resultsSettings, Parent parent ) {
        MenuButton optMenuButton = null;
        TextField originField = null, boundField = null;

        if(accordion != null){
            TitledPane pane = accordion.getPanes().get(0);
            VBox settingContent = (VBox) pane.getContent();
            HBox inputSettingsContent = (HBox) settingContent.getChildren().get(1);

            for (Node node : inputSettingsContent.getChildren()) {
                if (node.getId() != null) {
                    switch (node.getId()) {
                        case "optMenuButton" -> optMenuButton = (MenuButton) node;
                        case "originField" -> originField = (TextField) node;
                        case "boundField" -> boundField = (TextField) node;
                        default -> System.err.println("node is not yet initialized in ResultController.java!");
                    }
                }
            }
        } else {
             originField = (TextField) parent.lookup("#originField");
             boundField = (TextField) parent.lookup("#boundField");
             optMenuButton = (MenuButton) parent.lookup("#optMenuButton");
        }
        resultsSettings.add(optMenuButton);
        resultsSettings.add(originField);
        resultsSettings.add(boundField);
        app.setLimitOriginBound(Objects.requireNonNull(originField), Objects.requireNonNull(boundField));
    }

    @Override
    public void destroy() {

    }

    @Override
    public void keyboardListener( KeyEvent e ) {
        if(e.getCode() == KeyCode.ENTER) {
            getSettingsNode(accordion, resultsSettings, parent);
            app.toCalculation(result.getPlayer(),
                              (TextField) Objects.requireNonNull(resultsSettings.get(1)),
                              (TextField) Objects.requireNonNull(resultsSettings.get(2)),
                              (MenuButton) Objects.requireNonNull(resultsSettings.get(0)));
        }
    }
}

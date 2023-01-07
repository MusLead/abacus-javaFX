package de.uniks.abacus.controller;

import de.uniks.abacus.App;
import de.uniks.abacus.model.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.Objects;

public class OverviewController implements Controller{

    //TODO: add scrollpane!!!

    private final App app;
    private final Player player;

    public OverviewController( App app, Player player ) {
        this.app = app;
        this.player = player;
    }

    @Override
    public String getTitle() {
        return "Overview";
    }

    @Override
    public void init() {

    }

    @Override
    public Parent render() throws IOException {
        // Load FXML
        final Parent parent = FXMLLoader.load(
                Objects.requireNonNull(Controller.class.getResource("/de/uniks/abacus/views/OverviewPanel.fxml")));
        // Lookup Start button
        final Button continueButton = (Button) parent.lookup("#ContinuePlayButton");
        final Button mainMenuButton = (Button) parent.lookup("#mainMenuButton");
        final Button deletePlayerButton = (Button) parent.lookup("#deletePlayerButton");

        final Text nameText = (Text) parent.lookup("#nameText");
        final Text correctText = (Text) parent.lookup("#correctText");
        final Text wrongText = (Text) parent.lookup("#wrongText");
        final Accordion historyAccordion = (Accordion) parent.lookup("#historyAccordion");
        historyAccordion.getPanes().clear();
        nameText.setText(player.getName());
        correctText.setText("Correct: " + player.getRightSum());
        wrongText.setText("Wrong: " + player.getWrongSum());

        // Set Start button onAction
        continueButton.setOnAction(event -> {
            player.withHistories(new History().setTime(AppService.currentTime()));
            app.show(new OptionController(this.app,player));
        });
        mainMenuButton.setOnAction(e -> {
            app.show(new HomepageController(this.app));
        });
        deletePlayerButton.setOnAction(e -> {
            app.show(new HomepageController(this.app));
        });

        setAccordionHistory(historyAccordion);
        return parent;
    }

    private void setAccordionHistory( Accordion historyAccordion ) {
        if(player.getHistories().size() != 0) {
            for (History history : player.getHistories()) {
                VBox statusContainer = new VBox();
                for (Result result: history.getResults()) {
                    String resultText = result.getFirstVal() + " " + result.getOperation() + " " + result.getSecondVal()
                            + " = " + result.getResultVal() + " | " + result.getResultStatus();
                    Text text = new Text(resultText);
                    statusContainer.getChildren().add(text);
                }
                statusContainer.setSpacing(15);
                TitledPane titledPane = new TitledPane(history.getTime(),statusContainer);
                historyAccordion.getPanes().add(titledPane);
            }
        }
    }

    @Override
    public void destroy() {

    }

    @Override
    public void keyboardListener( KeyEvent e ) {

    }
}

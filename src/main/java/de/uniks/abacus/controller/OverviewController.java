package de.uniks.abacus.controller;

import de.uniks.abacus.App;
import de.uniks.abacus.model.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.Objects;

import static de.uniks.abacus.Constant.*;

public class OverviewController implements Controller{
    private final App app;
    private final Player player;

    public OverviewController( App app, Player player ) {
        this.app = app;
        this.player = player;
    }

    @Override
    public String getTitle() {
        return OVERVIEW_TITLE;
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

        final ScrollPane historyScrollPane = (ScrollPane) parent.lookup("#historyScrollPane");
        final Accordion historyAccordion = (Accordion) historyScrollPane.getContent();

        if(!IS_DEBUG){
            //if debug is true, then we want to see the default result of the FXML!
            historyAccordion.getPanes().clear();
        }
        nameText.setText(player.getName());
        correctText.setText("Correct: " + player.getRightSum());
        wrongText.setText("Wrong: " + player.getWrongSum());

        // Set Start button onAction
        continueButton.setOnAction(event -> app.show(new OptionController(this.app, player)));
        mainMenuButton.setOnAction(e -> app.show(new HomepageController(this.app)));
        deletePlayerButton.setOnAction(e -> {
            app.deletePlayer(player);
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
                String title = history.getTime() + " | Correct: " + history.getRightResultTotal() + "  Wrong: " + history.getWrongResultTotal();
                TitledPane titledPane = new TitledPane(title,statusContainer);
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

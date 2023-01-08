package de.uniks.abacus.controller;

import de.uniks.abacus.App;
import de.uniks.abacus.model.Player;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.Objects;

public class PlayerSlotController implements Controller{
    private final App app;
    private final Player player;

    public PlayerSlotController( App app, Player player ) {
        this.app = app;
        this.player = player;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public void init() {

    }

    @Override
    public Parent render() throws IOException {
        final Parent parent = FXMLLoader.load(
                Objects.requireNonNull(Controller.class.getResource("/de/uniks/abacus/views/PlayersSlot.fxml")));
        //Lookup
        final Text nameText = (Text) parent.lookup("#nameText");
        final Text correctText = (Text) parent.lookup("#correctText");
        final Text wrongText = (Text) parent.lookup("#wrongText");
        final Button deleteButton = (Button) parent.lookup("#deleteButton");
        final Button continueButton = (Button) parent.lookup("#continueButton");
        String name = player.getName();
        nameText.setText(name.toCharArray().length > 8 ? name.substring(0,8) + ".." : name);
        correctText.setText("Correct: " + player.getRightSum());
        wrongText.setText("Wrong: " + player.getWrongSum());
        deleteButton.setOnAction(event -> app.deletePlayer(player));
        continueButton.setOnAction(event -> app.show(new OverviewController(app, player)));
        return parent;
    }

    @Override
    public void destroy() {

    }

    @Override
    public void keyboardListener( KeyEvent e ) {

    }
}

package de.uniks.abacus.controller;

import de.uniks.abacus.App;
import de.uniks.abacus.model.Player;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Objects;

import static de.uniks.abacus.Constant.*;

public class HomepageController implements Controller {
    private final App app;

    private TextField userName;

    private final List<PropertyChangeListener> playerListnerList = new ArrayList<>();
    public HomepageController( App app ) {
        this.app = app;
    }


    @Override
    public String getTitle() {
        return HOMEPAGE_TITLE;
    }

    @Override
    public void init() {

    }

    @Override
    public Parent render() throws IOException {
        // Load FXML
        final Parent parent = FXMLLoader.load(
                Objects.requireNonNull(Controller.class.getResource("/de/uniks/abacus/views/Homepage.fxml")));
        // Lookup
        userName = (TextField) parent.lookup("#nameInput");
        /* https://stackoverflow.com/questions/30971407/javafx-is-it-possible-to-have-a-scroll-bar-in-vbox
        * try putting VBox inside a scrollPane. With this approach,
        * you don't have to worry about setting the prefHeight
        * and prefWidth attributes. You can use 'USE_COMPUTED_SIZE' for the VBox.
        * Example: VBox is added inside a ScrollPane, and various
        * elements like HBox, Labels, and rectangles are added
        * dynamically.
        * */
        final ScrollPane homepageScrollPane = (ScrollPane) parent.lookup("#homepageScrollPane");
        final VBox playerBar = (VBox) homepageScrollPane.getContent();
        final Button startButton = (Button) parent.lookup("#startButton");

        // Set Start button onAction
        startButton.setOnAction(event -> toControlPanel(userName));

        int id = 0;
        for (Player player: app.getCoreData().getPlayers()) {
            addPlayerSlot(parent, playerBar, id, player);
            id++;
        }

        creatExamplePlayersBar(playerBar,parent);

        return parent;
    }

    private void addPlayerSlot( Parent parent, VBox playerBar, int id, Player player ) throws IOException {
        PropertyChangeListener playerListener = e -> {
            /*
             * In case we are in the Homepage and we want to delete
             * the player, then do this!
             */
            if(e.getNewValue() == null) {
                try {
                    // Get the Children of the VBox
                    for (Node playerSlot : playerBar.getChildren()) {
                        // if the Node has HBox then continue to the next code!
                        if (playerSlot instanceof HBox playerSlotHBox) {
                            Text text = (Text) playerSlotHBox.getChildren().get(0);
                            // find Text with e.geOldValue()
                            if (e.getOldValue() == text.getText()) {
                                playerBar.getChildren().remove(playerSlot);
                            }
                        }
                    }
                } catch (ConcurrentModificationException exception) {
                    //FIXME why it gave me this Exception  everytime??
                    System.err.println("IGNORE: " + exception.getLocalizedMessage());
                }
            }
            parent.autosize();
            app.updateStageSize();
        };
        player.listeners().addPropertyChangeListener(Player.PROPERTY_NAME, playerListener);
        playerListnerList.add(playerListener);

        PlayerSlotController playerSlotController = new PlayerSlotController(app, player);
        playerSlotController.init();
        player.setId(id);
        playerBar.getChildren().add(playerSlotController.render());
    }

    private void toControlPanel( TextField userName ) {
        if(!userName.getText().equals("")){
            //if the text NOT EMPTY then do this!
            Player player = new Player()
                    .setName(userName.getText())
                    .withHistories();
            app.show(new OptionController(this.app, player));
        } else {
            //TODO make a warning that the name should not be empty!
        }
    }

    @Override
    public void destroy() {
        for (int i = 0; i < app.getCoreData().getPlayers().size() ; i++) {
            Player currentPlayer = app.getCoreData().getPlayers().get(i);
            PropertyChangeListener listener = playerListnerList.get(i);
            currentPlayer.listeners().removePropertyChangeListener(Player.PROPERTY_NAME,listener);
        }
    }

    @Override
    public void keyboardListener( KeyEvent e ) {
        if(e.getCode() == KeyCode.ENTER){
            toControlPanel(userName);
        }
    }

    public void creatExamplePlayersBar(VBox playerBar, Parent parent) throws IOException {
        if(IS_DEBUG){
            for (int i = 0; i < 20; i++) {
                Player player = new Player().setName("player " + i);
                addPlayerSlot(parent, playerBar, i, player);
            }
        }
    }
}

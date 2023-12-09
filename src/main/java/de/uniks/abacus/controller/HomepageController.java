package de.uniks.abacus.controller;

import de.uniks.abacus.App;
import de.uniks.abacus.model.*;
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

import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.ConcurrentModificationException;
import java.util.Objects;

import static de.uniks.abacus.Constant.*;
import static de.uniks.abacus.model.AppService.checkName;

public class HomepageController implements Controller {
    private final App app;

    private TextField userName;

    PropertyChangeListener gameListener;

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
        final HBox mainScene = (HBox) parent.lookup("#mainScene");

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

        if( app.getCoreData().getPlayers().isEmpty() && !IS_DEBUG){
            mainScene.getChildren().remove(homepageScrollPane);
        }

        gameListener = val -> {

            if(val.getNewValue() == null) { // only if we do not add a new user!
                /*
                 * In case we are in the Homepage, and we want to delete
                 * the player, then do this!
                 */
                try {
                    // Get the Children of the VBox
                    for (Node playerSlot : playerBar.getChildren()) {
                        // if the Node has HBox, then continue to the next code!
                        if (playerSlot instanceof HBox) {

                            // find Text with e.getOldValue()
                            // WARNING: that is why the name should not be the same!
                            String slotId = playerSlot.getId();
                            String oldValId =  ((Player) val.getOldValue()).getId();
                            if (oldValId.equals(slotId)) {
                                playerBar.getChildren().remove(playerSlot);
                            }
                        }
                    }
                } catch (ConcurrentModificationException exception) {
                    /* Reason of the Exception:
                     * it is not generally permissible for one thread to modify a Collection
                     * while another thread is iterating over it.
                     * In general, the results of
                     * the iteration are undefined under these circumstances.
                     * Some Iterator
                     * implementations (including those of all the general purpose collection
                     * implementations provided by the JRE) may choose to throw this exception
                     * if this behavior is detected.
                     * Iterators that do this are known as fail-fast
                     * iterators,
                     * as they fail quickly and cleanly, rather than risking arbitrary,
                     * non-deterministic behavior at an undetermined time in the future.
                     *
                     * */
                    // System.err.println("IGNORE: " + exception.getCause());
                }

                /*
                 * This if-statement makes sure that the ScrollPane (represents
                 * lists of players could be hidden if there is no player, and otherwise)
                 */
                if(mainScene.getChildren().size() < 2 && // meaning: the list of players is not being set
                        !app.getCoreData().getPlayers().isEmpty() ){
                    //if on the screen only a children from HBox and there is at least a player
                    mainScene.getChildren().add(homepageScrollPane);
                } else if(app.getCoreData().getPlayers().isEmpty() ) {
                    //only if there is no player, then remove the pane
                    mainScene.getChildren().remove(homepageScrollPane);
                }
            }
            // Make sure the size of the app suitable (UI/UX Optimisation)
            parent.autosize();
            app.updateStageSize();
        };
        Game thisGame = app.getCoreData();
        thisGame.listeners().addPropertyChangeListener(Game.PROPERTY_PLAYERS, gameListener);

        for (Player player: app.getCoreData().getPlayers()) {
            addPlayerSlot(playerBar, player);
        }

        creatExamplePlayersBar(playerBar);

        return parent;
    }

    private void addPlayerSlot( VBox playerBar,
                                Player player
    ) throws IOException {

        //show only a playerSlot in the homepage
        PlayerSlotController playerSlotController = new PlayerSlotController(app, player);
        playerSlotController.init();
        Parent playerSlot = playerSlotController.render();
        playerBar.getChildren().add(playerSlot);
        player.setId(playerSlot.getId());

    }

    private void toControlPanel( TextField userName ) {
        if( !userName.getText().isEmpty() && checkName(userName, app)){
            //if the text NOT EMPTY then do this!
            Player player = new Player()
                    .setName(userName.getText())
                    .withHistories();

            app.show(new OptionController(this.app, player));
        } else {
            app.showDialog("Ups..","name input is invalid");
        }
    }

    @Override
    public void destroy() {
        app.getCoreData().listeners().removePropertyChangeListener(Game.PROPERTY_PLAYERS, gameListener);
    }

    @Override
    public void keyboardListener( KeyEvent e ) {
        if(e.getCode() == KeyCode.ENTER){
            toControlPanel(userName);
        }
    }

    public void creatExamplePlayersBar( VBox playerBar ) throws IOException {
        if(IS_DEBUG){
            for (int i = 0; i < 20; i++) {
                Player player = new Player().setName("player " + i);
                addPlayerSlot(playerBar, player);
            }
        }
    }
}

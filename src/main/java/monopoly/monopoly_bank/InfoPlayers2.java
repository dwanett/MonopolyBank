package monopoly.monopoly_bank;

import java.net.URL;
import java.util.*;

import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import monopoly.monopoly_bank.logic.player.Player;

public class InfoPlayers2 {

    private Graphics graphics;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button back;

    @FXML
    private TextField namePlayer1;

    @FXML
    private TextField namePlayer2;

    @FXML
    private Button next;

    public InfoPlayers2(Graphics graphics) {
        this.graphics = graphics;
    }

    @FXML
    void initialize() {
        this.back.setOnAction(actionEvent -> {
            SelectCountPlayer selectCountPlayer = new SelectCountPlayer(this.getGraphics());
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SelectCountPlayer.fxml"));
            fxmlLoader.setController(selectCountPlayer);
            this.graphics.newScene(fxmlLoader, actionEvent);
        });
        this.next.setOnAction(actionEvent -> {
            ObservableList<Player> players = FXCollections.observableArrayList();
            players.add(new Player(this.namePlayer1.getText(), 15_000_000));
            players.add(new Player(this.namePlayer2.getText(), 15_000_000));
            MonopolyBank bank = new MonopolyBank(players, 0);
            GraphicsMonopolyBank gBank = new GraphicsMonopolyBank(bank, graphics);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("GameWindow.fxml"));
            fxmlLoader.setController(gBank);
            this.graphics.newScene(fxmlLoader, actionEvent);
        });
    }

    public Graphics getGraphics() {
        return graphics;
    }

    public void setGraphics(Graphics graphics) {
        this.graphics = graphics;
    }

}

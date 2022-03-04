package monopoly.monopoly_bank;

import java.net.URL;
import java.util.*;

import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

    @FXML
    private Label info;

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
            this.info.setText("");
            ObservableList<Player> players = FXCollections.observableArrayList();
            String name1 = this.namePlayer1.getText();
            String name2 = this.namePlayer2.getText();
            if (!name1.isEmpty() && !name2.isEmpty()) {
                if (!name1.equals(name2)) {
                    players.add(new Player(name1, 15_000_000));
                    players.add(new Player(name2, 15_000_000));
                    MonopolyBank bank = new MonopolyBank(players, 0);
                    GraphicsMonopolyBank gBank = new GraphicsMonopolyBank(bank, graphics);
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("GameWindow.fxml"));
                    fxmlLoader.setController(gBank);
                    this.graphics.newScene(fxmlLoader, actionEvent);
                }
                else
                    this.info.setText("Имена должны быть разными!");
            }
            else
                this.info.setText("Имена не должны быть пустыми!");
        });
    }

    public Graphics getGraphics() {
        return graphics;
    }

    public void setGraphics(Graphics graphics) {
        this.graphics = graphics;
    }

}

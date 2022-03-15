package monopoly.monopoly_bank;

import java.net.URL;
import java.util.*;

import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import monopoly.monopoly_bank.logic.player.Player;

public class InfoPlayers {

    private Graphics graphics;

    private int countPlayers;

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
    private TextField namePlayer3;

    @FXML
    private TextField namePlayer4;

    @FXML
    private VBox vBox;

    @FXML
    private Button next;

    @FXML
    private Label info;

    public InfoPlayers(Graphics graphics, int countPlayers) {
        this.graphics = graphics;
        this.countPlayers = countPlayers;

    }

    @FXML
    void initialize() {
        switch (this.countPlayers){
            case 2:
                this.vBox.getChildren().remove(this.namePlayer3);
                this.vBox.getChildren().remove(this.namePlayer4);
                break;
            case 3:
                this.vBox.getChildren().remove(this.namePlayer4);
                break;
        }

        this.back.setOnAction(actionEvent -> {
            SelectCountPlayer selectCountPlayer = new SelectCountPlayer(this.graphics);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SelectCountPlayer.fxml"));
            fxmlLoader.setController(selectCountPlayer);
            this.graphics.newScene(fxmlLoader);
        });

        this.next.setOnAction(actionEvent -> {
            this.info.setText("");
            List<String> namePlayers = getNamePlayers();
            if (namePlayers != null) {
                ObservableList<Player> players = FXCollections.observableArrayList();
                for (String name : namePlayers)
                    players.add(new Player(name, 15_000_000));
                MonopolyBank bank = new MonopolyBank(players, 0);
                GraphicsMonopolyBank gBank = new GraphicsMonopolyBank(bank, graphics);
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("GameWindow.fxml"));
                fxmlLoader.setController(gBank);
                this.graphics.newScene(fxmlLoader);
            }
        });
    }

    private List<String> getNamePlayers(){
        List<String> namePlayers = new ArrayList<>();
        String name1 = this.namePlayer1.getText();
        String name2 = this.namePlayer2.getText();
        String name3 = null;
        String name4 = null;
        if (!name1.isEmpty() && !name2.isEmpty()) {
            namePlayers.add(name1);
            namePlayers.add(name2);
            switch (this.countPlayers) {
                case 4:
                    name4 = this.namePlayer4.getText();
                    if (name4.isEmpty()) {
                        this.info.setText("Имена не должны быть пустыми!");
                        return null;
                    }
                    namePlayers.add(name4);
                case 3:
                    name3 = this.namePlayer3.getText();
                    if (name3.isEmpty()) {
                        this.info.setText("Имена не должны быть пустыми!");
                        return null;
                    }
                    namePlayers.add(name3);
                    break;
            }
        }
        else {
            this.info.setText("Имена не должны быть пустыми!");
            return null;
        }
        if (Collections.frequency(namePlayers, name1) >= 2 || Collections.frequency(namePlayers, name2) >= 2
                || Collections.frequency(namePlayers, name3) >= 2 || Collections.frequency(namePlayers, name4) >= 2) {
            this.info.setText("Имена должны отличаться");
            return null;
        }
        return namePlayers;
    }
}

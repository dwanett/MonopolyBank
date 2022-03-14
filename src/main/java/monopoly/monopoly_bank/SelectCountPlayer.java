package monopoly.monopoly_bank;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;

public class SelectCountPlayer {

    private Graphics graphics;

    int countPlayers;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button player2;

    @FXML
    private Button player3;

    @FXML
    private Button player4;

    public SelectCountPlayer(Graphics graphics) {

        this.graphics = graphics;
        this.countPlayers = 0;
    }

    @FXML
    void initialize() {
        player2.setOnAction(actionEvent -> {
            this.countPlayers = 2;
            InfoPlayers infoPlayers = new InfoPlayers(this.graphics, this.countPlayers);
            FXMLLoader fxmlLoader = new FXMLLoader(SelectCountPlayer.class.getResource("InfoPlayers.fxml"));
            fxmlLoader.setController(infoPlayers);
            graphics.newScene(fxmlLoader);
        });

       player3.setOnAction(actionEvent -> {
           this.countPlayers = 3;
           InfoPlayers infoPlayers = new InfoPlayers(this.graphics, this.countPlayers);
           FXMLLoader fxmlLoader = new FXMLLoader(SelectCountPlayer.class.getResource("InfoPlayers.fxml"));
           fxmlLoader.setController(infoPlayers);
           graphics.newScene(fxmlLoader);
       });

       player4.setOnAction(actionEvent -> {
           this.countPlayers = 4;
           InfoPlayers infoPlayers = new InfoPlayers(this.graphics, this.countPlayers);
           FXMLLoader fxmlLoader = new FXMLLoader(SelectCountPlayer.class.getResource("InfoPlayers.fxml"));
           fxmlLoader.setController(infoPlayers);
           graphics.newScene(fxmlLoader);
       });
    }

}

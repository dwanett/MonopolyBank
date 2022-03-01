package monopoly.monopoly_bank;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;

public class SelectCountPlayer {

    private Graphics graphics;

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
        this.setGraphics(graphics);
    }

    @FXML
    void initialize() {
        player2.setOnAction(actionEvent -> {
            InfoPlayers2 infoPlayers2 = new InfoPlayers2(this.getGraphics());
            FXMLLoader fxmlLoader = new FXMLLoader(SelectCountPlayer.class.getResource("InfoPlayers2.fxml"));
            fxmlLoader.setController(infoPlayers2);
            graphics.newScene(fxmlLoader, actionEvent);
        });

       player3.setOnAction(actionEvent -> {
           InfoPlayers3 infoPlayers3 = new InfoPlayers3(this.getGraphics());
           FXMLLoader fxmlLoader = new FXMLLoader(SelectCountPlayer.class.getResource("InfoPlayers3.fxml"));
           fxmlLoader.setController(infoPlayers3);
           graphics.newScene(fxmlLoader, actionEvent);
       });

       player4.setOnAction(actionEvent -> {
           InfoPlayers4 infoPlayers4 = new InfoPlayers4(this.getGraphics());
           FXMLLoader fxmlLoader = new FXMLLoader(SelectCountPlayer.class.getResource("InfoPlayers4.fxml"));
           fxmlLoader.setController(infoPlayers4);
           graphics.newScene(fxmlLoader, actionEvent);
       });
    }

    public Graphics getGraphics() {
        return graphics;
    }

    public void setGraphics(Graphics graphics) {
        this.graphics = graphics;
    }
}

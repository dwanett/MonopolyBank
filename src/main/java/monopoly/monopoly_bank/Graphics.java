package monopoly.monopoly_bank;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;

public class Graphics extends Application {

    private Stage primaryStage;

    private String css;

    public static void main(String[] args) {
        Application.launch();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        SelectCountPlayer selectCountPlayerController = new SelectCountPlayer(this);
        this.css = this.getClass().getResource("style.css").toExternalForm();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SelectCountPlayer.fxml"));
        fxmlLoader.setController(selectCountPlayerController);
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root, 950, 550);
        scene.getStylesheets().add(this.css);
        InputStream iconStream = getClass().getResourceAsStream("logo.jpg");
        Image image = new Image(iconStream);
        primaryStage.getIcons().add(image);
        primaryStage.setTitle("Monopoly bank");
        primaryStage.setScene(scene);
        primaryStage.show();
        this.primaryStage = primaryStage;
    }

    public void newScene(FXMLLoader fxmlLoader, ActionEvent actionEvent) {
        try {
            Scene scene = new Scene(fxmlLoader.load(), primaryStage.getScene().getWidth(), primaryStage.getScene().getHeight(), false, SceneAntialiasing.BALANCED);
            scene.getStylesheets().add(this.css);
            this.primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            this.primaryStage.setScene(scene);
            this.primaryStage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public String getCss() {
        return css;
    }

    public void setCss(String css) {
        this.css = css;
    }
}

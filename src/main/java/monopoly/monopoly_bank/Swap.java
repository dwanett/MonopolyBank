package monopoly.monopoly_bank;

import java.io.IOException;
import java.lang.ref.Reference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.Window;
import monopoly.monopoly_bank.logic.player.Player;
import monopoly.monopoly_bank.logic.titledeeds.TitleDeed;


public class Swap{

    private MonopolyBank bank;

    private Stage stage;

    private ObservableList<Player> players;

    private Map<String, ListView<ImageView>> playerTitleDeadsImageMap;

    @FXML
    private TableView<Player> tableLeft;

    @FXML
    private TableView<Player> tableRight;

    @FXML
    private TableColumn<Player, String> playersLeft;

    @FXML
    private TableColumn<Player, String> playersRight;

    @FXML
    private ListView<ImageView> listLeft;

    @FXML
    private ListView<ImageView> listRight;

    @FXML
    private TextField moneyLeft;

    @FXML
    private TextField moneyRight;

    @FXML
    private Button swap;

    @FXML
    private Label info;

    public Swap(MonopolyBank bank, ObservableList<Player> players, Map<String, ListView<ImageView>> playerTitleDeadsImageMap, Stage stage) {
        this.bank = bank;
        this.stage = stage;
        this.players = FXCollections.observableArrayList(players);
        this.playerTitleDeadsImageMap = playerTitleDeadsImageMap;
        this.listLeft = new ListView<ImageView>();
        this.listRight = new ListView<ImageView>();
    }

    @FXML
    void initialize() {
        this.tableLeft.setItems(this.players);
        this.tableRight.setItems(this.players);
        this.playersLeft.setCellValueFactory(cellData -> cellData.getValue().getName());
        this.playersRight.setCellValueFactory(cellData -> cellData.getValue().getName());
        this.listLeft.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        this.listRight.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        this.tableLeft.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, selectedLeftPlayer) -> {
            if (selectedLeftPlayer != null) {
                this.listLeft.setItems(newListImagesView(selectedLeftPlayer));
            }
        });
        this.tableRight.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, selectedRightPlayer) -> {
            if (selectedRightPlayer != null) {
                this.listRight.setItems(newListImagesView(selectedRightPlayer));
            }
        });

        this.swap.setOnAction(actionEvent -> {
            this.info.setText("");
            Player selectedLeftPlayer = this.tableLeft.getSelectionModel().getSelectedItem();
            Player selectedRightPlayer = this.tableRight.getSelectionModel().getSelectedItem();
            List<TitleDeed> leftTitleDeads = new ArrayList<>();
            List<TitleDeed> rightTitleDeads = new ArrayList<>();
            int moneyLeft = takeInfoLeftPlayer(selectedLeftPlayer, leftTitleDeads);
            int moneyRight = takeInfoRightPlayer(selectedRightPlayer, rightTitleDeads);
            if (moneyLeft != -1 && moneyRight != -1) {
                bank.swap(selectedLeftPlayer, selectedRightPlayer, leftTitleDeads, rightTitleDeads, moneyLeft, moneyRight);
                this.stage.close();
            }
        });
    }

    public ObservableList<ImageView> newListImagesView(Player selectedPlayer){
        ObservableList<ImageView> newList = FXCollections.observableArrayList();
        ObservableList<ImageView> oldList = selectedPlayer.getImageViewsTitleDeadsProperty();
        for (ImageView elem : oldList){
            ImageView newImages = new ImageView(elem.getImage());
            newImages.setFitHeight(elem.getImage().getHeight() / 5);
            newImages.setFitWidth(elem.getImage().getWidth() / 5);
            newList.add(newImages);
        }
        return newList;
    }

    public int takeInfoLeftPlayer(Player player, List<TitleDeed> titleDeads) {
        if (player != null) {
            ObservableList<ImageView> selectedImage = this.listLeft.getSelectionModel().getSelectedItems();
            String moneyString = this.moneyLeft.getText();
            if (moneyString.matches("\\d+")) {
                int money = Integer.parseInt(moneyString);
                for (ImageView imageView : selectedImage) {
                    titleDeads.add(player.findTitleDeadForImageFront(imageView.getImage()));
                }
                return money;
            } else
                this.info.setText("Деньги должны быть числом");
        } else
            this.info.setText("Выберите левого игрока");
        return -1;
    }

    public int takeInfoRightPlayer(Player player, List<TitleDeed> titleDeads){
        if (player != null) {
            ObservableList<ImageView> selectedImage = this.listRight.getSelectionModel().getSelectedItems();
            String moneyString = this.moneyRight.getText();
            if (moneyString.matches("\\d+")) {
                int money = Integer.parseInt(moneyString);
                for (ImageView imageView : selectedImage) {
                    titleDeads.add(player.findTitleDeadForImageFront(imageView.getImage()));
                }
                return money;
            } else
                this.info.setText("Деньги должны быть числом");
        } else
            this.info.setText("Выберите правого игрока");
        return -1;
    }
}

package monopoly.monopoly_bank;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import monopoly.monopoly_bank.logic.player.Player;
import monopoly.monopoly_bank.logic.titledeeds.TitleDeed;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class GraphicsMonopolyBank {

    private Graphics graphics;

    private MonopolyBank bank;

    private double size;

    private ObservableList<Player> players;

    private Map<String, ListView<ImageView>> playerTitleDeadsImageMap;

    @FXML
    private ListView<ImageView> freeTitleDeadsImage;

    @FXML
    private TableView<Player> tablePlayers;

    @FXML
    private TableColumn<Player, String> player;

    @FXML
    private TableColumn<Player, Integer> money;

    @FXML
    private TableColumn<Player, ListView<ImageView>> titleDead;

    @FXML
    private TextField countSteps;

    @FXML
    private TextField countMoneyAdd;

    @FXML
    private TextField countMoneyTake;

    @FXML
    private TextField newPrice;

    @FXML
    private Button steps;

    @FXML
    private Button addMoneyPlayer;

    @FXML
    private Button takeMoneyPlayer;

    @FXML
    private Button swap;

    @FXML
    private Button mortgaged;

    @FXML
    private Button buy;

    @FXML
    private Button auction;

    @FXML
    private Label infoBuy;

    @FXML
    private Label infoStep;

    @FXML
    private Label infoMortgaged;

    @FXML
    private Label infoSwapAuctionAddMoney;


    public GraphicsMonopolyBank(MonopolyBank bank, Graphics graphics) {
        this.graphics = graphics;
        this.bank = bank;
        players = FXCollections.observableList(bank.getPlayers());
        freeTitleDeadsImage = new ListView<ImageView>();
        playerTitleDeadsImageMap = new TreeMap<>();
        size = bank.getImageViewsTitleDeads().get(1).getFitHeight();
        for (Player elem : players) {
            ListView<ImageView> cur = new ListView<ImageView>();
            cur.setOrientation(Orientation.HORIZONTAL);
            cur.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            cur.setPrefHeight(size);
            playerTitleDeadsImageMap.put(elem.getName().getValue(), cur);
        }
    }

    @FXML
    void initialize() {
        this.tablePlayers.setItems(this.players);
        this.tablePlayers.setStyle("-fx-fixed-cell-size: " + (size + 50));
        this.player.setCellValueFactory(cellData -> cellData.getValue().getName());
        this.money.setCellValueFactory(cellData1 -> cellData1.getValue().getMoney().asObject());
        this.titleDead.setCellValueFactory(cellData2 -> {
            ListView<ImageView> cur = this.playerTitleDeadsImageMap.get(cellData2.getValue().getName().getValue());
            cur.setItems(cellData2.getValue().getImageViewsTitleDeads());
            cur.setStyle("-fx-background-color: transparent");
            return new SimpleObjectProperty<>(cur);
        });

        this.freeTitleDeadsImage.setItems(this.bank.getImageViewsTitleDeads());

        this.steps.setOnAction(actionEvent -> {
            this.infoStep.setText("");
            int step = getAndCheckIntValue(this.countSteps, this.infoStep);
            if (step != -1) {
                if (step < 2 || step > 12)
                    this.infoStep.setText("Количество очков должно быть от 2 до 12");
                else
                    this.bank.walk(step);
            }
        });

        this.buy.setOnAction(actionEvent -> {
            this.infoBuy.setText("");
            ImageView selectedTitleDead = this.freeTitleDeadsImage.getSelectionModel().getSelectedItem();
            if (selectedTitleDead != null) {
                Player selectedPlayer = this.tablePlayers.getSelectionModel().getSelectedItem();
                if (selectedPlayer != null) {
                    int index = this.bank.getImageViewsTitleDeads().indexOf(selectedTitleDead);
                    selectedPlayer.buyTitleDeed(this.bank, this.bank.getFreeTitleDeads().get(index));
                }
                else
                    this.infoBuy.setText("Выберите игрока");
            }
            else
                this.infoBuy.setText("Выберите улицу");
        });

        this.auction.setOnAction(actionEvent -> {
            this.infoBuy.setText("");
            ImageView selectedTitleDead = this.freeTitleDeadsImage.getSelectionModel().getSelectedItem();
            if (selectedTitleDead != null) {
                Player selectedPlayer = this.tablePlayers.getSelectionModel().getSelectedItem();
                if (selectedPlayer != null) {
                    int index = this.bank.getImageViewsTitleDeads().indexOf(selectedTitleDead);
                    int newPrice = getAndCheckIntValue(this.newPrice, this.infoBuy);
                    if (newPrice != -1)
                        selectedPlayer.auction(this.bank, this.bank.getFreeTitleDeads().get(index), newPrice);
                }
                else
                    this.infoBuy.setText("Выберите игрока");
            }
            else
                this.infoBuy.setText("Выберите улицу");
        });

        this.mortgaged.setOnAction(actionEvent -> {
            this.infoMortgaged.setText("");
            Player selectedPlayer = this.tablePlayers.getSelectionModel().getSelectedItem();
            if (selectedPlayer != null) {
                ListView<ImageView> selectedListImages = this.playerTitleDeadsImageMap.get(selectedPlayer.getName().getValue());
                List<ImageView> selectedImages = selectedListImages.getSelectionModel().getSelectedItems();
                if (selectedImages != null && selectedImages.size() != 0) {
                    List<ImageView> selectedImagesNewList = new ArrayList<>(selectedImages);
                    for(ImageView imageView : selectedImagesNewList) {
                        TitleDeed findTitleDead = selectedPlayer.findTitleDeadForImageFront(imageView.getImage());
                        if (findTitleDead == null)
                            findTitleDead = selectedPlayer.findTitleDeadForImageBack(imageView.getImage());
                        selectedPlayer.mortgageTitleDeed(findTitleDead);
                    }
                }
                else {
                    this.infoMortgaged.setText("Выберите улицу/ы");
                }
            }
            else {
                this.infoMortgaged.setText("Выберите игрока");
            }
        });

        this.addMoneyPlayer.setOnAction(actionEvent -> {
            this.infoStep.setText("");
            int money = getAndCheckIntValue(this.countMoneyAdd, this.infoStep);
            if (money != -1) {
                Player selectedPlayer = this.tablePlayers.getSelectionModel().getSelectedItem();
                if (selectedPlayer != null) {
                    selectedPlayer.addMoney(money);
                }
                else
                    this.infoStep.setText("Выберите игрока");
            }
        });

        this.takeMoneyPlayer.setOnAction(actionEvent -> {
            this.infoStep.setText("");
            int money = getAndCheckIntValue(this.countMoneyTake, this.infoStep);
            if (money != -1) {
                Player selectedPlayer = this.tablePlayers.getSelectionModel().getSelectedItem();
                if (selectedPlayer != null) {
                    selectedPlayer.takeMoney(money);
                }
                else
                    this.infoStep.setText("Выберите игрока");
            }
        });

        this.swap.setOnAction(actionEvent -> {
            Stage stage = new Stage();
            Swap classSwap = new Swap(this.bank, this.players, this.playerTitleDeadsImageMap, stage);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Swap.fxml"));
            fxmlLoader.setController(classSwap);
            try {
                Scene scene = new Scene(fxmlLoader.load(), 1024, 700);
                String css = getClass().getResource("swap.css").toExternalForm();
                scene.getStylesheets().add(css);
                InputStream iconStream = getClass().getResourceAsStream("logo.jpg");
                Image image = new Image(iconStream);
                stage.getIcons().add(image);
                stage.setTitle("Обмен");
                stage.setScene(scene);
                stage.setResizable(false);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public int getAndCheckIntValue(TextField textField, Label messageError) {
        long value = 0;
        String str = textField.getText();
        if (!str.isEmpty()) {
            if (str.matches("\\d+")) {
                value = Long.parseLong(str);
                if (value < Integer.MAX_VALUE) {
                    return (int)value;
                }
                else
                    messageError.setText("Слишком большое число");
            }
            else
                messageError.setText("Должны быть числом");
        }
        else
            return (int)value;
        return -1;
    }
}

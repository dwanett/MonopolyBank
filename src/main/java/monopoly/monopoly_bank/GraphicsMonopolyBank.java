package monopoly.monopoly_bank;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.Parent;
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
        this.tablePlayers.setStyle("-fx-fixed-cell-size: " + (size + 20));
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
            String text = this.countSteps.getText();
            int step = 0;
            if (!text.isEmpty()) {
                if (text.matches("\\d+"))
                    step = Integer.parseInt(this.countSteps.getText());
            }
            if (step < 2 || step > 12)
                this.infoStep.setText("Количество очков должно быть от 2 до 12");
            else
                this.bank.walk(step);
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
            String text = this.countMoneyAdd.getText();
            long money = 0;
            if (!text.isEmpty())
                money = Long.parseLong(text);
            if (money < Integer.MAX_VALUE) {
                Player selectedPlayer = this.tablePlayers.getSelectionModel().getSelectedItem();
                if (selectedPlayer != null) {
                    selectedPlayer.addMoney((int)money);
                }
                else
                    this.infoStep.setText("Выберите игрока");
            }
            else {
                this.infoStep.setText("Слишком большое число!");
            }
        });

        this.takeMoneyPlayer.setOnAction(actionEvent -> {
            this.infoStep.setText("");
            String text = this.countMoneyTake.getText();
            long money = 0;
            if (!text.isEmpty())
                money = Long.parseLong(text);
            if (money < Integer.MAX_VALUE) {
                Player selectedPlayer = this.tablePlayers.getSelectionModel().getSelectedItem();
                if (selectedPlayer != null) {
                    selectedPlayer.takeMoney((int)money);
                }
                else
                    this.infoStep.setText("Выберите игрока");
            }
            else {
                this.infoStep.setText("Слишком большое число!");
            }
        });

        this.swap.setOnAction(actionEvent -> {
            Stage stage = new Stage();
            Swap classSwap = new Swap(this.bank, this.players, this.playerTitleDeadsImageMap, stage);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Swap.fxml"));
            fxmlLoader.setController(classSwap);
            try {
                Scene scene = new Scene(fxmlLoader.load(), 1024, 700);
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
}

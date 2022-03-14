package monopoly.monopoly_bank;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import monopoly.monopoly_bank.logic.player.GroupTitleDeed;
import monopoly.monopoly_bank.logic.player.Player;
import monopoly.monopoly_bank.logic.titledeeds.Street;
import monopoly.monopoly_bank.logic.titledeeds.TitleDeed;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class GraphicsMonopolyBank {

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
    private TableColumn<Player, String> money;

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
    private Label infoBuy;

    @FXML
    private Label infoStep;

    @FXML
    private Label infoMortgaged;

    @FXML
    private Label infoSwapAuctionAddMoney;

    @FXML
    private Label infoUtils;

    public GraphicsMonopolyBank(MonopolyBank bank) {
        this.bank = bank;
        this.players = FXCollections.observableList(bank.getPlayers());
        this.freeTitleDeadsImage = new ListView<ImageView>();
        this.playerTitleDeadsImageMap = new TreeMap<>();
        this.size = bank.getImageViewsTitleDeads().get(1).getFitHeight();
        for (Player elem : this.players) {
            ListView<ImageView> cur = new ListView<ImageView>();
            cur.setOrientation(Orientation.HORIZONTAL);
            cur.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            cur.setPrefHeight(this.size);
            this.playerTitleDeadsImageMap.put(elem.getName().getValue(), cur);
        }
    }

    @FXML
    void initialize() {
        this.tablePlayers.setItems(this.players);
        this.freeTitleDeadsImage.setItems(this.bank.getImageViewsTitleDeads());

        this.tablePlayers.setStyle("-fx-fixed-cell-size: " + (size + 50));

        this.player.setCellValueFactory(cellData -> cellData.getValue().getName());
        this.money.setCellValueFactory(cellData1 -> cellData1.getValue().getMoney().asString("%,d"));
        this.titleDead.setCellValueFactory(cellData2 -> {
            ListView<ImageView> cur = this.playerTitleDeadsImageMap.get(cellData2.getValue().getName().getValue());
            cur.setItems(cellData2.getValue().getImageViewsTitleDeads());
            cur.setStyle("-fx-background-color: transparent");
            return new SimpleObjectProperty<>(cur);
        });

        this.countMoneyTake.textProperty().addListener(new changListner(this.countMoneyTake));
        this.countMoneyAdd.textProperty().addListener(new changListner(this.countMoneyAdd));
        this.newPrice.textProperty().addListener(new changListner(this.newPrice));
        this.countSteps.textProperty().addListener(new changListner(this.countSteps));
    }

    public int getAndCheckIntValue(TextField textField, Label messageError) {
        long value = 0;
        String str = textField.getText().replaceAll("[^0-9]", "");
        if (!str.isEmpty()) {
            value = Long.parseLong(str);
            if (value < Integer.MAX_VALUE) {
                return (int)value;
            }
            else
                messageError.setText("Слишком большое число");
        }
        else
            return (int)value;
        return -1;
    }

    @FXML
    public void mortgaged(ActionEvent event) {
        this.infoMortgaged.setText("");
        Player selectedPlayer = this.tablePlayers.getSelectionModel().getSelectedItem();
        if (selectedPlayer != null) {
            ListView<ImageView> selectedListImages = this.playerTitleDeadsImageMap.get(selectedPlayer.getName().getValue());
            List<ImageView> selectedImages = selectedListImages.getSelectionModel().getSelectedItems();
            if (selectedImages != null && selectedImages.size() != 0) {
                List<ImageView> selectedImagesNewList = new ArrayList<>(selectedImages);
                for(ImageView imageView : selectedImagesNewList) {
                    TitleDeed findTitleDead = selectedPlayer.findTitleDeadForImage(imageView.getImage());
                    GroupTitleDeed groupTitleDeed = selectedPlayer.getTitleDeeds().get(findTitleDead.getType());
                    boolean checkLvlRent = true;
                    for (TitleDeed elem : groupTitleDeed.getGroup()){
                        if (elem.getLvlTakeRent() > 0) {
                            checkLvlRent = false;
                            break;
                        }
                    }
                    if (checkLvlRent)
                        selectedPlayer.mortgageTitleDeed(findTitleDead);
                    else
                        this.infoMortgaged.setText("Нужно снять дома или отели!");
                }
            }
            else {
                this.infoMortgaged.setText("Выберите улицу/ы");
            }
        }
        else {
            this.infoMortgaged.setText("Выберите игрока");
        }
    }

    @FXML
    public void lap(ActionEvent event){
        this.infoUtils.setText("");
        Player selectedPlayer = this.tablePlayers.getSelectionModel().getSelectedItem();
        if (selectedPlayer != null)
            selectedPlayer.nextLap();
        else
            this.infoUtils.setText("Выберите игрока");
    }

    @FXML
    public void steps(ActionEvent event){
        this.infoStep.setText("");
        int step = getAndCheckIntValue(this.countSteps, this.infoStep);
        if (step != -1) {
            if (step < 2 || step > 12)
                this.infoStep.setText("Количество очков должно быть от 2 до 12");
            else
                this.bank.walk(step);
        }
    }

    @FXML
    public void buy(ActionEvent event){
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
    }

    @FXML
    public void auction(ActionEvent event){
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
    }

    @FXML
    public void addMoneyPlayer(ActionEvent event){
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
    }

    @FXML
    public void takeMoneyPlayer(ActionEvent event){
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
    }
    @FXML
    public void takeMoneyAllPlayers(ActionEvent event){
        this.infoUtils.setText("");
        Player selectedPlayer = this.tablePlayers.getSelectionModel().getSelectedItem();
        if (selectedPlayer != null) {
            for (Player target : players) {
                if (target != selectedPlayer)
                    if (target.getMoney().getValue() < 100_000){
                        this.infoUtils.setText("У игрока " + target.getName().getValue() + " не хватает денег!");
                        return;
                    }
            }
            for (Player target : players) {
                if (target != selectedPlayer)
                    selectedPlayer.takePlayerMoney(target, 100_000);
            }
        }
        else
            this.infoUtils.setText("Выберите игрока");
    }

    @FXML
    public void swap(ActionEvent event){
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
    }

    @FXML
    public void buyHomeOrHotel(ActionEvent event) {
        this.infoMortgaged.setText("");
        Player selectedPlayer = this.tablePlayers.getSelectionModel().getSelectedItem();
        if (selectedPlayer != null) {
            ListView<ImageView> selectedListImages = this.playerTitleDeadsImageMap.get(selectedPlayer.getName().getValue());
            List<ImageView> selectedImages = selectedListImages.getSelectionModel().getSelectedItems();
            if (selectedImages != null && selectedImages.size() != 0) {
                List<ImageView> selectedImagesNewList = new ArrayList<>(selectedImages);
                for(ImageView imageView : selectedImagesNewList) {
                    TitleDeed findTitleDead = selectedPlayer.findTitleDeadForImage(imageView.getImage());
                    if (!selectedPlayer.buyHomeOrHotel(findTitleDead)) {
                        if (findTitleDead.getClass() == Street.class) {
                            if (!selectedPlayer.checkDifferenceCountHomeGroupTitleDeed(selectedPlayer.getTitleDeeds().get(findTitleDead.getType()), findTitleDead.getLvlTakeRent() + 1))
                                this.infoMortgaged.setText("Разница в количестве домов должны быть не больше 1!");
                            else if (findTitleDead.getLvlTakeRent() == 5)
                                this.infoMortgaged.setText("На этой улице уже есть отель");
                            else if (selectedPlayer.getMoney().getValue() < ((Street)findTitleDead).getPriceHome())
                                this.infoMortgaged.setText("У игрока " + selectedPlayer.getName().getValue() + " не хватает денег!");
                            else
                                this.infoMortgaged.setText("У игрока " + selectedPlayer.getName().getValue() + " нет полной цветовой группы!");
                        }
                        else
                            this.infoMortgaged.setText("Здесь нальзя ставить дома и отели!");
                    }
                }
            }
            else {
                this.infoMortgaged.setText("Выберите улицу/ы!");
            }
        }
        else {
            this.infoMortgaged.setText("Выберите игрока!");
        }
    }

    @FXML
    public void sellHomeOrHotel(ActionEvent event) {
        this.infoMortgaged.setText("");
        Player selectedPlayer = this.tablePlayers.getSelectionModel().getSelectedItem();
        if (selectedPlayer != null) {
            ListView<ImageView> selectedListImages = this.playerTitleDeadsImageMap.get(selectedPlayer.getName().getValue());
            List<ImageView> selectedImages = selectedListImages.getSelectionModel().getSelectedItems();
            if (selectedImages != null && selectedImages.size() != 0) {
                List<ImageView> selectedImagesNewList = new ArrayList<>(selectedImages);
                for(ImageView imageView : selectedImagesNewList) {
                    TitleDeed findTitleDead = selectedPlayer.findTitleDeadForImage(imageView.getImage());
                    if (!selectedPlayer.sellHomeOrHotel(findTitleDead)) {
                        if (findTitleDead.getClass() == Street.class) {
                            if (findTitleDead.getLvlTakeRent() == 0)
                                this.infoMortgaged.setText("На этой улице нет домов или отеля!");
                            else if (!selectedPlayer.checkDifferenceCountHomeGroupTitleDeed(selectedPlayer.getTitleDeeds().get(findTitleDead.getType()), findTitleDead.getLvlTakeRent() - 1))
                                this.infoMortgaged.setText("Разница в количестве домов должны быть не больше 1!");

                        }
                        else
                            this.infoMortgaged.setText("Здесь не могут быть дома и отели!");
                    }
                }
            }
            else {
                this.infoMortgaged.setText("Выберите улицу/ы!");
            }
        }
        else {
            this.infoMortgaged.setText("Выберите игрока!");
        }
    }

    private class changListner implements ChangeListener<String> {
        private final TextField textField;

        public changListner(TextField textField){
            this.textField = textField;
        }

        @Override
        public void changed(ObservableValue observable, String oldValue, String newValue) {
            String tmp = newValue.replaceAll("[^0-9]", "");
            if (tmp.isEmpty()) {
                Platform.runLater(() -> {
                    textField.setText("");
                });
            }
            else {
                long value = Long.parseLong(tmp);
                String newString = String.format("%,d", value);
                Platform.runLater(() -> {
                    int oldPosiCaret = textField.getCaretPosition();
                    int count = 0;
                    for (int i = 0; i != newValue.length() && i != oldPosiCaret; i++){
                        if (newValue.charAt(i) == '\u00a0')
                            count++;
                    }
                    if (newValue.length() > oldValue.length())
                        oldPosiCaret += count;
                    textField.setText(newString);
                    textField.positionCaret(oldPosiCaret);
                });
            }
        }
    }
}
/*
1 000
10 000
100 000
1 000 000
10 000 000
100 000 000

 */
package monopoly.monopoly_bank;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import monopoly.monopoly_bank.logic.player.GroupTitleDeed;
import monopoly.monopoly_bank.logic.player.Player;
import monopoly.monopoly_bank.logic.titledeeds.TitleDeed;


public class Swap {

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

    @FXML
    private Label moneyLeftPlayer;

    @FXML
    private Label moneyRightPlayer;

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

        this.moneyLeft.textProperty().addListener(new changListner(this.moneyLeft));
        this.moneyRight.textProperty().addListener(new changListner(this.moneyRight));

        this.tableLeft.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, selectedLeftPlayer) -> {
            if (selectedLeftPlayer != null) {
                this.listLeft.setItems(newListImagesView(selectedLeftPlayer));
                this.moneyLeftPlayer.setText(String.format("%,d", selectedLeftPlayer.getMoney().get()));
            }
        });
        this.tableRight.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, selectedRightPlayer) -> {
            if (selectedRightPlayer != null) {
                this.listRight.setItems(newListImagesView(selectedRightPlayer));
                this.moneyRightPlayer.setText(String.format("%,d", selectedRightPlayer.getMoney().get()));
            }
        });

        this.swap.setOnAction(actionEvent -> {
            this.info.setText("");
            Player selectedLeftPlayer = this.tableLeft.getSelectionModel().getSelectedItem();
            Player selectedRightPlayer = this.tableRight.getSelectionModel().getSelectedItem();
            List<TitleDeed> leftTitleDeads = new ArrayList<>();
            List<TitleDeed> rightTitleDeads = new ArrayList<>();
            int moneyLeft = takeInfo(selectedLeftPlayer, leftTitleDeads, this.listLeft, this.moneyLeft);
            int moneyRight = takeInfo(selectedRightPlayer, rightTitleDeads, this.listRight, this.moneyRight);
            boolean leftCheckLvlRent = checkHomeorHotelSelectedTitleDead(selectedLeftPlayer, leftTitleDeads);
            boolean rightCheckLvlRent = checkHomeorHotelSelectedTitleDead(selectedRightPlayer, rightTitleDeads);
            if (leftCheckLvlRent && rightCheckLvlRent) {
                if (moneyLeft != -1 && moneyRight != -1) {
                    if (bank.swap(selectedLeftPlayer, selectedRightPlayer, leftTitleDeads, rightTitleDeads, moneyLeft, moneyRight))
                        this.stage.close();
                    else {
                        if (selectedLeftPlayer.getMoney().get() < moneyLeft)
                            this.info.setText("У " + selectedLeftPlayer.getName().getValue() + " не хватает денег");
                        if (selectedRightPlayer.getMoney().get() < moneyRight)
                            this.info.setText("У " + selectedRightPlayer.getName().getValue() + " не хватает денег");
                    }
                }
            } else
                this.info.setText("На выбранных улицах не должно быть домов!");
        });
    }

    public boolean checkHomeorHotelSelectedTitleDead(Player player, List<TitleDeed> titleDeads)
    {
        for (TitleDeed titleDead : titleDeads) {
            GroupTitleDeed groupTitleDeed = player.getTitleDeeds().get(titleDead.getType());
            for (TitleDeed elem : groupTitleDeed.getGroup()) {
                if (elem.getLvlTakeRent() > 0)
                    return false;
            }
        }
        return true;
    }

    public ObservableList<ImageView> newListImagesView(Player selectedPlayer) {
        ObservableList<ImageView> newList = FXCollections.observableArrayList();
        ObservableList<ImageView> oldList = selectedPlayer.getImageViewsTitleDeadsProperty();
        for (ImageView elem : oldList) {
            ImageView newImages = new ImageView(elem.getImage());
            newImages.setFitHeight(elem.getImage().getHeight() * 0.6);
            newImages.setFitWidth(elem.getImage().getWidth() * 0.6);
            newList.add(newImages);
        }
        return newList;
    }

    public int takeInfo(Player player, List<TitleDeed> titleDeads, ListView<ImageView> list, TextField moneyText) {
        if (player != null) {
            ObservableList<ImageView> selectedImage = list.getSelectionModel().getSelectedItems();
            int money = getAndCheckIntValue(moneyText, this.info);
            if (money != -1) {
                for (ImageView imageView : selectedImage) {
                    TitleDeed cur = player.findTitleDeadForImage(imageView.getImage());
                    titleDeads.add(cur);
                }
                return money;
            }
        }
        else
            this.info.setText("Выберите игрока/ов");
        return -1;
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

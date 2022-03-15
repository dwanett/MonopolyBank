package monopoly.monopoly_bank;

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
import monopoly.monopoly_bank.logic.titledeeds.MunicipalServices;
import monopoly.monopoly_bank.logic.titledeeds.TitleDeed;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class PayRent {

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
    private ListView<ImageView> list;

    @FXML
    private TextField countSteps;

    @FXML
    private Button pay;

    @FXML
    private Label info;

    public PayRent(MonopolyBank bank, ObservableList<Player> players, Map<String, ListView<ImageView>> playerTitleDeadsImageMap, Stage stage) {
        this.bank = bank;
        this.stage = stage;
        this.players = FXCollections.observableArrayList(players);
        this.playerTitleDeadsImageMap = playerTitleDeadsImageMap;
        this.list = new ListView<ImageView>();
    }

    @FXML
    void initialize() {
        this.tableLeft.setItems(this.players);
        this.tableRight.setItems(this.players);
        this.playersLeft.setCellValueFactory(cellData -> cellData.getValue().getName());
        this.playersRight.setCellValueFactory(cellData -> cellData.getValue().getName());
        this.countSteps.setVisible(false);

        this.tableRight.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, selectedRightPlayer) -> {
            if (selectedRightPlayer != null)
                this.list.setItems(newListImagesView(selectedRightPlayer));
        });

        this.list.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, selectedImage) -> {
            if (selectedImage != null) {
                Player selectedRightPlayer = this.tableRight.getSelectionModel().getSelectedItem();
                if (selectedRightPlayer != null) {
                    TitleDeed titleDeed = selectedRightPlayer.findTitleDeadForImage(selectedImage.getImage());
                    this.countSteps.setVisible(titleDeed.getClass() == MunicipalServices.class);
                }
            }
        });

        this.pay.setOnAction(actionEvent -> {
            this.info.setText("");
            int numberOfSteps = 0;
            Player selectedLeftPlayer = this.tableLeft.getSelectionModel().getSelectedItem();
            Player selectedRightPlayer = this.tableRight.getSelectionModel().getSelectedItem();
            ImageView selectedImageViewTitledead = this.list.getSelectionModel().getSelectedItem();
            if (selectedLeftPlayer != null && selectedRightPlayer != null){
                TitleDeed titleDeed = selectedRightPlayer.findTitleDeadForImage(selectedImageViewTitledead.getImage());
                if (titleDeed.getClass() == MunicipalServices.class) {
                    this.countSteps.setVisible(true);
                    int step = getAndCheckIntValue(this.countSteps, this.info);
                    if (step != -1) {
                        if (step < 2 || step > 12)
                            this.info.setText("Количество очков должно быть от 2 до 12!");
                        else
                            numberOfSteps = step;
                    }
                }
                if (selectedRightPlayer.takeRent(selectedLeftPlayer, titleDeed, numberOfSteps)) {
                    this.stage.close();
                } else
                    this.info.setText("Ошибка!");

            }
            else
                this.info.setText("Выберите игрока/ов!");
        });
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
}

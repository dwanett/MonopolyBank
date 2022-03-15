package monopoly.monopoly_bank;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.ImageView;
import monopoly.monopoly_bank.logic.player.GroupTitleDeed;
import monopoly.monopoly_bank.logic.player.Player;
import monopoly.monopoly_bank.logic.titledeeds.TitleDeed;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import java.util.*;

public class MonopolyBank {

    private List<Player> players;
    private int countPlayer;
    private int playerWralks;
    private ListProperty<TitleDeed> freeTitleDeads;
    private ListProperty<ImageView> imageViewsTitleDeads;

    public MonopolyBank (List<Player> players, int playerWralks) {
        this.setPlayers(players);
        this.setCountPlayer(players.size());
        this.initializeSpringConf();
        this.initImageViewsTitleDeads();
        if (playerWralks > getCountPlayer())
            this.setPlayerWralks(getCountPlayer() - 1);
        else if (playerWralks < 0)
            this.setPlayerWralks(0);
        else
            this.setPlayerWralks(playerWralks);
    }

    public List<Player> getPlayers() {
        return players;
    }

    private void setPlayers(List<Player> players) {
        this.players = players;
    }

    public int getCountPlayer() {
        return countPlayer;
    }

    private void setCountPlayer(int count_player) {
        this.countPlayer = count_player;
    }

    public int getPlayerWralks() {
        return playerWralks;
    }

    public Player getPlayerObjWralks() {
        return players.get(getPlayerWralks());
    }

    private void setPlayerWralks(int playerWralks) {
        this.playerWralks = playerWralks;
    }

    public ListProperty<TitleDeed> getFreeTitleDeads() {
        return freeTitleDeads;
    }

    public void setFreeTitleDeads(ListProperty<TitleDeed> freeTitleDeads) {
        this.freeTitleDeads.set(freeTitleDeads);
    }

    public ListProperty<ImageView> getImageViewsTitleDeadsProperty() {
        return imageViewsTitleDeads;
    }

    public ObservableList<ImageView> getImageViewsTitleDeads() {
        return imageViewsTitleDeads.get();
    }

    public void setImageViewsTitleDeads(ObservableList<ImageView> imageViewsTitleDeads) {
        this.imageViewsTitleDeads.set(imageViewsTitleDeads);
    }

    private void initializeSpringConf() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("configSpring.xml");
        ObservableList<TitleDeed> list = FXCollections.observableArrayList(context.getBeansOfType(TitleDeed.class).values());
        this.freeTitleDeads = new SimpleListProperty<>(list);
        context.close();
    }

    public void walk(int numberOfSteps) {
        Player curPlayer = players.get(playerWralks);

        int newPosition = curPlayer.getCurPosition() + numberOfSteps;

        if (newPosition >= 40) {
            newPosition = numberOfSteps - 40 + curPlayer.getCurPosition();
            curPlayer.nextLap();
        }
        curPlayer.setCurPosition(newPosition);

        checkPosition(curPlayer);

        if (getPlayerWralks() < getCountPlayer() - 1)
            setPlayerWralks(getPlayerWralks() + 1);
        else
            setPlayerWralks(0);
    }

    public boolean swap(Player leftPlayer, Player rightPlayer, List<TitleDeed> leftTitleDeads, List<TitleDeed> rightTitleDeads, int moneyLeft, int moneyRight){
        if (leftPlayer != rightPlayer) {
            if (rightPlayer.takePlayerMoney(leftPlayer, moneyLeft) && leftPlayer.takePlayerMoney(rightPlayer, moneyRight)) {
                for (TitleDeed elem : leftTitleDeads) {
                    List<TitleDeed> group = leftPlayer.getTitleDeeds().get(elem.getType()).getGroup();
                    group.remove(elem);
                    rightPlayer.addTitleDeed(elem);
                }
                for (TitleDeed elem : rightTitleDeads) {
                    List<TitleDeed> group = rightPlayer.getTitleDeeds().get(elem.getType()).getGroup();
                    group.remove(elem);
                    leftPlayer.addTitleDeed(elem);
                }
                leftPlayer.updateImageViewsTitleDeads();
                rightPlayer.updateImageViewsTitleDeads();
                return (true);
            }
        }
        return (false);
    }

    public void checkPosition(Player curPlayer) {
        for(TitleDeed elem : freeTitleDeads){
            if (elem.getPosition() == curPlayer.getCurPosition()) {
                if (curPlayer.buyTitleDeed(this, elem)) {
                    return;
                }
                //Может быть аукцион
            }
        }
        for (int i = 0; i < getCountPlayer(); i++) {
            if (i == getPlayerWralks())
                continue;
            Player check = players.get(i);
            for (Map.Entry<String, GroupTitleDeed> group : check.getTitleDeeds().entrySet()) {
                for (TitleDeed elem : group.getValue().getGroup()) {
                    if (elem.getPosition() == curPlayer.getCurPosition()) {
                        check.takeRent(curPlayer, elem, 0);
                        return;
                    }
                }
            }
        }
    }

    public void initImageViewsTitleDeads() {
        this.imageViewsTitleDeads = new SimpleListProperty<>(FXCollections.observableArrayList());
        for (TitleDeed elem : freeTitleDeads) {
            this.imageViewsTitleDeads.add(elem.creatImageView(elem.getImageFront()));
        }
    }
}

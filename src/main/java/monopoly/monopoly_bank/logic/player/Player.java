package monopoly.monopoly_bank.logic.player;

import javafx.beans.property.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import monopoly.monopoly_bank.MonopolyBank;
import monopoly.monopoly_bank.logic.titledeeds.*;
import java.util.*;


public class Player {
    private final StringProperty name;
    private IntegerProperty money;
    private int curPosition;
    private MapProperty<String, GroupTitleDeed> titleDeeds;
    private ListProperty<ImageView> imageViewsTitleDeads;

    public Player(String name, int money) {
        this.name = new SimpleStringProperty(name);
        this.money = new SimpleIntegerProperty(money);
        this.curPosition = 0;
        this.titleDeeds = new SimpleMapProperty<>(FXCollections.observableHashMap());
        this.imageViewsTitleDeads = new SimpleListProperty<ImageView>(FXCollections.observableArrayList());
    }

    public StringProperty getName() {
        return (this.name);
    }

    public IntegerProperty getMoney() {
        return (this.money);
    }

    public int getCurPosition() {
        return curPosition;
    }

    public void setCurPosition(int curPosition) {
        this.curPosition = curPosition;
    }

    public MapProperty<String, GroupTitleDeed> getTitleDeeds() {
        return (this.titleDeeds);
    }

    public ObservableList<ImageView> getImageViewsTitleDeads() {
        return imageViewsTitleDeads.get();
    }

    public ListProperty<ImageView> getImageViewsTitleDeadsProperty() {
        return imageViewsTitleDeads;
    }

    public void setImageViewsTitleDeads(ObservableList<ImageView> imageViewsTitleDeads) {
        this.imageViewsTitleDeads.set(imageViewsTitleDeads);
    }

    public boolean buyTitleDeed(MonopolyBank bank, TitleDeed newTitleDeed) {
        if (!newTitleDeed.isBuy()) {
            int tmpMoney = getMoney().get() - newTitleDeed.getPrice();

            if (tmpMoney >= 0) {
                this.money.set(tmpMoney);
                this.addTitleDeed(newTitleDeed);
                this.updateImageViewsTitleDeads();
                newTitleDeed.setBuy(true);
                int indexElem = bank.getFreeTitleDeads().indexOf(newTitleDeed);
                bank.getFreeTitleDeads().remove(indexElem);
                bank.getImageViewsTitleDeadsProperty().remove(indexElem);
                System.out.println(this.getName() + " buy street " + newTitleDeed.getName());
                return (true);
            } else
                System.err.println("Error: Not have money");
        }
        else {
            System.err.println("Error: " + newTitleDeed.getName() + " street is buy");
        }
        return (false);
    }

    public void addTitleDeed(TitleDeed newTitleDeed) {
        GroupTitleDeed tmp = titleDeeds.get(newTitleDeed.getType());

        if (tmp != null) {
            if (!tmp.getGroup().contains(newTitleDeed)) {
                tmp.add(newTitleDeed);
            }
            else
                System.err.println("Error: You have this street");
        }
        else {
            GroupTitleDeed newGroup = new GroupTitleDeed();
            newGroup.add(newTitleDeed);
            titleDeeds.put(newTitleDeed.getType(), newGroup);
        }
    }

    public void mortgageTitleDeed(TitleDeed titleDeed) {
        GroupTitleDeed tmp = titleDeeds.get(titleDeed.getType());

        if (tmp != null && tmp.getGroup().contains(titleDeed)) {
            TitleDeed elem = tmp.getGroup().get(tmp.getGroup().indexOf(titleDeed));
            if (!elem.isMortgaged()){
                elem.setMortgaged(true);
                this.updateImageViewsTitleDeads();
                this.money.set(this.money.get() + elem.getPricePledge());
                System.out.println("You mortgaged street");
            }
            else
                System.err.println("Error: This street is mortgaged");
            }
        else
            System.err.println("Error: You not have this street");
    }

    public void payRent(int rent) {
        this.money.set(this.money.get() - rent);
    }

    public void nextLap() {
        this.addMoney(2_000_000);
    }

    public void addMoney(int money) {
        this.money.set(this.money.get() + money);
    }

    public void takeMoney(int money) {
        this.money.set(this.money.get() - money);
    }

    public void takePlayerMoney(Player target, int money) {
        this.addMoney(money);
        target.takeMoney(money);
    }

    public void takeRent(Player targetPlayer, TitleDeed titleDeed, GroupTitleDeed group) {
        if (!titleDeed.isMortgaged()) {
            if (group.isFull()) {
                targetPlayer.payRent(titleDeed.getRentAllGroup());
                System.out.println(targetPlayer.getName() + " pay rent all color group");
            }
            else {
                targetPlayer.payRent(titleDeed.getRent());
                System.out.println(targetPlayer.getName() + " pay rent default");
            }
        }
    }

    @Override
        public String toString(){
            return this.getName().toString();
    }

    public void updateImageViewsTitleDeads() {
        this.imageViewsTitleDeads.clear();
        for (Map.Entry<String, GroupTitleDeed> group : this.getTitleDeeds().entrySet()) {
            for (TitleDeed elem : group.getValue().getGroup()) {
                if (elem.isMortgaged())
                    this.imageViewsTitleDeads.add(elem.getImageViewBack());
                else
                    this.imageViewsTitleDeads.add(elem.getImageViewFront());
                }
        }
    }

    public TitleDeed findTitleDeadForImageFront(Image front) {
        for (Map.Entry<String, GroupTitleDeed> group : this.getTitleDeeds().entrySet()) {
             for (TitleDeed elem : group.getValue().getGroup()) {
                 if (elem.getImageFront() == front)
                     return elem;
             }
        }
        return null;
    }

    public TitleDeed findTitleDeadForImageBack(Image front) {
        for (Map.Entry<String, GroupTitleDeed> group : this.getTitleDeeds().entrySet()) {
            for (TitleDeed elem : group.getValue().getGroup()) {
                if (elem.getImageBack() == front)
                    return elem;
            }
        }
        return null;
    }
}

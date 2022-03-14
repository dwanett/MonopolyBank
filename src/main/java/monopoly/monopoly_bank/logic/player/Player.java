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

    public boolean auction(MonopolyBank bank, TitleDeed newTitleDeed, int newPrice) {
        if (!newTitleDeed.isBuy()) {
            if (this.takeMoney(newPrice)) {
                this.addTitleDeed(newTitleDeed);
                this.updateImageViewsTitleDeads();
                newTitleDeed.setBuy(true);
                int indexElem = bank.getFreeTitleDeads().indexOf(newTitleDeed);
                bank.getFreeTitleDeads().remove(indexElem);
                bank.getImageViewsTitleDeadsProperty().remove(indexElem);
                System.out.println(this.getName().getValue() + " buy street " + newTitleDeed.getName());
                return (true);
            } else
                System.err.println("Error: "+ this.getName().getValue() +" not have money");
        }
        else {
            System.err.println("Error: " + newTitleDeed.getName() + " street is buy");
        }
        return (false);
    }

    public boolean buyTitleDeed(MonopolyBank bank, TitleDeed newTitleDeed) {
        if (!newTitleDeed.isBuy()) {
            if (this.takeMoney(newTitleDeed.getPrice())) {
                this.addTitleDeed(newTitleDeed);
                this.updateImageViewsTitleDeads();
                newTitleDeed.setBuy(true);
                int indexElem = bank.getFreeTitleDeads().indexOf(newTitleDeed);
                bank.getFreeTitleDeads().remove(indexElem);
                bank.getImageViewsTitleDeadsProperty().remove(indexElem);
                System.out.println(this.getName().getValue() + " buy street " + newTitleDeed.getName());
                return (true);
            } else
                System.err.println("Error: "+ this.getName().getValue() +" not have money");
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
                System.err.println("Error: "+ this.getName().getValue() + " have street " + newTitleDeed.getName());
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
            TitleDeed elem = tmp.getGroup().get(tmp.getGroup().indexOf(titleDeed)); //Надо убрать!
            if (!elem.isMortgaged()){
                elem.setMortgaged(true);
                this.updateImageViewsTitleDeads();
                this.addMoney(elem.getPricePledge());
                System.out.println(this.getName().getValue() + " mortgaged street " + elem.getName());
            }
            else {
                if (this.takeMoney(elem.getPricePledge() + (int)(elem.getPricePledge() * 0.1))) {
                    elem.setMortgaged(false);
                    this.updateImageViewsTitleDeads();
                    System.out.println(this.getName().getValue() + " buyback street " + elem.getName());
                }
                else
                    System.err.println("Error mortgage: "+ this.getName().getValue() +" not have money");
            }
        }
        else
            System.err.println("Error mortgage: "+ this.getName().getValue() + " not have street" + titleDeed.getName());
    }

    public void payRent(int rent) {
        this.money.set(this.money.get() - rent);
    }

    public void nextLap() {
        this.addMoney(2_000_000);
    }

    public void addMoney(int money) {
        long tmp = (long)this.money.get() + (long)money;
        if (tmp < Integer.MAX_VALUE)
            this.money.set((int) tmp);
    }

    public boolean takeMoney(int money) {
        int tmp = this.money.get() - money;
        if (tmp >= 0)
            this.money.set(tmp);
        else
            return (false);
        return (true);
    }

    public boolean takePlayerMoney(Player target, int money) {
        if (target.takeMoney(money)) {
            this.addMoney(money);
            return (true);
        }
        return (false);
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

    public boolean checkDifferenceCountHomeGroupTitleDeed(GroupTitleDeed group, int newLvlRent){
        int minCountHome = 0;
        int maxCountHome = 0;
        group.getGroup().get(0);
        if (group.getGroup().get(0).getClass() == Street.class) {
            for (TitleDeed elem : group.getGroup()) {
                if (minCountHome > ((Street)elem).getLvlTakeRent() || minCountHome == 0)
                    minCountHome = ((Street)elem).getLvlTakeRent();
                if (maxCountHome < ((Street)elem).getLvlTakeRent() || maxCountHome == 0)
                    maxCountHome = ((Street)elem).getLvlTakeRent();
            }
            return  (maxCountHome - newLvlRent <= 1 && newLvlRent - minCountHome <= 1);
        }
        return false;
    }

    public boolean buyHomeOrHotel(TitleDeed titleDeed){
        if (titleDeed.getClass() == Street.class) {
            GroupTitleDeed tmp = titleDeeds.get(titleDeed.getType());

            if (tmp != null && tmp.getGroup().contains(titleDeed)) {
                if (tmp.isFull()) {
                    if (titleDeed.getLvlTakeRent() < 5) {
                        if (checkDifferenceCountHomeGroupTitleDeed(tmp, titleDeed.getLvlTakeRent() + 1)) {
                            if (this.takeMoney(((Street) titleDeed).getPriceHome())) {
                                titleDeed.upLvlTakeRent();
                                this.updateImageViewsTitleDeads();
                                return (true);
                            } else
                                System.err.println("Error buy home: " + this.getName().getValue() + " not have money");
                        }else
                            System.err.println("Error buy home: " + this.getName().getValue() + " maximum difference in the number of houses 1");
                    } else
                        System.err.println("Error buy home: " + this.getName().getValue() + " has hotel on " + titleDeed.getName());
                } else
                    System.err.println("Error buy home: " + this.getName().getValue() + " not have full color group");
            } else
                System.err.println("Error buy home: " + this.getName().getValue() + " not have street" + titleDeed.getName());
        }
        return (false);
    }

    public boolean sellHomeOrHotel(TitleDeed titleDeed){
        if (titleDeed.getClass() == Street.class) {
            GroupTitleDeed tmp = titleDeeds.get(titleDeed.getType());

            if (tmp != null && tmp.getGroup().contains(titleDeed)) {
               if (titleDeed.getLvlTakeRent() != 0) {
                   if (checkDifferenceCountHomeGroupTitleDeed(tmp, titleDeed.getLvlTakeRent() - 1)) {
                       this.addMoney(((Street) titleDeed).getPriceHome() / 2);
                       titleDeed.downLvlTakeRent();
                       this.updateImageViewsTitleDeads();
                       return (true);
                   } else
                       System.err.println("Error buy home: " + this.getName().getValue() + " maximum difference in the number of houses 1");
               } else
                   System.err.println("Error buy home: " + this.getName().getValue() + " not has home or hotel on " + titleDeed.getName());
            } else
                System.err.println("Error buy home: " + this.getName().getValue() + " not have street" + titleDeed.getName());
        }
        return (false);
    }

    public void updateImageViewsTitleDeads() {
        this.imageViewsTitleDeads.clear();
        for (Map.Entry<String, GroupTitleDeed> group : this.getTitleDeeds().entrySet()) {
            for (TitleDeed elem : group.getValue().getGroup()) {
                if (elem.isMortgaged())
                    this.imageViewsTitleDeads.add(elem.creatImageView(elem.getImageBack()));
                else {
                    if (elem.getClass() == Street.class) {
                        switch (elem.getLvlTakeRent()) {
                            case 0: this.imageViewsTitleDeads.add(elem.creatImageView(elem.getImageFront())); break;
                            case 1: this.imageViewsTitleDeads.add(elem.creatImageView(((Street)elem).getImageHome1())); break;
                            case 2: this.imageViewsTitleDeads.add(elem.creatImageView(((Street)elem).getImageHome2())); break;
                            case 3: this.imageViewsTitleDeads.add(elem.creatImageView(((Street)elem).getImageHome3())); break;
                            case 4: this.imageViewsTitleDeads.add(elem.creatImageView(((Street)elem).getImageHome4())); break;
                            case 5: this.imageViewsTitleDeads.add(elem.creatImageView(((Street)elem).getImageHotel())); break;
                        }
                    }
                    else
                        this.imageViewsTitleDeads.add(elem.creatImageView(elem.getImageFront()));
                }
            }
        }
    }

    public TitleDeed findTitleDeadForImage(Image image) {
        for (Map.Entry<String, GroupTitleDeed> group : this.getTitleDeeds().entrySet()) {
             for (TitleDeed elem : group.getValue().getGroup()) {
                 if (elem.getImageFront() == image)
                     return elem;
                 if (elem.getImageBack() == image)
                     return elem;
                 if (elem.getClass() == Street.class) {
                     if (((Street)elem).getImageHome1() == image)
                         return elem;
                     if (((Street)elem).getImageHome2() == image)
                         return elem;
                     if (((Street)elem).getImageHome3() == image)
                         return elem;
                     if (((Street)elem).getImageHome4() == image)
                         return elem;
                     if (((Street)elem).getImageHotel() == image)
                         return elem;
                 }
             }
        }
        return null;
    }

    @Override
    public String toString(){
        return this.getName().toString();
    }
}

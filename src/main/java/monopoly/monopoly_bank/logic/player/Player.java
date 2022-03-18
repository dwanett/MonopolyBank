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
        boolean checkLvlRent = true;

        if (tmp != null && tmp.getGroup().contains(titleDeed)) {
            if (!titleDeed.isMortgaged()){
                for (TitleDeed elem : tmp.getGroup()){
                    if (elem.getLvlTakeRent() > 0) {
                        checkLvlRent = false;
                        break;
                    }
                }
                if(checkLvlRent) {
                    titleDeed.setMortgaged(true);
                    this.updateImageViewsTitleDeads();
                    this.addMoney(titleDeed.getPricePledge());
                    System.out.println(this.getName().getValue() + " mortgaged street " + titleDeed.getName());
                }
            }
            else {
                if (this.takeMoney(titleDeed.getPricePledge() + (int)(titleDeed.getPricePledge() * 0.1))) {
                    titleDeed.setMortgaged(false);
                    this.updateImageViewsTitleDeads();
                    System.out.println(this.getName().getValue() + " buyback street " + titleDeed.getName());
                }
                else
                    System.err.println("Error mortgage: "+ this.getName().getValue() +" not have money");
            }
        }
        else
            System.err.println("Error mortgage: "+ this.getName().getValue() + " not have street" + titleDeed.getName());
    }

    public void nextLap() {
        this.addMoney(2_000_000);
    }

    public void addMoney(int money) {
        long tmp = (long)this.money.get() + (long)money;
        if (tmp < Integer.MAX_VALUE) {
            this.money.set((int) tmp);
            System.out.println(this.getName().getValue() + " get " + money);
        }
    }

    public boolean takeMoney(int money) {
        int tmp = this.money.get() - money;
        if (tmp >= 0) {
            this.money.set(tmp);
            System.out.println(this.getName().getValue() + " gave " + money);
        }
        else
            return (false);
        return (true);
    }

    public boolean takePlayerMoney(Player target, int money) {
        if (target.takeMoney(money)) {
            this.addMoney(money);
            System.out.println(target.getName().getValue() + " pay " + money + " " + this.getName().getValue());
            return (true);
        }
        System.err.println("Error take player money: " + target.getName().getValue() + " has " + target.getMoney().getValue()+ "! Need " + money);
        return (false);
    }

    public boolean takeRent(Player targetPlayer, TitleDeed titleDeed, int numberOfSteps) {
        GroupTitleDeed tmp = this.titleDeeds.get(titleDeed.getType());

        if (!titleDeed.isMortgaged() && tmp != null) {
            if (tmp.isFull()) {
                if (titleDeed.getClass() == Street.class) {
                    switch (titleDeed.getLvlTakeRent()) {
                        case 0: return (this.takePlayerMoney(targetPlayer, titleDeed.getRentAllGroup()));
                        case 1: return (this.takePlayerMoney(targetPlayer, ((Street) titleDeed).getRentHome1()));
                        case 2: return (this.takePlayerMoney(targetPlayer, ((Street) titleDeed).getRentHome2()));
                        case 3: return (this.takePlayerMoney(targetPlayer, ((Street) titleDeed).getRentHome3()));
                        case 4: return (this.takePlayerMoney(targetPlayer, ((Street) titleDeed).getRentHome4()));
                        case 5: return (this.takePlayerMoney(targetPlayer, ((Street) titleDeed).getRentHotel()));
                    }
                }
                else if (titleDeed.getClass() == MunicipalServices.class) {
                    return (this.takePlayerMoney(targetPlayer, titleDeed.getRentAllGroup() * numberOfSteps));
                }
                else {
                    return (targetPlayer.takePlayerMoney(targetPlayer, titleDeed.getRentAllGroup()));
                }
            }
            else {
                if (titleDeed.getClass() == TrainStation.class) {
                    switch (titleDeed.getLvlTakeRent()) {
                        case 0: return (this.takePlayerMoney(targetPlayer, titleDeed.getRent()));
                        case 1: return (this.takePlayerMoney(targetPlayer, ((TrainStation) titleDeed).getRentStation2()));
                        case 2: return (this.takePlayerMoney(targetPlayer, ((TrainStation) titleDeed).getRentStation3()));
                    }
                } else if (titleDeed.getClass() == MunicipalServices.class) {
                    return (this.takePlayerMoney(targetPlayer, titleDeed.getRent() * numberOfSteps));
                }
                else {
                    return (this.takePlayerMoney(targetPlayer, titleDeed.getRent()));
                }
            }
        }
        return false;
    }

    public boolean checkDifferenceCountHomeGroupTitleDeed(GroupTitleDeed group, int newLvlRent){
        int minCountHome = 0;
        int maxCountHome = 0;
        if (group.getGroup().get(0).getClass() == Street.class) {
            for (TitleDeed elem : group.getGroup()) {
                if (minCountHome > ((Street)elem).getLvlTakeRent() || minCountHome == 0)
                    minCountHome = ((Street)elem).getLvlTakeRent();
                if (maxCountHome < ((Street)elem).getLvlTakeRent() || maxCountHome == 0)
                    maxCountHome = ((Street)elem).getLvlTakeRent();
            }
            return (maxCountHome - newLvlRent <= 1 && newLvlRent - minCountHome <= 1);
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
        return this.getName().getValue();
    }
}

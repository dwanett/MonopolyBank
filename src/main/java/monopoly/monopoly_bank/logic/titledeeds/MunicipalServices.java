package monopoly.monopoly_bank.logic.titledeeds;

import javafx.scene.image.Image;

public class MunicipalServices extends TitleDeed {

    public MunicipalServices(String name, String type , int price, int rentCoefficient, int rentAllCoefficient, int position, int fullGroup, String pathImageFront, String pathImageBack) {
        this.setName(name);
        this.setType(type);
        this.setPrice(price);
        this.setPricePledge(price / 2);
        this.setRent(rentCoefficient);
        this.setRentAllGroup(rentAllCoefficient);
        this.setPosition(position);
        this.setBuy(false);
        this.setFullGroup(fullGroup);
        this.setPathImageFront(pathImageFront);
        this.setPathImageBack(pathImageBack);
        this.setImageFront(new Image(getClass().getResourceAsStream(getPathImageFront())));
        this.setImageBack(new Image(getClass().getResourceAsStream(getPathImageBack())));
        this.setLvlTakeRent(0);
    }

}

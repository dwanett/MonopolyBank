package monopoly.monopoly_bank.logic.titledeeds;

import javafx.scene.image.Image;

public class TrainStation extends TitleDeed {
    private final int rentStation2;
    private final int rentStation3;

    public TrainStation(String name, String type, int price, int rent, int position, int fullGroup, String pathImageFront, String pathImageBack) {
        this.setName(name);
        this.setType(type);
        this.setPrice(price);
        this.setPricePledge(price / 2);
        this.setRent(rent);
        this.setPathImageFront(pathImageFront);
        this.setPathImageBack(pathImageBack);
        this.rentStation2 = rent * 2;
        this.rentStation3 = this.rentStation2 * 2;
        this.setRentAllGroup(this.rentStation3 * 2);
        this.setBuy(false);
        this.setPosition(position);
        this.setFullGroup(fullGroup);
        this.setImageFront(new Image(getClass().getResourceAsStream(getPathImageFront())));
        this.setImageBack(new Image(getClass().getResourceAsStream(getPathImageBack())));
        this.setLvlTakeRent(0);
    }

    public int getRentStation2() {
        return rentStation2;
    }

    public int getRentStation3() {
        return rentStation3;
    }

}

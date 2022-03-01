package monopoly.monopoly_bank.logic.titledeeds;

import javafx.scene.image.Image;

public class Street extends TitleDeed {
    private final int priceHome;
    private final int rentHome1;
    private final int rentHome2;
    private final int rentHome3;
    private final int rentHome4;
    private final int rentHotel;


    public Street(String name, String type, int price, int priceHome, int rent, int rentHome1, int rentHome2,
                  int rentHome3, int rentHome4, int rentHotel, int position, int fullGroup, String pathImageFront, String pathImageBack) {
        this.setName(name);
        this.setPrice(price);
        this.setPricePledge(price / 2);
        this.setRent(rent);
        this.setRentAllGroup(rent * 2);
        this.setMortgaged(false);
        this.setBuy(false);
        this.setPosition(position);
        this.setType(type);
        this.setPathImageFront(pathImageFront);
        this.setPathImageBack(pathImageBack);
        this.setImageFront(new Image(getClass().getResourceAsStream(getPathImageFront())));
        this.setImageBack(new Image(getClass().getResourceAsStream(getPathImageBack())));
        this.priceHome = priceHome;
        this.rentHome1 = rentHome1;
        this.rentHome2 = rentHome2;
        this.rentHome3 = rentHome3;
        this.rentHome4 = rentHome4;
        this.rentHotel = rentHotel;
        this.setFullGroup(fullGroup);

    }

    public int getPriceHome(){
        return (this.priceHome);
    }

    public int getRentHome1(){
        return (this.rentHome1);
    }

    public int getRentHome2(){
        return (this.rentHome2);
    }

    public int getRentHome3(){
        return (this.rentHome3);
    }

    public int getRentHome4(){
        return (this.rentHome4);
    }

    public int getRentHotel(){
        return (this.rentHotel);
    }
}


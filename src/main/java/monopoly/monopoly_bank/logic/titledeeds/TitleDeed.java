package monopoly.monopoly_bank.logic.titledeeds;
import monopoly.monopoly_bank.*;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class TitleDeed {
    private String name;
    private String type;
    private String pathImageFront;
    private String pathImageBack;
    private int price;
    private int pricePledge;
    private int rent;
    private int rentAllGroup;
    private boolean isMortgaged;
    private boolean isBuy;
    private int position;
    private int fullGroup;
    private Image imageFront;
    private Image imageBack;

    public int getFullGroup() {
        return fullGroup;
    }

    public void setFullGroup(int fullGroup) {
        this.fullGroup = fullGroup;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isMortgaged() {
        return isMortgaged;
    }

    public void setMortgaged(boolean mortgaged) {
        isMortgaged = mortgaged;
    }

    public boolean isBuy() {
        return isBuy;
    }

    public void setBuy(boolean buy) {
        isBuy = buy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPricePledge() {
        return pricePledge;
    }

    public void setPricePledge(int pricePledge) {
        this.pricePledge = pricePledge;
    }

    public int getRent() {
        return rent;
    }

    public void setRent(int rent) {
        this.rent = rent;
    }

    public int getRentAllGroup() {
        return rentAllGroup;
    }

    public void setRentAllGroup(int rentAllGroup) {
        this.rentAllGroup = rentAllGroup;
    }

    public String getPathImageFront() {
        return pathImageFront;
    }

    public void setPathImageFront(String pathImageFront) {
        this.pathImageFront = pathImageFront;
    }

    public String getPathImageBack() {
        return pathImageBack;
    }

    public void setPathImageBack(String pathImageBack) {
        this.pathImageBack = pathImageBack;
    }

    public void setImageFront(Image imageFront) {
        this.imageFront = imageFront;
    }

    public void setImageBack(Image imageBack) {
        this.imageBack = imageBack;
    }

    public Image getImageFront() {
        return imageFront;
    }

    public Image getImageBack() {
        return imageBack;
    }

    public ImageView getImageViewFront() {
        Image image = getImageFront();
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(image.getHeight() * 0.5);
        imageView.setFitWidth(image.getWidth() * 0.5);
        return (imageView);
    }

    public ImageView getImageViewBack() {
        Image image = getImageBack();
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(image.getHeight() * 0.5);
        imageView.setFitWidth(image.getWidth() * 0.5);
        return (imageView);
    }

}

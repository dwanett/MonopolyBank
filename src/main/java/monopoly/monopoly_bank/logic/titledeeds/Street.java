package monopoly.monopoly_bank.logic.titledeeds;

import javafx.scene.image.Image;

public class Street extends TitleDeed {
    private final int priceHome;
    private final int rentHome1;
    private final int rentHome2;
    private final int rentHome3;
    private final int rentHome4;
    private final int rentHotel;
    private String pathImageHome1;
    private String pathImageHome2;
    private String pathImageHome3;
    private String pathImageHome4;
    private String pathImageHotel;
    private Image imageHome1;
    private Image imageHome2;
    private Image imageHome3;
    private Image imageHome4;
    private Image imageHotel;


    public Street(String name, String type, int price, int priceHome, int rent, int rentHome1, int rentHome2,
                  int rentHome3, int rentHome4, int rentHotel, int position, int fullGroup, String pathImageFront,
                  String pathImageBack, String pathImageHome1, String pathImageHome2, String pathImageHome3,
                  String pathImageHome4, String pathImageHotel) {
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
        this.setPathImageHome1(pathImageHome1);
        this.setPathImageHome2(pathImageHome2);
        this.setPathImageHome3(pathImageHome3);
        this.setPathImageHome4(pathImageHome4);
        this.setPathImageHotel(pathImageHotel);
        this.setFullGroup(fullGroup);
        this.pathImageHome1 = pathImageHome1;
        this.pathImageHome2 = pathImageHome2;
        this.pathImageHome3 = pathImageHome3;
        this.pathImageHome4 = pathImageHome4;
        this.pathImageHotel = pathImageHotel;
        this.priceHome = priceHome;
        this.rentHome1 = rentHome1;
        this.rentHome2 = rentHome2;
        this.rentHome3 = rentHome3;
        this.rentHome4 = rentHome4;
        this.rentHotel = rentHotel;
        this.imageHome1 = new Image(getClass().getResourceAsStream(pathImageHome1));
        this.imageHome2 = new Image(getClass().getResourceAsStream(pathImageHome2));
        this.imageHome3 = new Image(getClass().getResourceAsStream(pathImageHome3));
        this.imageHome4 = new Image(getClass().getResourceAsStream(pathImageHome4));
        this.imageHotel = new Image(getClass().getResourceAsStream(pathImageHotel));
        this.setLvlTakeRent(0);
    }

    public String getPathImageHome1() {
        return pathImageHome1;
    }

    public void setPathImageHome1(String pathImageHome1) {
        this.pathImageHome1 = pathImageHome1;
    }

    public String getPathImageHome2() {
        return pathImageHome2;
    }

    public void setPathImageHome2(String pathImageHome2) {
        this.pathImageHome2 = pathImageHome2;
    }

    public String getPathImageHome3() {
        return pathImageHome3;
    }

    public void setPathImageHome3(String pathImageHome3) {
        this.pathImageHome3 = pathImageHome3;
    }

    public String getPathImageHome4() {
        return pathImageHome4;
    }

    public void setPathImageHome4(String pathImageHome4) {
        this.pathImageHome4 = pathImageHome4;
    }

    public String getPathImageHotel() {
        return pathImageHotel;
    }

    public void setPathImageHotel(String pathImageHotel) {
        this.pathImageHotel = pathImageHotel;
    }

    public Image getImageHome1() {
        return imageHome1;
    }

    public void setImageHome1(Image imageHome1) {
        this.imageHome1 = imageHome1;
    }

    public Image getImageHome2() {
        return imageHome2;
    }

    public void setImageHome2(Image imageHome2) {
        this.imageHome2 = imageHome2;
    }

    public Image getImageHome3() {
        return imageHome3;
    }

    public void setImageHome3(Image imageHome3) {
        this.imageHome3 = imageHome3;
    }

    public Image getImageHome4() {
        return imageHome4;
    }

    public void setImageHome4(Image imageHome4) {
        this.imageHome4 = imageHome4;
    }

    public Image getImageHotel() {
        return imageHotel;
    }

    public void setImageHotel(Image imageHotel) {
        this.imageHotel = imageHotel;
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

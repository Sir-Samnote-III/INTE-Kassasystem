public class Item {

    int price;
    String name;

    public Item(int price, String name){
        this.price = price;
        this.name = name;
    }

    public double getPrice(){
        return (double)price/100;
    }

    public String getName(){
        return name;
    }

    //Ändring
}

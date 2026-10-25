public class Item {

    private final String name;
    private final Money price;

    public Item(String name, Money price){
        this.price = price;
        this.name = name;
    }

    public Money getPrice(){
        return price;
    }

    public String getName(){
        return name;
    }

    //Ändring
}

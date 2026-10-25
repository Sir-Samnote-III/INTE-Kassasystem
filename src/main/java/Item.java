public class Item {

    private final String name;
    private final Money price;

    public Item(String name, Money price){
        if (price == null || price.getValue() < 0) {
            throw new IllegalArgumentException("Price must be zero or positive");
        }
        
        if (name == null || name.isBlank()){
            throw new IllegalArgumentException("Name cannot be empty");
        }

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

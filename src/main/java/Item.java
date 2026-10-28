public class Item {

    private final String name;
    private final Money price;
    private final Money deposit;

    public Item(String name, Money price) {
        this(name, price, new Money(0, "SEK"));
    }
    
    public Item(String name, Money price, Money deposit){
        if (price == null || price.getValue() < 0) {
            throw new IllegalArgumentException("Price must be zero or positive");
        }
        
        if (name == null || name.isBlank()){
            throw new IllegalArgumentException("Name cannot be empty");
        }

        if (deposit == null || deposit.getValue() < 0){
            throw new IllegalArgumentException("Deposit must be zero or positive");
        }

        this.price = price;
        this.name = name;
        this.deposit = deposit;
    }

    public Money getPrice(){
        return price;
    }

    public String getName(){
        return name;
    }
    
    public Money getDeposit(){
        return deposit;
    }
}

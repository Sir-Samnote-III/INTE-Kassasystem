public final class Item {

    private final String name;
    private final Money price;
    private final Money deposit;
    private final int ageLimit;

    public Item(String name, Money price) {
        this(name, price, price == null ? null : new Money(0, price.getCurrency()));
    }

    //vara med pant fast utan åldersgräns
    public Item(String name, Money price, Money deposit){
        this(name, price, deposit, ProductAgeLimits.NO_AGE_LIMIT);
    }
    
    public Item(String name, Money price, Money deposit, int ageLimit){
        if (price == null || price.getValue() < 0) {
            throw new IllegalArgumentException("Price must be zero or positive");
        }
        
        if (name == null || name.isBlank()){
            throw new IllegalArgumentException("Name cannot be empty");
        }

        if (deposit == null || deposit.getValue() < 0){
            throw new IllegalArgumentException("Deposit must be zero or positive");
        }
        if (ageLimit < 0){
            throw new IllegalArgumentException("Age limit cannot be negative");
        }

        this.price = price;
        this.name = name;
        this.deposit = deposit;
        this.ageLimit = ageLimit;
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

    public int getAgeLimit(){
        return ageLimit;
    }

    public Money getPriceWithDeposit(){
        long total = price.getValue() + deposit.getValue();
        return new Money(total, price.getCurrency());
    }
}

public class Change {



    public Money calculateChange(Money price, Money payment){
        if (payment.getValue() < 0){
            throw new IllegalArgumentException("Payment cannot be negative");
        }else if(price.getValue() < 0){
            throw new IllegalArgumentException("Price cannot be negative");
        }else if (payment.getValue() < price.getValue()){
            throw new IllegalArgumentException("Short payment");
        }
        int changeValue = payment.getValue() - price.getValue();
        return new Money(changeValue, "SEK");
    }
}

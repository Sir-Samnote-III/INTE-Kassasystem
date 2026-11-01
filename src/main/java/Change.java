import java.util.ArrayList;
import java.util.List;

public class Change {

    int[] denominations = {100000, 50000, 20000, 10000, 5000, 2000, 1000, 500, 200, 100};


    public Money calculateChange(Money price, Money payment){
        if (price == null){
            throw new IllegalArgumentException("Price cannot be null");
        }
        if (payment == null){
            throw new IllegalArgumentException("Payment cannot be null");
        }
        if (payment.getValue() < 0){
            throw new IllegalArgumentException("Payment cannot be negative");
        }
        if(price.getValue() < 0){
            throw new IllegalArgumentException("Price cannot be negative");
        }
        if (payment.getValue() < price.getValue()){
            throw new IllegalStateException("Short payment");
        }
        if (!price.getCurrency().equals(payment.getCurrency())){
            throw new IllegalArgumentException("Price and payment must use the same currency");
        }
        long changeValue = payment.getValue() - price.getValue();
        return new Money(changeValue, payment.getCurrency());
    }

    public List<Integer> calculateDenominations(Money changeAmount){
        List<Integer> result = new ArrayList<>();
        long remainingAmount = changeAmount.getValue(); //Hämtar totala värde i öre

        for (int denomination : denominations){
            while (remainingAmount >= denomination){
                result.add(denomination);
                remainingAmount -= denomination;
            }
        }
        return result;
    }
}

import java.util.ArrayList;
import java.util.List;

public class Change {
//test
    int[] denominations = {100000, 50000, 20000, 10000, 5000, 2000, 1000, 500, 200, 100};


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

    public List<Integer> calculateDenominations(Money changeAmount){
        List<Integer> result = new ArrayList<>();
        int remainingAmount = changeAmount.getValue(); //Hämtar totala värde i öre

        for (int denomination : denominations){
            while (remainingAmount >= denomination){
                result.add(denomination);
                remainingAmount -= denomination;
            }
        }
        return result;
    }
}

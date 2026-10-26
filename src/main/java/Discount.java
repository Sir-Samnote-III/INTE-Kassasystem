public class Discount {


    public Money applyDiscount(Money price, CustomerRole role, boolean isThursday, boolean isHoliday){
        int discountPercent = 0;

        if (!isHoliday){
            if (role == CustomerRole.PENSIONER){
                discountPercent += 10;
            }else if (role == CustomerRole.STUDENT){
                if (isThursday){
                    discountPercent += 10;
                }else{
                    discountPercent += 5;
                }
            }
        }
        if (price.getValue() > 99900){
            discountPercent += 5;
        }

        int discountAmount = (price.getValue() * discountPercent) / 100;
        int finalAmount = price.getValue() - discountAmount;

        return new Money(finalAmount, price.getCurrency());

    }


}

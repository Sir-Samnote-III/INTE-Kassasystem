public class Discount {
/*
Regler:
    Alla köp över 999 kr ger 5% rabatt
    Pensionär har 10% grundrabatt
    Studenter har 5% grundrabatt. På torsdagar ökar studentrabatten till 10%
    Inga personliga rabatter gäller på helgdagar.
    
Personliga rabatter kan staplas. Ex. Pensionär som handlar för 1 000 kr får 10% + 5% rabatt
*/

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

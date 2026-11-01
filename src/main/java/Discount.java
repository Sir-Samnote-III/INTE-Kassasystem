public class Discount {
/*
Regler:
    Alla köp över 999 kr ger 5% rabatt
    Pensionär har 10% grundrabatt
    Studenter har 5% grundrabatt. På torsdagar ökar studentrabatten till 10%
    Inga personliga rabatter gäller på helgdagar.

Personliga rabatter kan staplas. Ex. Pensionär som handlar för 1 000 kr får 10% + 5% rabatt
*/
    private static final int DISCOUNT_THRESHOLD_ORE = 99900; //999kr
    private static final int THRESHOLD_DISCOUNT_PERCENT = 5;
    private static final int PENSIONER_DISCOUNT_PERCENT = 10;
    private static final int STUDENT_BASE_DISCOUNT_PERCENT = 5;
    private static final int STUDENT_THURSDAY_DISCOUNT_PERCENT = 10;

    public Money applyDiscount(Money price, CustomerRole role, boolean isThursday, boolean isHoliday){
        int discountPercent = 0;


        if (!isHoliday){
            if (role == CustomerRole.PENSIONER){
                discountPercent += PENSIONER_DISCOUNT_PERCENT;
            }else if (role == CustomerRole.STUDENT){
                if (isThursday){
                    discountPercent += STUDENT_THURSDAY_DISCOUNT_PERCENT;
                }else{
                    discountPercent += STUDENT_BASE_DISCOUNT_PERCENT;
                }
            }
        }
        if (price.getValue() > DISCOUNT_THRESHOLD_ORE){
            discountPercent += THRESHOLD_DISCOUNT_PERCENT;
        }

        long discountAmount = (price.getValue() * discountPercent) / 100;
        long finalAmount = price.getValue() - discountAmount;

        return new Money(finalAmount, price.getCurrency());

    }


}


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;



public class DiscountTest {
    public static final int BELOW1000 = 99900;
    public static final int MORETHAN999 = 100000;
    Currency sek = new Currency(CurrencyCode.SEK, "Svensk Krona", "kr");


    //Regel 1: Pensionär, köp över 999kr, inte helgdag -> 15% rabatt (10 + 5)
    @Test
    void applyDiscount_PensionerOver999OnRegularDay_Returns15PercentDiscount(){
        Money price = new Money(MORETHAN999, sek);
        Discount discount = new Discount();

        Money discountedPrice = discount.applyDiscount(price, CustomerRole.PENSIONER, false, false);
        assertEquals(850_00, discountedPrice.getValue());
    }
    @Test
    //Regel 3: Pensionär, köp under 1 000kr, inte helgdag --> 10% rabatt
    void applyDiscount_PensionerBelow1000OnRegularDay_Returns10PercentDiscount(){
        Money price = new Money(BELOW1000, sek);
        Discount discount = new Discount();

        Money discountedPrice = discount.applyDiscount(price, CustomerRole.PENSIONER, false, false);
        assertEquals(899_10, discountedPrice.getValue());
    }

    //Regel 2: Pensionär, köp över 999 kr, helgdag --> 5% rabatt
    @Test
    void applyDiscount_PensionerOver999OnHoliday_Returns5PercentDiscount(){
        Money price = new Money(MORETHAN999, sek);
        Discount discount = new Discount();

        Money discountedPrice = discount.applyDiscount(price, CustomerRole.PENSIONER, false, true);
        assertEquals(95000, discountedPrice.getValue());
    }

    //Regel 4: Pensionär, köp under 1 000 kr, helgdag -> 0% rabatt
    @Test
    void applyDiscount_PensionerBelow1000OnHoliday_Returns0PercentDiscount(){
        Money price = new Money(BELOW1000, sek);
        Discount discount = new Discount();

        Money discountedPrice = discount.applyDiscount(price, CustomerRole.PENSIONER, false, true);
        assertEquals(999_00, discountedPrice.getValue());
    }

    //Regel 5: Student, köp över 999kr, torsdag, Ej helgdag -> 15% rabatt (10% + 5%)
    @Test
    void applyDiscount_StudentOver999OnThursdayNotHoliday_Returns15PercentDiscount(){
        Money price = new Money(MORETHAN999, sek);
        Discount discount = new Discount();

        Money discountedPrice = discount.applyDiscount(price, CustomerRole.STUDENT, true, false);
        assertEquals(85000, discountedPrice.getValue());
    }

    //Regel 6: Student, köp under 1 000 kr, torsdag, ej helgdag -> 10% rabatt
    @Test
    void applyDiscount_StudentBelow1000OnThursdayNotHoliday_Returns10PercentDiscount(){
        Money price = new Money(BELOW1000, sek);
        Discount discount = new Discount();

        Money discountedPrice = discount.applyDiscount(price, CustomerRole.STUDENT, true, false);
        assertEquals(899_10, discountedPrice.getValue());
    }

    //Regel 7: Student, köp över 999 kr, ej torsdag, ej helgdag -> 10% rabatt
    @Test
    void applyDiscount_StudentOver999NotThursdayNotHoliday_Returns10PercentDiscount(){
        Money price = new Money(MORETHAN999, sek);
        Discount discount = new Discount();

        Money discountedPrice = discount.applyDiscount(price, CustomerRole.STUDENT, false, false);
        assertEquals(90000, discountedPrice.getValue());
    }

    //Regel 8: Student, köp under 1 000kr, ej torsdag, ej helg -> 5% rabatt
    @Test
    void applyDiscount_StudentBelow1000NotThursdayNotHoliday_Returns5PercentDiscount(){
        Money price = new Money(BELOW1000,sek );
        Discount discount = new Discount();

        Money discountedPrice = discount.applyDiscount(price, CustomerRole.STUDENT, false, false);
        assertEquals(949_05, discountedPrice.getValue());
    }

    //Regel 9: Student, köp över 999kr, ej torsdag, helgdag -> 5% rabatt
    @Test
    void applyDiscount_StudentOver999NotThursdayIsHoliday_Returns5PercentDiscount(){
        Money price = new Money(MORETHAN999, sek);
        Discount discount = new Discount();

        Money discountedPrice = discount.applyDiscount(price, CustomerRole.STUDENT, false, true);
        assertEquals(950_00, discountedPrice.getValue());
    }

    //Regel 10: Student, köp under 1000 kr, torsdag, helgdag -> 0% rabatt
    @Test
    void applyDiscount_StudentBelow1000IsThursdayIsHoliday_Returns0PercentDiscount(){
        Money price = new Money(BELOW1000, sek);
        Discount discount = new Discount();

        Money discountedPrice = discount.applyDiscount(price,CustomerRole.STUDENT,true,true);
        assertEquals(999_00, discountedPrice.getValue());
    }

    //Regel 13: Standardkund, köp över 1 000kr, torsdag, helgdag -> 5% rabatt
    @Test
    void applyDiscount_StandardOver999IsThursdayIsHoliday_Returns5PercentDiscount(){
        Money price = new Money(MORETHAN999, sek);
        Discount discount = new Discount();

        Money discountedPrice = discount.applyDiscount(price, CustomerRole.STANDARD, true, true);
        assertEquals(950_00, discountedPrice.getValue());
    }

    //Regel 12: Standardkund, köp under 1000kr -> 0% rabatt
    @Test
    void applyDiscount_StandardCustomerBelow1000_ReturnsOriginalPrice(){
        Money price = new Money(BELOW1000, sek);
        Discount discount = new Discount();

        Money discountedPrice = discount.applyDiscount(price, CustomerRole.STANDARD, false, false);
        assertEquals(999_00, discountedPrice.getValue());
    }

}

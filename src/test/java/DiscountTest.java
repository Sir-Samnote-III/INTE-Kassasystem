
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;



public class DiscountTest {


    //Regel 1: Pensionär, köp över 999kr, inte helgdag -> 15% rabatt (10 + 5)
    @Test
    void applyDiscount_PensionerOver999OnRegularDay_Returns15PercentDiscount(){
        Money price = new Money(100000, "SEK");
        Discount discount = new Discount();

        Money discountedPrice = discount.applyDiscount(price, CustomerRole.PENSIONER, false, false);
        assertEquals(85000, discountedPrice.getValue());
    }
    @Test
    //Regel 3: Pensionär, köp under 1 000kr, inte helgdag --> 10% rabatt
    void applyDiscount_PensionerUnder1000OnRegularDay_Returns10PercentDiscount(){
        Money price = new Money(10000, "SEK");
        Discount discount = new Discount();

        Money discountedPrice = discount.applyDiscount(price, CustomerRole.PENSIONER, false, false);
        assertEquals(9000, discountedPrice.getValue());
    }

    //Regel 2: Pensionär, köp över 999 kr, helgdag --> 5% rabatt
    @Test
    void applyDiscount_PensionerOver999OnHoliday_Returns5PercentDiscount(){
        Money price = new Money(100000, "SEK");
        Discount discount = new Discount();

        Money discountedPrice = discount.applyDiscount(price, CustomerRole.PENSIONER, false, true);
        assertEquals(95000, discountedPrice.getValue());
    }

    //Regel 4: Pensionär, köp under 1 000 kr, helgdag -> 0% rabatt
    @Test
    void applyDiscount_PensionerUnder1000OnHoliday_Returns0PercentDiscount(){
        Money price = new Money(10000, "SEK");
        Discount discount = new Discount();

        Money discountedPrice = discount.applyDiscount(price, CustomerRole.PENSIONER, false, true);
        assertEquals(10000, discountedPrice.getValue());
    }

    //Regel 5: Student, köp över 999kr, torsdag, Ej helgdag -> 15% rabatt (10% + 5%)
    @Test
    void applyDiscount_StudentOver999OnThursdayNotHoliday_Returns15PercentDiscount(){
        Money price = new Money(100000, "SEK");
        Discount discount = new Discount();

        Money discountedPrice = discount.applyDiscount(price, CustomerRole.STUDENT, true, false);
        assertEquals(85000, discountedPrice.getValue());
    }

    //Regel 6: Student, köp under 1 000 kr, torsdag, ej helgdag -> 10% rabatt
    @Test
    void applyDiscount_StudentUnder1000OnThursdayNotHoliday_Returns10PercentDiscount(){
        Money price = new Money(95000, "SEK");
        Discount discount = new Discount();

        Money discountedPrice = discount.applyDiscount(price, CustomerRole.STUDENT, true, false);
        assertEquals(85500, discountedPrice.getValue());
    }

    //Regel 7: Student, köp över 999 kr, ej torsdag, ej helgdag -> 10% rabatt
    @Test
    void applyDiscount_StudentOver999NotThursdayNotHoliday_Returns10PercentDiscount(){
        Money price = new Money(100000, "SEK");
        Discount discount = new Discount();

        Money discountedPrice = discount.applyDiscount(price, CustomerRole.STUDENT, false, false);
        assertEquals(90000, discountedPrice.getValue());
    }

    //Regel 8: Student, köp under 1 000kr, ej torsdag, ej helg -> 5% rabatt
    @Test
    void applyDiscount_StudentUnder1000NotThursdayNotHoliday_Returns5PercentDiscount(){
        Money price = new Money(90000,"SEK" );
        Discount discount = new Discount();

        Money discountedPrice = discount.applyDiscount(price, CustomerRole.STUDENT, false, false);
        assertEquals(85500, discountedPrice.getValue());
    }

    //Regel 9: Student, köp över 999kr, ej torsdag, helgdag -> 5% rabatt
    @Test
    void applyDiscount_StudentOver999NotThursdayIsHoliday_Returns5PercentDiscount(){
        Money price = new Money(150000, "SEK");
        Discount discount = new Discount();

        Money discountedPrice = discount.applyDiscount(price, CustomerRole.STUDENT, false, true);
        assertEquals(142500, discountedPrice.getValue());
    }

    //Regel 10: Student, köp under 1000 kr, torsdag, helgdag -> 0% rabatt
    @Test
    void applyDiscount_StudentUnder1000IsThursdayIsHoliday_Returns0PercentDiscount(){
        Money price = new Money(90000, "SEK");
        Discount discount = new Discount();

        Money discountedPrice = discount.applyDiscount(price,CustomerRole.STUDENT,true,true);
        assertEquals(90000, discountedPrice.getValue());
    }

    //Regel 13: Standardkund, köp över 1 000kr, torsdag, helgdag -> 5% rabatt
    @Test
    void applyDiscount_StandardOver999IsThursdayIsHoliday_Returns5PercentDiscount(){
        Money price = new Money(150000, "SEK");
        Discount discount = new Discount();

        Money discountedPrice = discount.applyDiscount(price, CustomerRole.STANDARD, true, true);
        assertEquals(142500, discountedPrice.getValue());
    }

    //Regel 12: Standardkund, köp under 1000kr -> 0% rabatt
    @Test
    void applyDiscount_StandardCustomerUnder1000_ReturnsOriginalPrice(){
        Money price = new Money(50000, "SEK");
        Discount discount = new Discount();

        Money discountedPrice = discount.applyDiscount(price, CustomerRole.STANDARD, false, false);
        assertEquals(50000, discountedPrice.getValue());
    }

}

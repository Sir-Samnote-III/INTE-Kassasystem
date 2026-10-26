
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

    //Regel 10: Student, köp över 999kr, Torsdag, Ej helgdag -> 15% rabatt (10% + 5%)
    @Test
    void applyDiscount_StudentOver999OnThursdayNotHoliday_Returns15PercentDiscount(){
        Money price = new Money(100000, "SEK");
        Discount discount = new Discount();

        Money discountedPrice = discount.applyDiscount(price, CustomerRole.STUDENT, true, false);
        assertEquals(85000, discountedPrice.getValue());
    }
    //Regel 1: Standardkund, köp under 1000kr -> 0% rabatt
    @Test
    void applyDiscount_StandardCustomerUnder1000_ReturnsOriginalPrice(){
        Money price = new Money(50000, "SEK");
        Discount discount = new Discount();

        Money discountedPrice = discount.applyDiscount(price, CustomerRole.STANDARD, false, false);
        assertEquals(50000, discountedPrice.getValue());
    }

}

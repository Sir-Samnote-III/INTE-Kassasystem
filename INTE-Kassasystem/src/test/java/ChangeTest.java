import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ChangeTest {

    @Test
    void calculateWhenPaymentIsHigherThanPrice(){
        Money price = new Money(10000, "SEK");  //100kr
        Money payment = new Money(15000, "SEK"); //150kr
        Change change = new Change();

        Money returnMoney = change.calculateChange(price, payment);

        assertEquals(5000, returnMoney.getValue());
    }

    @Test
    void calculateWhenPaymentEqualsPrice(){
        Money price = new Money(10000, "SEK");  //100kr
        Money payment = new Money(10000, "SEK"); //100kr
        Change change = new Change();

        Money returnsMoney = change.calculateChange(price,payment);

        assertEquals(0, returnsMoney.getValue());
    }

    @Test
    void calculateWhenPaymentIsHigherThanPrice_PriceIsZero(){
        Money price = new Money(0, "SEK");
        Money payment = new Money(1000, "SEK");
        Change change = new Change();

        Money returnsMoney = change.calculateChange(price, payment);

        assertEquals(1000, returnsMoney.getValue());
    }

    @Test
    void calculateWhenPaymentIsLessThanPrice(){
        Money price = new Money(15000, "SEK");
        Money payment = new Money(1000, "SEK");
        Change change = new Change();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->{
            change.calculateChange(price, payment);
        });
        assertEquals("Short payment", exception.getMessage());
    }

    @Test
    void calculateWhenPriceIsNegative(){
        Money price = new Money(-5000, "SEK");
        Money payment = new Money(10000, "SEK");
        Change change = new Change();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->{
            change.calculateChange(price, payment);
        });
        assertEquals("Price cannot be negative", exception.getMessage());

    }

    @Test
    void calculateWhenPaymentIsNegative(){
        Money price = new Money(10000, "SEK");
        Money payment = new Money(-50, "SEK");
        Change change = new Change();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->{
            change.calculateChange(price, payment);
        });
        assertEquals("Payment cannot be negative", exception.getMessage());
    }


}

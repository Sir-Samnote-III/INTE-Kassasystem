import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

public class ChangeTest {

    @Test
    void calculateWhenPaymentIsHigherThanPrice(){
        //Standardfall: Giltigt köp där kunden betalar mer än priset
        Money price = new Money(10000, "SEK");  //100kr
        Money payment = new Money(15000, "SEK"); //150kr
        Change change = new Change();

        Money returnMoney = change.calculateChange(price, payment);

        assertEquals(5000, returnMoney.getValue());
    }

    @Test
    void calculateWhenPaymentEqualsPrice(){
        //Standardfall: Kudnen betalar examt jämna pengar
        Money price = new Money(10000, "SEK");  //100kr
        Money payment = new Money(10000, "SEK"); //100kr
        Change change = new Change();

        Money returnsMoney = change.calculateChange(price,payment);

        assertEquals(0, returnsMoney.getValue());
    }

    @Test
    void calculateWhenPaymentIsHigherThanPrice_PriceIsZero(){
        //Varan är gratis
        Money price = new Money(0, "SEK");
        Money payment = new Money(1000, "SEK");
        Change change = new Change();

        Money returnsMoney = change.calculateChange(price, payment);

        assertEquals(1000, returnsMoney.getValue());
    }

    @Test
    void calculateWhenPaymentIsLessThanPrice(){
        //Ogiltig inmatning: Kunden betalar för lite
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
        //Ogilitg inmatning, priset får inte vara negativt.
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
        //Ogiltig inmatning: Betalningen får inte vara negativ.
        Money price = new Money(10000, "SEK");
        Money payment = new Money(-50, "SEK");
        Change change = new Change();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->{
            change.calculateChange(price, payment);
        });
        assertEquals("Payment cannot be negative", exception.getMessage());
    }

    @Test
    void giveBackCorrectDenominations(){
        //Verifierar att växeln bryts ner i minsta antal korrekta valörer
        Money price = new Money(2400, "SEK");
        Money payment = new Money(10000, "SEK");
        Change change = new Change();

        Money totalChange = change.calculateChange(price, payment);
        List<Integer> actualDenominations = change.calculateDenominations(totalChange);

        List<Integer> expectedDenominations = Arrays.asList(5000, 2000, 500, 100);
        assertEquals(expectedDenominations, actualDenominations);
    }

}

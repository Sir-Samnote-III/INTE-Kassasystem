import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MoneyTest {

    @Test
    void moneyExist() {
        Currency sek = new Currency(CurrencyCode.SEK, "Svensk Krona", "kr");
        Money pris = new Money(100,sek );
        assertNotNull(pris);
    }

    @Test
    void correctAmountOfMoney() {
        Currency sek = new Currency(CurrencyCode.SEK, "Svensk Krona", "kr");

        Money pris = new Money(1050, sek);
        assertEquals(10.5, pris.formattedValue());
    }

    @Test
    void whenCurrencyIsNull(){
        Currency invalid = null;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->{
            new Money(12500, invalid);
        });
        assertEquals("Currency cannot be null", exception.getMessage());
    }
}
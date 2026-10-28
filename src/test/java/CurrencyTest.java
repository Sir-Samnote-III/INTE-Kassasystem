import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CurrencyTest {

    @Test
    void currencyShouldStoreCodeAndName(){
        Currency sek = new Currency("SEK", "Svenska kronor", "kr");

        assertEquals("SEK", sek.getCode());
        assertEquals("Svenska kronor", sek.getName());
        assertEquals("kr", sek.getSymbol());
    }

    @Test
    void currencyShouldStoreExchangeRate(){
        Currency eur = new Currency("EUR", "Euro", "€", 11.50);

        assertEquals("EUR", eur.getCode());
        assertEquals("Euro", eur.getName());
        assertEquals("€", eur.getSymbol());

        assertEquals(11.50, eur.getExchangeRate());
    }
    @Test
    void shouldThrowExceptionWhenCreatingForeignCurrencyWithoutExchangeRate(){
        Exception exception = assertThrows(IllegalArgumentException.class, () ->{
            new Currency("Eur", "Euro","€");
        });
        assertEquals("Exchange rate is required for foreign currencies", exception.getMessage());
    }
}

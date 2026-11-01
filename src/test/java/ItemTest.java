import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

public class ItemTest {

    // Svensk krona som används i alla tester (växelkurs 1.0 eftersom sek är basvalutan).
    private final Currency sek = new Currency(CurrencyCode.sek, "Svensk krona", "kr", 1.0);
    
    @Test
    void priceIsStoredAsMoney() {
        Item banana = new Item("Banana", new Money(1000, "sek"));
        assertEquals(1000, banana.getPrice().getValue());
    }

    @Test
    void nameIsStored() {
        Item cookies = new Item("Cookies", new Money(1020, "sek"));
        assertEquals("Cookies", cookies.getName());

    }

    @Test
    void negativePriceThrowsException(){
        Money negativePrice = new Money(-1, "sek");
        assertThrows(IllegalArgumentException.class,
            () -> new Item("Banana", negativePrice));
    }

    @Test
    void zeroPriceAllowed(){
        Item freeItem = new Item("Free sample", new Money(0, "sek"));
        assertEquals(0, freeItem.getPrice().getValue());
    }

    @Test
    void nullPriceThrowsException(){
        assertThrows(IllegalArgumentException.class, () -> new Item("Banana", null));
    }

    @ParameterizedTest 
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  "})
    void invalidNameThrowsException(String invalidName){
        assertThrows(IllegalArgumentException.class, () -> new Item(invalidName, new Money(1000, "sek")));
    }

    @Test 
    void itemWithoutDepositHasZeroDeposit(){
        Item banana = new Item("Banana", new Money(1000, "sek"));
        assertEquals(0, banana.getDeposit().getValue());
    }

    @Test 
    void itemWithDepositReturnsDeposit(){
        Item soda = new Item("Soda", new Money(1500, "sek"), new Money(200, "sek"));
        assertEquals(200, soda.getDeposit().getValue());
    }

    @Test 
    void negativeDepositThrowsException(){
        assertThrows(IllegalArgumentException.class, () -> new Item("Soda", new Money(1500, "sek"), new Money(-1, "sek")));
    }

    @Test 
    void nullDepositThrowsException(){
        assertThrows(IllegalArgumentException.class, () -> new Item("Soda", new Money(1500, "sek"), null));
    }

    @Test 
    void priceWithDepositAddsDepositToPrice(){
        Item soda = new Item("Soda", new Money(1500, "sek"), new Money(200, "sek"));
        assertEquals(1700, soda.getPriceWithDeposit().getValue());
    }

    @Test 
    void priceWithDepositEqualsPriceWhenNoDeposit(){
        Item banana = new Item("Banana", new Money(1000, "sek"));
        assertEquals(1000, banana.getPriceWithDeposit().getValue());
    }
}

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

public class ItemTest {

    @Test
    void priceIsStoredAsMoney() {
        Item banana = new Item("Banana", new Money(1000, "SEK"));
        assertEquals(1000, banana.getPrice().getValue());
    }

    @Test
    void nameIsStored() {
        Item cookies = new Item("Cookies", new Money(1020, "SEK"));
        assertEquals("Cookies", cookies.getName());

    }

    @Test
    void negativePriceThrowsException(){
        Money negativePrice = new Money(-1, "SEK");
        assertThrows(IllegalArgumentException.class,
            () -> new Item("Banana", negativePrice));
    }

    @Test
    void zeroPriceAllowed(){
        Item freeItem = new Item("Free sample", new Money(0, "SEK"));
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
        assertThrows(IllegalArgumentException.class, () -> new Item(invalidName, new Money(1000, "SEK")));
    }
}

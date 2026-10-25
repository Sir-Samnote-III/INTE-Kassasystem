import org.junit.jupiter.api.Test;

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
}

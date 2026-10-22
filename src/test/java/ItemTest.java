import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ItemTest {

    @Test
    public void testPrice(){
        Item vara1 = new Item(1000, "Banan");
        assertEquals(10,vara1.getPrice());
    }

    @Test
    public void testName(){
        Item vara2 = new Item(1020, "Kex");
            assertEquals("Kex", vara2.getName());

    }
}

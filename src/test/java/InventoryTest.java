import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class InventoryTest{

    @Test 
    void itemNotInInventoryHasZeroStock(){
        Inventory inventory = new Inventory();
        Item banana = new Item("Banana", new Money(1000, "SEK"));
        assertEquals(0, inventory.getStock(banana));   
    }
}
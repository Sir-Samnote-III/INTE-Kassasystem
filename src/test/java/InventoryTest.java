import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class InventoryTest{

    // Svensk krona som används i alla tester (växelkurs 1.0 eftersom sek är basvalutan).
    private final Currency sek = new Currency(CurrencyCode.sek, "Svensk krona", "kr", 1.0);

    @Test 
    void itemNotInInventoryHasZeroStock(){
        Inventory inventory = new Inventory();
        Item banana = new Item("Banana", new Money(1000, "sek"));
        assertEquals(0, inventory.getStock(banana));   
    }

    @Test 
    void addingStockIncreasesStock(){
        Inventory inventory = new Inventory();
        Item banana = new Item("Banana", new Money(1000, "sek"));

        inventory.addStock(banana, 10);

        assertEquals(10, inventory.getStock(banana));   
    }

    @Test
    void removingStockDecreasesStock() {
        Inventory inventory = new Inventory();
        Item banana = new Item("Banana", new Money(1000, "sek"));
        inventory.addStock(banana, 10);

        inventory.removeStock(banana, 3);

        assertEquals(7, inventory.getStock(banana));
    }

    @Test
    void removingAllStockGivesZero() {
        Inventory inventory = new Inventory();
        Item banana = new Item("Banana", new Money(1000, "sek"));
        inventory.addStock(banana, 10);

        inventory.removeStock(banana, 10);

        assertEquals(0, inventory.getStock(banana));
    }

    @Test
    void removingMoreThanStockThrowsException() {
        Inventory inventory = new Inventory();
        Item banana = new Item("Banana", new Money(1000, "sek"));
        inventory.addStock(banana, 10);

        assertThrows(IllegalStateException.class, () -> inventory.removeStock(banana, 11));
        assertEquals(10, inventory.getStock(banana));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    void addingInvalidQuantityThrowsException(int quantity){
        Inventory inventory = new Inventory();
        Item banana = new Item("Banana", new Money(1000, "sek"));

        assertThrows(IllegalArgumentException.class, () -> inventory.addStock(banana, quantity));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    void removingInvalidQuantityThrowsException(int quantity){
        Inventory inventory = new Inventory();
        Item banana = new Item("Banana", new Money(1000, "sek"));
        inventory.addStock(banana, 10);

        assertThrows(IllegalArgumentException.class, () -> inventory.removeStock(banana, quantity));
    }
}
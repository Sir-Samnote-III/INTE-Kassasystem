import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ItemGroupTest {
    
    // Svensk krona som används i alla tester (växelkurs 1.0 eftersom sek är basvalutan).
    private final Currency sek = new Currency(CurrencyCode.SEK, "Svensk krona", "kr", 1.0);

    @Test 
    void newGroupIsEmpty(){
        ItemGroup fruit = new ItemGroup("Fruit");
        assertEquals(0, fruit.size());
    }

    @Test 
    void addingItemIncreasesSize(){
        ItemGroup fruit = new ItemGroup("Fruit");
        fruit.add(new Item("Banana", new Money(1000, sek)));
        assertEquals(1, fruit.size());
    }

    @Test 
    void groupContainsAddedItem(){
        ItemGroup fruit = new ItemGroup("Fruit");
        Item banana = new Item("Banana", new Money(1000, sek));
        fruit.add(banana);
        assertTrue(fruit.contains(banana));
    }

    @Test 
    void groupDoesNotContainItemThatWasNotAdded(){
        ItemGroup fruit = new ItemGroup("Fruit");
        Item apple = new Item("Apple", new Money(500, sek));
        assertFalse(fruit.contains(apple));
    }

    @Test 
    void removingItemDecreasesSize(){
        ItemGroup fruit = new ItemGroup("Fruit");
        Item banana = new Item("Banana", new Money(1000, sek));
        fruit.add(banana);

        fruit.remove(banana);

        assertEquals(0, fruit.size());
        assertFalse(fruit.contains(banana));
    }

    @Test
    void removingItemNotInGroupThrowsException(){
        ItemGroup fruit = new ItemGroup("Fruit");
        Item apple = new Item("Apple", new Money(500, sek));

        assertThrows(IllegalArgumentException.class, () -> fruit.remove(apple));
    }

    @Test 
    void addingSameItemTwiceCountsOnce(){
        ItemGroup fruit = new ItemGroup("Fruit");
        Item banana = new Item("Banana", new Money(1000, sek));

        fruit.add(banana);
        fruit.add(banana);

        assertEquals(1, fruit.size());
    }

    @Test 
    void addingNullItemThrowsException(){
        ItemGroup fruit = new ItemGroup("Fruit");
        assertThrows(IllegalArgumentException.class, () -> fruit.add(null));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   "})
    void invalidGroupNameThrowsException(String invalidName){
        assertThrows(IllegalArgumentException.class, () -> new ItemGroup(invalidName));
    }

    @Test 
    void canIterateOverItemsInOrder(){
        ItemGroup fruit = new ItemGroup("Fruit");
        Item banana = new Item("Banana", new Money(1000, sek));
        Item apple = new Item("Apple", new Money(500, sek));
        fruit.add(banana);
        fruit.add(apple);

        List<Item> result = new ArrayList<>();
        for (Item item : fruit){
            result.add(item);
        }

        assertEquals(List.of(banana, apple), result);
    }

    @Test
    void iteratorDoesNotAllowRemoval(){
        ItemGroup fruit = new ItemGroup("Fruit");
        fruit.add(new Item("Banana", new Money(1000, sek)));

        Iterator<Item> iterator = fruit.iterator();
        iterator.next();

        assertThrows(UnsupportedOperationException.class, iterator::remove);
    }

    // Gruppens namn ska gå att hämta, t.ex. för att visa det på ett kvitto eller i statistik.
    @Test
    void groupNameCanBeRetrieved() {
        ItemGroup fruit = new ItemGroup("Fruit");
        assertEquals("Fruit", fruit.getName());
    }

}

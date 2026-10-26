import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ItemGroupTest {
    @Test 
    void newGroupIsEmpty(){
        ItemGroup fruit = new ItemGroup("Fruit");
        assertEquals(0, fruit.size());
    }

    @Test 
    void addingItemIncreasesSize(){
        ItemGroup fruit = new ItemGroup("Fruit");
        fruit.add(new Item("Banana", new Money(1000, "SEK")));
        assertEquals(1, fruit.size());
    }
}

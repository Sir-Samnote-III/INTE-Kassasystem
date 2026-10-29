import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class ReceiptTest {
    
    //ett nytt kvitto utan varor ska ha totalsumman 0 kr
    @Test 
    void newReceipt0Total(){
        Receipt receipt = new Receipt();
        assertEquals(0, receipt.getTotal().getValue());
    }

    //denna test testar totalsumman av alla varors pris inklusive pant
    // banan 10kr, läsk 15kr med 2 kr pant ska bli 27 kr.
    @Test 
    void totalPriceWithDeposit(){
        Receipt receipt = new Receipt();
        receipt.addItem(new Item("Banana", new Money(1000, "SEK")));
        receipt.addItem(new Item("Soda", new Money(1500, "SEK"), new Money(200, "SEK")));

        assertEquals(2700, receipt.getTotal().getValue());
    }

    //ifall samma vara finns två gånger och en tas bort ska bara en vara försvinna
    //två läsk 15 kr ink pant blir 34kr. Vi tar bort en så ska det va 17kr kvar
    @Test
    void removingItemRemovesOnlyOneCopy(){
        Receipt receipt = new Receipt();
        Item soda = new Item("Soda", new Money(1500, "SEK"), new Money(200, "SEK"));
        receipt.addItem(soda);
        receipt.addItem(soda);

        receipt.removeItem(soda);

        assertEquals(1700, receipt.getTotal().getValue());
    }

    //att ta bort en vara som inte finns med på kvittot ska ge undantag
    @Test
    void removingNonExistingItem(){
        Receipt receipt = new Receipt();
        Item banana = new Item("Banana", new Money(1000, "SEK"));

        assertThrows(IllegalArgumentException.class, () -> receipt.removeItem(banana));
    }

    //varor ska hämtas sorterade i bokstavsordning efter namn oavsett ordning
    @Test 
    void itemsAreSortedByName(){
        Receipt receipt = new Receipt();
        Item soda = new Item("Soda", new Money(1500, "SEK"));
        Item apple = new Item("Apple", new Money(500, "SEK"));
        Item banana = new Item("Banana", new Money(1000, "SEK"));

        receipt.addItem(soda);
        receipt.addItem(apple);
        receipt.addItem(banana);

        assertEquals(List.of(apple, banana, soda), receipt.getItemsSortedByName());
    }

    //ett test för att sortering inte ska bry sig om stora eller små bokstäver
    @Test
    void sortingIgnoresUpperAndLowerCase(){
        Receipt receipt = new Receipt();
        Item soda = new Item("Soda", new Money(1500, "SEK"));
        Item apple = new Item("apple", new Money(500, "SEK"));
        receipt.addItem(soda);
        receipt.addItem(apple);

        assertEquals(List.of(apple, soda), receipt.getItemsSortedByName());
    }
}

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

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
}

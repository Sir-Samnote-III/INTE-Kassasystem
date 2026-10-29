import java.util.ArrayList;
import java.util.List;

public class Receipt {
    
    private final List<Item> items = new ArrayList<>();

    //lägga till vara på kvittot
    public void addItem(Item item){
        items.add(item);
    }

    //returnerar kvittots totalsumma (pris och pant)
    public Money getTotal(){
        int total = 0;
        for (Item item : items){
            total += item.getPriceWithDeposit().getValue();
        }
        
        return new Money(total, "SEK");
    }
}

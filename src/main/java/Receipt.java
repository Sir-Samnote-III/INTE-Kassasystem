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

    //tar bort bara ett exemplar av varan från kvittot och ifall varan finns flera gånger tas bara ett exmplar bort
    public void removeItem(Item item){
        if (!items.remove(item)){
            throw new IllegalArgumentException("Item is not on the receipt");
        }
    }
}

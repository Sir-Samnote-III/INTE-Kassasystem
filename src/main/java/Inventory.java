import java.util.HashMap;
import java.util.Map;


public class Inventory {
    
    private final Map<Item, Integer> stock = new HashMap<>();
    
    public void addStock(Item item, int quantity){
        stock.put(item, getStock(item) + quantity);
    }
    
    public int getStock(Item item){
        return stock.getOrDefault(item, 0); 
        //getOrDefault returnerar 0 om varan inte finns i lagret, så testet från cykel 1 fortsätter vara grönt.
    }
}

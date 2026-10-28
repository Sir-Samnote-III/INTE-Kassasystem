import java.util.HashMap;
import java.util.Map;


public class Inventory {
    
    // Kopplar varje vara till antalet exemplar i lager.
    private final Map<Item, Integer> stock = new HashMap<>();
    

    // Lägger in varor i lagret, t.ex. när en leverans kommer.
    // Antalet måste vara minst 1.
    public void addStock(Item item, int quantity){
        
        if (quantity <= 0){
            throw new IllegalArgumentException("Quantity must be positive");
        }
        
        stock.put(item, getStock(item) + quantity);
    }
    
     // Returnerar hur många exemplar av varan som finns.
    // En vara som aldrig lagts in i lagret har saldo 0.
    public int getStock(Item item){
        return stock.getOrDefault(item, 0); 
    }

    // Tar bort varor från lagret, t.ex. när de säljs.
    // Antalet måste vara minst 1 och får inte vara större än saldot.
    public void removeStock(Item item, int quantity) {
        if (quantity <= 0){
            throw new IllegalArgumentException("Quantity must be positive");
        }
        
        if (quantity > getStock(item)) {
            throw new IllegalStateException("Not enough stock");
        }
        stock.put(item, getStock(item) - quantity);
    
    }
}

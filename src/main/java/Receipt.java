import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.time.Clock;
import java.time.LocalDateTime;

public class Receipt {
    
    private final List<Item> items = new ArrayList<>();

    private final LocalDateTime dateTime;

    //använder datorns riktiga klocka
    public Receipt(){
        this(Clock.systemDefaultZone());
    }

    //konstruktor för tester, klockan skickas in utifrån dependency injection
    public Receipt(Clock clock){
        this.dateTime = LocalDateTime.now(clock);
    }

    //returnerar datum och tid för kvittot
    public LocalDateTime getDateTime(){
        return dateTime;
    }

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

    // Returnerar en kopia av varorna sorterade i bokstavsordning efter namn.
    // Kvittots egen lista ändras inte.
    public List<Item> getItemsSortedByName(){
        List<Item> sorted = new ArrayList<>(items);
        sorted.sort(Comparator.comparing(Item::getName, String.CASE_INSENSITIVE_ORDER));
        return sorted;

    }

    // Skriver ut kvittot: en rad per vara i bokstavsordning, sist totalsumman.
    //Namn längre än 20 tecken kortas av
    public void print(ReceiptPrinter printer){
        for (Item item : getItemsSortedByName()){
            String name = item.getName();
            if (name.length() > 20){
                name = name.substring(0, 20);
            }
            
            printer.printLine(name + " " + item.getPriceWithDeposit().formattedValue());
        }
        printer.printLine("TOTAL " + getTotal().formattedValue());
    }
}

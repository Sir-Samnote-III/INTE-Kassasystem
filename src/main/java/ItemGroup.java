import java.util.List;
import java.util.ArrayList;

public class ItemGroup {
    
    private final String name;
    private final List<Item> items = new ArrayList<>();

    public ItemGroup(String name){
        this.name = name;
    }
    
    public void add(Item item){
        items.add(item);
    }

    public int size(){
        return items.size();
    }

    public boolean contains(Item item){
        return items.contains(item);
    }

    public void remove(Item item){
        items.remove(item);
    }
}

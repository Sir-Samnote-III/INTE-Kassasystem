import java.util.LinkedHashSet;
import java.util.Set;

public class ItemGroup {
    
    private final String name;
    private final Set<Item> items = new LinkedHashSet<>();

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
        if (!items.remove(item)){
            throw new IllegalArgumentException("Item is not in the group");
        }
    }
}

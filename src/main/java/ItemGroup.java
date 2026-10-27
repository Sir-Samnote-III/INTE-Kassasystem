import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

public class ItemGroup implements Iterable<Item> {
    
    @Override
    public Iterator<Item> iterator() {
        return items.iterator();
    }

    private final String name;
    private final Set<Item> items = new LinkedHashSet<>();

    public ItemGroup(String name){
        if (name == null || name.isBlank()){
            throw new IllegalArgumentException("Group name cannot be empty");
        }
        this.name = name;
    }
    
    public void add(Item item){
       if (item == null){
            throw new IllegalArgumentException("Item cannot be null");
       } 
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

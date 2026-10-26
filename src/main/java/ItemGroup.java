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
}

import java.util.ArrayList;
import java.util.List;

public class FakePrinter implements ReceiptPrinter {
    
    private final List<String> lines = new ArrayList<>();

    @Override 
    public void printLine(String line){
        lines.add(line);
    }

    public List<String> getLines(){
        return lines;
    }

}

public class Money {

    int value;
    String currency;

    public Money(int value, String currency) {
        this.value = value;
        this.currency = currency;
    }

    public int formattedValue(Money a){
        return a.getValue()/1000;
    }

    public int getValue(){
        return value;
    }
}

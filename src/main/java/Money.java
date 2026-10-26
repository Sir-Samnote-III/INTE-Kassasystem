public class Money {

    int value;
    String currency;

    public Money(int value, String currency) {
        this.value = value;
        this.currency = currency;
    }

    public double formattedValue() {
        return (double) this.getValue()/100;
    }

    public int getValue() {
        return value;
    }

    public String getCurrency(){
        return currency;
    }
}
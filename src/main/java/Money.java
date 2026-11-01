public class Money {

    long value;
    String currency;

    public Money(long value, String currency) {
        this.value = value;
        this.currency = currency;
    }

    public double formattedValue() {
        return (double) this.getValue()/100;
    }

    public long getValue() {
        return value;
    }

    public String getCurrency(){
        return currency;
    }
}
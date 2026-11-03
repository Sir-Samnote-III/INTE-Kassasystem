public final class Money {

    private long value;
    private Currency currency;

    public Money(long value, Currency currency) {
        if (currency == null){
            throw new IllegalArgumentException("Currency cannot be null");
        }
        this.value = value;
        this.currency = currency;
    }

    public double formattedValue() {
        return (double) this.getValue()/100;
    }

    public long getValue() {
        return value;
    }

    public Currency getCurrency(){
        return currency;
    }
}
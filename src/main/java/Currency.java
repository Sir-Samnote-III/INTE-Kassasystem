public class Currency {

    private CurrencyCode code;
    private String name;
    private String symbol;
    private double exchangeRate;

    public Currency(CurrencyCode code, String name, String symbol, double exchangeRate){
        this.code = code;
        this.name = name;
        this.symbol = symbol;
        this.exchangeRate = exchangeRate;
    }
    public Currency(CurrencyCode code, String name, String symbol){
        if (!code.equals("SEK")){
            throw new IllegalArgumentException("Exchange rate is required for foreign currencies");
        }
        this.code = code;
        this.name = name;
        this.symbol = symbol;
        this.exchangeRate = 1.0;
    }

    public CurrencyCode getCode() {
        return code;
    }

    public String getName(){
        return name;
    }

    public String getSymbol(){
        return symbol;
    }

    public double getExchangeRate(){
        return exchangeRate;
    }
}

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

public class Customer {
    
    private String customerNamn;
    private String customerId;
    private String adress;
    private String personnummer;
    private String telefonnummer;
    private String ePostadress;
    private Membership membership;

    private int antalKop = 0;
    private BigDecimal totalKopSumma = BigDecimal.ZERO;
    private BigDecimal biggestPurchase = BigDecimal.ZERO;
    private LocalDate latestPruchase;

    public Customer(String customerNamn, String customerId, String adress, String personnummer, String telefonnummer, String ePostadress) {
        this(customerNamn, customerId, adress, personnummer, telefonnummer, ePostadress, null);
    }

    public Customer(String customerNamn, String customerId, String adress, String personnummer, String telefonnummer, String ePostadress, Membership membership) {
        this.customerNamn = customerNamn;
        this.customerId = customerId;
        this.adress = adress;
        this.personnummer = personnummer;
        this.telefonnummer = telefonnummer;
        this.ePostadress = ePostadress;
        this.membership = membership;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getCustomerNamn() {
        return customerNamn;
    }

    public String getAdress() {
        return adress;
    }

    public String getPersonnummer() {
        return personnummer;
    }

    public String getTelefunnummer() {
        return telefonnummer;
    }

    public String getEPost() {
        return ePostadress;
    }

    public int getAntalKop() {
        return antalKop;
    }

    public BigDecimal getTotalSumma() {
        return totalKopSumma;
    }

    public Membership getMembership() {
        return membership;
    }

    public void registreradeKop(BigDecimal belopp) {
        if (belopp == null || belopp.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Köp summan måste vara större än noll");
        }

        totalKopSumma = totalKopSumma.add(belopp);
        antalKop++;
        latestPruchase = LocalDate.now();

        if (belopp.compareTo(biggestPurchase) > 0) {
            biggestPurchase = belopp;
        }

        if (membership != null) {
            int beloppIOren = belopp.movePointRight(2).intValueExact();

            Money kopesumma = new Money(beloppIOren, "SEK");
            membership.registerPoints(kopesumma);
        }
    }

    public BigDecimal getAveragePruchase() {
        if (antalKop == 0) {
            return BigDecimal.ZERO;
        }

        return totalKopSumma.divide(BigDecimal.valueOf(antalKop), 2, RoundingMode.HALF_UP);
    }

    public BigDecimal getBiggestPurchase() {
        return biggestPurchase;
    }

    public LocalDate getLatestPurchase() {
        return latestPruchase;
    }
}

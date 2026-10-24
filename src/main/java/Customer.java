import java.math.BigDecimal;

public class Customer {
    
    private String customerNamn;
    private String customerId;
    private String adress;
    private String personnummer;
    private String telefonnummer;
    private String ePostadress;

    private int antalKop = 0;
    private BigDecimal totalKopSumma = BigDecimal.ZERO;

    public Customer(String customerNamn, String customerId, String adress, String personnummer, String telefonnummer, String ePostadress) {
        this.customerNamn = customerNamn;
        this.customerId = customerId;
        this.adress = adress;
        this.personnummer = personnummer;
        this.telefonnummer = telefonnummer;
        this.ePostadress = ePostadress;
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

    public void registreradeKop(BigDecimal belopp) {
        if (belopp == null || belopp.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Köp summan måste vara större än noll");
        }

        totalKopSumma = totalKopSumma.add(belopp);
        antalKop++;
    }
}

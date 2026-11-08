import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

public final class Customer {

    private final String customerName;
    private final String customerId;
    private final String address;
    private final String personalNumber;
    private final String phoneNumber;
    private final String emailAddress;
 
    private final Membership membership;
    private final CustomerRole customerRole;
    private final LocalDate birthDate;
 
    private int purchaseCount = 0;
    private BigDecimal totalPurchaseAmount = BigDecimal.ZERO;
    private BigDecimal biggestPurchase = BigDecimal.ZERO;
    private LocalDate latestPruchase;
    
    public Customer(String customerName, String customerId, String address, String personalNumber, String phoneNumber, String emailAddress) {
        this(customerName, customerId, address, personalNumber, phoneNumber, emailAddress, null, CustomerRole.STANDARD);
    }
 
    public Customer(String customerName, String customerId, String address, String personalNumber, String phoneNumber, String emailAddress, Membership membership, CustomerRole customerRole) {
        this(customerName, customerId, address, personalNumber, phoneNumber, emailAddress, membership, customerRole, null);
    }
 
    public Customer(String customerName, String customerId, String address, String personalNumber, String phoneNumber, String emailAddress, Membership membership, CustomerRole customerRole, LocalDate birthDate) {
        this.customerName = customerName;
        this.customerId = customerId;
        this.address = address;
        this.personalNumber = personalNumber;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
        this.membership = membership;
        this.customerRole = customerRole;
 
        if (birthDate != null && birthDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Birth date cannot be in the future");
        }
 
        this.birthDate = birthDate;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getAddress() {
        return address;
    }

    public String getPersonalNumber() {
        return personalNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return emailAddress;
    }

    public CustomerRole getCustomerRole() {
        return customerRole;
    }

    public int getPurchaseCount() {
        return purchaseCount;
    }
 
    public BigDecimal getTotalAmount() {
        return totalPurchaseAmount;
    }
 
    public Membership getMembership() {
        return membership;
    }

    public void registerPurchase(BigDecimal belopp, boolean isThursday, Currency currency) {
        if (belopp == null || belopp.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Köp summan måste vara större än noll");
        }

        totalPurchaseAmount = totalPurchaseAmount.add(belopp);
        purchaseCount++;
        latestPruchase = LocalDate.now();

        if (belopp.compareTo(biggestPurchase) > 0) {
            biggestPurchase = belopp;
        }

        if (membership != null) {
            int beloppIOren = belopp.movePointRight(2).intValueExact();

            Money kopesumma = new Money(beloppIOren, currency);
            membership.registerPoints(kopesumma, customerRole, isThursday);
        }
    }

    public BigDecimal getAveragePruchase() {
        if (purchaseCount == 0) {
            return BigDecimal.ZERO;
        }

        return totalPurchaseAmount.divide(BigDecimal.valueOf(purchaseCount), 2, RoundingMode.HALF_UP);
    }

    public BigDecimal getBiggestPurchase() {
        return biggestPurchase;
    }

    public LocalDate getLatestPurchase() {
        return latestPruchase;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }
}
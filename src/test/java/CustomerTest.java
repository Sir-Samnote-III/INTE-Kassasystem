import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CustomerTest {

    private static final Currency sek = new Currency(CurrencyCode.SEK, "Swedish Krona", "kr", 1.0);
    private static final int EXPECTED_POINTS = 500;
    private static final BigDecimal PURCHASE_AMOUNT = new BigDecimal("250.00");

    @Test
    void customerWithAllDetails() {
        Customer customer = new Customer(
            "Bob", "K01", "Gatan 1",
            "200002101234", "0701234567", "bob@gmail.com"
        );

        assertEquals("Bob", customer.getCustomerName());
        assertEquals("K01", customer.getCustomerId());
        assertEquals("Gatan 1", customer.getAddress());
        assertEquals("200002101234", customer.getPersonalNumber());
        assertEquals("0701234567", customer.getPhoneNumber());
        assertEquals("bob@gmail.com", customer.getEmail());
    }

    @Test
    void customersWithSeparateDetails() {
        Customer customer1 = new Customer(
            "Bob", "K01", "Storgatan 1",
            "199001011234", "0701234567", "bob@example.com"
        );

        Customer customer2 = new Customer(
            "Filip", "K02", "Storgatan 2",
            "199505056789", "0707654321", "filip@example.com"
        );

        assertEquals("Bob", customer1.getCustomerName());
        assertEquals("K01", customer1.getCustomerId());

        assertEquals("Filip", customer2.getCustomerName());
        assertEquals("K02", customer2.getCustomerId());
    }

    @Test
    void getCustomerRoleReturnsCorrectRole() {
        Membership membership = new Membership(LocalDate.of(2026, 1, 1), LocalDate.of(2026, 12, 31));

        Customer customer = new Customer(
            "Anna", "A1", "Stockholm",
            "200001011234", "0701234567", "anna@gmail.com",
            membership, CustomerRole.PENSIONER
        );

        assertEquals(CustomerRole.PENSIONER, customer.getCustomerRole());
    }

    @Test
        void birthDateCanBeNull() {
        Customer customer = new Customer(
            "Anna", "A1", "Stockholm",
            "200001011234", "0701234567", "anna@gmail.com",
            null, CustomerRole.STANDARD
        );

        assertNull(customer.getBirthDate());
    }

    @Test
    void nullPurchaseThrowsException() {
    Customer customer = new Customer(
            "Anna", "A1", "Stockholm",
            "200001011234", "0701234567", "anna@gmail.com"
        );

        assertThrows(IllegalArgumentException.class,
            () -> customer.registerPurchase(null, false, sek));

        assertEquals(0, customer.getPurchaseCount());
        assertEquals(0, BigDecimal.ZERO.compareTo(customer.getTotalAmount()));
    }

    @Test
    void averagePurchaseRoundsToTwoDecimals() {
        Customer customer = new Customer(
            "Anna", "A1", "Stockholm",
            "200001011234", "0701234567", "anna@gmail.com"
        );

        customer.registerPurchase(new BigDecimal("10.00"), false, sek);
        customer.registerPurchase(new BigDecimal("10.01"), false, sek);
        customer.registerPurchase(new BigDecimal("10.02"), false, sek);

        assertEquals(0,
            new BigDecimal("10.01").compareTo(customer.getAveragePurchase()));
    }

    @Test
    void averagePurchaseIsZeroWhenNoPurchases() {
        Customer customer = new Customer(
            "Anna", "A1", "Stockholm",
            "200001011234", "0701234567", "anna@gmail.com"
        );

        assertEquals(0, BigDecimal.ZERO.compareTo(customer.getAveragePurchase()));
    }

    @Test
        void getMembershipReturnsCorrectMembership() {
        Membership membership = new Membership(LocalDate.of(2026, 1, 1), LocalDate.of(2026, 12, 31));

        Customer customer = new Customer(
            "Anna", "A1", "Stockholm",
            "200001011234", "0701234567", "anna@gmail.com",
            membership, CustomerRole.STANDARD
        );

        assertSame(membership, customer.getMembership());
    }

    @Test
    void getBirthDateReturnsCorrectDate() {
        LocalDate birthDate = LocalDate.of(2000, 5, 15);

        Customer customer = new Customer(
            "Anna", "A1", "Stockholm",
            "200005151234", "0701234567", "anna@gmail.com",
            null, CustomerRole.STANDARD, birthDate
        );

        assertEquals(birthDate, customer.getBirthDate());
    }

    @Test
    void futureBirthDateThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
            new Customer(
                "Anna", "A1", "Stockholm",
                "200005151234", "0701234567", "anna@gmail.com",
                null, CustomerRole.STANDARD, LocalDate.now().plusDays(1)
            )
        );
    }

    @Test
    void customerHasNoRegisteredPurchases() {
        Customer customer = new Customer(
            "Bob", "K01", "Gatan 1",
            "200201011234", "0701234567", "bob@gmail.com"
        );

        assertEquals(0, customer.getPurchaseCount());
    }

    @Test
    void registerOnePurchase() {
        Customer customer = new Customer(
            "Bob", "K01", "Storgatan 1",
            "199001011234", "0701234567", "bob@example.com"
        );

        customer.registerPurchase(new BigDecimal("100.00"), false, sek);

        assertEquals(1, customer.getPurchaseCount());
    }

    @Test
    void multiplePurchasesUpdateInformation() {
        Customer customer = new Customer(
            "Bob", "K01", "Storgatan 1",
            "199001011234", "0701234567", "bob@example.com"
        );

        customer.registerPurchase(new BigDecimal("100.00"), false, sek);
        customer.registerPurchase(new BigDecimal("50.00"), false, sek);

        assertEquals(2, customer.getPurchaseCount());
        assertEquals(0, new BigDecimal("150.00").compareTo(customer.getTotalAmount()));
    }

    @Test
    void invalidPurchaseDoesNotChangeHistory() {
        Customer customer = new Customer(
            "Marcus", "K01", "Gatan 2",
            "199001011234", "0701234567", "marcus@gmail.com"
        );

        customer.registerPurchase(new BigDecimal("100.00"), false, sek);

        assertThrows(IllegalArgumentException.class, () -> customer.registerPurchase(new BigDecimal("-20.00"), false, sek));

        assertEquals(1, customer.getPurchaseCount());
        assertEquals(0, new BigDecimal("100.00").compareTo(customer.getTotalAmount()));
    }

    @Test
    void twoCustomersHaveSeparateHistories() {
        Customer bob = new Customer(
            "Bob", "K01", "Gatan 2",
            "199001011234", "0701234567", "bob@gmail.com"
        );

        Customer anna = new Customer(
            "Anna", "K02", "Gatan 45",
            "200201011234", "0701234557", "anna@gmail.com"
        );

        bob.registerPurchase(new BigDecimal("200.00"), false, sek);

        assertEquals(1, bob.getPurchaseCount());
        assertEquals(0, anna.getPurchaseCount());

        assertEquals(0, new BigDecimal("200.00").compareTo(bob.getTotalAmount()));
        assertEquals(0, BigDecimal.ZERO.compareTo(anna.getTotalAmount()));
    }

    @Test
    void registeredBuyMember() {
        Membership membership = new Membership(LocalDate.of(2026, 2, 1), LocalDate.of(2027, 1, 1));

        Customer customer = new Customer(
            "Anna", "A1", "Gatan 2",
            "200004225432", "0701234567", "anna@gmail.com",
            membership, CustomerRole.STANDARD
        );

        customer.registerPurchase(PURCHASE_AMOUNT, false, sek);

        assertEquals(EXPECTED_POINTS, membership.getPoints());
    }

    @Test
    void customerRegistersBonusBasedOnCustomerRole() {
        Membership membership = new Membership(
            LocalDate.of(2026, 2, 1),
            LocalDate.of(2027, 1, 1)
        );

        Customer customer = new Customer(
            "Anna", "A1", "Gatna 2",
            "200004225432", "0701234567", "anna@gmail.com",
            membership, CustomerRole.PENSIONER
        );

        customer.registerPurchase(new BigDecimal("100.00"), false, sek);

        assertEquals(300, membership.getPoints());
    }

    @Test
    void registerBiggestPurchase() {
        Customer customer = new Customer(
            "Anna", "A1", "Gatna 2",
            "200004225432", "0701234567", "anna@gmail.com"
        );

        customer.registerPurchase(new BigDecimal("200.00"), false, sek);

        assertEquals(0, new BigDecimal("200.00").compareTo(customer.getBiggestPurchase()));
    }

    @Test
    void biggestPurchaseGetsUpdated() {
        Customer customer = new Customer(
            "Anna", "A1", "Gatna 2",
            "200004225432", "0701234567", "anna@gmail.com"
        );

        customer.registerPurchase(new BigDecimal("300.00"), false, sek);
        customer.registerPurchase(new BigDecimal("100.00"), false, sek);
        customer.registerPurchase(new BigDecimal("600.00"), false, sek);

        assertEquals(0, new BigDecimal("600.00").compareTo(customer.getBiggestPurchase()));
    }

    @Test
    void registeredPurchasesSaveCurrentDate() {
        Customer customer = new Customer(
            "Anna", "A1", "Gatna 2",
            "200004225432", "0701234567", "anna@gmail.com"
        );

        customer.registerPurchase(new BigDecimal("300.00"), false, sek);

        assertEquals(LocalDate.now(), customer.getLatestPurchase());
    }

    @Test
    void averagePurchasesAreCorrect() {
        Customer customer = new Customer(
            "Anna", "A1", "Gatna 2",
            "200004225432", "0701234567", "anna@gmail.com"
        );

        customer.registerPurchase(new BigDecimal("100.00"), false, sek);
        customer.registerPurchase(new BigDecimal("200.00"), false, sek);
        customer.registerPurchase(new BigDecimal("300.00"), false, sek);

        assertEquals(0, new BigDecimal("200.00").compareTo(customer.getAveragePurchase()));
    }

    @Test
    void invalidBuysDontChangeTheStatistics() {
        Customer customer = new Customer(
            "Anna", "A1", "Gatna 2",
            "200004225432", "0701234567", "anna@gmail.com"
        );

        assertThrows(IllegalArgumentException.class, () -> customer.registerPurchase(new BigDecimal("-100.00"), false, sek));

        assertEquals(0, customer.getPurchaseCount());
        assertEquals(0, BigDecimal.ZERO.compareTo(customer.getTotalAmount()));
    }
}
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.beans.Transient;
import java.math.BigDecimal;
import java.time.LocalDate;

public class CustomerTest {

    @Test
    void kundMedAllaUppgifter() {
        Customer customer = new Customer("Bob",
        "K01",
        "Gatan 1",
        "200002101234",
        "0701234567",
        "bob@gmail.com");

        assertEquals("Bob", customer.getCustomerNamn());
        assertEquals("K01", customer.getCustomerId());
        assertEquals("Gatan 1", customer.getAdress());
        assertEquals("200002101234", customer.getPersonnummer());
        assertEquals("0701234567", customer.getTelefunnummer());
        assertEquals("bob@gmail.com", customer.getEPost());
    }

 
    @Test
    void kunderMedSeparataUppgifter() {
        Customer customer1 = new Customer(
        "Bob", "K01", "Storgatan 1",
        "199001011234", "0701234567", "bob@example.com"
    );
        Customer customer2 = new Customer(
        "Filip", "K02", "Storgatan 2",
        "199505056789", "0707654321", "filip@example.com"
    );

        assertEquals("Bob", customer1.getCustomerNamn());
        assertEquals("K01", customer1.getCustomerId());

        assertEquals("Filip", customer2.getCustomerNamn());
        assertEquals("K02", customer2.getCustomerId());
    }

    @Test
    void kundHarIngaRegistreradeKop() {
        Customer customer = new Customer(
        "Bob",
        "K01",
        "Gatan 1",
        "200201011234",
        "0701234567",
        "bob@gmail.com"
    );

        assertEquals(0, customer.getAntalKop());
    }

    @Test 
    void registreraEttKop() {
        Customer customer = new Customer(
        "Bob",
        "K01",
        "Storgatan 1",
        "199001011234",
        "0701234567",
        "bob@example.com"
    );

        customer.registreradeKop(new BigDecimal("100.00"));

        assertEquals(1, customer.getAntalKop());
    }


    //flera köp där man uppdaterar antal och summa  
    @Test 
    void fleraKopUppdateraInformation() {
        Customer customer = new Customer(
        "Bob",
        "K01",
        "Storgatan 1",
        "199001011234",
        "0701234567",
        "bob@example.com"
    );

    customer.registreradeKop(new BigDecimal("100.00"));
    customer.registreradeKop(new BigDecimal("50.00"));

    assertEquals(2, customer.getAntalKop());
    assertEquals(0, new BigDecimal("150.00").compareTo(customer.getTotalSumma()));
        
    }

    //Ogiltiga köp förändrar inte kundens tidigare historik
    @Test 
    void ogilitigtKopForandrarInteHistorik() {
        
        Customer customer = new Customer(
        "Marcus",
        "K01",
        "Gatan 2",
        "199001011234",
        "0701234567",
        "marcus@gmail.com"
    );

        customer.registreradeKop(new BigDecimal("100.00"));

        assertThrows(IllegalArgumentException.class, () -> customer.registreradeKop(new BigDecimal("-20.00")));

        assertEquals(1, customer.getAntalKop());
        assertEquals(0, new BigDecimal("100.00").compareTo(customer.getTotalSumma()));
    }

    @Test 
    void tvaKunderOlikaHistoriker() {

        Customer bob = new Customer(
        "Bob",
        "K01",
        "Gatan 2",
        "199001011234",
        "0701234567",
        "bob@gmail.com"
    );

        Customer anna = new Customer(
        "Anna",
        "K02",
        "Gatan 45",
        "200201011234",
        "0701234557",
        "anna@gmail.com"
    );

        bob.registreradeKop(new BigDecimal("200.00"));

        assertEquals(1, bob.getAntalKop());
        assertEquals(0, anna.getAntalKop());

        assertEquals(0, new BigDecimal("200.00").compareTo(bob.getTotalSumma()));
        assertEquals(0, BigDecimal.ZERO.compareTo(anna.getTotalSumma()));
    }

    @Test 
    void registeredBuyMember() {
        Membership membership = new Membership(LocalDate.of(2026, 2, 1), LocalDate.of(2027, 1, 1));

        Customer customer = new Customer("Anna", "A1", "Gatna 2", "200004225432", "0701234567", "anna@gmail.com", membership);

        customer.registreradeKop(new BigDecimal("250.00"));

        assertEquals(500, membership.getPoints());
    }

    @Test 
    void registerBiggestPurchase() {
        Customer customer = new Customer("Anna", "A1", "Gatna 2", "200004225432", "0701234567", "anna@gmail.com");

        customer.registreradeKop(new BigDecimal("200.00"));

        assertEquals(0, new BigDecimal("200.00").compareTo(customer.getBiggestPurchase()));
    }

    @Test
    void biggestPurchaseGetsUpdated() {
        Customer customer = new Customer("Anna", "A1", "Gatna 2", "200004225432", "0701234567", "anna@gmail.com"); 

        customer.registreradeKop(new BigDecimal("300.00"));
        customer.registreradeKop(new BigDecimal("100.00"));
        customer.registreradeKop(new BigDecimal("600.00"));

        assertEquals(0, new BigDecimal("600.00").compareTo(customer.getBiggestPurchase()));
    }

    @Test 
    void registeredPurchasesSaveCurrentDate() {
        Customer customer = new Customer("Anna", "A1", "Gatna 2", "200004225432", "0701234567", "anna@gmail.com");

        customer.registreradeKop(new BigDecimal("300.00"));

        assertEquals(LocalDate.now(), customer.getLatestPurchase());
    }

    @Test 
    void averagePurchasesAreCorrect() {
        Customer customer = new Customer("Anna", "A1", "Gatna 2", "200004225432", "0701234567", "anna@gmail.com");

        customer.registreradeKop(new BigDecimal("100.00"));
        customer.registreradeKop(new BigDecimal("200.00"));
        customer.registreradeKop(new BigDecimal("300.00"));

        assertEquals(0, new BigDecimal("200.00").compareTo(customer.getAveragePruchase()));
    }

    @Test
    void invalidBuysDontChangeTheStatistics() {
        Customer customer = new Customer("Anna", "A1", "Gatna 2", "200004225432", "0701234567", "anna@gmail.com");

        assertThrows(IllegalArgumentException.class, () -> customer.registreradeKop(new BigDecimal("-100.00")));

        assertEquals(0, customer.getAntalKop());
        assertEquals(0, BigDecimal.ZERO.compareTo(customer.getTotalSumma()));

    }

}

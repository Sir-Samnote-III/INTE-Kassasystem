import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;

public class CheckoutTest {

    @Test
    void scanItem_ShouldIncludeDepositInTotal() {
        // RÄTTELSE 1: Staff tar två strängar (ID och Namn)
        Staff staff = new Staff("S01", "Antonios");
        Checkout checkout = new Checkout("SEK", staff);

        // RÄTTELSE 2: Money tar en String för valutan, inte ett Currency-objekt
        Money price = new Money(1500, "SEK");
        Money deposit = new Money(200, "SEK");
        Item soda = new Item("Läsk", price, deposit);

        checkout.scanItem(soda);

        assertEquals(1700, checkout.getTotal().getValue());
    }

    @Test
    void pay_ShouldRegisterPurchaseOnCustomerAndStaff() {
        Staff staff = new Staff("S02", "Luka");
        Checkout checkout = new Checkout("SEK", staff);

        Customer customer = new Customer("Test Testsson", "CUST-01", "Gata 1", "900101-1234", "070123", "test@test.se");
        checkout.attachCustomer(customer);

        checkout.scanItem(new Item("Jacka", new Money(10000, "SEK")));

        Money payment = new Money(10000, "SEK");
        checkout.pay(payment);

        // Verifiera kunden
        assertEquals(1, customer.getAntalKop());
        assertEquals(new BigDecimal("100.00"), customer.getTotalSumma());

        // Verifiera personalen (RÄTTELSE 3)
        assertEquals(1, staff.getAmountSale());
        assertEquals(new BigDecimal("100.00"), staff.getTotalSale());
    }
}
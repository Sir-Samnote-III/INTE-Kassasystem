import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;

public class CheckoutTest {

    @Test
    void scanItem_ShouldIncludeDepositInTotal() {
// 1. Skapa valuta-objektet
        Currency sek = new Currency(CurrencyCode.SEK, "Svensk Krona", "kr");

        Staff staff = new Staff("S01", "Antonios");
        // 2. Skicka in objektet (sek), inte CurrencyCode.SEK
        Checkout checkout = new Checkout(sek, staff);

        // 3. Använd objektet för Money
        Money price = new Money(1500L, sek);
        Money deposit = new Money(200L, sek);
        Item soda = new Item("Läsk", price, deposit);

        checkout.scanItem(soda);

        assertEquals(1700L, checkout.getTotal().getValue());
    }

    @Test
    void pay_ShouldRegisterPurchaseOnCustomerAndStaff() {
        // 1. Skapa valuta-objektet
        Currency sek = new Currency(CurrencyCode.SEK, "Svensk Krona", "kr", 1.0);

        Staff staff = new Staff("S02", "Luka");
        // 2. Skicka in objektet, inte strängen "SEK"
        Checkout checkout = new Checkout(sek, staff);

        Customer customer = new Customer("Test Testsson", "CUST-01", "Gata 1", "900101-1234", "070123", "test@test.se");
        checkout.attachCustomer(customer);

        checkout.scanItem(new Item("Jacka", new Money(10000L, sek)));

        Money payment = new Money(10000L, sek);
        checkout.pay(payment);

        assertEquals(1, customer.getAntalKop());
        assertEquals(new BigDecimal("100.00"), customer.getTotalSumma());

        assertEquals(1, staff.getAmountSale());
        assertEquals(new BigDecimal("100.00"), staff.getTotalSale());
    }
}
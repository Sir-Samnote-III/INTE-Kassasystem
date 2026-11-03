import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public class CheckoutTest {

    @Test
    void scanItem_ShouldIncludeDepositInTotal() {
        // 1. Skapa valuta-objektet
        Currency sek = new Currency(CurrencyCode.SEK, "Svensk Krona", "kr", 1.0);

        Staff staff = new Staff("S01", "Antonios");
        // 2. Skicka in objektet (sek), inte CurrencyCode.SEK
        Checkout checkout = new Checkout(sek, staff);

        // 3. Använd objektet för Money
        Money price = new Money(1500L, sek);
        Money deposit = new Money(200L, sek);
        Item soda = new Item("Läsk", price, deposit);

        checkout.scanItem(LocalDate.of(2000, 1, 1), soda);

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

        checkout.scanItem(LocalDate.of(2000, 1, 1), new Item("Jacka", new Money(10000L, sek)));

        Money payment = new Money(10000L, sek);
        checkout.pay(payment);

        assertEquals(1, customer.getPurchaseCount());
        assertEquals(new BigDecimal("100.00"), customer.getTotalAmount());

        assertEquals(1, staff.getAmountSale());
        assertEquals(new BigDecimal("100.00"), staff.getTotalSale());
    }

    // Lagret ska uppdateras automatiskt när något köps:
    // 10 läsk i lager, kunden köper 2 → 8 kvar efter betalning.
    @Test
    void pay_ShouldReduceStockInInventory() {
        Currency sek = new Currency(CurrencyCode.SEK, "Svensk Krona", "kr", 1.0);
        Checkout checkout = new Checkout(sek, new Staff("S03", "Mohammad"));
        Inventory inventory = new Inventory();
        Item soda = new Item("Läsk", new Money(1500L, sek), new Money(200L, sek));
        inventory.addStock(soda, 10);
        checkout.attachInventory(inventory);

        checkout.scanItem(LocalDate.of(2000, 1, 1), soda, soda);
        checkout.pay(new Money(5000L, sek));

        assertEquals(8, inventory.getStock(soda));
    }

    // Lagret ska inte ändras förrän kunden har betalat.
    @Test
    void scanItem_ShouldNotChangeStockBeforePayment() {
        Currency sek = new Currency(CurrencyCode.SEK, "Svensk Krona", "kr", 1.0);
        Checkout checkout = new Checkout(sek, new Staff("S03", "Mohammad"));
        Inventory inventory = new Inventory();
        Item soda = new Item("Läsk", new Money(1500L, sek), new Money(200L, sek));
        inventory.addStock(soda, 10);
        checkout.attachInventory(inventory);

        checkout.scanItem(LocalDate.of(2000, 1, 1), soda);

        assertEquals(10, inventory.getStock(soda));
    }

    @Test 
    void customerUnderAgeCannotBuyEnergyDrink() {
        Currency sek = new Currency(CurrencyCode.SEK, "Svensk Krona", "kr", 1.0);

        Checkout checkout = new Checkout(sek, new Staff("S01", "Filip"));

        Item energyDrink = new Item("Energy drink", new Money(2000L, sek), new Money(0L, sek), ProductAgeLimits.ENERGY_DRINK);

        assertThrows(IllegalStateException.class, 
            () -> checkout.scanItem(LocalDate.of(2012, 1, 2), energyDrink));

        assertEquals(0L, checkout.getTotal().getValue());
    }

    @Test 
    void customerMeetsAgeRequirementCanBuyAlcohol() {
        Currency euro = new Currency(CurrencyCode.EUR, "Euro", "€", 1.0);

        Checkout checkout = new Checkout(euro, new Staff("S01", "Mattias"));

        Item alcohol = new Item("Alcohol", new Money(5000l, euro), new Money(0L, euro), ProductAgeLimits.ALCOHOL);

        LocalDate birthDate = LocalDate.now().minusYears(20);

        checkout.scanItem(birthDate, alcohol);

        assertEquals(5000L, checkout.getTotal().getValue());
        assertEquals(euro, checkout.getTotal().getCurrency());
    }
}
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.DayOfWeek;

public class Checkout {
    private Currency baseCurrency;
    private Staff activeStaff;
    private Customer currentCustomer; // Kunden som gör köpet
    private AgeControl ageControl;
    private Inventory inventory;

    private List<Item> scannedItems;
    private Money currentTotal;

    private Discount discountCalculator;
    private Change changeCalculator;

    public Checkout(Currency baseCurrency, Staff activeStaff) {
        this.baseCurrency = baseCurrency;
        this.activeStaff = activeStaff;
        this.scannedItems = new ArrayList<>();
        this.currentTotal = new Money(0, baseCurrency);

        this.discountCalculator = new Discount();
        this.changeCalculator = new Change();
        this.ageControl = new AgeControl();
    }

    // Möjliggör inloggning av personal
    public void setActiveStaff(Staff staff) {
        this.activeStaff = staff;
    }

    // Kopplar kassan till butikens lager, så att lagersaldot minskar när något köps.
    public void attachInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    // Kopplar ett medlemskap/kund till det aktuella köpet
    public void attachCustomer(Customer customer) {
        this.currentCustomer = customer;
    }

    // 1. Scanna vara - Använder Mohammads "getPriceWithDeposit"
    public void scanItem(LocalDate birthDate, Item... items) {
        if (this.activeStaff == null) {
            throw new IllegalStateException("No staff logged in at checkout");
        }

        long newTotal = currentTotal.getValue();

        for (Item item : items) {

            if (item.getAgeLimit() > ProductAgeLimits.NO_AGE_LIMIT) {
                if (!ageControl.meetsAge(birthDate, item.getAgeLimit(), LocalDate.now())) {
                    throw new IllegalStateException("Customer does not meet age requirement");
                }
            }

            scannedItems.add(item);
            newTotal += item.getPriceWithDeposit().getValue();
        }

        this.currentTotal = new Money(newTotal, baseCurrency);
    }

    // 2. Applicera rabatt - Använder Antonios Discount
    public void applyDiscount(CustomerRole role, boolean isThursday, boolean isHoliday) {
        this.currentTotal = discountCalculator.applyDiscount(currentTotal, role, isThursday, isHoliday);
    }

    // 3. Betala - Använder Change, Customer och Staff
    public Money pay(Money payment) {
        if (this.activeStaff == null) {
            throw new IllegalStateException("Cannot process payment without active staff");
        }

        Money change = changeCalculator.calculateChange(currentTotal, payment);

        // Minska lagersaldot med en för varje köpt vara (om kassan är kopplad till ett lager).
        if (this.inventory != null) {
            for (Item item : scannedItems) {
                inventory.removeStock(item, 1);
            }   
        }

        // Omvandla totalen från ören (int) till kronor (BigDecimal) för att matcha era metoder
        BigDecimal belopp = BigDecimal.valueOf(currentTotal.getValue()).movePointLeft(2);

        // Integrera Lukas kundsystem
        if (this.currentCustomer != null) {
            boolean isThursday =
            LocalDate.now().getDayOfWeek() == DayOfWeek.THURSDAY;

            currentCustomer.registerPurchase(belopp, isThursday, this.baseCurrency);
        }

        // RÄTTELSE 3: Registrera försäljningen på den inloggade personalen!
        activeStaff.registerSale(belopp);

        return change;
    }

    // 4. Löser in poäng från det kopplade medlemskapet
    public void applyBonusPoints(int pointsToRedeem) {
        // Kontrollera att det finns en kund OCH att kunden har ett medlemskap
        if (this.currentCustomer == null || this.currentCustomer.getMembership() == null) {
            throw new IllegalStateException("No active customer with membership attached to checkout");
        }

        // 1. Be Lukas Membership-klass att dra av poängen (kastar fel om saldot är för lågt)
        this.currentCustomer.getMembership().removePoints(pointsToRedeem);

        // 2. Minska totalbeloppet i kassan (Eftersom er uträkning ger att 1 poäng = 1 öre)
        long newTotal = Math.max(0, currentTotal.getValue() - pointsToRedeem);

        this.currentTotal = new Money(newTotal, baseCurrency);
    }

    public Money getTotal() {
        return currentTotal;
    }
}
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class TerminalUiTest {

    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outContent;

    @BeforeEach
    void setUpStreams() {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent, true, StandardCharsets.UTF_8));
    }

    @AfterEach
    void restoreStreams() {
        System.setIn(originalIn);
        System.setOut(originalOut);
    }

    private void provideInput(String data) {
        System.setIn(new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8)));
    }

    @Test
    void testMainMethod() {
        // Täcker main(String[] args) genom att direkt välja 8 (Exit)
        provideInput("8\n");
        TerminalUi.main(new String[]{});
        String output = outContent.toString(StandardCharsets.UTF_8);
        assertTrue(output.contains("1. Scan item"));
    }

    @Test
    void testEmptyCartDisplayAndEmptyPay() {
        // 2 = Visa tom varukorg, 5 = Försök betala med tom varukorg, 8 = Exit
        provideInput("2\n5\n8\n");
        new TerminalUi().start();

        String output = outContent.toString(StandardCharsets.UTF_8);
        assertTrue(output.contains("----------- Cart -----------"));
        assertTrue(output.contains("Error: The cart is empty"));
    }

    @Test
    void testMenuChoiceAndReadIntErrorBranches() {
        // "abc" -> NumberFormatException i readInt
        // "0"   -> under min (< 1) i readMenuChoice
        // "9"   -> över max (> 8) i readMenuChoice
        // "8"   -> Exit
        provideInput("abc\n0\n9\n8\n");
        new TerminalUi().start();

        String output = outContent.toString(StandardCharsets.UTF_8);
        assertTrue(output.contains("Error: Invalid input. Please enter a number."));
        assertTrue(output.contains("Error: Please select an option between 1 and 8."));
    }

    @Test
    void testAgeControlAllBranchesAndDateErrors() {
        String futureDate = LocalDate.now().plusDays(5).toString();
        String underAgeDate = LocalDate.now().minusYears(18).toString();
        String adultDate = LocalDate.now().minusYears(25).toString();

        // 1. Välj Beer (1 -> 3), skriv ogiltigt datum ("bad-date"), skriv framtida datum -> avbryter
        // 2. Välj Beer (1 -> 3), skriv minderårigt datum -> nekas (birthDate sparas)
        // 3. Välj Beer (1 -> 3) igen -> birthDate är nu INTE null, nekas direkt
        // 4. Avbryt köp (6 -> "yes") för att nollställa birthDate
        // 5. Välj Beer (1 -> 3), skriv vuxet datum -> godkänns
        // 6. Välj Beer (1 -> 3) igen -> återanvänder vuxet birthDate (birthDate != null branch)
        // 7. Exit (8)
        provideInput(
                "1\n3\nbad-date\n" + futureDate + "\n" +
                        "1\n3\n" + underAgeDate + "\n" +
                        "1\n3\n" +
                        "6\nyes\n" +
                        "1\n3\n" + adultDate + "\n" +
                        "1\n3\n" +
                        "8\n"
        );
        new TerminalUi().start();

        String output = outContent.toString(StandardCharsets.UTF_8);
        assertTrue(output.contains("Error: Invalid date format. Please use exactly YYYY-MM-DD."));
        assertTrue(output.contains("Error: The date of birth cannot be in the future"));
        assertTrue(output.contains("Error: The customer is too young to buy Beer"));
    }

    @Test
    void testOutOfStockBranch() {
        // Scanna 10 bananer (maxlager), försök scanna en 11:e + 1 Soda (så loopen ser olika varor i korgen)
        StringBuilder sb = new StringBuilder();
        sb.append("1\n2\n"); // 1 Soda i korgen (testar cartItem != item grenen i loopen)
        for (int i = 0; i < 11; i++) {
            sb.append("1\n1\n"); // 11 Bananas
        }
        sb.append("8\n");

        provideInput(sb.toString());
        new TerminalUi().start();

        String output = outContent.toString(StandardCharsets.UTF_8);
        assertTrue(output.contains("Error: Banana is out of stock"));
    }

    @Test
    void testBonusPointsAllBranchesAndDiscountedReceipt() {
        // 1. Försök använda bonuspoäng utan att vara identifierad (val 4) -> Error
        // 2. Scanna Soda för 17 kr / 1700 öre (1 -> 2)
        // 3. Identifiera medlem (val 3) -> Anna har 2000 poäng
        // 4. Använd bonuspoäng (val 4): mata in "0" (<= 0) -> Error
        // 5. Använd bonuspoäng (val 4): mata in "2500" (> available) -> Error
        // 6. Använd bonuspoäng (val 4): mata in "1700" (>= total 1700) -> Error
        // 7. Använd bonuspoäng (val 4): mata in "500" (giltigt!) -> sänker totalen till 1200 öre (12 kr)
        // 8. Betala (val 5):
        //    - visar korgen med "Discount:  5.0 kr"
        //    - mata in "fel" (ogiltig double)
        //    - mata in "5" (för lite betalt, < 12 kr)
        //    - mata in "20" (giltig betalning -> skriver ut kvitto med "Discount: 5.0 kr")
        // 9. Exit (val 8)
        provideInput(
                "4\n" +
                        "1\n2\n" +
                        "3\n" +
                        "4\n0\n" +
                        "4\n2500\n" +
                        "4\n1700\n" +
                        "4\n500\n" +
                        "5\nfel\n5\n20\n" +
                        "8\n"
        );
        new TerminalUi().start();

        String output = outContent.toString(StandardCharsets.UTF_8);
        assertTrue(output.contains("Error: Identify as a member first (option 3)"));
        assertTrue(output.contains("Error: You can use between 1 and 2000 points"));
        assertTrue(output.contains("Error: The points must be less than the total"));
        assertTrue(output.contains("Discount:  5.0 kr"));
        assertTrue(output.contains("Error: Invalid input. Please enter a decimal number."));
        assertTrue(output.contains("Error: Not enough. The total is 12.0 kr"));
        assertTrue(output.contains("Discount: 5.0 kr"));
        assertTrue(output.contains("Change: 8.0 kr"));
    }

    @Test
    void testCancelPurchaseAllConfirmBranches() {
        // Testar readConfirm med:
        // - ogiltig inmatning ("maybe")
        // - "n" (avbryter inte)
        // - "no" (avbryter inte)
        // - "y" (avbryter)
        provideInput("6\nmaybe\nn\n6\nno\n6\ny\n8\n");
        new TerminalUi().start();

        String output = outContent.toString(StandardCharsets.UTF_8);
        assertTrue(output.contains("Error: Please enter 'y' for yes or 'n' for no."));
        assertTrue(output.contains("The purchase was cancelled."));
    }

    @Test
    void testManageInventoryBranches() {
        // 1. Val 7 -> välj Banana (1) -> lägg till 0 (ogiltigt) -> Error
        // 2. Val 7 -> välj Banana (1) -> lägg till 5 (giltigt, nu 15 i lager)
        // 3. Exit (8)
        provideInput("7\n1\n0\n7\n1\n5\n8\n");
        new TerminalUi().start();

        String output = outContent.toString(StandardCharsets.UTF_8);
        assertTrue(output.contains("Error: The quantity must be positive"));
    }

    @Test
    void testPrivateReadLongAndForeignCurrencyReceiptViaReflection() throws Exception {
        // 1. Täcker den privata metoden readLong() (både felinmatning och giltigt tal)
        provideInput("not-a-long\n123456789\n");
        TerminalUi ui = new TerminalUi();

        Method readLong = TerminalUi.class.getDeclaredMethod("readLong", String.class);
        readLong.setAccessible(true);
        long result = (long) readLong.invoke(ui, "Enter long: ");
        assertEquals(123456789L, result);

        // 2. Täcker grenen (currency.getCode() != CurrencyCode.SEK) och (due == gross.getValue()) i printReceipt
        Currency eur = new Currency(CurrencyCode.EUR, "Euro", "€", 11.5);
        Receipt foreignReceipt = new Receipt(eur);
        foreignReceipt.addItem(new Item("Banana", new Money(1000, eur)));

        Method printReceipt = TerminalUi.class.getDeclaredMethod("printReceipt", Receipt.class, Money.class, Money.class);
        printReceipt.setAccessible(true);
        printReceipt.invoke(ui, foreignReceipt, new Money(1000, eur), new Money(0, eur));

        String output = outContent.toString(StandardCharsets.UTF_8);
        assertTrue(output.contains("Comparable to:"));
    }
}
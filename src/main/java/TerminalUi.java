import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.DayOfWeek;
import java.util.Scanner;


public class TerminalUi {
    private List<Item> cartItems = new ArrayList<>();
    private static final int VAT_PERCENT = 25; //kommer inte ihåg regel för VAT, ändra eller säg till om någon av er kommer ihåg

    //== View renderer ==//
    
    private void showMainMenu() {
        System.out.println("1. Scan item");
        System.out.println("2. View my cart");
        System.out.println("3. Identify as a member");
        System.out.println("4. Use bonus points");
        System.out.println("5. Pay");
        System.out.println("6. Cancel purchase"); // lite osäker om vi ska ha med denna
        System.out.println("7. Staff: manage inventory"); // I fall personen som har UI controller behöver detta val så finns det, om det inte behövs då tar vi bort den
        System.out.println("8. Exit");
    }

    private void showError(String message) {
        System.out.println("Error: " + message);
    }

    private void displayCart(Checkout checkout, List<Item> items) {
        Money total = checkout.getTotal();
        String symbol = total.getCurrency().getSymbol();

        System.out.println();
        System.out.println("--- Cart ---");

        if (items.isEmpty()) {
            System.out.println("The cart is empty");
        }

        long sum = 0;
        for (Item item : items) {
            System.out.println(item.getName() + " " + item.getPriceWithDeposit().formattedValue() + " " + symbol);
            sum += item.getPriceWithDeposit().getValue();
        }

        System.out.println();
        long discount = sum - total.getValue();
        if (discount > 0) {
            System.out.println("Discount:  " + discount / 100.0 + " " + symbol);
        }
        System.out.println("Total: " + total.formattedValue() + " " + symbol);
    }

    //printar kvitto med VAT
    private void printReceipt(Receipt receipt, Money paid, Money change) {
        Money gross = receipt.getTotal();
        Currency currency = gross.getCurrency();
        String symbol = currency.getSymbol();

        long due = paid.getValue() - change.getValue();
        long vat = due * VAT_PERCENT / (100 + VAT_PERCENT);

        System.out.println();
        System.out.println("----------- Receipt -----------");
        System.out.println(receipt.getDateTime());

        for (Item item : receipt.getItemsSortedByName()) {
            System.out.println(item.getName() + "  " + item.getPriceWithDeposit().formattedValue() + " " + symbol);
        }

        //fortsätter imorgon med att skriva ut hela kvittot
    }


}

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.DayOfWeek;
import java.util.Scanner;


public class TerminalUi {
    private List<Item> cartItems = new ArrayList<>();
    private static final int VAT_PERCENT = 25; //kommer inte ihåg regel för VAT, ändra eller säg till om någon av er kommer ihåg

    private Scanner scanner = new Scanner(System.in);

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
        System.out.println("----------- Cart -----------");

        if (items.isEmpty()) {
            System.out.println("The cart is empty");
        }

        long sum = 0;
        for (Item item : items) {
            System.out.println(item.getName() + " " + item.getPriceWithDeposit().formattedValue() + " " + symbol);
            sum += item.getPriceWithDeposit().getValue();
        }

        System.out.println();
        long discount = sum - checkout.getTotal().getValue();
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

        System.out.println();

        System.out.println("Subtotal: " + gross.formattedValue() + " " + symbol);
        if (due < gross.getValue()) {
            System.out.println("Discount: " + (gross.getValue() - due) / 100.0 + " " + symbol);
        } 

        System.out.println("Total: " + due / 100.0 + " " + symbol);
        System.out.println("Incl. VAT (" + VAT_PERCENT + "%):  " + vat / 100.0 + " " + symbol);

        if (currency.getCode() != CurrencyCode.SEK) {
            System.out.println("Comparable to: " + Math.round(due * currency.getExchangeRate()) / 100.0 + " SEK");
        }
        System.out.println("Thank you for your visit");
    }

    //== Input handler==/

    private int readInt(String prompt){
        while (true){
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch(NumberFormatException e){
                showError("Invalid input. Please enter a number.");
            }
        }
    }

    private long readLong(String prompt){
        while (true){
            System.out.print(prompt);
            try {
                return Long.parseLong(scanner.nextLine().trim());
            } catch(NumberFormatException e){
                showError("Invalid input. Please enter a number.");
            }
        }
    }

    private double readDouble(String prompt){
        while (true){
            System.out.print(prompt);
            try {
                String input = scanner.nextLine().trim();
                return Double.parseDouble(input);
            } catch(NumberFormatException e){
                showError("Invalid input. Please enter a decimal number.");
            }
        }
    }

    private LocalDate readDate(String prompt){
        while (true){
            String input = readString(prompt + " (YYYY-MM-DD): ");
            try{
                return LocalDate.parse(input);
            }catch (Exception e){
                showError("Invalid date format. Please use exactly YYYY-MM-DD.");
            }
        }
    }

    private int readMenuChoice(String prompt, int min, int max){
        while (true){
            int choice = readInt(prompt);
            if (choice >= min && choice <= max){
                return choice;
            }
            showError("Please select an option between " + min + " and " + max + ".");
        }
    }

    private String readString(String prompt){
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private boolean readConfirm(String prompt){
        while (true) {
            String input = readString(prompt + " (y/n): ").toLowerCase();
            if (input.equals("y") || input.equals("yes")){
                return true;
            }
            if (input.equals("n") || input.equals("no")){
                return false;
            }
            showError("Please enter 'y' for yes or 'n' for no.");
        }
    }

}

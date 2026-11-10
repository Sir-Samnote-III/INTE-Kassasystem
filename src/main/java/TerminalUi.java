import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.DayOfWeek;
import java.util.Scanner;


public class TerminalUi {
    private List<Item> cartItems = new ArrayList<>();
    private static final int VAT_PERCENT = 25; //kommer inte ihåg regel för VAT, ändra eller säg till om någon av er kommer ihåg

    private Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);

    //== UI Controller ==//
    private final Currency sek = new Currency(CurrencyCode.SEK, "Svensk krona", "kr", 1.0);
    private final Inventory inventory = new Inventory();
    private final List<Item> catalog;
    private final Customer member;
    private Checkout checkout;
    private LocalDate birthDate;
    private boolean isMember;

    public TerminalUi(){
        catalog = List.of(
            new Item("Banana", new Money(1000, sek)),
            new Item("Soda", new Money(1500, sek), new Money(200, sek)),
            new Item("Beer", new Money(2500, sek), new Money(200, sek), ProductAgeLimits.ALCOHOL));
        member = new Customer("Anna Andersson", "C01", "Storgatan 1", "19900101-1234", "0701234567", "anna@example.com", new Membership(LocalDate.now(), LocalDate.now().plusYears(1)),
        CustomerRole.STANDARD, LocalDate.of(1990, 1, 1));

        for (Item item : catalog){
            inventory.addStock(item, 10);
        }
        member.getMembership().registerPoints(new Money(100000, sek), CustomerRole.STANDARD, false);
        startNewPurchase();
    }

    public static void main(String[] args){
        new TerminalUi().start();
    }

    public void start(){
        boolean running = true;
        while (running){
            showMainMenu();
            switch (readMenuChoice("Choose an option: ", 1, 8)){
                case 1 -> scanItem();
                case 2 -> displayCart(checkout, cartItems);
                case 3 -> identifyMember();
                case 4 -> useBonusPoints();
                case 5 -> processTransaction();
                case 6 -> cancelPurchase();
                case 7 -> manageInventory();
                default -> running = false;
            }
        }
    }

    private void startNewPurchase(){
        checkout = new Checkout(sek, new Staff("S01", "Cashier"));
        checkout.attachInventory(inventory);
        cartItems.clear();
        birthDate = null;
        isMember = false;
    }

    private Item chooseProduct(){
        for (int i = 0; i < catalog.size(); i++){
            Item item = catalog.get(i);
            System.out.println((i + 1) + ". " + item.getName() + " " + item.getPriceWithDeposit().formattedValue() + " kr (in stock: " + inventory.getStock(item) + ")");
        }
        return catalog.get(readMenuChoice("Choose product: ", 1, catalog.size()) - 1);
    }

    private void scanItem(){
        Item item = chooseProduct();
        int inCart = 0;
        for (Item cartItem : cartItems){
            if (cartItem == item){
                inCart++;
            }
        }
        if (inCart >= inventory.getStock(item)){
            showError(item.getName() + " is out of stock");
            return;
        }
        if (item.getAgeLimit() > ProductAgeLimits.NO_AGE_LIMIT){
            if (birthDate == null){
                birthDate = readDate("Customer's date of birth");
            }
            if (birthDate.isAfter(LocalDate.now())){
                showError("The date of birth cannot be in the future");
                birthDate = null;
                return;
            }
            if (!new AgeControl().meetsAge(birthDate, item.getAgeLimit(), LocalDate.now())){
                showError("The customer is too young to buy " + item.getName());
                return;
            }
        }
        checkout.scanItem(birthDate, item);
        cartItems.add(item);
        
    }

    private void identifyMember(){
        checkout.attachCustomer(member);
        isMember = true;
        System.out.println("Welcome " + member.getCustomerName() + "! Points: " + member.getMembership().getPoints());
    }

    private void useBonusPoints(){
        if (!isMember){
            showError("Identify as a member first (option 3)");
            return;
        }
        int available = member.getMembership().getPoints();
        int points = readInt("Points to use (you have " + available + "): ");
        if (points <= 0 || points > available){
            showError("You can use between 1 and " + available + " points");
            return;
        }
        if (points >= checkout.getTotal().getValue()){
            showError("The points must be less than the total");
            return; 
        }
        checkout.applyBonusPoints(points);
    }

    private void processTransaction(){
        if (cartItems.isEmpty()) {
            showError("The cart is empty");
            return;
        }
        boolean isThursday = LocalDate.now().getDayOfWeek() == DayOfWeek.THURSDAY;
        checkout.applyDiscount(CustomerRole.STANDARD, isThursday, false);
        displayCart(checkout, cartItems);

        Money payment = new Money(Math.round(readDouble("Amount paid (kr): ") * 100), sek);
        while (payment.getValue() < checkout.getTotal().getValue()){
            showError("Not enough. The total is " + checkout.getTotal().formattedValue() + " kr");
            payment = new Money(Math.round(readDouble("Amount paid (kr): ") * 100), sek);
        }
        Money change = checkout.pay(payment);
        Receipt receipt = new Receipt(sek);
        for (Item item : cartItems){
            receipt.addItem(item);
        }
        printReceipt(receipt, payment, change);
        System.out.println("Change: " + change.formattedValue() + " kr");
        startNewPurchase();
    }

    private void cancelPurchase(){
        if (readConfirm("Cancel the purchase?")){
            startNewPurchase();
            System.out.println("The purchase was cancelled.");
        }
    }

    private void manageInventory(){
        Item item = chooseProduct();
        int quantity = readInt("Quantity to add: ");
        if (quantity <= 0) {
            showError("The quantity must be positive");
            return;
        }
        inventory.addStock(item, quantity);
    }

    //== View renderer ==//
    
    private void showMainMenu() {
        System.out.println();
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

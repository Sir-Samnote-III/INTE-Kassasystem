public class Customer {
    
    private String customerNamn;
    private String customerId;

    public Customer(String customerNamn, String customerId) {
        this.customerNamn = customerNamn;
        this.customerId = customerId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getCustomerNamn() {
        return customerNamn;
    }
}

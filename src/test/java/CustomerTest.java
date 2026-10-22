import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerTest {
 
    @Test
    void kundMedNamnOchId() {
        Customer customer = new Customer("Bob", "K01");

        assertEquals("K01", customer.getCustomerId());
        assertEquals("Bob", customer.getCustomerNamn());
    }
}

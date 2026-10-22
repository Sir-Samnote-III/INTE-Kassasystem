import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StaffTest {
    
   @Test 
   void skapaPersonal() {
        Staff staff = new Staff("P01", "Billy");

        assertEquals("P01", staff.getStaffId());
        assertEquals("Billy", staff.getNamn());
   }
   
   

}

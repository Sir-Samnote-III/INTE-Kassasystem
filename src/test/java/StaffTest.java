import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

public class StaffTest {
    
   @Test 
   void skapaPersonal() {
        Staff staff = new Staff("P01", "Billy");

        assertEquals("P01", staff.getStaffId());
        assertEquals("Billy", staff.getNamn());
   }
   
   @Test 
   void staffHasNoSales() {
      Staff staff = new Staff("S01", "Sara");

      assertEquals(0, staff.getAmountSale());
      assertEquals(0, BigDecimal.ZERO.compareTo(staff.getTotalSale()));
   }

   @Test 
   void registerASale() {
      Staff staff = new Staff("S01", "Anna");

      staff.registerSale(new BigDecimal("100.00"));

      assertEquals(1, staff.getAmountSale());

      assertEquals(0, new BigDecimal("100.00").compareTo(staff.getTotalSale()));
   }

   @Test
   void registerMoreSales() {
      Staff staff = new Staff("S01", "Bob");

      staff.registerSale(new BigDecimal("100.00"));
      staff.registerSale(new BigDecimal("50.00"));

      assertEquals(2, staff.getAmountSale());
      assertEquals(0, new BigDecimal("150.00").compareTo(staff.getTotalSale()));
   }

   @Test
   void moreSalesGivesBonus() {
      Staff staff = new Staff("S01", "Filip");

      staff.registerSale(new BigDecimal("30000.00"));
      staff.registerSale(new BigDecimal("20000.00"));

      assertEquals(0, new BigDecimal("50000.00").compareTo(staff.getTotalSale()));
      assertEquals(0, new BigDecimal("500.00").compareTo(staff.calculateBonus()));
   }

   @Test 
   void bonusCalculatedFromCurrentTotalSales() {
      Staff staff = new Staff("S01", "Bob");

      staff.registerSale(new BigDecimal("49000.00"));
      assertTrue(staff.calculateBonus().compareTo(BigDecimal.ZERO) == 0);

      staff.registerSale(new BigDecimal("1000.00"));
      assertTrue(staff.calculateBonus().compareTo(new BigDecimal("500.00")) == 0);

      //En annan försäljning efter gräns ska inte ge ett större fast bonus
      staff.registerSale(new BigDecimal("10000.00"));
      assertTrue(staff.calculateBonus().compareTo(new BigDecimal("500.00")) == 0);
   }
}

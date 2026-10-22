import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class MembershipTest {
    
    @Test 
    void giltigtMedlemskap() {
        Membership membership = new Membership(
            LocalDate.of(2026, 3, 9),
            LocalDate.of(2026, 12, 31)
        );

        assertTrue(membership.isValid(LocalDate.of(2026, 6, 15)));
    }

    @Test 
    void medlemskapOgiltigForeStart() {
        Membership membership = new Membership(LocalDate.of(2026, 1, 1), LocalDate.of(2026, 12, 31));

        assertFalse(membership.isValid(LocalDate.of(2025, 12, 31)));
    }

    @Test 
    void medlemskapOgiltigEfterSlut() {
        Membership membership = new Membership(LocalDate.of(2026, 2, 9), LocalDate.of(2026, 12, 31));

        assertFalse(membership.isValid(LocalDate.of(2027, 1, 1)));
    }
}

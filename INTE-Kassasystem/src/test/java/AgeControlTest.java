import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class AgeControlTest {
    
    @Test 
    void uppnatAldersGrans() {
        AgeControl control = new AgeControl();

        LocalDate fodelseDatum = LocalDate.of(2006, 10, 20);
        LocalDate dagensDatum = LocalDate.of(2026, 10, 20);

        assertTrue(control.meetsAge(
            fodelseDatum, 20, dagensDatum
        ));
    }

    @Test 
    void inteUppnatAldersGrans() {
        AgeControl control = new AgeControl();

        LocalDate fodelseDatum = LocalDate.of(2007, 10, 22);
        LocalDate dagensDatum = LocalDate.of(2026, 10, 20);

        assertFalse(control.meetsAge(
            fodelseDatum, 20, dagensDatum
        ));
    }
}

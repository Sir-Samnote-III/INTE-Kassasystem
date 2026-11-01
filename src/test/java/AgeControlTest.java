import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class AgeControlTest {
    
    @Test 
    void uppnatAldersGrans() {
        AgeControl control = new AgeControl();

        LocalDate fodelseDatum = LocalDate.of(2006, 10, 20);
        LocalDate dagensDatum = LocalDate.of(2026, 10, 20);

        assertTrue(control.meetsAge(fodelseDatum, 20, dagensDatum));
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

    @Test
    void customerWhoTurns20TomorrowDoesNotMeetAgeRequirement() {    
        LocalDate today = LocalDate.of(2026, 11, 1);
        LocalDate birthDate = today.minusYears(20).plusDays(1);

        AgeControl ageControl = new AgeControl();

        assertFalse(ageControl.meetsAge(birthDate, 20, today));
    }

    @Test
    void customerWhoTurns20TodayMeetsAgeRequirement() {
        LocalDate today = LocalDate.of(2026, 11, 1);
        LocalDate birthDate = today.minusYears(20);

        AgeControl ageControl = new AgeControl();

        assertTrue(ageControl.meetsAge(birthDate, 20, today));
    }
}

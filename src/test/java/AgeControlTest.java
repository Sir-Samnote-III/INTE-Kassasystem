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
        int ageLimit = 20;
        LocalDate today = LocalDate.of(2026, 11, 1);
        LocalDate birthDate = today.minusYears(ageLimit).plusDays(1);

        AgeControl ageControl = new AgeControl();

        assertFalse(ageControl.meetsAge(birthDate, ageLimit, today));
    }

    @Test
    void nullBirthDateThrowsException() {
        AgeControl control = new AgeControl();

        assertThrows(IllegalArgumentException.class, () ->
            control.meetsAge(null, 20, LocalDate.of(2026, 11, 1))
        );
    }

    @Test
    void nullTodayDateThrowsException() {
        AgeControl control = new AgeControl();

        assertThrows(IllegalArgumentException.class, () ->
            control.meetsAge(LocalDate.of(2000, 1, 1), 20, null)
        );
    }

    @Test
    void negativeAgeLimitThrowsException() {
        AgeControl control = new AgeControl();

        assertThrows(IllegalArgumentException.class, () ->
            control.meetsAge(LocalDate.of(2000, 1, 1), -1, LocalDate.of(2026, 11, 1)));
    }

    @Test
    void futureBirthDateThrowsException() {
        AgeControl control = new AgeControl();

        assertThrows(IllegalArgumentException.class, () ->
            control.meetsAge(LocalDate.of(2026, 11, 2), 20, LocalDate.of(2026, 11, 1)));
    }

    @Test
    void zeroAgeLimitIsAlwaysMetForValidBirthDate() {
        AgeControl control = new AgeControl();

        assertTrue(control.meetsAge(LocalDate.of(2026, 1, 1), 0, LocalDate.of(2026, 11, 1)));
    }

    @Test
    void customerWhoTurns20TodayMeetsAgeRequirement() {
        LocalDate today = LocalDate.of(2026, 11, 1);
        LocalDate birthDate = today.minusYears(20);

        AgeControl ageControl = new AgeControl();

        assertTrue(ageControl.meetsAge(birthDate, 20, today));
    }
}

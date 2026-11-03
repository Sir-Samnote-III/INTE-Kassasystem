import java.time.LocalDate;

public class AgeControl {
    
    public boolean meetsAge(LocalDate birthDate, int ageLimit, LocalDate currentDate) {

        if (birthDate == null || currentDate == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }

        if (ageLimit < 0) {
            throw new IllegalArgumentException("Age limit cannot be negative");
        }

        if (birthDate.isAfter(currentDate)) {
            throw new IllegalArgumentException("Birthdate can't be in the future");
        }

        LocalDate ageLimitDate = birthDate.plusYears(ageLimit);

        return !currentDate.isBefore(ageLimitDate);
    }
}
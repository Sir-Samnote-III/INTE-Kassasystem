import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Membership {

    private LocalDate startDate;
    private LocalDate endDate;
    private int totalPoints = 0;

    private static final int BASE_POINTS = 50;

    public Membership(LocalDate startDate, LocalDate endDate) {

        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start and end date cannot be null");
        }

        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date cannot be before start date");
        }

        this.startDate = startDate;
        this.endDate = endDate;
    }

    public boolean isValid(LocalDate date) {

        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }

        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }

    public boolean isValidToday() {
        return isValid(LocalDate.now());
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public int getDaysRemaining(LocalDate date) {

        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }

        if (date.isAfter(endDate)) {
            return 0;
        }

        return (int) ChronoUnit.DAYS.between(date, endDate);
    }

    public void extend(int days) {

        if (days <= 0) {
            throw new IllegalArgumentException("Number of days needs to be positive");
        }

        endDate = endDate.plusDays(days);
    }

    public void registerPoints(Money purchaseAmount, CustomerRole role, boolean isThursday) {

        if (purchaseAmount == null || purchaseAmount.getValue() <= 0) {
            throw new IllegalArgumentException("Purchase amount needs to be positive!");
        }

        if (role == null) {
            throw new IllegalArgumentException("Customer role cannot be null");
        }

        int percentage = 2;

        if (role == CustomerRole.PENSIONER) {
            percentage = 3;
        }

        if (role == CustomerRole.STUDENT && isThursday) {
            percentage = 3;
        }

        int gainedPoints = (int) (purchaseAmount.getValue() * percentage / 100);

        totalPoints += gainedPoints;
    }

    public void removePoints(int pointsToRemove) {

        if (pointsToRemove <= 0) {
            throw new IllegalArgumentException("Points to remove must be positive");
        }

        if (pointsToRemove > totalPoints) {
            throw new IllegalStateException("Not enough points on membership");
        }

        totalPoints -= pointsToRemove;
    }

    public int getPoints() {
        return totalPoints;
    }


    public Money getBonus(Currency currency) {
        long bonusAmount = (totalPoints / BASE_POINTS) * BASE_POINTS;
        return new Money(bonusAmount, currency);
    }
}
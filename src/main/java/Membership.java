import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Membership {
    
    private LocalDate startDatum;
    private LocalDate slutDatum;
    private int totalPoints = 0;

    public Membership(LocalDate startDatum, LocalDate slutDatum) {
        
        if (startDatum == null || slutDatum == null) {
            throw new IllegalArgumentException("Start and end date cannot be null");
        }

        if (slutDatum.isBefore(startDatum)) {
            throw new IllegalArgumentException("End date cannot be before start date");
        }
        
        this.startDatum = startDatum;
        this.slutDatum = slutDatum;
    }

    public boolean isValid(LocalDate datum) {

        if (datum == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }

        return !datum.isBefore(startDatum) && !datum.isAfter(slutDatum);
    }

    public boolean isValidToday(LocalDate datum) {
        return isValid(LocalDate.now());
    }

    public LocalDate getStartDatum() {
        return startDatum;
    }

    public LocalDate getSlutDatum() {
        return slutDatum;
    }

    public long getDaysRemaining(LocalDate datum) {
        if (datum == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }

        if (datum.isAfter(slutDatum)) {
            return 0;
        }

        return ChronoUnit.DAYS.between(datum, slutDatum); 
    }

    public void extend(int dagar) {
        if (dagar <= 0) {
            throw new IllegalArgumentException("Number of days needs to be positive");
        }

        slutDatum = slutDatum.plusDays(dagar);
    }

    public void registerPoints(Money pruchaseAmount, CustomerRole role, boolean isThursday) {
        if (pruchaseAmount == null || pruchaseAmount.getValue() <= 0) {
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

        int gainedPoints = (int) ((long) pruchaseAmount.getValue() * percentage / 100);
        totalPoints += gainedPoints;
    }

    public void redeemPoints(int pointsToRedeem) {
        if (pointsToRedeem < 0) {
            throw new IllegalArgumentException("Cannot redeem negative points");
        }
        if (pointsToRedeem > this.totalPoints) {
            throw new IllegalStateException("Not enough points on membership");
        }

        this.totalPoints -= pointsToRedeem;
    }

    public int getPoints() {
        return totalPoints;
    }

    public Money getBonus() {
        return new Money(totalPoints, "SEK");
    }

}

import java.time.LocalDate;

public class Membership {
    
    private LocalDate startDatum;
    private LocalDate slutDatum;

    public Membership(LocalDate startDatum, LocalDate slutDatum) {
        this.startDatum = startDatum;
        this.slutDatum = slutDatum;
    }

    public boolean isValid(LocalDate datum) {
        return !datum.isBefore(startDatum) && !datum.isAfter(slutDatum);
    }
}

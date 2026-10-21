import java.time.LocalDate;

public class AgeControl {
    
    public boolean meetsAge(LocalDate fodelseDatum, int aldersGrans, LocalDate dagensDatum) {

        if (fodelseDatum == null || dagensDatum == null) {
            throw new IllegalArgumentException("Datum får inte vara null");
        }

        if (aldersGrans < 0) {
            throw new IllegalArgumentException("Åldersgränsen kan inte vara negativ");
        }

        LocalDate datumForeAldersGrans = fodelseDatum.plusYears(aldersGrans);

        return !dagensDatum.isBefore(datumForeAldersGrans);
    }
}

import java.math.BigDecimal;

public class Staff {
    
    private final String staffId;
    private final String name;
    private int amountSale = 0;
    private BigDecimal totalSale = BigDecimal.ZERO;
    private BigDecimal shiftSale = BigDecimal.ZERO;
    private int shiftAmountSale = 0;

    //regler för bonus kan diskuteras, lägger en för 500kr om personalen säljer för minst 50 000kr i månaden
    private final BigDecimal bonusThreshold = new BigDecimal("50000.00");
    private final BigDecimal bonusSale = new BigDecimal("500.00");

    public Staff(String staffId, String name) {
        this.staffId = staffId;
        this.name = name;
    }

    public String getStaffId() {
        return staffId;
    }

    public String getName() {
        return name;
    }

    public void registerSale(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Sales amount needs to be greater than 0");
        }

        totalSale = totalSale.add(amount);
        amountSale++;

        shiftSale = shiftSale.add(amount);
        shiftAmountSale++;
    }

    public void resetShift() {
        shiftSale = BigDecimal.ZERO;
        shiftAmountSale = 0;
    }

    public BigDecimal getShiftSale() {
        return shiftSale;
    }

    public int getShiftAmountSale() {
        return shiftAmountSale;
    }

    public BigDecimal calculateBonus() {
        if (totalSale.compareTo(bonusThreshold) >= 0) {
            return bonusSale;
        }

        return BigDecimal.ZERO;
    }

    public BigDecimal getTotalSale() {
        return totalSale;
    }

    public int getAmountSale() {
        return amountSale;
    }
}
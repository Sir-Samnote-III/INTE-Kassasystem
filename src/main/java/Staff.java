import java.math.BigDecimal;

public class Staff {
    
    private String staffId;
    private String namn;
    private int amountSale = 0;
    private BigDecimal totalSale = BigDecimal.ZERO;

    //regler för bonus kan diskuteras, lägger en för 500kr om personalen säljer för minst 50 000kr i månaden
    private BigDecimal bonusGrans = new BigDecimal("50000.00");
    private BigDecimal bonusSale = new BigDecimal("500.00");

    public Staff(String staffId, String namn) {
        this.staffId = staffId;
        this.namn = namn;
    }

    public String getStaffId() {
        return staffId;
    }

    public String getNamn() {
        return namn;
    }

    public void registerSale(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Sales amount needs to be greater than 0");
        }

        totalSale = totalSale.add(amount);
        amountSale++;
    }

    public BigDecimal calculateBonus() {
        if (totalSale.compareTo(bonusGrans) >= 0) {
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

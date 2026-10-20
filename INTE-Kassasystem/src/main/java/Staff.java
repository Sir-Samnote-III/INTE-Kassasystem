public class Staff {
    
    private String staffId;
    private String namn;

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
}

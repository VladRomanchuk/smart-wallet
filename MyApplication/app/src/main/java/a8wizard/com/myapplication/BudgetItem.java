package a8wizard.com.myapplication;

public class BudgetItem {
    private int idBudget;
    private String startDate;
    private String endDate;
    private String category;
    private String amount;
    private String left;
    private long timeStartDate;
    private long timeEndDate;

    public BudgetItem(int idBudget, String startDate, String endDate,
                      String category, String amount, String left,
                      long timeStartDate, long timeEndDate) {
        super();
        this.idBudget = idBudget;
        this.startDate = startDate;
        this.endDate = endDate;
        this.category = category;
        this.amount = amount;
        this.left = left;
        this.timeStartDate = timeStartDate;
        this.timeEndDate = timeEndDate;
    }

    public long getTimeStartDate() {
        return timeStartDate;
    }

    public void setTimeStartDate(long timeStartDate) {
        this.timeStartDate = timeStartDate;
    }

    public long getTimeEndDate() {
        return timeEndDate;
    }

    public void setTimeEndDate(long timeEndDate) {
        this.timeEndDate = timeEndDate;
    }

    public int getIdBudget() {
        return idBudget;
    }

    public void setIdBudget(int idBudget) {
        this.idBudget = idBudget;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getLeft() {
        return left;
    }

    public void setLeft(String left) {
        this.left = left;
    }

}
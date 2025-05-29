package models;

public class activitylog{
    private int logId;
    private int userId;
    private String logDate;
    private String logAction;

    // Constructor
    public activitylog(int logId, int userId, String logDate, String logAction) {
        this.logId = logId;
        this.userId = userId;
        this.logDate = logDate;
        this.logAction = logAction;
    }

    // Getters
    public int getLogId() {
        return logId;
    }

    public int getUserId() {
        return userId;
    }

    public String getLogDate() {
        return logDate;
    }

    public String getLogAction() {
        return logAction;
    }

    // Setters
    public void setLogId(int logId) {
        this.logId = logId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setLogDate(String logDate) {
        this.logDate = logDate;
    }

    public void setLogAction(String logAction) {
        this.logAction = logAction;
    }
}
package models;

public class staff {
    private int uid;
    private String fname;
    private String lname;
    private String phonenum;
    private String email;
    private String status; // Should match the values in the ComboBox
    private String type; // Should match the values in the ComboBox

    // Constructor with user type
    public staff(int uid, String fname, String lname, String phonenum,
        String email, String type, String status) {
        this.uid = uid;
        this.fname = fname;
        this.lname = lname;
        this.phonenum = phonenum;
        this.email = email;
        this.type = type;
        this.status = status;
    }

    // Constructor for staffForm without user type
    public staff(int uid, String fname, String lname, String phonenum,
        String email, String status) {
        this.uid = uid;
        this.fname = fname;
        this.lname = lname;
        this.phonenum = phonenum;
        this.email = email;
        this.status = status;
        this.type = null; // or set a default value if needed
    }

    // Getters
    public int getUid() {
        return uid;
    }

    public String getFname() {
        return fname;
    }

    public String getLname() {
        return lname;
    }

    public String getEmail() {
        return email;
    }

    public String getPhonenum() {
        return phonenum;
    }

    public String getStatus() {
        return status;
    }

    public String getType() {
        return type;
    }

    // Setters
    public void setUid(int uid) {
        this.uid = uid;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhonenum(String phonenum) {
        this.phonenum = phonenum;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setType(String type) {
        this.type = type;
    }
}
package config;

public class Session {
    private static Session instance;
    private int uid;
    private String fname;
    private String lname;
    private String phone_num;
    private String email;
    private String type;
    private String status;

    private Session() {
        // Private cons. that prevents us to instantiate another instance
    }

    public static synchronized Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    public static boolean isInstanceEmpty() {
        return instance == null;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getPhone_num() {
        return phone_num;
    }

    public void setPhone_num(String phone_num) {
        this.phone_num = phone_num;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Method to reset session data
    public void reset() {
        this.uid = 0;
        this.fname = null;
        this.lname = null;
        this.email = null;
        this.type = null;
        this.status = null;
        this.phone_num = null;
    }
}
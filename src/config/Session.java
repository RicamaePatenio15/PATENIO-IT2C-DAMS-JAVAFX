
package config;
public class Session{
    
    private static Session instance;
    private int uid;
    private String fname;
    private String lname;
    private String email;
    private String type;
    private String status;
    
    private Session(){
       
        
    }

    public static synchronized Session getInstance() {
        if(instance==null){
            instance=new Session();
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

    
    
    
}
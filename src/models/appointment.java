package models;


public class appointment {
   private int appointmentID;
    private int patientID;
    private String patientName;
    private int serviceID;
    private String serviceName;
    private String appointmentSchedule;
    private String status;
    private String notes;
    private String gender;
    private String contactNumber;
    private String schedule;
    private String serviceAvailability;
    private double price;
   
    
    
    public appointment(int appointmentID, String firstName, String gender, String contactNumber, int serviceID, double price) {
        this.appointmentID = appointmentID;
        this.patientName = firstName;
        this.gender = gender;
        this.contactNumber = contactNumber;
        this.serviceID = serviceID;
        this.price = price;
        }
    // constructor for retrieving the appointment filtering the "pending" status
public appointment(int appointmentID, String firstName, String gender, String contactNumber, double price) {
    this.appointmentID = appointmentID;
    this.patientName = firstName; 
    this.gender = gender;
    this.contactNumber = contactNumber;
    this.price = price;
}

      // Constructor for adding new appointment
public appointment(int patientID, String patientName, String gender,
                          String contactNumber, int serviceID, String serviceName,
                          String appointmentSchedule, String status, String notes) {
        this.patientID = patientID;
        this.patientName = patientName;
        this.gender = gender;
        this.contactNumber = contactNumber;
        this.serviceID = serviceID;
        this.serviceName = serviceName;
        this.appointmentSchedule = appointmentSchedule;
        this.status = status;
        this.notes = notes;
    }
// updating

 public appointment(int appointmentID, int patientID, String serviceName, String appointmentSchedule, String status, String notes) {
        this.appointmentID = appointmentID;
        this.patientID = patientID;
        this.serviceName = serviceName;
        this.appointmentSchedule = appointmentSchedule;
        this.status = status;
        this.notes = notes;
    }
 
// Constructor for updating/retrieving appointment
public appointment(int appointmentID, int patientID, String patientName,
                         String gender, String contactNumber, int serviceID,
                         String serviceName, String appointmentSchedule, String status,
                         String notes) {
        this.appointmentID = appointmentID;
        this.patientID = patientID;
        this.patientName = patientName;
        this.gender = gender;
        this.contactNumber = contactNumber;
        this.serviceID = serviceID;
        this.serviceName = serviceName;
        this.appointmentSchedule = appointmentSchedule;
        this.status = status;
        this.notes = notes;
    }



   // Getters and setters
    public int getAppointmentID() {
        return appointmentID;
    }

    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
    }

    public int getPatientID() {
        return patientID;
    }

    public void setPatientID(int patientID) {
        this.patientID = patientID;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public int getServiceID() {
        return serviceID;
    }

    public void setServiceID(int serviceID) {
        this.serviceID = serviceID;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getAppointmentSchedule() {
        return appointmentSchedule;
    }

    public void setAppointmentSchedule(String appointmentSchedule) {
        this.appointmentSchedule = appointmentSchedule;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getServiceAvailability() {
        return serviceAvailability;
    }

    public void setServiceAvailability(String serviceAvailability) {
        this.serviceAvailability = serviceAvailability;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    
     
}




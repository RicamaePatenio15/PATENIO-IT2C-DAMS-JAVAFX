package models;

public class patientHistory {
    private int appointmentID;
    private String firstName;
    private String lastName;
    private String contactNumber;
    private String serviceName;
    private String status;
   private String appointmentSchedule;

    // Constructor
    public patientHistory(int appointmentID, String firstName, String lastName, String contactNumber, String serviceName, String status, String appointmentSchedule) {
        this.appointmentID = appointmentID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.contactNumber = contactNumber;
        this.serviceName = serviceName;
        this.status = status;
        this.appointmentSchedule = appointmentSchedule;
    }

    // Getters
    public int getAppointmentID() {
        return appointmentID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getStatus() {
        return status;
    }

   public String getAppointmentSchedule() {
    return appointmentSchedule;
}

    // Setters
    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void setStatus(String status) {
        this.status = status;
    }

   public void setAppointmentSchedule(String appointmentSchedule) {
    this.appointmentSchedule = appointmentSchedule;
}
}
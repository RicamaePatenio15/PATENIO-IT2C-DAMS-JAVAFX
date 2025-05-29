package models;

public class patient {
    private int patientID;
    private String firstName;
    private String lastName;
    private String contactNumber;
    private String gender;
    private String date;
    private String time;
    private String address;
    private int appointmentID;
    private String treatment;
    private String status; // Added status field
    private String fullName;

    // Constructor
    public patient(int patientID, String firstName, String lastName,
        String contactNumber, String gender, String date, String time,
        String address) {
        this.patientID = patientID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.contactNumber = contactNumber;
        this.gender = gender;
        this.date = date;
        this.time = time;
        this.address = address;
    }

    // Getters
    public int getPatientID() {
        return patientID;
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

    public String getGender() {
        return gender;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getAddress() {
        return address;
    }

    public int getAppointmentID() {
        return appointmentID;
    }

    public String getTreatment() {
        return treatment;
    }

    public String getStatus() { // Added getStatus method
        return status;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    // Setters
    public void setPatientID(int patientID) {
        this.patientID = patientID;
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

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public void setStatus(String status) { // Added setStatus method
        this.status = status;
    }
}
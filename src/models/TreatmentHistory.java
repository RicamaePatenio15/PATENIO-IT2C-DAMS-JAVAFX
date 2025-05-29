package models;

import java.time.LocalDate;

public class TreatmentHistory {
    private String firstName;
    private String lastName;
    private String serviceName;
    private String date;

    // Constructor
    public TreatmentHistory(String firstName, String lastName, String serviceName, String date) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.serviceName = serviceName;
        this.date = date;
    }

    // Getters
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getDate() {
        return date;
    }

    // Setters
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "TreatmentHistory{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", date=" + date +
                '}';
    }
}
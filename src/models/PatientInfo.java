package models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PatientInfo {
     private SimpleStringProperty firstName = new SimpleStringProperty();
     private SimpleStringProperty lastName = new SimpleStringProperty();
     private SimpleStringProperty gender = new SimpleStringProperty();
     private SimpleStringProperty contactNumber = new SimpleStringProperty();
     private SimpleStringProperty address = new SimpleStringProperty();

     public StringProperty firstNameProperty() {
          return firstName;
     }

     public StringProperty lastNameProperty() {
          return lastName;
     }

     public StringProperty genderProperty() {
          return gender;
     }

     public StringProperty contactNumberProperty() {
          return contactNumber;
     }

     public StringProperty addressProperty() {
          return address;
     }

     public StringProperty fullNameProperty() {
          return new SimpleStringProperty(
              firstName.get() + " " + lastName.get());
     }

     public void setFirstName(String firstName) {
          this.firstName.set(firstName);
     }

     public void setLastName(String lastName) {
          this.lastName.set(lastName);
     }

     public void setGender(String gender) {
          this.gender.set(gender);
     }

     public void setContactNumber(String contactNumber) {
          this.contactNumber.set(contactNumber);
     }

     public void setAddress(String address) {
          this.address.set(address);
     }
}
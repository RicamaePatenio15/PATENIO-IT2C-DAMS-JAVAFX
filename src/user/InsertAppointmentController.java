package user;

import config.Session;
import config.alertMessage;
import config.connectDB;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.appointment;

public class InsertAppointmentController implements Initializable {
    @FXML private Button add_appointmentbtn;

    @FXML private ComboBox<Integer> appointment_PatientID;

    @FXML private Label appointment_name;

    @FXML private TextArea appointment_notes;

    @FXML private DatePicker appointment_schedule;

    @FXML private ComboBox<String> appointment_serviceAvail;

    @FXML private TextField appointment_contactNumber;

    @FXML private Button cancel_addbtn;

    private ObservableList<Integer> patientIDs =
        FXCollections.observableArrayList();
    private ObservableList<String> serviceAvailability =
        FXCollections.observableArrayList();

    private UserDashboardController userdashboardcontroller;

    public void setUserdashboardcontroller(
        UserDashboardController userdashboardcontroller) {
        this.userdashboardcontroller = userdashboardcontroller;
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadPatientIDs();
        loadServiceAvailability();
    }

    private void loadPatientIDs() {
        try {
            appointment_PatientID.getItems().addAll(connectDB.getPatientIDs());
        } catch (SQLException e) {
            System.out.println("Error loading patient IDs: " + e.getMessage());
        }
    }

    private void loadServiceAvailability() {
        try {
            appointment_serviceAvail.getItems().addAll(
                connectDB.getServiceNames());
        } catch (SQLException e) {
            System.out.println(
                "Error loading service availability: " + e.getMessage());
        }
    }

    @FXML
    private void handlePatientIDSelection() {
        try {
            int patientID = appointment_PatientID.getValue();
            String patientName = connectDB.getPatientName(patientID);
            appointment_name.setText(patientName);
        } catch (SQLException e) {
            System.out.println(
                "Error retrieving patient name: " + e.getMessage());
        }
    }

    @FXML
    private void handleInsertAppointmentButtonAction() {
        if (appointment_PatientID.getValue() == null) {
            new alertMessage().errorMessage("Please select a patient");
            return;
        }
        if (appointment_serviceAvail.getValue() == null) {
            new alertMessage().errorMessage("Please select a service");
            return;
        }
        if (appointment_schedule.getValue() == null) {
            new alertMessage().errorMessage("Please select a schedule");
            return;
        }
        
        
    // Validate date
    LocalDate selectedDate = appointment_schedule.getValue();
    LocalDate currentDate = LocalDate.now();
    if (selectedDate.isBefore(currentDate)) {
        new alertMessage().errorMessage("Appointment date cannot be before the current date");
        return;
    }

        try {
            int patientID = appointment_PatientID.getValue();
            String patientName = connectDB.getPatientName(patientID);
            String gender = connectDB.getPatientGender(patientID);
            String contactNumber = appointment_contactNumber != null
                ? appointment_contactNumber.getText()
                : "";
            String serviceName = appointment_serviceAvail.getValue().toString();
            String notes =
                appointment_notes != null ? appointment_notes.getText() : "";

            // Set status to "Pending" by default
            String status = "Pending";

            int serviceID = connectDB.getServiceID(serviceName);

            appointment appointment = new appointment(patientID, patientName,
                gender, contactNumber, serviceID, serviceName,
                appointment_schedule.getValue().toString(), status, notes);

            int appointmentID = connectDB.addAppointment(appointment);
            new alertMessage().successMessage(
                "Appointment added successfully with ID: " + appointmentID);

            connectDB.addTreatmentHistory(patientID, appointmentID);

            userdashboardcontroller.loadAppointments();
            userdashboardcontroller.updateAppointmentCounter();
            
             connectDB.insertLogEntry(Session.getInstance().getUid(),
            "Appointment added successfully for patient " + patientName);
            // Close the current window
            Stage stage = (Stage) appointment_PatientID.getScene().getWindow();
            stage.close();
        } catch (SQLException e) {
            new alertMessage().errorMessage(
                "Error adding appointment: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancelButtonAction(ActionEvent event) {
        // Clear the form fields
        appointment_PatientID.getSelectionModel().clearSelection();
        appointment_serviceAvail.getSelectionModel().clearSelection();
        appointment_schedule.getEditor().clear();

        // Close the current window
        Stage stage = (Stage) appointment_PatientID.getScene().getWindow();
        stage.close();
    }
}
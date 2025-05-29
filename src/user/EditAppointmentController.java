package user;

import config.Session;
import config.alertMessage;
import config.connectDB;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.stage.Stage;
import models.appointment;

public class EditAppointmentController implements Initializable {
    @FXML private ComboBox<Integer> appointment_AppointmentID;

    @FXML private ComboBox<String> editappointment_serviceAvail;

    @FXML private ComboBox<String> editappointment_status;

    @FXML private Label editAppointmentPatientName;

    @FXML private TextArea editappointment_notes;

    @FXML private DatePicker editappointment_schedule;

    @FXML private Button cancel_addbtn;

    @FXML private Button edit_appointmentbtn;

    private alertMessage alert = new alertMessage();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            loadAppointmentIDs();
        } catch (SQLException e) {
            System.out.println(
                "Error loading appointment IDs: " + e.getMessage());
        }

        try {
            loadServiceNames();
        } catch (SQLException e) {
            System.out.println(
                "Error loading service names: " + e.getMessage());
        }
        loadStatusOptions();

        appointment_AppointmentID.valueProperty().addListener((observable,
                                                                  oldValue,
                                                                  newValue) -> {
            if (newValue != null) {
                appointment selectedAppointment = null;
                try {
                    selectedAppointment = connectDB.getAppointment(newValue);
                } catch (SQLException ex) {
                    Logger.getLogger(EditAppointmentController.class.getName())
                        .log(Level.SEVERE, null, ex);
                }
                if (selectedAppointment != null) {
                    editappointment_serviceAvail.getSelectionModel().select(
                        selectedAppointment.getServiceName());
                    editappointment_status.getSelectionModel().select(
                        selectedAppointment.getStatus());
                    editAppointmentPatientName.setText(
                        selectedAppointment.getPatientName());
                    editappointment_notes.setText(
                        selectedAppointment.getNotes());
                    editappointment_schedule.setValue(LocalDate.parse(
                        selectedAppointment.getAppointmentSchedule()));
                }
            }
        });
    }

    private void loadAppointmentIDs() throws SQLException {
        connectDB db = new connectDB();
        ObservableList<Integer> appointmentIDs =
            db.getAppointmentIDsByStatus("Pending");
        appointment_AppointmentID.setItems(appointmentIDs);
    }

    private void loadServiceNames() throws SQLException {
        ObservableList<String> serviceNames = connectDB.getServiceNames();
        editappointment_serviceAvail.setItems(serviceNames);
    }

    private void loadStatusOptions() {
        ObservableList<String> statusOptions =
            FXCollections.observableArrayList(
                "Pending", "Completed", "Cancelled");
        editappointment_status.setItems(statusOptions);
    }


@FXML
private void handleEditAppointmentBtnAction(ActionEvent event) throws SQLException {
    int selectedAppointmentID =
        appointment_AppointmentID.getSelectionModel().getSelectedItem();
    if (selectedAppointmentID != 0) {
        appointment originalAppointment = connectDB.getAppointment(selectedAppointmentID);

        String selectedStatus =
            editappointment_status.getSelectionModel().getSelectedItem();
        String notes = editappointment_notes.getText();
        LocalDate schedule = editappointment_schedule.getValue();
        
          // Validate date
        LocalDate currentDate = LocalDate.now();
        if (schedule != null && schedule.isBefore(currentDate)) {
            alert.errorMessage("Appointment date cannot be before the current date");
            return;
        }
        

        // Use the original values for the fields that haven't been changed
        String updatedStatus = selectedStatus != null ? selectedStatus : originalAppointment.getStatus();
        String updatedNotes = notes != null && !notes.isEmpty() ? notes : originalAppointment.getNotes();
        LocalDate updatedSchedule = schedule != null ? schedule : LocalDate.parse(originalAppointment.getAppointmentSchedule());

        appointment updatedAppointment = new appointment(
            selectedAppointmentID, 
            updatedSchedule.toString(), 
            updatedStatus, 
            updatedNotes, 
            0.0 // price
        );

        try {
            connectDB db = new connectDB();
            db.updateAppointment(updatedAppointment);
            System.out.println("Appointment updated successfully");
            alert.successMessage("Appointment updated successfully");
            db.insertLogEntry(Session.getInstance().getUid(), "Appointment updated successfully.");
            // Clear fields
            appointment_AppointmentID.getSelectionModel().clearSelection();
            editappointment_serviceAvail.getSelectionModel().clearSelection();
            editappointment_status.getSelectionModel().clearSelection();
            editAppointmentPatientName.setText("");
            editappointment_notes.clear();
            
            // Close the window
            Stage stage = (Stage) editappointment_status.getScene().getWindow();
            stage.close();
        } catch (SQLException ex) {
            Logger.getLogger(EditAppointmentController.class.getName())
                .log(Level.SEVERE, null, ex);
        }
    }
}

    @FXML
    private void handleCancelAddBtnAction(ActionEvent event) {
        ((Stage) cancel_addbtn.getScene().getWindow()).close();
    }
}
package config;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import models.appointment;

public class PanelPrintingController implements Initializable {

    @FXML
    private Label appointment_id;

    @FXML
    private Label patient_name;

    @FXML
    private Label scheduled_date;

    @FXML
    private Label service_availed;

    @FXML
    private Label service_fee;

    private appointment selectedAppointment;

    public void setSelectedAppointment(appointment selectedAppointment) {
        this.selectedAppointment = selectedAppointment;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public void propagatePrint() throws SQLException {
    if (selectedAppointment != null) {
        appointment_id.setText(String.valueOf(selectedAppointment.getAppointmentID()));
        patient_name.setText(selectedAppointment.getPatientName());
        scheduled_date.setText(selectedAppointment.getAppointmentSchedule());
        service_availed.setText(selectedAppointment.getServiceName());
        
        // Retrieve the price through the ServiceID
        connectDB db = new connectDB();
        double price = db.getPriceByServiceID(selectedAppointment.getServiceID());
        service_fee.setText(String.valueOf(price));
    } else {
        Logger.getLogger(PanelPrintingController.class.getName()).log(Level.SEVERE, "Selected appointment is null.");
    }
}
   
    public void clearPrint() {
    appointment_id.setText("");
    patient_name.setText("");
    scheduled_date.setText("");
    service_availed.setText("");
    service_fee.setText("");
}
}
    

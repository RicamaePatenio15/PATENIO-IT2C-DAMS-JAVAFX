package admin;

import config.connectDB;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.service;

public class EditServiceController implements Initializable {
    @FXML private Button edit_cancelbtn;

    @FXML private Button edit_updatebtn;

    @FXML private Text editservice_id;

    @FXML private TextField editservice_name;

    @FXML private TextField editservice_price;

    private service selectedService;
    private AdminDashboardController adminDashboardController;

    // Method to set the AdminDashboardController reference
    public void setAdminDashboardController(
        AdminDashboardController adminDashboardController) {
        this.adminDashboardController = adminDashboardController;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {}

    // Set the selected service
    public void setService(service service) {
        this.selectedService = service;
        // Set the service details
        editservice_id.setText(String.valueOf(selectedService.getService_id()));
        editservice_name.setText(selectedService.getService_name());
        editservice_price.setText(String.valueOf(selectedService.getPrice()));
    }

    @FXML
    // Update the service details
    private void updateService(ActionEvent event) {
        String serviceName = editservice_name.getText();
        double servicePrice = Double.parseDouble(editservice_price.getText());

        connectDB.updateServiceData(
            selectedService.getService_id(), serviceName, servicePrice);

        // Close the window
        ((Stage) edit_updatebtn.getScene().getWindow()).close();
    }

    @FXML
    private void cancelServiceedit(ActionEvent event) {
        // Close the edit form
        ((Stage) edit_cancelbtn.getScene().getWindow()).close();
    }
}
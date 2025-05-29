package admin;

import config.alertMessage;
import config.connectDB;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddServiceController implements Initializable {
    @FXML private Button add_servicebtn;

    @FXML private TextField addservice_name;

    @FXML private TextField addservice_price;

    @FXML private Button cancel_addbtn;

    private alertMessage alert;

    private AdminDashboardController adminDashboardController;

    public void setAdminDashboardController(
        AdminDashboardController adminDashboardController) {
        this.adminDashboardController = adminDashboardController;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        alert = new alertMessage();
    }

    @FXML
    void addService(ActionEvent event) {
        String serviceName = addservice_name.getText().trim();
        String servicePriceStr = addservice_price.getText().trim();

        // Validate input
        if (serviceName.isEmpty() || servicePriceStr.isEmpty()) {
            alert.errorMessage("Please fill out all fields.");
            return;
        }

        try {
            double servicePrice = Double.parseDouble(servicePriceStr);
            if (servicePrice <= 0) {
                alert.errorMessage("Service price must be greater than zero.");
                return;
            }

            connectDB.addService(serviceName, servicePrice);
            alert.successMessage("Service added successfully.");

            // Close the current window
            Stage currentStage = (Stage) add_servicebtn.getScene().getWindow();
            currentStage.close();
        } catch (NumberFormatException e) {
            alert.errorMessage(
                "Invalid service price. Please enter a valid number.");
        }
    }

    @FXML
    void cancelServiceAdd(ActionEvent event) {
        // Close the add form
        ((Stage) cancel_addbtn.getScene().getWindow()).close();
    }
}
package dentalclinicsystem;

import config.alertMessage;
import config.connectDB;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.stage.Modality;

public class VeriyEmailController implements Initializable {

    @FXML
    private TextField verifyEmail;

    @FXML
    private Button verifyEmail_btn;

    private alertMessage alert;
    private int uId;

    public VeriyEmailController() {
        this.alert = new alertMessage();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void handleVerifyEmailButtonAction(ActionEvent event) throws IOException {
        try {
            String email = verifyEmail.getText();
            if (email.isEmpty()) {
                alert.errorMessage("Email field cannot be empty.");
                return;
            }

            // Connect to database
            String query = "SELECT * FROM tbl_user WHERE email = ?";
            ResultSet rs = connectDB.getData(query, email);

            if (rs.next()) {
                   // Email exists, redirect to next FXML
    
   FXMLLoader loader = new FXMLLoader(getClass().getResource("/dentalclinicsystem/ForgotPassword.fxml"));
Parent root = loader.load();
ForgotPasswordController controller = loader.getController();
controller.setuId(rs.getInt("u_id")); // Pass the uId to the ForgotPasswordController
Stage stage = new Stage();
stage.initModality(Modality.APPLICATION_MODAL);
stage.setScene(new Scene(root));
stage.show();
    // Close current FXML
    ((Stage) verifyEmail_btn.getScene().getWindow()).close();
            } else {
                alert.errorMessage("Email does not exist.");
                // Close current FXML
                ((Stage) verifyEmail_btn.getScene().getWindow()).close();
            }
        } catch (SQLException | IOException e) {
            alert.errorMessage("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public void setuId(int uId) {
    this.uId = uId;
}
}
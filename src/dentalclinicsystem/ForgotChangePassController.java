package dentalclinicsystem;

import config.passwordHasher;
import config.alertMessage;
import config.connectDB;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ForgotChangePassController implements Initializable {

    @FXML
    private Button changepass_cancelbutton;

    @FXML
    private Button changepass_savebutton;

    @FXML
    private PasswordField createnewpass_conpass;

    @FXML
    private PasswordField createnewpass_newpass;

    @FXML
    private CheckBox show_conpass;

    @FXML
    private CheckBox show_newpass;
    
        @FXML
    private TextField createnewpass_connewpass_text;
            @FXML
    private TextField createnewpass_newpass_text;

    private alertMessage alert = new alertMessage();
    private connectDB db = new connectDB();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void handleCancelButtonAction(ActionEvent event) {
        // Close the current scene
        ((Stage) changepass_cancelbutton.getScene().getWindow()).close();
    }

    @FXML
    private void handleSaveButtonAction(ActionEvent event) {
        String newPassword = createnewpass_newpass.getText();
        String confirmPassword = createnewpass_conpass.getText();

        if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
            alert.errorMessage("Please fill in both password fields.");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            alert.errorMessage("Passwords do not match.");
            return;
        }

        try {
            String hashedPassword = passwordHasher.hashPassword(newPassword);
            // Update the password in the database
          db.updatePassword(hashedPassword, uId);
            alert.successMessage("Password updated successfully.");
            // Close the current scene
            ((Stage) changepass_savebutton.getScene().getWindow()).close();
        } catch (Exception e) {
            alert.errorMessage("Error updating password: " + e.getMessage());
        }
    }

  @FXML
private void handleShowNewPassCheckBoxAction(ActionEvent event) {
    if (show_newpass.isSelected()) {
        createnewpass_newpass_text.setText(createnewpass_newpass.getText());
        createnewpass_newpass_text.setVisible(true);
        createnewpass_newpass.setVisible(false);
    } else {
        createnewpass_newpass_text.setText(createnewpass_newpass.getText());
        createnewpass_newpass_text.setVisible(false);
        createnewpass_newpass.setVisible(true);
    }
}

@FXML
private void handleShowConPassCheckBoxAction(ActionEvent event) {
    if (show_conpass.isSelected()) {
        createnewpass_connewpass_text.setText(createnewpass_conpass.getText());
        createnewpass_connewpass_text.setVisible(true);
        createnewpass_conpass.setVisible(false);
    } else {
        createnewpass_connewpass_text.setText(createnewpass_conpass.getText());
        createnewpass_connewpass_text.setVisible(false);
        createnewpass_conpass.setVisible(true);
    }
}
    
    private int uId;
    public void setuId(int uId) {
        this.uId = uId;
    }
}
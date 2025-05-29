package dentalclinicsystem;

import config.Session;
import config.alertMessage;
import config.connectDB;
import config.passwordHasher;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class ChangePasswordController implements Initializable {
    @FXML private Button changepass_cancelbutton;

    @FXML private PasswordField changepass_conpass;

    @FXML private PasswordField changepass_newpass;

    @FXML private PasswordField changepass_oldpass;

    @FXML private CheckBox show_conpass;

    @FXML private CheckBox show_newpass;

    @FXML private CheckBox show_oldpass;

    // Optional: TextFields to show passwords
    @FXML private TextField changepass_oldpass_text;

    @FXML private TextField changepass_newpass_text;

    @FXML private TextField changepass_conpass_text;

    @Override
    public void initialize(URL url, ResourceBundle rb) {}

    alertMessage alert = new alertMessage();

    @FXML
    private void SaveChangePass(ActionEvent event) {
        // Check if any of the password fields are empty
        if (changepass_oldpass.getText().isEmpty()
            || changepass_newpass.getText().isEmpty()
            || changepass_conpass.getText().isEmpty()) {
            alert.errorMessage("Please fill in all fields!");
            return; // Exit the method if any field is empty
        }

        try {
            connectDB dbc = new connectDB();
            Session ses = Session.getInstance();

            String query =
                "SELECT * FROM tbl_user WHERE u_id = '" + ses.getUid() + "'";
            ResultSet rs = dbc.getData(query);

            if (rs.next()) {
                String olddbpass = rs.getString("pass");
                String oldhash =
                    passwordHasher.hashPassword(changepass_oldpass.getText());

                if (olddbpass.equals(oldhash)) {
                    String newPassString = changepass_newpass.getText();
                    String conPassString = changepass_conpass.getText();

                    if (!newPassString.equals(conPassString)) {
                        alert.errorMessage("Your Password Do not Match!");
                    } else {
                        String npass =
                            passwordHasher.hashPassword(newPassString);
                        dbc.updateData("UPDATE tbl_user SET pass = '" + npass
                            + "' WHERE u_id = '" + ses.getUid() + "'");
                        alert.successMessage("Password Updated Successfully!");
                        closeWindow();
                        dbc.insertLogEntry(ses.getUid(), "Password updated successfully.");
                    }
                } else {
                    alert.errorMessage("Old Password is Incorrect!");
                }
            }
        } catch (SQLException | NoSuchAlgorithmException ex) {
            System.out.println("" + ex);
        }
    }

    @FXML
    private void changepass_cancelAction(ActionEvent event) {
        clearFields();
        closeWindow();
    }

    private void clearFields() {
        changepass_oldpass.clear();
        changepass_newpass.clear();
        changepass_conpass.clear();
        changepass_oldpass_text.clear();
        changepass_newpass_text.clear();
        changepass_conpass_text.clear();
    }

    private void closeWindow() {
        javafx.stage.Stage stage =
            (javafx.stage.Stage) changepass_cancelbutton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void showOldPassword(ActionEvent event) {
        if (show_oldpass.isSelected()) {
            changepass_oldpass_text.setText(changepass_oldpass.getText());
            changepass_oldpass_text.setVisible(true);
            changepass_oldpass.setVisible(false);
        } else {
            changepass_oldpass.setText(changepass_oldpass_text.getText());
            changepass_oldpass_text.setVisible(false);
            changepass_oldpass.setVisible(true);
        }
    }

    @FXML
    public void showNewPassword(ActionEvent event) {
        if (show_newpass.isSelected()) {
            changepass_newpass_text.setText(changepass_newpass.getText());
            changepass_newpass_text.setVisible(true);
            changepass_newpass.setVisible(false);
        } else {
            changepass_newpass.setText(changepass_newpass_text.getText());
            changepass_newpass_text.setVisible(false);
            changepass_newpass.setVisible(true);
        }
    }

    @FXML
    public void showConPassword(ActionEvent event) {
        if (show_conpass.isSelected()) {
            changepass_conpass_text.setText(changepass_conpass.getText());
            changepass_conpass_text.setVisible(true);
            changepass_conpass.setVisible(false);
        } else {
            changepass_conpass.setText(changepass_conpass_text.getText());
            changepass_conpass_text.setVisible(false);
            changepass_conpass.setVisible(true);
        }
    }
}
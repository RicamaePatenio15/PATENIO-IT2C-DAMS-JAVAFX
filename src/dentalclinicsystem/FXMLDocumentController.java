
package dentalclinicsystem;

import admin.Admin_activityLogController;
import config.Session;
import config.Users;
import config.alertMessage;
import config.connectDB;
import config.passwordHasher;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.SecurityQuestion;

public class FXMLDocumentController implements Initializable {
    @FXML private Button login_button;
    @FXML private TextField login_email;
    @FXML private AnchorPane login_form;
    @FXML private AnchorPane registration_form;
    @FXML private PasswordField login_pass;
    @FXML private Hyperlink login_reglink;
    @FXML private Hyperlink forgotpassword;
    @FXML private CheckBox login_showpass;
    @FXML private AnchorPane main_form;
    @FXML private TextField reg_email;
    @FXML private TextField reg_fname;
    @FXML private TextField reg_lname;
    @FXML private Hyperlink reg_loginlink;
    @FXML private PasswordField reg_pass;
    @FXML private TextField reg_phonenum;
    @FXML private CheckBox reg_showpass;
    @FXML private ComboBox<String> reg_type;
    @FXML private Button register_button;
    @FXML private TextField login_textfield;
    @FXML private TextField reg_showpassfield;
    @FXML private FontAwesomeIcon close;
    @FXML private FontAwesomeIcon minimize;
        @FXML
    private ComboBox<String> ChooseSecQuest;
            @FXML
    private TextField securityquestionanswer;
    Admin_activityLogController logController = new Admin_activityLogController();

    public void switchForm(ActionEvent event) {
        if (event.getSource() == login_reglink) {
            // Registration form show
            login_form.setVisible(false);
            registration_form.setVisible(true);
        } else if (event.getSource() == reg_loginlink) {
            // Login form show
            login_form.setVisible(true);
            registration_form.setVisible(false);
        }
    }

    public void loginShowPassword(ActionEvent event) {
        if (login_showpass.isSelected()) {
            login_textfield.setText(login_pass.getText());
            login_textfield.setVisible(true);
            login_pass.setVisible(false);
        } else {
            login_pass.setText(login_textfield.getText());
            login_textfield.setVisible(false);
            login_pass.setVisible(true);
        }
    }

    public void registerShowPassword(ActionEvent event) {
        if (reg_showpass.isSelected()) {
            reg_showpassfield.setText(reg_pass.getText());
            reg_showpassfield.setVisible(true);
            reg_pass.setVisible(false);
        } else {
            reg_showpassfield.setText(reg_pass.getText());
            reg_showpassfield.setVisible(false);
            reg_pass.setVisible(true);
        }
    }

    // Database Tools
    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;

    public void registerAccount(ActionEvent event) throws SQLException {
    String fname = reg_fname.getText().trim();
    String lname = reg_lname.getText().trim();
    String phoneNum = reg_phonenum.getText().trim();
    String email = reg_email.getText().trim();
    String password = reg_pass.getText().trim();
    String userType = reg_type.getSelectionModel().getSelectedItem() == null
        ? ""
        : reg_type.getSelectionModel().getSelectedItem();
    String securityQuestionAnswer = securityquestionanswer.getText().trim();

    // Validate user input
    if (fname.isEmpty() || lname.isEmpty() || phoneNum.isEmpty()
        || email.isEmpty() || password.isEmpty() || userType.isEmpty() || securityQuestionAnswer.isEmpty()) {
        alertMessage alert = new alertMessage();
        alert.errorMessage("Please fill all blank fields");
        return;
    }
    // Validate email format
    if (!email.matches(
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
        alertMessage alert = new alertMessage();
        alert.errorMessage("Invalid email format");
        return;
    }
    // Validate phone number format
    if (!phoneNum.matches("^[0-9]{11}$")) {
        alertMessage alert = new alertMessage();
        alert.errorMessage("Invalid phone number format");
        return;
    }
    // Check password length
    if (password.length() < 8) {
        alertMessage alert = new alertMessage();
        alert.errorMessage("Password must be at least 8 characters long.");
        reg_pass.setText("");
        return;
    }
    if (checkDuplicateEmail(email)) {
        alertMessage alert = new alertMessage();
        alert.errorMessage("Email already exists");
        return;
    }
    // Connect to database
    connectDB con = new connectDB();
    Connection connect = con.getConnection();
    try {
        // Hash password
        String hashedPassword = passwordHasher.hashPassword(password);
        // Create SQL query
        String query = "INSERT INTO tbl_user (fname, lname, phone_num, "
            + "email, pass, u_type, status) VALUES "
            + "('" + fname + "', '" + lname + "', '" + phoneNum + "', '"
            + email + "', '" + hashedPassword + "', '" + userType
            + "', 'Pending')";
        // Print SQL query for debugging
        System.out.println("SQL Query: " + query);
        // Execute SQL query
        Statement stmt = connect.createStatement();
        int rowsAffected = stmt.executeUpdate(query);
        // Get the newly inserted user ID
        ResultSet rs = stmt.executeQuery("SELECT LAST_INSERT_ID()");
        int userId = 0;
        if (rs.next()) {
            userId = rs.getInt(1);
        }
       // Add security question
SecurityQuestion securityQuestion = new SecurityQuestion(
    0, userId, securityQuestionAnswer);
con.addSecurityQuestion(securityQuestion);
        // Check if any rows were affected
        if (rowsAffected > 0) {
            alertMessage alert = new alertMessage();
            alert.successMessage("Account registered successfully!");
            // Clear form fields
            registerClear();
        } else {
            alertMessage alert = new alertMessage();
            alert.errorMessage("Failed to register account");
        }
        stmt.close();
    } catch (SQLException | NoSuchAlgorithmException e) {
        alertMessage alert = new alertMessage();
        alert.errorMessage("Error registering account: " + e.getMessage());
    }
}

    public void userList() {
        List<String> listU = new ArrayList<>();
        listU.addAll(Arrays.asList(Users.user));
        ObservableList listData = FXCollections.observableList(listU);
        reg_type.setItems(listData);
    }

    public void registerClear() {
        reg_fname.clear();
        reg_lname.clear();
        reg_phonenum.clear();
        reg_email.clear();
        reg_pass.clear();
        reg_showpassfield.clear();
        reg_type.getSelectionModel().clearSelection();
    }

    public boolean checkDuplicateEmail(String email) {
        boolean duplicate = false;
        String query = "SELECT 1 FROM tbl_user WHERE email = ? LIMIT 1";
        try (Connection connect = new connectDB().getConnection();
             PreparedStatement stmt = connect.prepareStatement(query)) {
            stmt.setString(1, email);
            try (ResultSet result = stmt.executeQuery()) {
                duplicate = result.next();
            }
        } catch (SQLException e) {
            System.err.println(
                "Error checking for duplicate email: " + e.getMessage());
        }
        return duplicate;
    }

    private Stage stage;
    private Scene scene;
    private Parent root;

    DentalClinicSystem m = new DentalClinicSystem();

    public void loginAccount(ActionEvent event) throws IOException {
        String status;
        String type;
        if (login_email.getText().isEmpty() || login_pass.getText().isEmpty()) {
            alertMessage alert = new alertMessage();
            alert.errorMessage("Please fill all the blank fields");
        } else {
            connectDB con = new connectDB();
            try {
                String query = "SELECT * FROM tbl_user WHERE email = '"
                    + login_email.getText() + "'";
                ResultSet resultSet = con.getData(query);
                if (resultSet.next()) {
                    String hashedPass = resultSet.getString("pass");
                    String rehashedPass =
                        passwordHasher.hashPassword(login_pass.getText());

                    if (hashedPass.equals(rehashedPass)) {
                        status = resultSet.getString("status");
                        type = resultSet.getString("u_type");

                        if (status.equals("Active")) {
                            Session ses = Session.getInstance();
                            ses.setUid(resultSet.getInt("u_id"));
                            ses.setFname(resultSet.getString("fname"));
                            ses.setLname(resultSet.getString("lname"));
                            ses.setEmail(resultSet.getString("email"));
                            ses.setPhone_num(resultSet.getString("phone_num"));
                            ses.setType(resultSet.getString("u_type"));
                            ses.setStatus(resultSet.getString("status"));
                            alertMessage alert = new alertMessage();
                            switch (type) {
                                case "Admin":

                                    alert.successMessage("Login Successfully!");
                                    closeLoginForm();
                                    connectDB.insertLogEntry(ses.getUid(), "Admin logged in successfully.");

                                    m.changeScene(getClass(), login_form,
                                        "/admin/AdminDashboard.fxml");

                                    break;
                                case "User":
                                    alert.successMessage("Login Successfully!");
                                    closeLoginForm();
                                    connectDB.insertLogEntry(ses.getUid(), "User logged in successfully.");
                                    m.changeScene(getClass(), login_form,
                                        "/user/userDashboard.fxml");

                                    break;
                                default:

                                    alert.errorMessage(
                                        "No account type found, contact the "
                                        + "Administrator.");
                                    break;
                            }
                        } else {
                            alertMessage alert = new alertMessage();
                            alert.errorMessage(
                                "Inactive account, please contact the admin!");
                        }
                    } else {
                        alertMessage alert = new alertMessage();
                        alert.errorMessage("Invalid email or password");
                    }
                } else {
                    alertMessage alert = new alertMessage();
                    alert.errorMessage("Invalid email or password");
                }
            } catch (SQLException | NoSuchAlgorithmException ex) {
                alertMessage alert = new alertMessage();
                alert.errorMessage("Error logging in: " + ex.getMessage());
            }
        }
    }

    public void closeLoginForm() {
        Stage stg = (Stage) login_form.getScene().getWindow();
        stg.close();
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        reg_type.getItems().addAll("User", "Admin");
        initializeQuestion();
    }

    @FXML
    private void handleCloseIconClick(MouseEvent event) {
        alertMessage alert = new alertMessage();
        if (alert.confirmationMessage(
                "Are you sure you want to close this window?")) {
            Stage stage = (Stage) close.getScene().getWindow();
            stage.close();
        } else {
        }
    }
    
    @FXML
private void verifyEmailForgotPass(ActionEvent event) {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("veriyEmail.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root));
        stage.show();
    } catch (IOException e) {
        e.printStackTrace();
    }
}

private void initializeQuestion(){
   
    ChooseSecQuest.getItems().addAll(
        "What is your birthyear?",
        "What is your favorite food?",
        "What is your favorite color?"
    );

    ChooseSecQuest.getSelectionModel().select(0);

    
}
}
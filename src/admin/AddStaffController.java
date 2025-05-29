package admin;

import config.alertMessage;
import config.connectDB;
import config.passwordHasher;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class AddStaffController implements Initializable {
    @FXML private Button addButton;
    @FXML private Button addStaffImage_button;
    @FXML private ImageView addStaff_imageView;
    @FXML private TextField add_email;
    @FXML private TextField add_fname;
    @FXML private TextField add_lname;
    @FXML private PasswordField add_password;
    @FXML private TextField addStaff_textfield;
    @FXML private TextField add_phonenum;
    @FXML private ComboBox<String> add_status; // Specify the type for ComboBox
    @FXML private ComboBox<String> add_type; // Specify the type for ComboBox
    @FXML private Button cancelButton;
    @FXML private CheckBox add_showpassword;

    private AdminDashboardController adminDashboardController;
    // Variable to store the selected image file
    private File selectedImageFile;

    public void setAdminDashboardController(
        AdminDashboardController adminDashboardController) {
        this.adminDashboardController = adminDashboardController;
    }

    @FXML
    public void addButton(ActionEvent event) throws NoSuchAlgorithmException {
        alertMessage alert =
            new alertMessage(); // Create a single instance for alerts

        String fname = add_fname.getText().trim();
        String lname = add_lname.getText().trim();
        String phoneNum = add_phonenum.getText().trim();
        String email = add_email.getText().trim();
        String password = add_password.getText().trim();
        String userType = add_type.getSelectionModel().getSelectedItem();
        String status = add_status.getSelectionModel().getSelectedItem();

        // Validate user input
        if (fname.isEmpty() || lname.isEmpty() || phoneNum.isEmpty()
            || email.isEmpty() || password.isEmpty() || userType == null
            || status == null) {
            alert.errorMessage("Please fill all blank fields");
            return;
        }
        // Validate email format
        if (!email.matches(
                "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            alert.errorMessage("Invalid email format");
            return;
        }
        // Validate phone number format
        if (!phoneNum.matches("^[0-9]{11}$")) {
            alert.errorMessage("Invalid phone number format");
            return;
        }
        // Check password length
        if (password.length() < 8) {
            alert.errorMessage("Password must be at least 8 characters long.");
            add_password.setText("");
            return;
        }
        if (checkDuplicateEmail(
                email)) { // Assuming this method checks for duplicate emails
            alert.errorMessage("Email already exists");
            return;
        }

        // Check if an image file was selected
        if (selectedImageFile != null) {
            try {
                // Read the image file into a byte array
                byte[] imageBytes =
                    Files.readAllBytes(selectedImageFile.toPath());

                // Connect to database
                try (Connection connect = connectDB.getConnection();
                     PreparedStatement pstmt = connect.prepareStatement(
                         "INSERT INTO tbl_user (fname, lname, phone_num, "
                         + "email, pass, u_type, status) VALUES (?, ?, ?, ?, "
                         + "?, ?, ?)")) {
                    // Hash password
                    String hashedPassword =
                        passwordHasher.hashPassword(password);

                    // Set parameters for the prepared statement
                    pstmt.setString(1, fname);
                    pstmt.setString(2, lname);
                    pstmt.setString(3, phoneNum);
                    pstmt.setString(4, email);
                    pstmt.setString(5, hashedPassword);
                    pstmt.setString(6, userType);
                    pstmt.setString(7, status);

                    // Execute SQL query
                    int rowsAffected = pstmt.executeUpdate();
                    if (rowsAffected > 0) {
                        alert.successMessage(
                            "Account registered successfully!");
                        registerClear(); // Assuming this method clears the form
                                         // fields

                        // Get the generated user ID
                        int userId = getUserId(email);

                        // Insert the image into the profile image table
                        int imageId = insertProfileImage(userId, imageBytes);

                        // Update the profileimage column in tbl_user
                        updateProfileImageColumn(userId, imageId);

                        if (adminDashboardController != null) {
                            adminDashboardController.propagateStaffTable();
                            // Refresh the AdminDashboard
                            adminDashboardController.propagateDashboardTable();
                        }
                        // Get the current stage
                        Stage currentStage =
                            (Stage) addButton.getScene().getWindow();
                        currentStage.close(); // Close the current window

                    } else {
                        alert.errorMessage("Failed to register account");
                    }
                }
            } catch (IOException | SQLException e) {
                alert.errorMessage(
                    "Error registering account: " + e.getMessage());
            }
        } else {
            // Connect to database without image data
            try (Connection connect = connectDB.getConnection();
                 PreparedStatement pstmt = connect.prepareStatement(
                     "INSERT INTO tbl_user (fname, lname, phone_num, email, "
                     + "pass, u_type, status) VALUES (?, ?, ?, ?, ?, ?, ?)")) {
                // Hash password
                String hashedPassword = passwordHasher.hashPassword(password);

                // Set parameters for the prepared statement
                pstmt.setString(1, fname);
                pstmt.setString(2, lname);
                pstmt.setString(3, phoneNum);
                pstmt.setString(4, email);
                pstmt.setString(5, hashedPassword);
                pstmt.setString(6, userType);
                pstmt.setString(7, status);

                // Execute SQL query
                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    alert.successMessage("Account registered successfully!");
                    registerClear(); // Assuming this method clears the form
                                     // fields

                    if (adminDashboardController != null) {
                        adminDashboardController.propagateStaffTable();
                        // Refresh the AdminDashboard
                        adminDashboardController.propagateDashboardTable();
                    }
                    // Get the current stage
                    Stage currentStage =
                        (Stage) addButton.getScene().getWindow();
                    currentStage.close(); // Close the current window

                } else {
                    alert.errorMessage("Failed to register account");
                }
            } catch (SQLException | NoSuchAlgorithmException e) {
                alert.errorMessage(
                    "Error registering account: " + e.getMessage());
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialize the user type ComboBox
        add_type.getItems().addAll("User", "Admin");
        // Initialize the status ComboBox
        add_status.getItems().addAll(
            "Active", "Inactive", "Pending"); // Add appropriate status options
    }

    // Assuming this method checks for duplicate emails
    private boolean checkDuplicateEmail(String email) {
        // Implement your logic to check for duplicate emails in the database
        return false; // Placeholder return value
    }

    // Assuming this method clears the form fields
    private void registerClear() {
        add_fname.clear();
        add_lname.clear();
        add_email.clear();
        add_phonenum.clear();
        add_password.clear();
        add_type.getSelectionModel().clearSelection();
        add_status.getSelectionModel().clearSelection();
    }
    public void showAddStaffForm(Stage stage) {
        try {
            Parent root =
                FXMLLoader.load(getClass().getResource("addStafff.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println(
                "Error loading add staff form: " + e.getMessage());
        }
    }

    public void addShowPassword(ActionEvent event) {
        if (add_showpassword.isSelected()) {
            addStaff_textfield.setText(add_password.getText());
            addStaff_textfield.setVisible(true);
            add_password.setVisible(false);
        } else {
            add_password.setText(addStaff_textfield.getText());
            addStaff_textfield.setVisible(false);
            add_password.setVisible(true);
        }
    }

    @FXML
    public void cancelButton(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void addStaffImage(ActionEvent event) {
        // Create a FileChooser to select an image file
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Staff Image");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter(
                "Image Files", "*.jpg", "*.jpeg", "*.png", "*.gif"));

        // Show the FileChooser and get the selected file
        selectedImageFile =
            fileChooser.showOpenDialog(addButton.getScene().getWindow());

        // Check if a file was selected
        if (selectedImageFile != null) {
            // Display the selected image in the ImageView
            Image image = new Image(selectedImageFile.toURI().toString());
            addStaff_imageView.setFitWidth(190); // Set the fit width
            addStaff_imageView.setFitHeight(146); // Set the fit height
            addStaff_imageView.setPreserveRatio(
                false); // Preserve the aspect ratio
            addStaff_imageView.setImage(image);
        }
    }
    // Method to get the generated user ID
    private int getUserId(String email) throws SQLException {
        String query = "SELECT u_id FROM tbl_user WHERE email = ?";

        try (Connection connect = connectDB.getConnection();
             PreparedStatement pstmt = connect.prepareStatement(query)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("u_id");
            } else {
                return -1; // User not found
            }
        }
    }

    // Method to insert the profile image into the database
    private int insertProfileImage(int userId, byte[] imageBytes)
        throws SQLException {
        String query = "INSERT INTO tbl_ProfileImage (UserId, ImageName, "
                       + "ImagePath, ImageFile) VALUES (?, ?, ?, ?)";

        try (Connection connect = connectDB.getConnection();
             PreparedStatement pstmt = connect.prepareStatement(
                 query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, selectedImageFile.getName());
            pstmt.setString(3, selectedImageFile.getAbsolutePath());
            pstmt.setBytes(4, imageBytes);
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException(
                        "Failed to retrieve generated ImageID");
                }
            }
        }
    }

    // Method to update the profileimage column in tbl_user
    private void updateProfileImageColumn(int userId, int imageId)
        throws SQLException {
        String query = "UPDATE tbl_user SET profileimage = ? WHERE u_id = ?";

        try (Connection connect = connectDB.getConnection();
             PreparedStatement pstmt = connect.prepareStatement(query)) {
            pstmt.setInt(1, imageId);
            pstmt.setInt(2, userId);
            pstmt.executeUpdate();
        }
    }
}
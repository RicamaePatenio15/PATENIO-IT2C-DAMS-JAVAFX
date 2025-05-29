package admin;

import config.connectDB;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.ProfileImage;
import models.staff;

public class EditStaffController implements Initializable {
    @FXML private Button cancelButton;

    @FXML private Button updatebutton;

    @FXML private TextField edit_email;

    @FXML private TextField edit_fname;

    @FXML private TextField edit_lname;

    @FXML private TextField edit_phonenum;

    @FXML private ComboBox<String> edit_status;

    @FXML private ComboBox<String> edit_type;

    @FXML private Button editStaffImage_button;

    @FXML private ImageView editStaff_imageView;

    private staff selectedStaff;
    private AdminDashboardController adminDashboardController;
    private File selectedImageFile;

    public void setAdminDashboardController(
        AdminDashboardController adminDashboardController) {
        this.adminDashboardController = adminDashboardController;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        edit_status.getItems().addAll("Active", "Inactive");
        edit_type.getItems().addAll("Admin", "User ");
    }

    public void setStaff(staff staff) {
        this.selectedStaff = staff;
        loadImage();
        edit_fname.setText(staff.getFname());
        edit_lname.setText(staff.getLname());
        edit_phonenum.setText(staff.getPhonenum());
        edit_email.setText(staff.getEmail());
        edit_type.setValue(staff.getType());
        edit_status.setValue(staff.getStatus());
    }
    @FXML
    void updateStaff(ActionEvent event) {
        String fname = edit_fname.getText().trim();
        String lname = edit_lname.getText().trim();
        String phonenum = edit_phonenum.getText().trim();
        String email = edit_email.getText().trim();
        String status = edit_status.getValue();
        String userType = edit_type.getValue();

        if (fname.isEmpty() || lname.isEmpty() || phonenum.isEmpty()
            || email.isEmpty() || status == null || userType == null) {
            showAlert("Validation Error", "All fields must be filled out.");
            return;
        }

        if (!phonenum.matches("\\d{10,15}")) {
            showAlert("Validation Error",
                "Phone number must be between 10 to 15 digits.");
            return;
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            showAlert(
                "Validation Error", "Please enter a valid email address.");
            return;
        }

        try {
            System.out.println(
                "Updating staff with ID: " + selectedStaff.getUid());

            if (selectedImageFile != null) {
                byte[] imageBytes =
                    Files.readAllBytes(selectedImageFile.toPath());

                // Retrieve the existing profile image for the staff member
                ProfileImage existingImage =
                    connectDB.getProfileImageByUserId(selectedStaff.getUid());

                if (existingImage != null) {
                    // Update the existing profile image with the new image file
                    existingImage.setImageName(selectedImageFile.getName());
                    existingImage.setImagePath(
                        selectedImageFile.getAbsolutePath());
                    existingImage.setImageFile(imageBytes);

                    // Update the profile image in the database
                    connectDB.updateProfileImage(existingImage);
                } else {
                    // Create a new profile image if one doesn't exist
                    ProfileImage newImage = new ProfileImage(0,
                        selectedImageFile.getName(), selectedStaff.getUid(),
                        selectedImageFile.getAbsolutePath(), imageBytes);
                    connectDB.addProfileImage(newImage);
                }
            }

            System.out.println("Updating staff with values: " + fname + ", "
                + lname + ", " + phonenum + ", " + email + ", " + userType
                + ", " + status);

            connectDB.updateStaff(selectedStaff.getUid(), fname, lname,
                phonenum, email, userType, status);
            loadImage(); // Reload the staff image

            System.out.println("Staff updated successfully!");

            if (adminDashboardController != null) {
                adminDashboardController.propagateDashboardTable();
                adminDashboardController.propagateStaffTable();
            }

            showAlert("Success", "Staff information updated successfully.");

            Stage currentStage = (Stage) updatebutton.getScene().getWindow();
            currentStage.close();

        } catch (SQLException e) {
            System.out.println("Error updating staff: " + e.getMessage());
            showAlert("Update Error",
                "Failed to update staff information: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error reading image file: " + e.getMessage());
            showAlert(
                "Update Error", "Failed to read image file: " + e.getMessage());
        } catch (Exception e) {
            System.out.println(
                "An unexpected error occurred: " + e.getMessage());
            showAlert("Update Error",
                "An unexpected error occurred: " + e.getMessage());
        }
    }

    @FXML
    void editStaffImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Staff Image");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter(
                "Image Files", "*.jpg", "*.jpeg", "*.png", "*.gif"));

        selectedImageFile = fileChooser.showOpenDialog(
            editStaffImage_button.getScene().getWindow());

        if (selectedImageFile != null) {
            Image image = new Image(selectedImageFile.toURI().toString());
            editStaff_imageView.setImage(image);
        }
    }

    private void loadImage() {
        if (selectedStaff != null) {
            try {
                ProfileImage profileImage =
                    connectDB.getProfileImageByUserId(selectedStaff.getUid());
                if (profileImage != null) {
                    byte[] imageBytes = profileImage.getImageFile();
                    Image image =
                        new Image(new ByteArrayInputStream(imageBytes));
                    editStaff_imageView.setFitWidth(190); // Set the fit width
                    editStaff_imageView.setFitHeight(146); // Set the fit height
                    editStaff_imageView.setPreserveRatio(
                        false); // Preserve the aspect ratio
                    editStaff_imageView.setImage(image);
                }
            } catch (SQLException e) {
                System.out.println(
                    "Error loading staff image: " + e.getMessage());
            }
        }
    }

    @FXML
    void cancelEdit(ActionEvent event) {
        ((Stage) cancelButton.getScene().getWindow()).close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
package admin;

import config.connectDB;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.staff;

public class ViewStaffController implements Initializable {
    @FXML private Text viewStaff_email;

    @FXML private Text viewStaff_fname;

    @FXML private ImageView viewStaff_imageView;

    @FXML private Text viewStaff_lname;

    @FXML private Text viewStaff_phonenum;

    @FXML private Text viewStaff_status;

    @FXML private Text viewStaff_type;

    @FXML private FontAwesomeIcon close_viewform;

    @FXML private AnchorPane ViewStaff_form;

    private staff selectedStaff;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        close_viewform.setOnMouseClicked(event -> {
            Stage stage = (Stage) close_viewform.getScene().getWindow();
            stage.close();
        });

        ViewStaff_form.setPrefWidth(548);
        ViewStaff_form.setPrefHeight(333);
    }

    public void setStaff(staff staff) {
        this.selectedStaff = staff;
        try {
            updateUI();
        } catch (SQLException | IOException e) {
            System.out.println(
                "Error loading staff details: " + e.getMessage());
        }
    }

    private void updateUI() throws SQLException, IOException {
        if (selectedStaff != null) {
            viewStaff_fname.setText(selectedStaff.getFname());
            viewStaff_lname.setText(selectedStaff.getLname());
            viewStaff_email.setText(selectedStaff.getEmail());
            viewStaff_phonenum.setText(selectedStaff.getPhonenum());
            viewStaff_status.setText(selectedStaff.getStatus());
            viewStaff_type.setText(selectedStaff.getType() != null
                    ? selectedStaff.getType()
                    : "N/A");

            loadStaffImage();
        }
    }

    public void loadStaffImage() throws SQLException, IOException {
        byte[] imageBytes = connectDB.getStaffImage(selectedStaff.getUid());

        if (imageBytes != null) {
            Image image = new Image(new ByteArrayInputStream(imageBytes));
            viewStaff_imageView.setFitWidth(200);
            viewStaff_imageView.setFitHeight(178);
            viewStaff_imageView.setPreserveRatio(false);
            viewStaff_imageView.setImage(image);
        } else {
            viewStaff_imageView.setImage(null);
        }
    }
}
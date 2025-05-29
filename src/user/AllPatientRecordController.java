
package user;

import config.connectDB;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import models.patient;

public class AllPatientRecordController implements Initializable {
    
    @FXML private TableColumn<patient, Integer> allpatientPID_Col;

    @FXML private TableColumn<patient, String> allpatientFirstName_Col;

    @FXML private TableColumn<patient, String> allpatientLastName_Col;

    @FXML private TableColumn<patient, String> allpatientConNum_Col;

    @FXML private TableColumn<patient, String> allpatientAddress_Col;

    @FXML private TableColumn<patient, String> allpatientStatus_Col;

    @FXML private TableView<patient> allpatientRecord_TableView;

    @FXML private FontAwesomeIcon back_icon;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialize the table columns
        allpatientPID_Col.setCellValueFactory(
            new PropertyValueFactory<>("patientID"));
        allpatientFirstName_Col.setCellValueFactory(
            new PropertyValueFactory<>("firstName"));
        allpatientLastName_Col.setCellValueFactory(
            new PropertyValueFactory<>("lastName"));
        allpatientConNum_Col.setCellValueFactory(
            new PropertyValueFactory<>("contactNumber"));
        allpatientAddress_Col.setCellValueFactory(
            new PropertyValueFactory<>("address"));

        allpatientStatus_Col.setCellValueFactory(
            new PropertyValueFactory<>("status"));
        // Load the data into the table

        allpatientRecord_TableView.setItems(
            connectDB.loadPatientRecordsWithContactAndAddress());
    }

    @FXML
    void handleBackIconClick(MouseEvent event) {
        ((Node) event.getSource()).getScene().getWindow().hide();
    }
}

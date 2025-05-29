package user;

import config.alertMessage;
import config.connectDB;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import models.TreatmentHistory;

public class PatientHistoryController implements Initializable {

    @FXML
    private TableView<TreatmentHistory> PHistory;

    @FXML
    private TableColumn<TreatmentHistory, String> firstNameCol;

    @FXML
    private TableColumn<TreatmentHistory, String> lastNameCol;

    @FXML
    private TableColumn<TreatmentHistory, String> serviceNameCol;

    @FXML
    private TableColumn<TreatmentHistory, LocalDate> dateCol;

    @FXML
    private ComboBox<Integer> patientHistorySrchID;

    @FXML
    private FontAwesomeIcon back_icon;

    private alertMessage alert = new alertMessage();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeTableColumns();
        populateComboBox();
    }

    private void initializeTableColumns() {
         firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
    lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
    serviceNameCol.setCellValueFactory(new PropertyValueFactory<>("serviceName"));
    dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
    }

    private void populateComboBox() {
        ObservableList<Integer> patientIDs = FXCollections.observableArrayList(connectDB.getUniquePatientIDs());
        patientHistorySrchID.setItems(patientIDs);
    }

    @FXML
    private void handlePatientHistorySearchID() {
        int selectedPatientID = patientHistorySrchID.getValue();
        if (selectedPatientID != 0) {
            if (connectDB.isValidPatientID(selectedPatientID)) {
                PHistory.setItems(connectDB.filterPatientRecords(selectedPatientID));
            } else {
                alert.errorMessage("Invalid patient ID");
            }
        } else {
            alert.errorMessage("No patient ID selected");
        }
    }

    @FXML
    void handleCancelAddBtnAction(MouseEvent event) {
        ((Stage) back_icon.getScene().getWindow()).close();
    }
}
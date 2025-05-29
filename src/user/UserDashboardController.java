package user;

import config.PanelPrinter;
import config.PanelPrintingController;
import config.Session;
import config.alertMessage;
import config.connectDB;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import models.PatientInfo;
import models.SecurityQuestion;
import models.activitylog;
import models.appointment;
import models.patient;
import models.service;

public class UserDashboardController implements Initializable {
    // MAIN FORM DECLARATIONS
    @FXML private AnchorPane header_form;
    @FXML private Label top_name;
    @FXML private Circle top_profile;
    @FXML private Button logout_button;
    @FXML private Label current_form;
    @FXML private Label timeLabel;

    @FXML private AnchorPane mainForm;
    @FXML private Label usermenu_email;
    @FXML private Label usermenu_id;
    @FXML private Button dashboard_button;
    @FXML private Button patients_button;
    @FXML private Button appointments_button;
    @FXML private Button service_btn;
    @FXML private Button profile_button;

    // DASHBOARD DECLARATIONS
    @FXML private AnchorPane userDashboard;
    @FXML private Label dashboard_AP;
    @FXML private Label dashboard_IP;
    @FXML private Label dashboard_TP;
    @FXML private Label dashboard_tA;
    @FXML private TableView<appointment> dashboad_tableView;

    @FXML
    private TableColumn<appointment, String> dashboad_col_appointmentSchedule;
    @FXML private TableColumn<appointment, Integer> dashboad_col_appointmentID;
    @FXML private TableColumn<appointment, String> dashboad_col_name;
    @FXML private TableColumn<appointment, String> dashboad_col_status;
    @FXML private BarChart<String, Number> dashboad_chart_DD;
    @FXML private AreaChart<String, Number> dashboad_chart_PD;

    // PATIENT DECLARATIONS

    @FXML private AnchorPane patientForm;
    @FXML private TextField Patientinfo_FirstName;
    @FXML private TextField Patientinfo_LastName;
    @FXML private ComboBox<String> Patientinfo_Gender;
    @FXML private TextField Patientinfo_ContactNumber;
    @FXML private TextArea Patientinfo_Address;
    @FXML private Button Patientinfo_Confirmbtn;
    @FXML private Label Patientinfoadd_Name;
    @FXML private Label Patientinfoadd_Gender;
    @FXML private Label Patientinfoadd_ContactNumber;
    @FXML private Label Patientinfoadd_Address;
    @FXML private Button Patientinfoadd_AddBtn;
    @FXML private Button viewAllPatientRecord_Btn;
    @FXML private Button viewInvidualPatientRecord_Btn;
    private PatientInfo patientInfo = new PatientInfo();

    // APPOINTMENT FORM

    @FXML private AnchorPane appointmentForm;
    @FXML private TableView<appointment> appointments_tableView;
    @FXML
    private TableColumn<appointment, Integer> appointments_col_appointmentID;
    @FXML
    private TableColumn<appointment, String> appointments_col_contactNumber;
    @FXML private TableColumn<appointment, String> appointments_col_schedule;
    @FXML
    private TableColumn<appointment, String> appointments_col_treatment_service;
    @FXML private TableColumn<appointment, String> appointments_col_gender;
    @FXML private TableColumn<appointment, String> appointments_col_name;
    @FXML private TableColumn<appointment, String> appointments_col_status;
    @FXML private Button appointment_insertBtn;
    @FXML private Button appointment_updateBtn;
    @FXML private Button appointment_clearBtn;
    @FXML private Button appointment_deleteBtn;
    @FXML private Button appointment_Print;
    @FXML private DatePicker srchDayAppointment;
    @FXML private FontAwesomeIcon currentDayAppointment;
    // PRINT FORM
    @FXML private AnchorPane PrintForm;
    @FXML private TableView<appointment> PrintAppointment_TableView;
    @FXML private TableColumn<appointment, Integer> PrintAPPID_Col;
    @FXML private TableColumn<appointment, String> PrintPName_Col;
    @FXML private TableColumn<appointment, String> PrintGender_Col;
    @FXML private TableColumn<appointment, String> PrintContactNumber_Col;
    @FXML private TableColumn<appointment, String> PrintPrice_Col;
    @FXML private Label PrintPreviewAPPID;
    @FXML private Label PrintPreviewPname;
    @FXML private Label PrintPreviewSchedule;
    @FXML private Label PrintPreviewServiceAvail;
    @FXML private Label PrintPreviewFee;
    @FXML private Button print_btn;

    @FXML private Button PrintAppointment_Btn;
    // SERVICE DECLARATIONS

    @FXML private AnchorPane serviceForm;
    @FXML private TableView<service> serviceTableView;
    @FXML private TableColumn<service, Integer> service_id;
    @FXML private TableColumn<service, String> service_name;
    @FXML private TableColumn<service, Double> service_price;

    // PROFILE DECLARATIONS

    @FXML private AnchorPane profileForm;
    @FXML private Text profile_Fname;
    @FXML private Text profile_Lname;
    @FXML private Text profile_Phonenum;
    @FXML private Text profile_UserId;
    @FXML private Button profile_changepassButton;
    @FXML private Circle profile_circleImage;
    @FXML private Button profile_importBtn;
    @FXML private TextField profile_editEmail;
    @FXML private TextField profile_editFname;
    @FXML private TextField profile_editLname;
    @FXML private TextField profile_editPhonenum;
    @FXML private ComboBox<String> profile_editStatus;
    @FXML private Text profile_email;
    @FXML private Button profile_saveChanges;
    @FXML private Text profile_status;
    @FXML private ComboBox<String> securityQuestion;
    @FXML private TextField securityQuestionAnswer;
    

    // ACTIVITY LOG
    @FXML private AnchorPane ActivityLogForm;
    @FXML private TableColumn<activitylog, Integer> log_id;
    @FXML private TableColumn<activitylog, Integer> user_id;
    @FXML private TableColumn<activitylog, String> log_date;
    @FXML private TableColumn<activitylog, String> log_action;
    @FXML private TableView<activitylog> table_activitylog;
    @FXML private Button activity_log;

    // Other Declarations
    private Timeline timeline;
    private ObservableList<appointment> appointments =
        FXCollections.observableArrayList();
    @FXML private Pane paneToPrint;
    PanelPrintingController printcontroller = new PanelPrintingController();

    alertMessage alert = new alertMessage();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Main Form
        header_form();
        userdashboard_menu();
        startTimeline();

        // Dashboard

        initializeTableColumnsAppointments();
        retrieveAppointmentsTable();
        updatePatientCounter();
        updateAppointmentCounter();
        loadPendingAppointmentCount();
        loadCompletedAndCancelledPatientCount();
        loadPatientCountByStatus();
        loadAppointmentCountByStatus();

        // Patient Form
        Patientinfo_Gender.getItems().addAll("Male", "Female");

        // Appointment Form
        initializeTableColumns();
        loadAppointments();

        try {
            // Print Form
            configureTableView();
        } catch (SQLException ex) {
            Logger.getLogger(UserDashboardController.class.getName())
                .log(Level.SEVERE, null, ex);
        }
        try {
            propagatePrintPreview();
        } catch (SQLException ex) {
            Logger.getLogger(UserDashboardController.class.getName())
                .log(Level.SEVERE, null, ex);
        }
        // Service Form
        setupServiceTableView();
        loadServiceData();

        // Profile Setting Form
        refreshProfileData();
        profile_editStatus.getItems().addAll("Active", "Inactive");
        initializeQuestion();
        // Activity Log
        loadActivityLogData();
    }

    public void loadAppointmentCountByStatus() {
        try {
            XYChart.Series series = new XYChart.Series();
            series.setName("Appointment Count by Status");

            int pendingCount = connectDB.countPendingAppointments();
            int completedCount = connectDB.countCompletedAppointments();
            int cancelledCount = connectDB.countCancelledAppointments();

            series.getData().add(new XYChart.Data("Pending", pendingCount));
            series.getData().add(new XYChart.Data("Completed", completedCount));
            series.getData().add(new XYChart.Data("Cancelled", cancelledCount));

            dashboad_chart_DD.getData().clear();
            dashboad_chart_DD.getData().add(series);
        } catch (SQLException e) {
            System.out.println(
                "Error loading appointment count by status: " + e.getMessage());
        }
    }

    public void loadPatientCountByStatus() {
        try {
            XYChart.Series series = new XYChart.Series();
            series.setName("Patient Count by Status");

            int activeCount =
                connectDB.countActivePatients(); // Pending appointments
            int inactiveCount =
                connectDB.countInactivePatients(); // Completed and Cancelled
                                                   // appointments

            series.getData().add(new XYChart.Data("Active", activeCount));
            series.getData().add(new XYChart.Data("Inactive", inactiveCount));

            dashboad_chart_PD.getData().clear();
            dashboad_chart_PD.getData().add(series);
        } catch (SQLException e) {
            System.out.println(
                "Error loading patient count by status: " + e.getMessage());
        }
    }

    public void loadCompletedAndCancelledPatientCount() {
        try {
            int count = connectDB.countCompletedAndCancelledPatients();
            dashboard_IP.setText(String.valueOf(count));
        } catch (SQLException e) {
            System.out.println(
                "Error loading completed and cancelled patient count: "
                + e.getMessage());
        }
    }

    public void loadPendingAppointmentCount() {
        try {
            int count = connectDB.countPendingAppointments();
            dashboard_AP.setText(String.valueOf(count));
        } catch (SQLException e) {
            System.out.println(
                "Error loading pending appointment count: " + e.getMessage());
        }
    }

    public void updateAppointmentCounter() {
        try {
            int totalAppointments = connectDB.getTotalAppointments();
            dashboard_tA.setText(String.valueOf(totalAppointments));
        } catch (SQLException e) {
            System.out.println(
                "Error getting total appointments: " + e.getMessage());
        }
    }

    public void updatePatientCounter() {
        try {
            int totalPatients = connectDB.getTotalPatients();
            dashboard_TP.setText(String.valueOf(totalPatients));
        } catch (SQLException e) {
            System.out.println(
                "Error getting total patients: " + e.getMessage());
            // You can also display an error message to the user here
        }
    }
    private void loadServiceData() {
        ObservableList<service> serviceList = connectDB.getServiceData();
        serviceTableView.setItems(serviceList);
    }
    // Header Format Code
    public void header_form() {
        connectDB con = new connectDB();
        Session session = Session.getInstance();
        String fname = session.getFname();
        top_name.setText(fname);
    }

    public void logout_button(ActionEvent event) {
        try {
            if (alert.confirmationMessage("Are you sure you want to logout?")) {
                Session.getInstance().reset();
                Parent root = FXMLLoader.load(getClass().getResource(
                    "/dentalclinicsystem/FXMLDocument.fxml"));
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.initStyle(StageStyle.UNDECORATED);
                stage.show();
                logout_button.getScene().getWindow().hide();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startTimeline() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            LocalTime currentTime = LocalTime.now();
            String formattedTime = currentTime.format(formatter);
            timeLabel.setText(formattedTime);
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }
    // Admin Dashboard Menu Code
    public void userdashboard_menu() {
        connectDB con = new connectDB();
        Session session = Session.getInstance();
        String email = session.getEmail();
        int uid = session.getUid();
        String fname = session.getFname();
        usermenu_email.setText(email);
        usermenu_id.setText(String.valueOf(uid));
    }

    // Switching the form based on the dashboard menu
    private void switchForm(AnchorPane formToShow, String formName) {
        userDashboard.setVisible(false);
        patientForm.setVisible(false);
        appointmentForm.setVisible(false);
        profileForm.setVisible(false);
        serviceForm.setVisible(false);
        ActivityLogForm.setVisible(false);
        PrintForm.setVisible(false);

        formToShow.setVisible(true);
        current_form.setText(formName + " Form");
    }

    @FXML
    private void switchForm(ActionEvent event) {
        if (event.getSource() == dashboard_button) {
            switchForm(userDashboard, "Dashboard");
            userdashboard_menu();
            header_form();
        } else if (event.getSource() == patients_button) {
            switchForm(patientForm, "Patient's");
        } else if (event.getSource() == appointments_button) {
            switchForm(appointmentForm, "Appointment's");
        } else if (event.getSource() == service_btn) {
            switchForm(serviceForm, "Service");
        } else if (event.getSource() == profile_button) {
            switchForm(profileForm, "Profile");
        } else if (event.getSource() == activity_log) {
            switchForm(ActivityLogForm, "Activity Log");
        } else if (event.getSource() == print_btn) { // Add this line
            switchForm(PrintForm, "Print");
        }
    }

    // Dashboard

    private void initializeTableColumnsAppointments() {
        dashboad_col_appointmentSchedule.setCellValueFactory(
            new PropertyValueFactory<>("appointmentSchedule"));
        dashboad_col_appointmentID.setCellValueFactory(
            new PropertyValueFactory<>("appointmentID"));
        dashboad_col_name.setCellValueFactory(
            new PropertyValueFactory<>("patientName"));
        dashboad_col_status.setCellValueFactory(
            new PropertyValueFactory<>("status"));
    }

    private void retrieveAppointmentsTable() {
        try {
            List<appointment> appointments = new ArrayList<>();
            String query =
                "SELECT a.AppointmentID, a.AppointmentSchedule, a.Status, "
                + "p.FirstName, p.LastName "
                + "FROM tbl_appointment a "
                + "JOIN tbl_patient p ON a.PatientID = p.PatientID";

            try (Connection connect = connectDB.getConnection();
                 Statement stmt = connect.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {
                while (rs.next()) {
                    appointment appointment =
                        new appointment(rs.getInt("AppointmentID"),
                            0, // PatientID
                            rs.getString("FirstName") + " "
                                + rs.getString("LastName"),
                            "", // Gender
                            "", // ContactNumber
                            0, // ServiceID
                            "", // ServiceName
                            rs.getString("AppointmentSchedule"),
                            rs.getString("Status"),
                            "" // Notes
                        );
                    appointments.add(appointment);
                }
            }
            dashboad_tableView.getItems().addAll(appointments);
        } catch (SQLException e) {
            new alertMessage().errorMessage(
                "Error loading appointments: " + e.getMessage());
        }
    }

    // PATIENT FORM
    // Method to initialize the bindings and button actions
    private void initializeBindingsAndActions() {
        // Bind the text fields bidirectionally
        Patientinfoadd_Name.textProperty().bindBidirectional(
            patientInfo.fullNameProperty());
        Patientinfoadd_Gender.textProperty().bindBidirectional(
            patientInfo.genderProperty());
        Patientinfoadd_ContactNumber.textProperty().bindBidirectional(
            patientInfo.contactNumberProperty());
        Patientinfoadd_Address.textProperty().bindBidirectional(
            patientInfo.addressProperty());

        // Set the confirm button action
        Patientinfo_Confirmbtn.setOnAction(
            event -> handleConfirmButtonAction());

        // Set the add button action
        Patientinfoadd_AddBtn.setOnAction(event -> handleAddButtonAction());
    }

    // Handle the confirm button action
    @FXML
    private void handleConfirmButtonAction() {
        // Get the patient information from the form
        String fullName = Patientinfo_FirstName.getText().trim() + " "
            + Patientinfo_LastName.getText().trim();
        String gender = Patientinfo_Gender.getValue();
        String contactNumber = Patientinfo_ContactNumber.getText().trim();
        String address = Patientinfo_Address.getText().trim();

        // Validate input
        if (!fullName.matches("[a-zA-Z ]+")) {
            alert.errorMessage("Name should only contain letters and spaces.");
            return;
        }

        if (gender == null || gender.isEmpty()) {
            alert.errorMessage("Please select a gender.");
            return;
        }

        if (!contactNumber.matches("\\d{10,12}")) {
            alert.errorMessage("Contact number should be 10-12 digits.");
            return;
        }

        if (!address.matches("^[a-zA-Z\\s,.-]+$")) {
            alert.errorMessage("Address should only contain letters, "
                + "spaces, commas, periods, "
                + "and hyphens. No numbers allowed.");
            return;
        }

        // Check for null values and set the text of the labels
        if (Patientinfoadd_Name != null) {
            Patientinfoadd_Name.setText(fullName);
        }
        if (Patientinfoadd_Gender != null) {
            Patientinfoadd_Gender.setText(gender);
        }
        if (Patientinfoadd_ContactNumber != null) {
            Patientinfoadd_ContactNumber.setText(contactNumber);
        }
        if (Patientinfoadd_Address != null) {
            Patientinfoadd_Address.setText(address);
        }
        try {
        connectDB.insertLogEntry(Session.getInstance().getUid(),
            "Patient information updated for " + fullName);
    } catch (SQLException e) {
        System.out.println("Error adding log entry: " + e.getMessage());
    }
    }

    @FXML
    private void handleViewAllPatientRecordButtonAction() {
        try {
            FXMLLoader loader =
                new FXMLLoader(getClass().getResource("allPatientRecord.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            new alertMessage().errorMessage(
                "Error loading all patient records: " + e.getMessage());
        }
    }

    // Handle the add button action
    @FXML
    private void handleAddButtonAction() {
        // Get the patient information from the form
        String firstName = Patientinfo_FirstName.getText().trim();
        String lastName = Patientinfo_LastName.getText().trim();
        String gender = Patientinfo_Gender.getValue();
        String contactNumber = Patientinfo_ContactNumber.getText().trim();
        String address = Patientinfo_Address.getText().trim();

        // Create a new patient object
        patient newPatient =
            new patient(0, // Patient ID will be generated automatically
                firstName, lastName, contactNumber, gender,
                java.time.LocalDate.now().toString(), // Current date
                java.time.LocalTime.now().toString(), // Current time
                address);

        try {
            // Add the patient to the database
            int patientID = connectDB.addPatient(newPatient);
            System.out.println(
                "Patient added successfully with ID: " + patientID);
            connectDB.insertLogEntry(
                Session.getInstance().getUid(), "Added new patient");
        } catch (SQLException e) {
            System.out.println("Error adding patient: " + e.getMessage());
        }
        updatePatientCounter();
        // Clear the form fields
        Patientinfo_FirstName.clear();
        Patientinfo_LastName.clear();
        Patientinfo_Gender.getSelectionModel().clearSelection();
        Patientinfo_ContactNumber.clear();
        Patientinfo_Address.clear();
        Patientinfoadd_Name.setText("");
        Patientinfoadd_Gender.setText("");
        Patientinfoadd_ContactNumber.setText("");
        Patientinfoadd_Address.setText("");
    }

    // APPOINTMENT FORM

    private void initializeTableColumns() {
        appointments_col_appointmentID.setCellValueFactory(
            new PropertyValueFactory<>("appointmentID"));
        appointments_col_contactNumber.setCellValueFactory(
            new PropertyValueFactory<>("contactNumber"));
        appointments_col_schedule.setCellValueFactory(
            new PropertyValueFactory<>("appointmentSchedule"));
        appointments_col_treatment_service.setCellValueFactory(
            new PropertyValueFactory<>("serviceName"));
        appointments_col_gender.setCellValueFactory(
            new PropertyValueFactory<>("gender"));
        appointments_col_name.setCellValueFactory(
            new PropertyValueFactory<>("patientName"));
        appointments_col_status.setCellValueFactory(
            new PropertyValueFactory<>("status"));
    }

    public void loadAppointments() {
        try {
            appointments.clear();
            appointments.addAll(connectDB.getPendingAppointments());
            appointments_tableView.setItems(appointments);
        } catch (SQLException e) {
            System.out.println("Error loading appointments: " + e.getMessage());
        }
    }

    @FXML
    private void handleInsertButtonAction(ActionEvent event)
        throws SQLException {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("insertAppointment.fxml"));
            Parent root = loader.load();
            InsertAppointmentController controller = loader.getController();
            controller.setUserdashboardcontroller(
                this); // Pass the current instance
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();
            connectDB.insertLogEntry(
                Session.getInstance().getUid(), "Inserted new appointment");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleUpdateButtonAction(ActionEvent event)
        throws SQLException {
        try {
            FXMLLoader loader =
                new FXMLLoader(getClass().getResource("editAppointment.fxml"));
            Parent root = loader.load();
            EditAppointmentController controller = loader.getController();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();
            connectDB.insertLogEntry(
                Session.getInstance().getUid(), "Updated appointment");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeleteButtonAction(ActionEvent event) {
        // Get the selected appointment
        appointment selectedAppointment =
            appointments_tableView.getSelectionModel().getSelectedItem();

        if (selectedAppointment != null) {
            // Confirm deletion
            if (alert.confirmationMessage(
                    "Are you sure you want to delete this appointment?")) {
                try {
                    // Delete the appointment from the database
                    connectDB.deleteAppointment(
                        selectedAppointment.getAppointmentID());

                    // Remove the appointment from the table view
                    appointments_tableView.getItems().remove(
                        selectedAppointment);

                    // Refresh the table view
                    loadAppointments();

                    alert.successMessage("Appointment deleted successfully!");
                    connectDB.insertLogEntry(
                        Session.getInstance().getUid(), "Deleted appointment");
                } catch (SQLException e) {
                    alert.errorMessage(e.getMessage());
                }
            }
        } else {
            alert.errorMessage("Please select an appointment to delete.");
        }
    }

   @FXML
private void handleSearchByDateButtonAction(ActionEvent event) {
    LocalDate selectedDate = srchDayAppointment.getValue();
    if (selectedDate != null) {
        try {
            appointments.clear();
            connectDB db = new connectDB();
            appointments.addAll(db.getAppointmentsBySchedule(selectedDate.toString()));
            appointments_tableView.setItems(appointments);
        } catch (SQLException e) {
            System.out.println("Error loading appointments by date: " + e.getMessage());
        }
    } else {
        loadAppointments(); // Load all appointments if no date is selected
    }
}

private boolean isFiltered = false;

@FXML
private void handleCurrentDayAppointmentAction(MouseEvent event) {
    try {
        appointments.clear();
        connectDB db = new connectDB();
        if (isFiltered) {
            loadAppointments(); // Load all appointments
            isFiltered = false;
        } else {
            appointments.addAll(db.getAppointmentsBySchedule(LocalDate.now().toString()));
            appointments_tableView.setItems(appointments);
            isFiltered = true;
        }
    } catch (SQLException e) {
        System.out.println("Error loading appointments: " + e.getMessage());
    }
}
    // PRINT FORM

@FXML
private void handlePrintAppointmentBtnAction(ActionEvent event) throws SQLException {
    appointment selectedAppointment = PrintAppointment_TableView.getSelectionModel().getSelectedItem();
    if (selectedAppointment != null) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/config/panelPrinting.fxml"));
            Parent root = loader.load();
            PanelPrintingController controller = loader.getController();
            controller.setSelectedAppointment(selectedAppointment);
            controller.propagatePrint(); // Call propagatePrint after setting the selected appointment
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.show();
            connectDB.insertLogEntry(Session.getInstance().getUid(),
                "Appointment printed successfully for patient " + selectedAppointment.getPatientName());
        } catch (IOException e) {
            alert.errorMessage("Error loading print panel: " + e.getMessage());
        }
    } else {
        alert.errorMessage("Please select an appointment to print.");
    }
}

public void configureTableView() throws SQLException {
    // Retrieve pending appointments from the database
    ObservableList<appointment> appointments = connectDB.getPendingAppointments();

    // Set the data for the table view
    PrintAppointment_TableView.setItems(appointments);

    // Set the cell value factories for the table columns
    PrintAPPID_Col.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
    PrintPName_Col.setCellValueFactory(new PropertyValueFactory<>("patientName"));
    PrintGender_Col.setCellValueFactory(new PropertyValueFactory<>("gender"));
    PrintContactNumber_Col.setCellValueFactory(new PropertyValueFactory<>("contactNumber"));
    PrintPrice_Col.setCellValueFactory(new PropertyValueFactory<>("price"));

    // Add a listener to the selectedItemProperty
    PrintAppointment_TableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
        if (newValue != null) {
            try {
                propagatePrintPreview();
                printcontroller.propagatePrint();
            } catch (SQLException ex) {
                Logger.getLogger(UserDashboardController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            // Clear the print preview and print controller when selection is cleared
            clearPrintPreview();
            printcontroller.clearPrint();
        }
    });
}

public void clearPrintPreview() {
    PrintPreviewAPPID.setText("");
    PrintPreviewPname.setText("");
    PrintPreviewSchedule.setText("");
    PrintPreviewServiceAvail.setText("");
    PrintPreviewFee.setText("");
}

public void propagatePrintPreview() throws SQLException {
    appointment selectedAppointment = PrintAppointment_TableView.getSelectionModel().getSelectedItem();
    if (selectedAppointment != null) {
        // Update the labels with the selected appointment's values
        PrintPreviewAPPID.setText(String.valueOf(selectedAppointment.getAppointmentID()));
        PrintPreviewPname.setText(selectedAppointment.getPatientName());
        PrintPreviewSchedule.setText(selectedAppointment.getAppointmentSchedule());
        PrintPreviewServiceAvail.setText(selectedAppointment.getServiceName());

        // Retrieve the price through the ServiceID
connectDB db = new connectDB();
int serviceID = selectedAppointment.getServiceID();
System.out.println("Service ID: " + serviceID);
double price = db.getPriceByServiceID(serviceID);
System.out.println("Price: " + price);
PrintPreviewFee.setText(String.valueOf(price));
    } else {
        clearPrintPreview();
    }
}

    @FXML
    private void handleGeneratePdfButtonAction() {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/config/panelPrinting.fxml"));
            Node node = loader.load();
            PanelPrinter pdfGenerator = new PanelPrinter();
            pdfGenerator.generatePdf(node, "output.pdf");
        } catch (IOException e) {
            System.err.println("Error generating PDF: " + e.getMessage());
        }
    }
    // SERVICE FORM
    private void setupServiceTableView() {
        service_id.setCellValueFactory(
            new PropertyValueFactory<>("service_id"));
        service_name.setCellValueFactory(
            new PropertyValueFactory<>("service_name"));
        service_price.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    // PROFILE SETTING FORM

    private void refreshProfileData() {
        Session session = Session.getInstance();
        profile_UserId.setText(String.valueOf(session.getUid()));
        profile_Fname.setText(session.getFname());
        profile_Lname.setText(session.getLname());
        profile_Phonenum.setText(String.valueOf(
            session.getPhone_num())); // This should work if phonenum is an int
        profile_email.setText(session.getEmail());
        profile_status.setText(session.getStatus());

        loadProfileImage(session.getUid());

        // Set editable fields
        profile_editFname.setText(session.getFname());
        profile_editLname.setText(session.getLname());
        profile_editPhonenum.setText(String.valueOf(
            session.getPhone_num())); // This should work if phonenum is an int
        profile_editEmail.setText(session.getEmail());
        profile_editStatus.setValue(session.getStatus());
    }

    // Handle save changes button action
   @FXML
private void handleSaveChanges(ActionEvent event) {
    String fname = profile_editFname.getText().trim();
    String lname = profile_editLname.getText().trim();
    String phonenum = profile_editPhonenum.getText().trim();
    String email = profile_editEmail.getText().trim();
    String status = profile_editStatus.getValue();
    String securityAnswer = securityQuestionAnswer.getText().trim();

    // Validate input
    if (fname.isEmpty() || lname.isEmpty() || phonenum.isEmpty()
        || email.isEmpty() || status == null) {
        alert.errorMessage("All fields must be filled out.");
        return;
    }

    // Additional validation for phone number format (optional)
    if (!phonenum.matches(
            "\\d{10}")) { // Example: Check if phone number is 10 digits
        alert.errorMessage("Phone number must be 10 digits.");
        return;
    }

    // Additional validation for email format (optional)
    if (!email.matches(
            "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) { // Simple email
        // regex
        alert.errorMessage("Please enter a valid email address.");
        return;
    }
    connectDB db = new connectDB();
// Update security question answer
SecurityQuestion securityQuestion = new SecurityQuestion(
    0, Session.getInstance().getUid(), securityAnswer);
db.updateSecurityQuestion(securityQuestion);
    // Update the staff in the database
    try {
        int uid =
            Session.getInstance().getUid(); // Get the current user's ID
        connectDB.updateUser(uid, fname, lname, phonenum, email,
            status); // Call the update method
        refreshProfileData(); // Reload the profile data to reflect
                              // changes
        header_form();
        alert.successMessage("Profile updated successfully!");
        connectDB.insertLogEntry(
            Session.getInstance().getUid(), "Updated profile information");
    } catch (Exception e) {
        alert.errorMessage(e.getMessage());
    }
}

    @FXML
    private void handleChangePassword(ActionEvent event) throws SQLException {
        // Logic to change password
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/dentalclinicsystem/changePassword.fxml"));
            Parent root = loader.load();
            // Create a new stage (window)
            Stage stage = new Stage();
            // Set the scene with the loaded FXML
            Scene scene = new Scene(root);
            stage.setScene(scene);
            // Set the preferred width and height
            stage.setWidth(429); // Set your desired width
            stage.setHeight(405); // Set your desired height
            // Make the window undecorated
            stage.initStyle(StageStyle.UNDECORATED);
            // Show the stage
            stage.show();
            connectDB.insertLogEntry(Session.getInstance().getUid(),
                "Opened change password window");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleProfileImportButton(ActionEvent event)
        throws SQLException {
        // Create a FileChooser to select an image file
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Profile Image");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter(
                "Image Files", "*.jpg", "*.jpeg", "*.png", "*.gif"));

        // Show the FileChooser and get the selected file
        File selectedImageFile = fileChooser.showOpenDialog(
            profile_importBtn.getScene().getWindow());

        // Check if a file was selected
        if (selectedImageFile != null) {
            // Display the selected image in the Circle
            displaySelectedImage(selectedImageFile);
            // Upload the image to the database
            uploadProfileImage(selectedImageFile);
            connectDB.insertLogEntry(
                Session.getInstance().getUid(), "Updated profile image");
        }
    }

    // Method to display the selected image in the Circle
    private void displaySelectedImage(File selectedImageFile) {
        // Create an Image object from the selected file
        Image image = new Image(selectedImageFile.toURI().toString());

        // Create a ImageView object to display the image
        ImageView imageView = new ImageView(image);

        // Set the width and height of the ImageView to match the Circle
        imageView.setFitWidth(profile_circleImage.getRadius() * 2);
        imageView.setFitHeight(profile_circleImage.getRadius() * 2);

        // Clip the ImageView to the shape of the Circle
        Circle clip = new Circle(profile_circleImage.getCenterX(),
            profile_circleImage.getCenterY(), profile_circleImage.getRadius());
        imageView.setClip(clip);

        // Add the ImageView to the Circle
        profile_circleImage.setFill(
            new ImagePattern(imageView.snapshot(null, null)));
    }

    // Method to upload the profile image to the database
    private void uploadProfileImage(File selectedImageFile) {
        try {
            // Read the image file into a byte array
            byte[] imageBytes = Files.readAllBytes(selectedImageFile.toPath());

            // Connect to database
            try (Connection connect = connectDB.getConnection()) {
                // Update tbl_profileimage
                try (PreparedStatement pstmt1 = connect.prepareStatement(
                         "UPDATE tbl_profileimage SET ImageFile = ? WHERE "
                         + "UserId = ?")) {
                    pstmt1.setBytes(1, imageBytes);
                    pstmt1.setInt(2, Session.getInstance().getUid());
                    pstmt1.executeUpdate();
                }

                // Update tbl_user
                try (PreparedStatement pstmt2 = connect.prepareStatement(
                         "UPDATE tbl_user SET profileimage = ? WHERE u_id = "
                         + "?")) {
                    pstmt2.setInt(1,
                        getProfileImageId(
                            Session.getInstance()
                                .getUid())); // Get the ImageId from
                    // tbl_profileimage
                    pstmt2.setInt(2, Session.getInstance().getUid());
                    pstmt2.executeUpdate();

                    connectDB.insertLogEntry(Session.getInstance().getUid(),
                        "Updated profile image in tbl_user");
                }
            }
        } catch (SQLException | IOException e) {
            alert.errorMessage(e.getMessage());
        }
    }

    // Method to load the profile image
    private void loadProfileImage(int userId) {
        try (Connection connect = connectDB.getConnection();
             PreparedStatement pstmt = connect.prepareStatement(
                 "SELECT ImageFile FROM tbl_profileimage WHERE UserId = ?")) {
            pstmt.setInt(1, userId);
            ResultSet resultSet = pstmt.executeQuery();

            if (resultSet.next()) {
                byte[] imageBytes = resultSet.getBytes("ImageFile");
                if (imageBytes != null) {
                    Image image =
                        new Image(new ByteArrayInputStream(imageBytes));
                    profile_circleImage.setFill(new ImagePattern(image));
                }
            }
        } catch (SQLException e) {
            alert.errorMessage(e.getMessage());
        }
    }

    // Method to get the ImageId from tbl_profileimage
    private int getProfileImageId(int userId) throws SQLException {
        String query = "SELECT ImageId FROM tbl_profileimage WHERE UserID = ?";
        try (Connection connect = connectDB.getConnection();
             PreparedStatement pstmt = connect.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("ImageId");
            } else {
                return -1; // Return -1 if no image found
            }
        }
    }
    
    
    private void initializeQuestion(){
   
    securityQuestion.getItems().addAll(
        "What is your birthyear?",
        "What is your favorite food?",
        "What is your favorite color?"
    );

    securityQuestion.getSelectionModel().select(0);

    
}
    
  // ACTIVITY LOG
    private void loadActivityLogData() {
        log_id.setCellValueFactory(new PropertyValueFactory<>("logId"));
        user_id.setCellValueFactory(new PropertyValueFactory<>("userId"));
        log_date.setCellValueFactory(new PropertyValueFactory<>("logDate"));
        log_action.setCellValueFactory(new PropertyValueFactory<>("logAction"));

        try {
            table_activitylog.setItems(connectDB.getUserLogData());
        } catch (SQLException e) {
            System.out.println(
                "Error loading activity log data: " + e.getMessage());
        }
    }

    @FXML
    private void handlePrintAppointmentButtonAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/config/panelPrinting.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
      @FXML
    private void invidualPatientRecord(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("patientHistory.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
           stage.initStyle(StageStyle.UNDECORATED);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
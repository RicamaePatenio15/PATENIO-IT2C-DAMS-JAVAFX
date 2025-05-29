package admin;

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
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import models.SecurityQuestion;
import models.appointment;
import models.patient;
import models.service;
import models.staff;

public class AdminDashboardController implements Initializable {
    // FXML DECLARATIONS
    // MAIN FORM DECLARATIONS

    @FXML private AnchorPane header_form;
    @FXML private Label topFname;
    @FXML private Circle topProfile;
    @FXML private AnchorPane admindashboard_menu;
    @FXML private Label dashmenu_email;
    @FXML private Label dashmenu_id;
    @FXML private AnchorPane dentalappoint_signage;
    @FXML private Label currentForm;
    @FXML private Button logout_button;
    @FXML private Label timeLabel;
    @FXML private Button dashboard_button;
    @FXML private Button staff_button;
    @FXML private Button patients_button;
    @FXML private Button appointments_button;
    @FXML private Button service_btn;
    @FXML private Button payment_button;
    @FXML private Button profile_button;
    @FXML private AnchorPane Formview;
    @FXML private AnchorPane PatientsInfo_Forn;
    @FXML private AnchorPane StaffForm_table;
    @FXML private AnchorPane admindash_lower;
    @FXML private AnchorPane admindash_upper;
    @FXML private AnchorPane appointmentform;
    @FXML private Button activity_log;

    // DASHBOARD DECLARATIONS

    @FXML private AnchorPane dashboard_form;
    @FXML private Label dashboard_AP;
    @FXML private Label dashboard_TP;
    @FXML private Label dashboard_tA;
    @FXML private TableView<staff> dashboardTableView;
    @FXML private TableColumn<staff, Integer> dashboard_col_UID;
    @FXML private TableColumn<staff, String> dashboard_col_name;
    @FXML private TableColumn<staff, String> dashboard_col_status;
    @FXML private TableColumn<staff, String> dashboard_col_email;
    @FXML private AreaChart<String, Number> dashboard_chartTP;
    @FXML private BarChart<String, Number> dashboard_chartTU;

    // STAFF DECLARATIONS

    @FXML private AnchorPane staffForm;
    @FXML private TableView<staff> staffTableView;
    @FXML private TableColumn<staff, Integer> staff_col_staffID;
    @FXML private TableColumn<staff, String> staff_col_emailaddress;
    @FXML private TableColumn<staff, String> staff_col_fname;
    @FXML private TableColumn<staff, String> staff_col_lname;
    @FXML private TableColumn<staff, String> staff_col_phonenum;
    @FXML private TableColumn<staff, String> staff_col_status;
    @FXML private TableColumn<staff, Void> staff_col_actions;
    @FXML private FontAwesomeIcon addStaff_button;
    @FXML private FontAwesomeIcon refreshstaff_button;
    @FXML private FontAwesomeIcon filterStaff_button;
    @FXML private TextField search_textfield;

    // PATIENT DECLARATIONS

    @FXML private AnchorPane patientsForm;
    @FXML private TableView<patient> adminpatientsTableView;
    @FXML private TableColumn<patient, Integer> patients_col_patientID;
    @FXML private TableColumn<patient, String> patients_col_name;
    @FXML private TableColumn<patient, String> patients_col_contactNumber;
    @FXML private TableColumn<patient, String> patients_col_date;
    @FXML private Button PhistoryRecord_Btn;
    // APPOINTMENT DECLARATIONS

    @FXML private TableView<appointment> adminappointmentsTableView;
    @FXML private TableColumn<appointment, String> adminAppointmentName_Col;
    @FXML private TableColumn<appointment, String> adminTreatment_Col;
    @FXML private TableColumn<appointment, String> adminAppointmentSchedule_Col;
    @FXML private TableColumn<appointment, String> adminAppointmentStatus_Col;
    @FXML private TableColumn<appointment, Integer> adminAppointmentID_Col;
    @FXML private AnchorPane appointmentsForm;
    @FXML private DatePicker srchDayAppointment;
    @FXML private FontAwesomeIcon currentDayAppointment;

    // SERVICE DECLARATIONS

    @FXML private AnchorPane serviceForm;
    @FXML private TableView<service> serviceTableViewadmin;
    @FXML private TableColumn<service, Integer> service_idadmin;
    @FXML private TableColumn<service, String> service_nameadmin;
    @FXML private TableColumn<service, Double> service_priceadmin;
    @FXML private TableColumn<service, Void> service_actionadmin;
    @FXML private FontAwesomeIcon addservices_btn;
    @FXML private FontAwesomeIcon refreshservices_btn;
    @FXML private TextField srchservices;

    // PROFILE SETTING DECLARATIONS

    @FXML private Button act_log;
    @FXML private AnchorPane profileForm;
    @FXML private Circle profile_circle;
    @FXML private Button profile_importBtn;
    @FXML private Text profile_AdminId;
    @FXML private Text profile_Fname;
    @FXML private Text profile_Lname;
    @FXML private Text profile_Phonenum;
    @FXML private Text profile_email;
    @FXML private Text profile_status;
    @FXML private TextField profile_editFname;
    @FXML private TextField profile_editLname;
    @FXML private TextField profile_editPhonenum;
    @FXML private TextField profile_editEmail;
    @FXML private ComboBox<String> profile_editStatus;
    @FXML private Button profile_changepassButton;
    @FXML private Button profile_saveChanges;
     @FXML private ComboBox<String> adminsecurityQuestion;
    @FXML private TextField adminsecurityQuestionAnswer;
    // @FXML private FontAwesomeIcon back_icon;

    // Other Declarations

    private alertMessage alert = new alertMessage();
    private FilteredList<staff> filteredData;
    private final ObservableList<staff> dataList =
        FXCollections.observableArrayList();
    private boolean isAscending = true; // Flag to track sorting order
    private SortedList<staff> sortedData;
    private Timeline timeline;
    private int userID;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Session session = Session.getInstance();
        userID = session.getUid();

        // Main Form
        admindashboard_menu();
        header_form();
        startTimeline();

        // Dashboard Form
        updateTotalPatientsLabel();
        updateActivePatientsLabel();
        updateTotalAppointmentsLabel();
        try {
            loadPatientCount();
        } catch (SQLException ex) {
            Logger.getLogger(AdminDashboardController.class.getName())
                .log(Level.SEVERE, null, ex);
        }
        try {
            loadTotalUserCountChart();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        profile_editStatus.getItems().addAll("Active", "Inactive");
        setupDashboardTable();

        // Staff
        propagateStaffTable();
        setupAddStaffButton();
        setupRefreshStaffButton();
        setupStaffActions();
        setupSearchFilter();
        setupSortButton();
        setupStaffTable();

        // Patient
        initializePatientsTableView();

        // Appointment
        initializeTableColumns();
        initializeAppointmentsTableView();
        // Service
        propagateServiceData();
        setupServiceActions();
        RefreshServiceButton();
        setupAddServiceButton();
        setupSearchFilterForServices();
        loadServiceData();

        // Profile Setting Form
        loadProfileData();
        initializeQuestion();
    }

    // MAIN FORM CODE

    public void header_form() {
        connectDB con = new connectDB();
        Session session = Session.getInstance();
        String fname = session.getFname();
        topFname.setText(fname);
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
    public void admindashboard_menu() {
        connectDB con = new connectDB();
        Session session = Session.getInstance();
        String email = session.getEmail();
        int uid = session.getUid();
        String fname = session.getFname();
        dashmenu_email.setText(email);
        dashmenu_id.setText(String.valueOf(uid));
    }

    // Switching the form based on the dashboard menu
    private void switchForm(AnchorPane formToShow, String formName) {
        dashboard_form.setVisible(false);
        staffForm.setVisible(false);
        patientsForm.setVisible(false);
        appointmentsForm.setVisible(false);
        serviceForm.setVisible(false); // Add this line
        profileForm.setVisible(false);
        formToShow.setVisible(true);
        currentForm.setText(formName + " Form");
    }

    @FXML
    private void switchForm(ActionEvent event) {
        if (event.getSource() == dashboard_button) {
            switchForm(dashboard_form, "Dashboard");
            admindashboard_menu();
            header_form();
        } else if (event.getSource() == staff_button) {
            switchForm(staffForm, "Staff");
            propagateStaffTable();
        } else if (event.getSource() == patients_button) {
            switchForm(patientsForm, "Patient's");
        } else if (event.getSource() == appointments_button) {
            switchForm(appointmentsForm, "Appointment's");
        } else if (event.getSource() == service_btn) {
            switchForm(serviceForm, "Service");
        } else if (event.getSource() == profile_button) {
            switchForm(profileForm, "Profile");
        }
    }

    // DASHBOARD FORM CODE

    // Populate Dashboard Table
    public void propagateDashboardTable() {
        ObservableList<staff> staffList = connectDB.getDatastaff();
        dashboardTableView.getItems().clear();
        dashboardTableView.setItems(staffList);
    }

    public void updateTotalPatientsLabel() {
        try {
            int totalPatients = connectDB.getTotalPatients();
            dashboard_TP.setText(String.valueOf(totalPatients));
        } catch (SQLException e) {
            System.err.println(
                "Error retrieving total patients: " + e.getMessage());
        }
    }
    // Method to update the active patients label
    public void updateActivePatientsLabel() {
        try {
            int activePatients = connectDB.countActivePatients();
            dashboard_AP.setText(String.valueOf(activePatients));
        } catch (SQLException e) {
            System.err.println(
                "Error retrieving active patients: " + e.getMessage());
        }
    }
    // Method to update the total appointments label
    public void updateTotalAppointmentsLabel() {
        try {
            int totalAppointments = connectDB.getTotalAppointments();
            dashboard_tA.setText(String.valueOf(totalAppointments));
        } catch (SQLException e) {
            System.err.println(
                "Error retrieving total appointments: " + e.getMessage());
        }
    }

    private void loadPatientCount() throws SQLException {
        int totalPatients = connectDB.getTotalPatients();

        // Create a series for the chart
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Total Patients");

        // Add data to the series
        series.getData().add(new XYChart.Data<>("Total", totalPatients));

        // Add the series to the chart
        dashboard_chartTP.getData().addAll(series);
    }

    // Method to load total user count chart from database
    private void loadTotalUserCountChart() throws SQLException {
        // Clear the chart data
        dashboard_chartTU.getData().clear();

        // Create a series for the chart
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Total Users");

        // Retrieve the total number of admin users and normal users from the
        // database
        int totalAdminUsers = connectDB.countAdminUsers();
        int totalNormalUsers = connectDB.countNormalUsers();

        // Add data to the series
        series.getData().add(new XYChart.Data<>("Admin", totalAdminUsers));
        series.getData().add(new XYChart.Data<>("User", totalNormalUsers));

        // Add the series to the chart
        dashboard_chartTU.getData().addAll(series);
    }

    // STAFF FORM CODE

    // Propagate Staff Table
    @FXML
    public void propagateStaffTable() {
        ObservableList<staff> staffList = connectDB.getDatastaff();
        dataList.clear();
        dataList.addAll(staffList);
        staffTableView.setItems(dataList);
    }

    // Sort Staff Table
    private void sortStaffTable() {
        // Create a new SortedList with the current filtered data
        SortedList<staff> newSortedData = new SortedList<>(filteredData);
        // Toggle sorting order
        if (isAscending) {
            newSortedData.setComparator(
                (s1, s2) -> Integer.compare(s1.getUid(), s2.getUid()));
        } else {
            newSortedData.setComparator(
                (s1, s2) -> Integer.compare(s2.getUid(), s1.getUid()));
        }
        // Update the table view with the new sorted data
        staffTableView.setItems(newSortedData);
        isAscending = !isAscending; // Toggle the sorting order
    }

    // Handle the delete function in staff
    private void deleteStaff(staff selectedStaff) {
        if (alert.confirmationMessage(
                "Are you sure you want to delete this staff?")) {
            connectDB.deleteStaff(selectedStaff.getUid());
            propagateDashboardTable();
        }
    }

    private void viewStaffDetails(staff selectedStaff) {
        try {
            FXMLLoader loader =
                new FXMLLoader(getClass().getResource("viewStaff.fxml"));
            Parent root = loader.load();
            ViewStaffController viewStaffController = loader.getController();
            viewStaffController.setStaff(
                selectedStaff); // Pass the selected staff to the controller

            // Get the current stage (the window that contains the button)
            Stage stage =
                (Stage) dashboardTableView.getScene()
                    .getWindow(); // Use the table view's scene to get the stage

            Stage detailStage = new Stage();
            detailStage.initStyle(StageStyle.UNDECORATED);
            detailStage.initModality(Modality.WINDOW_MODAL);
            detailStage.initOwner(stage); // Set the owner to the current stage
            Scene scene = new Scene(root);
            detailStage.setScene(scene);
            detailStage.setTitle("View Staff Details");
            detailStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Setup Dashboard Table
    private void setupDashboardTable() {
        dashboard_col_UID.setCellValueFactory(
            new PropertyValueFactory<>("uid"));
        dashboard_col_name.setCellValueFactory(
            new PropertyValueFactory<>("Fname"));
        dashboard_col_email.setCellValueFactory(
            new PropertyValueFactory<>("email"));
        dashboard_col_status.setCellValueFactory(
            new PropertyValueFactory<>("status"));
        propagateDashboardTable();
    }

    // Setup Staff Table
    private void setupStaffTable() {
        staff_col_staffID.setCellValueFactory(
            new PropertyValueFactory<>("uid"));
        staff_col_fname.setCellValueFactory(
            new PropertyValueFactory<>("Fname"));
        staff_col_lname.setCellValueFactory(
            new PropertyValueFactory<>("Lname"));
        staff_col_phonenum.setCellValueFactory(
            new PropertyValueFactory<>("phonenum"));
        staff_col_emailaddress.setCellValueFactory(
            new PropertyValueFactory<>("email"));
        staff_col_status.setCellValueFactory(
            new PropertyValueFactory<>("status"));

        setupStaffActions();
        propagateStaffTable();
    }

    // Setup Add Staff Button
    private void setupAddStaffButton() {
        addStaff_button.setOnMouseClicked(event -> {
            try {
                FXMLLoader loader =
                    new FXMLLoader(getClass().getResource("addStafff.fxml"));
                Parent root = loader.load();
                AddStaffController addStaffController = loader.getController();
                addStaffController.setAdminDashboardController(this);
                Stage stage = new Stage();
                stage.initStyle(StageStyle.UNDECORATED);
                stage.initModality(Modality.WINDOW_MODAL);
                stage.initOwner(addStaff_button.getScene().getWindow());
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("Add Staff");
                stage.showAndWait();
                connectDB.insertLogEntry(userID, "Added a new staff member");
            } catch (IOException e) {
                System.out.println(
                    "Error loading add staff form: " + e.getMessage());
            } catch (SQLException ex) {
                Logger.getLogger(AdminDashboardController.class.getName())
                    .log(Level.SEVERE, null, ex);
            }
        });
    }

    // Setup Refresh Staff Button
    private void setupRefreshStaffButton() {
        refreshstaff_button.setOnMouseClicked(
            event -> { propagateStaffTable(); });
    }
    private void setupStaffActions() {
        staff_col_actions.setCellFactory(column
            -> new TableCell<staff, Void>() {
            private final HBox hBox = new HBox();
            private final Button viewButton = new Button("View");
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");

            {
                staff_col_actions.setPrefWidth(250);
                // Set button styles
                viewButton.setStyle("-fx-cursor: hand; -fx-background-color: "
                    + "#2196F3; -fx-text-fill: white;");
                editButton.setStyle("-fx-cursor: hand; -fx-background-color: "
                    + "#00E676; -fx-text-fill: white;");
                deleteButton.setStyle("-fx-cursor: hand; -fx-background-color: "
                    + "#ff1744; -fx-text-fill: white;");

                // Set button widths
                viewButton.setPrefWidth(70);
                editButton.setPrefWidth(70);
                deleteButton.setPrefWidth(70);
                hBox.setAlignment(Pos.CENTER);
                hBox.setSpacing(5); // Space between buttons
                hBox.getChildren().addAll(viewButton, editButton, deleteButton);

                // View Button Functionality
                viewButton.setOnMouseClicked((MouseEvent event) -> {
                    staff selectedStaff =
                        getTableView().getItems().get(getIndex());
                    viewStaffDetails(selectedStaff);
                });

                // Edit Button Functionality
                editButton.setOnMouseClicked((MouseEvent event) -> {
                    staff selectedStaff =
                        getTableView().getItems().get(getIndex());
                    try {
                        FXMLLoader loader = new FXMLLoader(
                            getClass().getResource("editStaff.fxml"));
                        Parent root = loader.load();
                        EditStaffController editStaffController =
                            loader.getController();
                        editStaffController.setStaff(selectedStaff);
                        Stage stage = new Stage();
                        stage.initStyle(StageStyle.UNDECORATED);
                        stage.initModality(Modality.WINDOW_MODAL);
                        stage.initOwner(editButton.getScene().getWindow());
                        Scene scene = new Scene(root);
                        stage.setScene(scene);
                        stage.setTitle("Edit Staff");
                        stage.showAndWait();
                        connectDB.insertLogEntry(userID,
                            "Edited staff member with ID "
                                + selectedStaff.getUid());
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (SQLException ex) {
                        Logger
                            .getLogger(AdminDashboardController.class.getName())
                            .log(Level.SEVERE, null, ex);
                    }
                });

                // Delete Button Functionality
                deleteButton.setOnMouseClicked((MouseEvent event) -> {
                    staff selectedStaff =
                        getTableView().getItems().get(getIndex());
                    deleteStaff(selectedStaff);
                    try {
                        connectDB.insertLogEntry(userID,
                            "Deleted staff member with ID "
                                + selectedStaff.getUid());
                    } catch (SQLException ex) {
                        Logger
                            .getLogger(AdminDashboardController.class.getName())
                            .log(Level.SEVERE, null, ex);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : hBox);
            }
        });
    }

    // Setup Search Filter
    private void setupSearchFilter() {
        filteredData = new FilteredList<>(dataList, b -> true);
        search_textfield.textProperty().addListener((observable, oldValue,
                                                        newValue) -> {
            filteredData.setPredicate(staff -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return staff.getFname().toLowerCase().contains(lowerCaseFilter)
                    || staff.getLname().toLowerCase().contains(lowerCaseFilter)
                    || staff.getEmail().toLowerCase().contains(lowerCaseFilter)
                    || String.valueOf(staff.getPhonenum())
                           .contains(lowerCaseFilter)
                    || // Convert phonenum to String
                    staff.getStatus().toLowerCase().contains(lowerCaseFilter)
                    || String.valueOf(staff.getUid()).contains(lowerCaseFilter);
            });
        });

        // Create the SortedList only once
        sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(
            staffTableView.comparatorProperty());
        staffTableView.setItems(sortedData);
    }

    // Setup Sort Button
    private void setupSortButton() {
        filterStaff_button.setOnMouseClicked(event -> { sortStaffTable(); });
    }

    // SERVICE FORM CODE

    private void propagateServiceData() {
        // Set cell value factories matching service model property names &
        // getters
        service_idadmin.setCellValueFactory(
            new PropertyValueFactory<>("service_id"));
        service_nameadmin.setCellValueFactory(
            new PropertyValueFactory<>("service_name"));
        service_priceadmin.setCellValueFactory(
            new PropertyValueFactory<>("price"));
    }

    private void loadServiceData() {
        ObservableList<service> serviceList = connectDB.getServiceData();
        serviceTableViewadmin.setItems(serviceList);
    }

    private void setupServiceActions() {
        service_actionadmin.setCellFactory(column
            -> new TableCell<service, Void>() {
            private final HBox hBox = new HBox();
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");

            {
                service_actionadmin.setPrefWidth(250);

                // Set button styles
                editButton.setStyle("-fx-cursor: hand; -fx-background-color: "
                    + "#00E676; -fx-text-fill: white;");
                deleteButton.setStyle("-fx-cursor: hand; -fx-background-color: "
                    + "#ff1744; -fx-text-fill: white;");

                // Set button widths
                editButton.setPrefWidth(70);
                deleteButton.setPrefWidth(70);
                hBox.setAlignment(Pos.CENTER);
                hBox.setSpacing(5); // Space between buttons
                hBox.getChildren().addAll(editButton, deleteButton);

                // Edit Button Functionality
                editButton.setOnMouseClicked((MouseEvent event) -> {
                    service selectedService =
                        getTableView().getItems().get(getIndex());
                    try {
                        FXMLLoader loader = new FXMLLoader(
                            getClass().getResource("editService.fxml"));
                        Parent root = loader.load();
                        EditServiceController editServiceController =
                            loader.getController();
                        editServiceController.setService(
                            selectedService); // Set the selected service

                        Stage stage = new Stage();
                        stage.initStyle(StageStyle.UNDECORATED);
                        stage.initModality(Modality.WINDOW_MODAL);
                        stage.initOwner(editButton.getScene().getWindow());
                        Scene scene = new Scene(root);
                        stage.setScene(scene);
                        stage.setTitle("Edit Service");
                        stage.showAndWait();
                        connectDB.insertLogEntry(userID,
                            "Edited service with ID "
                                + selectedService.getService_id());
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (SQLException ex) {
                        Logger
                            .getLogger(AdminDashboardController.class.getName())
                            .log(Level.SEVERE, null, ex);
                    }
                });

                // Delete Button Functionality
                deleteButton.setOnMouseClicked((MouseEvent event) -> {
                    service selectedService =
                        getTableView().getItems().get(getIndex());
                    deleteService(selectedService); // Call deleteService
                                                    // instead of deleteStaff
                    try {
                        connectDB.insertLogEntry(userID,
                            "Deleted service with ID "
                                + selectedService.getService_id());
                    } catch (SQLException ex) {
                        Logger
                            .getLogger(AdminDashboardController.class.getName())
                            .log(Level.SEVERE, null, ex);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : hBox);
            }
        });
    }

    // Handle the delete function in service
    private void deleteService(service selectedService) {
        if (alert.confirmationMessage(
                "Are you sure you want to delete this service?")) {
            connectDB.deleteService(selectedService.getService_id());
            loadServiceData();
        }
    }

    // Setup Refresh service Button
    private void RefreshServiceButton() {
        refreshservices_btn.setOnMouseClicked(event -> { loadServiceData(); });
    }
    private void setupAddServiceButton() {
        addservices_btn.setOnMouseClicked(event -> {
            try {
                FXMLLoader loader =
                    new FXMLLoader(getClass().getResource("addService.fxml"));
                Parent root = loader.load();
                AddServiceController addServiceController =
                    loader.getController();
                addServiceController.setAdminDashboardController(this);
                Stage stage = new Stage();
                stage.initStyle(StageStyle.UNDECORATED);
                stage.initModality(Modality.WINDOW_MODAL);
                stage.initOwner(addservices_btn.getScene().getWindow());
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("Add Service");
                stage.showAndWait();
                connectDB.insertLogEntry(userID, "Added a new service");
            } catch (IOException e) {
                System.out.println(
                    "Error loading add staff form: " + e.getMessage());
            } catch (SQLException ex) {
                Logger.getLogger(AdminDashboardController.class.getName())
                    .log(Level.SEVERE, null, ex);
            }
        });
    }

    // Setup Search Filter for Services
    private void setupSearchFilterForServices() {
        srchservices.textProperty().addListener((observable, oldValue,
                                                    newValue) -> {
            ObservableList<service> serviceList = connectDB.getServiceData();
            FilteredList<service> filteredData =
                new FilteredList<>(serviceList, b -> true);
            filteredData.setPredicate(service -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return service.getService_name().toLowerCase().contains(
                           lowerCaseFilter)
                    || String.valueOf(service.getService_id())
                           .contains(lowerCaseFilter);
            });
            serviceTableViewadmin.setItems(filteredData);
        });
    }

    // PATIENT FORM

    private void initializePatientsTableView() {
        // Set up the table columns
        patients_col_patientID.setCellValueFactory(
            new PropertyValueFactory<>("patientID"));
        patients_col_name.setCellValueFactory(
            new PropertyValueFactory<>("fullName"));
        patients_col_contactNumber.setCellValueFactory(
            new PropertyValueFactory<>("contactNumber"));
        patients_col_date.setCellValueFactory(
            new PropertyValueFactory<>("date"));

        // Retrieve patient data from the database
        ObservableList<patient> patientData = connectDB.getPatientData();

        // Populate the table view with patient data
        adminpatientsTableView.setItems(patientData);
    }

    
    @FXML
    private void invidualPatientRecord(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/user/patientHistory.fxml"));
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
    
    // APPOINTMENT FORM

// Initialize table columns
private void initializeTableColumns() {
    adminAppointmentID_Col.setCellValueFactory(
        new PropertyValueFactory<>("appointmentID"));
    adminAppointmentName_Col.setCellValueFactory(
        new PropertyValueFactory<>("patientName"));
    adminTreatment_Col.setCellValueFactory(
        new PropertyValueFactory<>("serviceName"));
    adminAppointmentSchedule_Col.setCellValueFactory(
        new PropertyValueFactory<>("appointmentSchedule"));
    adminAppointmentStatus_Col.setCellValueFactory(
        new PropertyValueFactory<>("status"));
}

private void initializeAppointmentsTableView() {
    try {
        ObservableList<appointment> appointments =
            connectDB.getAppointments();
        adminappointmentsTableView.setItems(appointments);
    } catch (SQLException e) {
        System.out.println(
            "Error retrieving appointments: " + e.getMessage());
    }
}

// Search by date
@FXML
private void handleSearchByDateButtonAction(ActionEvent event) {
    LocalDate selectedDate = srchDayAppointment.getValue();
    if (selectedDate != null) {
        try {
            adminappointmentsTableView.getItems().clear();
            connectDB db = new connectDB();
            adminappointmentsTableView.setItems(db.getAppointmentsBySchedule(selectedDate.toString()));
        } catch (SQLException e) {
            System.out.println("Error loading appointments by date: " + e.getMessage());
        }
    } else {
        initializeAppointmentsTableView(); // Load all appointments if no date is selected
    }
}

private boolean isFiltered = false;

@FXML
private void handleCurrentDayAppointmentAction(MouseEvent event) {
    try {
        adminappointmentsTableView.getItems().clear();
        connectDB db = new connectDB();
        if (isFiltered) {
            initializeAppointmentsTableView(); // Load all appointments
            isFiltered = false;
        } else {
            adminappointmentsTableView.setItems(db.getAppointmentsBySchedule(LocalDate.now().toString()));
            isFiltered = true;
        }
    } catch (SQLException e) {
        System.out.println("Error loading appointments: " + e.getMessage());
    }
}

    // PROFILE SETTING FORM CODE

    private void loadProfileData() {
        Session session = Session.getInstance();
        profile_AdminId.setText(String.valueOf(session.getUid()));
        profile_Fname.setText(session.getFname());
        profile_Lname.setText(session.getLname());
        profile_Phonenum.setText(String.valueOf(
            session.getPhone_num())); // This should work if phonenum is an int
        profile_email.setText(session.getEmail());
        profile_status.setText(session.getStatus());

        // Load profile image
        loadProfileImage(session.getUid());
        // Set editable fields
        profile_editFname.setText(session.getFname());
        profile_editLname.setText(session.getLname());
        profile_editPhonenum.setText(String.valueOf(
            session.getPhone_num())); // This should work if phonenum is an int
        profile_editEmail.setText(session.getEmail());
        profile_editStatus.setValue(session.getStatus());
    }
    
    private void initializeQuestion(){
   
    adminsecurityQuestion.getItems().addAll(
        "What is your birthyear?",
        "What is your favorite food?",
        "What is your favorite color?"
    );

    adminsecurityQuestion.getSelectionModel().select(0);

    
}

    // Handle save changes button action

    @FXML
    private void handleSaveChanges(ActionEvent event) {
        String fname = profile_editFname.getText().trim();
        String lname = profile_editLname.getText().trim();
        String phonenum = profile_editPhonenum.getText().trim();
        String email = profile_editEmail.getText().trim();
        String status = profile_editStatus.getValue();
         String securityAnswer = adminsecurityQuestionAnswer.getText().trim();

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
            loadProfileData(); // Reload the profile data to reflect changes
            connectDB.insertLogEntry(userID, "Updated profile information");
        } catch (Exception e) {
            alert.errorMessage(e.getMessage());
        }
    }

    // Handle change password button action

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
            connectDB.insertLogEntry(userID, "Changed password");
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
            Image image = new Image(selectedImageFile.toURI().toString());
            profile_circle.setFill(new ImagePattern(image));
            // Upload the image to the database
            uploadProfileImage(selectedImageFile);
            connectDB.insertLogEntry(userID, "Imported a new profile image");
        }
    }

    // Method to display the selected image in the Circle
    private void displaySelectedImage(File selectedImageFile) {
        // Create an Image object from the selected file
        Image image = new Image(selectedImageFile.toURI().toString());

        // Create a ImageView object to display the image
        ImageView imageView = new ImageView(image);

        // Set the width and height of the ImageView to match the Circle
        imageView.setFitWidth(profile_circle.getRadius() * 2);
        imageView.setFitHeight(profile_circle.getRadius() * 2);

        // Clip the ImageView to the shape of the Circle
        Circle clip = new Circle(profile_circle.getCenterX(),
            profile_circle.getCenterY(), profile_circle.getRadius());
        imageView.setClip(clip);

        // Add the ImageView to the Circle
        profile_circle.setFill(
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
                }
                connectDB.insertLogEntry(Session.getInstance().getUid(),
                    "Uploaded a new profile image");
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
                    profile_circle.setFill(new ImagePattern(image));
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

    @FXML
    private void activityLogButtonClicked(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("admin_activityLog.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setWidth(620);
            stage.setHeight(430);
            stage.initStyle(StageStyle.UNDECORATED);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

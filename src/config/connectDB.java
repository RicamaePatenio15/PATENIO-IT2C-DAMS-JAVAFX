package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import models.ProfileImage;
import models.SecurityQuestion;
import models.TreatmentHistory;
import models.activitylog;
import models.appointment;
import models.patient;
import models.patientHistory;
import models.service;
import models.staff;

public class connectDB {
    // Database credentials
    private static final String DB_URL =
        "jdbc:mysql://localhost:3306/patenio_db";
    private static final String USER = "root";
    private static final String PASS = "";

    // Method to get a new database connection
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }

    // Function to save data
    public static int insertData(String sql, Object... params) {
        try (Connection connect = getConnection();
             PreparedStatement pst = connect.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                pst.setObject(i + 1, params[i]);
            }
            pst.executeUpdate();
            return 1; // Success
        } catch (SQLException ex) {
            System.err.println("Connection Error: " + ex);
            return 0; // Failure
        }
    }

    // Function to retrieve data
    public ResultSet getData(String sql) throws SQLException {
        Connection connect = getConnection();
        Statement stmt = connect.createStatement();
        return stmt.executeQuery(sql); // Remember to close this ResultSet later
    }

    // Function to update data
   public int updateData(String sql, Object... params) {
    try (Connection connect = getConnection();
         PreparedStatement pst = connect.prepareStatement(sql)) {
        for (int i = 0; i < params.length; i++) {
            pst.setObject(i + 1, params[i]);
        }
        return pst.executeUpdate();
    } catch (SQLException ex) {
        System.out.println("Connection Error: " + ex);
        return 0;
    }
}
    // Method to retrieve staff data
    public static ObservableList<staff> getDatastaff() {
        ObservableList<staff> list = FXCollections.observableArrayList();
        String query = "SELECT * FROM tbl_user WHERE u_type != 'admin'";
        try (Connection connect = getConnection();
             PreparedStatement ps = connect.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new staff(rs.getInt("u_id"), rs.getString("fname"),
                    rs.getString("lname"), rs.getString("phone_num"),
                    rs.getString("email"), rs.getString("u_type"),
                    rs.getString("status")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void updateStaff(int uid, String fname, String lname,
        String phonenum, String email, String userType, String status)
        throws SQLException {
        String query = "UPDATE tbl_user SET fname = ?, lname = ?, phone_num = "
            + "?, email = ?, u_type = ?, status = ? WHERE u_id = ?";
        try (Connection connect = getConnection();
             PreparedStatement pstmt = connect.prepareStatement(query)) {
            pstmt.setString(1, fname);
            pstmt.setString(2, lname);
            pstmt.setString(3, phonenum);
            pstmt.setString(4, email);
            pstmt.setString(5, userType);
            pstmt.setString(6, status);
            pstmt.setInt(7, uid);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating staff: " + e.getMessage());
            throw e; // Rethrow the exception for further handling
        }
    }

    public static void deleteStaff(int staffId) {
        String query = "DELETE FROM tbl_user WHERE u_id = ?";
        try (Connection connect = getConnection();
             PreparedStatement ps = connect.prepareStatement(query)) {
            ps.setInt(1, staffId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deleting staff: " + e.getMessage());
        }
    }

    // Other methods...

    public static int countActiveAdmins() {
        int count = 0;
        String query = "SELECT COUNT(*) FROM tbl_user WHERE status = 'Active' "
            + "AND u_type = 'Admin'"; // Adjust the query as needed

        try (Connection conn = getConnection(); // Assuming you have a method to
                                                // get a connection
             Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                count = rs.getInt(1); // Get the count from the result set
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    // Function to update data
    public static void updateUser(int uid, String fname, String lname,
        String phonenum, String email, String status) {
        String query = "UPDATE tbl_user SET fname = ?, lname = ?, phone_num = "
            + "?, email = ?, status = ? WHERE u_id = ?";

        try (Connection connect = getConnection();
             PreparedStatement pst = connect.prepareStatement(query)) {
            // Set the parameters for the prepared statement
            pst.setString(1, fname);
            pst.setString(2, lname);
            pst.setString(3, phonenum);
            pst.setString(4, email);
            pst.setString(5, status);
            pst.setInt(6, uid); // Assuming user_id is an integer

            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Data Updated Successfully!");
            } else {
                System.out.println("Data Update Failed!");
            }
        } catch (SQLException ex) {
            System.out.println("Connection Error: " + ex);
        }
    }

    // Database retrieval method for service data
    public static ObservableList<service> getServiceData() {
        ObservableList<service> list = FXCollections.observableArrayList();
        String query = "SELECT * FROM tbl_service";
        try (Connection connect = getConnection();
             PreparedStatement ps = connect.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                service svc = new service(rs.getInt("ServiceID"),
                    rs.getString("ServiceName"), rs.getDouble("Price"));
                list.add(svc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static boolean deleteService(int serviceID) {
        String query = "DELETE FROM tbl_service WHERE ServiceID = ?";
        try (Connection connect = getConnection();
             PreparedStatement ps = connect.prepareStatement(query)) {
            ps.setInt(1, serviceID);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void updateServiceData(
        int serviceID, String serviceName, double servicePrice) {
        String query = "UPDATE tbl_service SET ServiceName = ?, Price = ? "
            + "WHERE ServiceID = ?";
        try (Connection connect = getConnection();
             PreparedStatement pst = connect.prepareStatement(query)) {
            // Set the parameters for the prepared statement
            pst.setString(1, serviceName);
            pst.setDouble(2, servicePrice);
            pst.setInt(3, serviceID);

            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Service Data Updated Successfully!");
            } else {
                System.out.println("Service Data Update Failed!");
            }
        } catch (SQLException ex) {
            System.out.println("Connection Error: " + ex);
        }
    }

    public static void addService(String serviceName, double servicePrice) {
        String query =
            "INSERT INTO tbl_service (ServiceName, Price) VALUES (?, ?)";

        try (Connection connect = getConnection();
             PreparedStatement pst = connect.prepareStatement(query)) {
            // Set the parameters for the prepared statement
            pst.setString(1, serviceName);
            pst.setDouble(2, servicePrice);

            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Service added successfully!");
            } else {
                System.out.println("Failed to add service.");
            }
        } catch (SQLException ex) {
            System.out.println("Connection Error: " + ex);
        }
    }
    // Method to add a new profile image
    public static void addProfileImage(ProfileImage profileImage)
        throws SQLException {
        String query = "INSERT INTO tbl_profileimage (ImageName, UserID, "
            + "ImagePath, ImageFile) VALUES (?, ?, ?, ?)";
        try (Connection connect = getConnection();
             PreparedStatement pst = connect.prepareStatement(
                 query, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, profileImage.getImageName());
            pst.setInt(2, profileImage.getUserId());
            pst.setString(3, profileImage.getImagePath());
            pst.setBytes(4, profileImage.getImageFile());
            pst.executeUpdate();
        }
    }

    // Method to retrieve profile image data
    public static ProfileImage getProfileImageByUserId(int userId)
        throws SQLException {
        String query = "SELECT * FROM tbl_profileimage WHERE UserID = ?";
        try (Connection connect = getConnection();
             PreparedStatement pst = connect.prepareStatement(query)) {
            pst.setInt(1, userId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return new ProfileImage(rs.getInt("ImageId"),
                    rs.getString("ImageName"), rs.getInt("UserID"),
                    rs.getString("ImagePath"), rs.getBytes("ImageFile"));
            } else {
                return null; // No image found
            }
        }
    }

    // Method to update a profile image
    public static void updateProfileImage(ProfileImage profileImage)
        throws SQLException {
        String query = "UPDATE tbl_profileimage SET ImageName = ?, UserID = ?, "
            + "ImagePath = ?, ImageFile = ? WHERE ImageId = ?";
        try (Connection connect = getConnection();
             PreparedStatement pst = connect.prepareStatement(query)) {
            pst.setString(1, profileImage.getImageName());
            pst.setInt(2, profileImage.getUserId());
            pst.setString(3, profileImage.getImagePath());
            pst.setBytes(4, profileImage.getImageFile());
            pst.setInt(5, profileImage.getImageId());
            pst.executeUpdate();

            // Update the profileimage column in tbl_user
            String updateQuery =
                "UPDATE tbl_user SET profileimage = ? WHERE u_id = ?";
            try (PreparedStatement updatePst =
                     connect.prepareStatement(updateQuery)) {
                updatePst.setInt(1, profileImage.getImageId());
                updatePst.setInt(2, profileImage.getUserId());
                updatePst.executeUpdate();
            }
        }
    }

    // Method to delete a profile image
    public static void deleteProfileImage(int imageId) throws SQLException {
        String query = "DELETE FROM tbl_profileimage WHERE ImageId = ?";
        try (Connection connect = getConnection();
             PreparedStatement pst = connect.prepareStatement(query)) {
            pst.setInt(1, imageId);
            pst.executeUpdate();
        }
    }

    // Method to update staff image
    public static void updateStaffImage(int staffId, byte[] imageBytes)
        throws SQLException {
        String query =
            "UPDATE tbl_profileimage SET ImageFile = ? WHERE UserId = ?";
        try (Connection connect = getConnection();
             PreparedStatement pstmt = connect.prepareStatement(query)) {
            pstmt.setBytes(1, imageBytes);
            pstmt.setInt(2, staffId);
            pstmt.executeUpdate();
        }
    }

    // Method to retrieve staff image
    public static byte[] getStaffImage(int staffId) throws SQLException {
        String query =
            "SELECT ImageFile FROM tbl_profileimage WHERE UserId = ?";
        try (Connection connect = getConnection();
             PreparedStatement pstmt = connect.prepareStatement(query)) {
            pstmt.setInt(1, staffId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getBytes("ImageFile");
            } else {
                return null; // No image found
            }
        }
    }
    // Method to add a new patient
    public static int addPatient(patient newPatient) throws SQLException {
        String query = "INSERT INTO tbl_patient (PatientID, FirstName, "
            + "LastName, ContactNumber, Gender, Date, Time, "
            + "Address) VALUES (NULL, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connect = getConnection();
             PreparedStatement pstmt = connect.prepareStatement(
                 query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, newPatient.getFirstName());
            pstmt.setString(2, newPatient.getLastName());
            pstmt.setString(3, newPatient.getContactNumber());
            pstmt.setString(4, newPatient.getGender());
            pstmt.setString(5, newPatient.getDate());
            pstmt.setString(6, newPatient.getTime());
            pstmt.setString(7, newPatient.getAddress());
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException(
                        "Failed to retrieve generated PatientID");
                }
            }
        }
    }
    // Method to add a new appointment
    public static int addAppointment(appointment newAppointment)
        throws SQLException {
        String query = "INSERT INTO tbl_appointment (PatientID, ServiceID, "
            + "AppointmentSchedule, Status, Notes) VALUES (?, ?, ?, ?, ?)";

        try (Connection connect = getConnection();
             PreparedStatement pstmt = connect.prepareStatement(
                 query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, newAppointment.getPatientID());
            pstmt.setInt(2, newAppointment.getServiceID());
            pstmt.setString(3, newAppointment.getAppointmentSchedule());
            pstmt.setString(4, newAppointment.getStatus());
            pstmt.setString(5, newAppointment.getNotes());
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException(
                        "Failed to retrieve generated AppointmentID");
                }
            }
        }
    }
    public static int getPatientID(String name, String contactNumber)
        throws SQLException {
        String query = "SELECT PatientID FROM tbl_patient WHERE FirstName || ' "
            + "' || LastName = ? AND ContactNumber = ?";

        try (Connection connect = getConnection();
             PreparedStatement pstmt = connect.prepareStatement(query)) {
            pstmt.setString(1, name);
            pstmt.setString(2, contactNumber);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("PatientID");
                } else {
                    throw new SQLException("Patient not found");
                }
            }
        }
    }

    public static ObservableList<patient> getPatientData() {
        ObservableList<patient> list = FXCollections.observableArrayList();
        String query =
            "SELECT * FROM tbl_patient"; // Adjust the query as needed
        try (Connection connect = getConnection();
             PreparedStatement ps = connect.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                patient pat = new patient(rs.getInt("patientID"),
                    rs.getString("firstName"), rs.getString("lastName"),
                    rs.getString("contactNumber"), rs.getString("gender"),
                    rs.getString("date"), rs.getString("time"),
                    rs.getString("address"));
                list.add(pat);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public ObservableList<Integer> getAppointmentIDsByStatus(String status)
        throws SQLException {
        ObservableList<Integer> appointmentIDs =
            FXCollections.observableArrayList();
        String query =
            "SELECT AppointmentID FROM tbl_appointment WHERE Status = ?";

        try (Connection connect = getConnection();
             PreparedStatement pstmt = connect.prepareStatement(query)) {
            pstmt.setString(1, status);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    appointmentIDs.add(rs.getInt("AppointmentID"));
                }
            }
        }
        return appointmentIDs;
    }
    // Method to retrieve appointment IDs
    public static ObservableList<Integer> getAppointmentIDs()
        throws SQLException {
        String query = "SELECT AppointmentID FROM tbl_appointment";
        ObservableList<Integer> appointmentIDs =
            FXCollections.observableArrayList();

        try (Connection connect = getConnection();
             Statement stmt = connect.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                appointmentIDs.add(rs.getInt("AppointmentID"));
            }
        }
        return appointmentIDs;
    }

    // Method to retrieve all appointments
    public static ObservableList<appointment> getAppointments()
        throws SQLException {
        String query =
            "SELECT a.AppointmentID, p.PatientID, p.FirstName, p.LastName, "
            + "p.Gender, p.ContactNumber, s.ServiceID, s.ServiceName, "
            + "a.AppointmentSchedule, a.Status, a.Notes "
            + "FROM tbl_appointment a "
            + "JOIN tbl_patient p ON a.PatientID = p.PatientID "
            + "JOIN tbl_service s ON a.ServiceID = s.ServiceID";

        ObservableList<appointment> appointments =
            FXCollections.observableArrayList();

        try (Connection connect = getConnection();
             Statement stmt = connect.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                appointment appointment = new appointment(
                    rs.getInt("AppointmentID"), rs.getInt("PatientID"),
                    rs.getString("FirstName") + " " + rs.getString("LastName"),
                    rs.getString("Gender"), rs.getString("ContactNumber"),
                    rs.getInt("ServiceID"), rs.getString("ServiceName"),
                    rs.getString("AppointmentSchedule"), rs.getString("Status"),
                    rs.getString("Notes"));
                appointments.add(appointment);
            }
        }
        return appointments;
    }

    public static appointment getAppointment(int appointmentID)
        throws SQLException {
        String query =
            "SELECT a.AppointmentID, p.PatientID, p.FirstName, p.LastName, "
            + "p.Gender, p.ContactNumber, s.ServiceID, s.ServiceName, "
            + "a.AppointmentSchedule, a.Status, a.Notes "
            + "FROM tbl_appointment a "
            + "JOIN tbl_patient p ON a.PatientID = p.PatientID "
            + "JOIN tbl_service s ON a.ServiceID = s.ServiceID "
            + "WHERE a.AppointmentID = ?";

        try (Connection connect = getConnection();
             PreparedStatement pstmt = connect.prepareStatement(query)) {
            pstmt.setInt(1, appointmentID);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    appointment appointment = new appointment(
                        rs.getInt("AppointmentID"), rs.getInt("PatientID"),
                        rs.getString("FirstName") + " "
                            + rs.getString("LastName"),
                        rs.getString("Gender"), rs.getString("ContactNumber"),
                        rs.getInt("ServiceID"), rs.getString("ServiceName"),
                        rs.getString("AppointmentSchedule"),
                        rs.getString("Status"), rs.getString("Notes"));
                    return appointment;
                } else {
                    return null;
                }
            }
        }
    }

    public void updateAppointment(appointment appointment) throws SQLException {
        String query = "UPDATE tbl_appointment "
            + "SET AppointmentSchedule = ?, "
            + "Status = ?, "
            + "Notes = ? "
            + "WHERE AppointmentID = ?";

        try (Connection connect = getConnection();
             PreparedStatement pstmt = connect.prepareStatement(query)) {
            pstmt.setString(1, appointment.getAppointmentSchedule());
            pstmt.setString(2, appointment.getStatus());
            pstmt.setString(3, appointment.getNotes());
            pstmt.setInt(4, appointment.getAppointmentID());

            pstmt.executeUpdate();
        }
    }

    public static int deleteAppointment(int appointmentID) throws SQLException {
        String query = "DELETE FROM tbl_appointment WHERE AppointmentID = ?";

        try (Connection connect = getConnection();
             PreparedStatement pstmt = connect.prepareStatement(query)) {
            pstmt.setInt(1, appointmentID);
            return pstmt.executeUpdate();
        }
    }

    public static ObservableList<appointment>
    getAppointmentsWithPatientAndService() throws SQLException {
        String query =
            "SELECT a.AppointmentID, p.PatientID, p.FirstName, p.LastName, "
            + "p.Gender, p.ContactNumber, s.ServiceID, s.ServiceName, "
            + "a.AppointmentSchedule, a.Status "
            + "FROM tbl_appointment a "
            + "JOIN tbl_patient p ON a.PatientID = p.PatientID "
            + "JOIN tbl_service s ON a.ServiceID = s.ServiceID";

        ObservableList<appointment> appointments =
            FXCollections.observableArrayList();

        try (Connection connect = getConnection();
             Statement stmt = connect.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                appointment appointment = new appointment(
                    rs.getInt("AppointmentID"), rs.getInt("PatientID"),
                    rs.getString("FirstName") + " " + rs.getString("LastName"),
                    rs.getString("Gender"), rs.getString("ContactNumber"),
                    rs.getInt("ServiceID"), rs.getString("ServiceName"),
                    rs.getString("AppointmentSchedule"), rs.getString("Status"),
                    "");
                appointments.add(appointment);
            }
        }
        return appointments;
    }

    public static String getPatientGender(int patientID) throws SQLException {
        String query = "SELECT Gender FROM tbl_patient WHERE PatientID = ?";

        try (Connection connect = getConnection();
             PreparedStatement pstmt = connect.prepareStatement(query)) {
            pstmt.setInt(1, patientID);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("Gender");
                } else {
                    return ""; // Patient not found
                }
            }
        }
    }

    public static String getPatientName(int patientID) throws SQLException {
        String query =
            "SELECT FirstName, LastName FROM tbl_patient WHERE PatientID = ?";

        try (Connection connect = getConnection();
             PreparedStatement pstmt = connect.prepareStatement(query)) {
            pstmt.setInt(1, patientID);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("FirstName") + " "
                        + rs.getString("LastName");
                } else {
                    return "";
                }
            }
        }
    }
    public static ObservableList<Integer> getPatientIDs() throws SQLException {
        String query = "SELECT PatientID FROM tbl_patient";
        ObservableList<Integer> patientIDs =
            FXCollections.observableArrayList();

        try (Connection connect = getConnection();
             Statement stmt = connect.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                patientIDs.add(rs.getInt("PatientID"));
            }
        }
        return patientIDs;
    }

    public static ObservableList<String> getServiceNames() throws SQLException {
        String query = "SELECT ServiceName FROM tbl_service";
        ObservableList<String> serviceNames =
            FXCollections.observableArrayList();

        try (Connection connect = getConnection();
             Statement stmt = connect.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                serviceNames.add(rs.getString("ServiceName"));
            }
        }
        return serviceNames;
    }

    public static int getServiceID(String serviceName) throws SQLException {
        String query =
            "SELECT ServiceID FROM tbl_service WHERE ServiceName = ?";

        try (Connection connect = getConnection();
             PreparedStatement pstmt = connect.prepareStatement(query)) {
            pstmt.setString(1, serviceName);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("ServiceID");
                } else {
                    return 0;
                }
            }
        }
    }

    public static ObservableList<activitylog> getUserLogData()
        throws SQLException {
        String query = "SELECT * FROM tbl_userlog";
        ObservableList<activitylog> userLogs =
            FXCollections.observableArrayList();

        try (Connection connect = getConnection();
             Statement stmt = connect.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                activitylog log =
                    new activitylog(rs.getInt("LogID"), rs.getInt("UserID"),
                        rs.getString("LogDate"), rs.getString("LogAction"));
                userLogs.add(log);
            }
        }
        return userLogs;
    }

    public static void insertLogEntry(int userID, String logActionDescription)
        throws SQLException {
        String query =
            "INSERT INTO tbl_userlog (UserID, LogAction) VALUES (?, ?)";
        try (Connection connect = getConnection();
             PreparedStatement pstmt = connect.prepareStatement(query)) {
            pstmt.setInt(1, userID);
            pstmt.setString(2, logActionDescription);
            pstmt.executeUpdate();
        }
    }

    public static int getTotalPatients() throws SQLException {
        String query = "SELECT COUNT(*) FROM tbl_patient";
        try (Connection connect = getConnection();
             Statement stmt = connect.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                return 0; // No patients found
            }
        }
    }
    public static int getTotalAppointments() throws SQLException {
        String query = "SELECT COUNT(*) FROM tbl_appointment";
        try (Connection connect = getConnection();
             Statement stmt = connect.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                return 0; // No appointments found
            }
        }
    }

    public static void addTreatmentHistory(int patientID, int appointmentID)
        throws SQLException {
        String query = "INSERT INTO tbl_treatmenthistory (PatientID, "
            + "AppointmentID, DateStamp) VALUES (?, ?, ?)";
        try (Connection connect = getConnection();
             PreparedStatement pstmt = connect.prepareStatement(query)) {
            pstmt.setInt(1, patientID);
            pstmt.setInt(2, appointmentID);
            pstmt.setString(3, java.time.LocalDate.now().toString());
            pstmt.executeUpdate();
        }
    }
    // Method to load patient records into the table
    public static ObservableList<patient> loadPatientRecords() {
        ObservableList<patient> patientList =
            FXCollections.observableArrayList();
        String query = "SELECT p.PatientID, p.FirstName, p.LastName, "
            + "p.Date, a.AppointmentID, s.ServiceName "
            + "FROM tbl_patient p "
            + "JOIN tbl_appointment a ON p.PatientID = a.PatientID "
            + "JOIN tbl_service s ON a.ServiceID = s.ServiceID";

        try (Connection connect = getConnection();
             Statement stmt = connect.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                patient patient = new patient(rs.getInt("PatientID"),
                    rs.getString("FirstName"), rs.getString("LastName"),
                    "", // ContactNumber
                    "", // Gender
                    rs.getString("Date"),
                    "", // Time
                    "" // Address
                );
                patient.setAppointmentID(rs.getInt("AppointmentID"));
                patient.setTreatment(rs.getString("ServiceName"));
                patientList.add(patient);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patientList;
    }

    // Method to load patient records with contact numbers and addresses
    public static ObservableList<patient>
    loadPatientRecordsWithContactAndAddress() {
        ObservableList<patient> patientList =
            FXCollections.observableArrayList();
        String query = "SELECT p.PatientID, p.FirstName, p.LastName, "
            + "p.ContactNumber, p.Address, a.Status "
            + "FROM tbl_patient p "
            + "JOIN tbl_appointment a ON p.PatientID = a.PatientID";

        try (Connection connect = getConnection();
             Statement stmt = connect.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                patient patient = new patient(rs.getInt("PatientID"),
                    rs.getString("FirstName"), rs.getString("LastName"),
                    rs.getString("ContactNumber"),
                    "", // Gender
                    "", // Date
                    "", // Time
                    rs.getString("Address"));
                patient.setStatus(rs.getString("Status")); // Set the status
                patientList.add(patient);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patientList;
    }

    // Method to retrieve all pending appointments
    public static ObservableList<appointment> getPendingAppointments()
        throws SQLException {
        String query =
            "SELECT a.AppointmentID, p.PatientID, p.FirstName, p.LastName, "
            + "p.Gender, p.ContactNumber, s.ServiceID, s.ServiceName, "
            + "a.AppointmentSchedule, a.Status, a.Notes "
            + "FROM tbl_appointment a "
            + "JOIN tbl_patient p ON a.PatientID = p.PatientID "
            + "JOIN tbl_service s ON a.ServiceID = s.ServiceID "
            + "WHERE a.Status = 'Pending'";

        ObservableList<appointment> appointments =
            FXCollections.observableArrayList();

        try (Connection connect = getConnection();
             Statement stmt = connect.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                appointment appointment = new appointment(
                    rs.getInt("AppointmentID"), rs.getInt("PatientID"),
                    rs.getString("FirstName") + " " + rs.getString("LastName"),
                    rs.getString("Gender"), rs.getString("ContactNumber"),
                    rs.getInt("ServiceID"), rs.getString("ServiceName"),
                    rs.getString("AppointmentSchedule"), rs.getString("Status"),
                    rs.getString("Notes"));
                appointments.add(appointment);
            }
        }
        return appointments;
    }
    public double getPriceByServiceID(int serviceID) throws SQLException {
        String query = "SELECT Price FROM tbl_service WHERE ServiceID = ?";
        try (Connection connect = getConnection();
             PreparedStatement pstmt = connect.prepareStatement(query)) {
            pstmt.setInt(1, serviceID);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("Price");
                } else {
                    return 0.0; // or throw an exception
                }
            }
        }
    }

    public ObservableList<appointment> getAppointmentsPending(String status)
        throws SQLException {
        String query = "SELECT a.AppointmentID, p.FirstName, p.Gender, "
                       + "p.ContactNumber, s.Price "
            + "FROM tbl_appointment a "
            + "JOIN tbl_patient p ON a.PatientID = p.PatientID "
            + "JOIN tbl_service s ON a.ServiceID = s.ServiceID "
            + "WHERE a.Status = ?";

        ObservableList<appointment> appointments =
            FXCollections.observableArrayList();

        try (Connection connect = getConnection();
             PreparedStatement pstmt = connect.prepareStatement(query)) {
            pstmt.setString(1, status);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    appointment appointment = new appointment(
                        rs.getInt("AppointmentID"), rs.getString("FirstName"),
                        rs.getString("Gender"), rs.getString("ContactNumber"),
                        rs.getDouble("Price"));
                    appointments.add(appointment);
                }
            }
        }
        return appointments;
    }

    // Method to count pending appointments
    public static int countPendingAppointments() throws SQLException {
        String query =
            "SELECT COUNT(*) FROM tbl_appointment WHERE Status = 'Pending'";
        try (Connection connect = getConnection();
             Statement stmt = connect.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                return 0; // No pending appointments found
            }
        }
    }

    // Method to count patients with cancelled or completed appointments
    public static int countCompletedAndCancelledPatients() throws SQLException {
        String query =
            "SELECT COUNT(DISTINCT p.PatientID) FROM tbl_appointment a "
            + "JOIN tbl_patient p ON a.PatientID = p.PatientID "
            + "WHERE a.Status IN ('Cancelled', 'Completed')";
        try (Connection connect = getConnection();
             Statement stmt = connect.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                return 0; // No patients found
            }
        }
    }

    // Method to count completed appointments
    public static int countCompletedAppointments() throws SQLException {
        String query =
            "SELECT COUNT(*) FROM tbl_appointment WHERE Status = 'Completed'";
        try (Connection connect = getConnection();
             Statement stmt = connect.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                return 0; // No completed appointments found
            }
        }
    }

    // Method to count cancelled appointments
    public static int countCancelledAppointments() throws SQLException {
        String query =
            "SELECT COUNT(*) FROM tbl_appointment WHERE Status = 'Cancelled'";
        try (Connection connect = getConnection();
             Statement stmt = connect.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                return 0; // No cancelled appointments found
            }
        }
    }

    // Method to count active patients (pending appointments)
    public static int countActivePatients() throws SQLException {
        String query =
            "SELECT COUNT(DISTINCT p.PatientID) FROM tbl_appointment a "
            + "JOIN tbl_patient p ON a.PatientID = p.PatientID "
            + "WHERE a.Status = 'Pending'";
        try (Connection connect = getConnection();
             Statement stmt = connect.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                return 0; // No active patients found
            }
        }
    }

    // Method to count inactive patients (completed and cancelled appointments)
    public static int countInactivePatients() throws SQLException {
        String query =
            "SELECT COUNT(DISTINCT p.PatientID) FROM tbl_appointment a "
            + "JOIN tbl_patient p ON a.PatientID = p.PatientID "
            + "WHERE a.Status IN ('Completed', 'Cancelled')";
        try (Connection connect = getConnection();
             Statement stmt = connect.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                return 0; // No inactive patients found
            }
        }
    }

    // Method to count admin users
    public static int countAdminUsers() throws SQLException {
        String query = "SELECT COUNT(*) FROM tbl_user WHERE u_type = 'Admin'";
        try (Connection connect = getConnection();
             Statement stmt = connect.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                return 0; // No admin users found
            }
        }
    }

    // Method to count normal users
    public static int countNormalUsers() throws SQLException {
        String query = "SELECT COUNT(*) FROM tbl_user WHERE u_type = 'User'";
        try (Connection connect = getConnection();
             Statement stmt = connect.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                return 0; // No normal users found
            }
        }
    }

 /// Method to get unique PatientIDs
    public static ArrayList<Integer> getUniquePatientIDs() {
        ArrayList<Integer> patientIDs = new ArrayList<>();
        String query = "SELECT DISTINCT PatientID FROM tbl_treatmenthistory";
        try (Connection connect = getConnection();
             PreparedStatement pstmt = connect.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                patientIDs.add(rs.getInt("PatientID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patientIDs;
    }public static ObservableList<TreatmentHistory> filterPatientRecords(int patientID) {
    if (patientID == 0) {
        System.out.println("Invalid patient ID");
        return FXCollections.observableArrayList();
    }

    ObservableList<TreatmentHistory> filteredPatientRecords = FXCollections.observableArrayList();
   String query = "SELECT " +
    "p.FirstName AS FirstName, " +
    "p.LastName AS LastName, " +
    "s.ServiceName AS serviceName, " +
    "th.Date AS Date " +
    "FROM " +
    "tbl_treatmenthistory th " +
    "INNER JOIN " +
    "tbl_patient p ON th.PatientID = p.PatientID " +
    "INNER JOIN " +
    "tbl_service s ON th.ServiceID = s.ServiceID " +
    "WHERE " +
    "p.PatientID = ?";
    try (Connection connect = getConnection();
         PreparedStatement pstmt = connect.prepareStatement(query)) {
        pstmt.setInt(1, patientID);
        try (ResultSet rs = pstmt.executeQuery()) {
            System.out.println("Filtering patient records for PatientID: " + patientID);
            while (rs.next()) {
                System.out.println("Retrieved patient record: " + rs.getString("FirstName") + " " + rs.getString("LastName"));
                TreatmentHistory treatmentHistory = new TreatmentHistory(
                    rs.getString("FirstName"),
                    rs.getString("LastName"),
                    rs.getString("ServiceName"),
                   rs.getString("date")
                );
                filteredPatientRecords.add(treatmentHistory);
            }
        }
    } catch (SQLException e) {
        System.out.println("Database error: " + e.getMessage());
    }
    return filteredPatientRecords;
}
public static boolean isValidPatientID(int patientID) {
    String query = "SELECT COUNT(*) FROM tbl_patient WHERE PatientID = ?";
    try (Connection connect = getConnection();
         PreparedStatement pstmt = connect.prepareStatement(query)) {
        pstmt.setInt(1, patientID);
        try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
    } catch (SQLException e) {
        System.out.println("Error: " + e.getMessage());
    }
    return false;
}
   
   public static ResultSet getData(String sql, Object... params) throws SQLException {
        Connection connect = getConnection();
        PreparedStatement pstmt = connect.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            pstmt.setObject(i + 1, params[i]);
        }
        return pstmt.executeQuery();
    }
   
   public SecurityQuestion retrieveSecurityQuestion(int uId) {
    SecurityQuestion securityQuestion = null;
    String query = "SELECT * FROM tbl_securityquestion WHERE u_id = ?";

    try {
        connectDB db = new connectDB();
        ResultSet rs = db.getData(query, uId);

        if (rs.next()) {
            securityQuestion = new SecurityQuestion(
                    rs.getInt("id"),
                    rs.getInt("u_id"),
                    rs.getString("answer")
            );
        }
    } catch (SQLException e) {
        System.out.println("Error retrieving security question: " + e.getMessage());
    }

    return securityQuestion;
}

public void addSecurityQuestion(SecurityQuestion securityQuestion) {
    String query = "INSERT INTO tbl_securityquestion (u_id, answer) VALUES (?, ?)";

    connectDB db = new connectDB();
    int result = db.insertData(query, 
            securityQuestion.getUId(), 
            securityQuestion.getAnswer());

    if (result == 0) {
        System.out.println("Error adding security question");
    }
}

public void updateSecurityQuestion(SecurityQuestion securityQuestion) {
    String query = "UPDATE tbl_securityquestion SET answer = ? WHERE u_id = ?";

    connectDB db = new connectDB();
    try {
        int result = db.updateData(query, 
                securityQuestion.getAnswer(),
                securityQuestion.getUId());

        if (result == 0) {
            System.out.println("Error updating security question");
        }
    } catch (Exception e) {
        System.out.println("Error updating security question: " + e.getMessage());
    }
}
    public void updatePassword(String newPassword, int uId) throws SQLException {
    String query = "UPDATE tbl_user SET pass = ? WHERE u_id = ?";
    try (Connection connect = getConnection();
         PreparedStatement pstmt = connect.prepareStatement(query)) {
        pstmt.setString(1, newPassword);
        pstmt.setInt(2, uId);
        pstmt.executeUpdate();
    }
}
    public static ObservableList<appointment> getAppointmentsBySchedule(String schedule)
        throws SQLException {
    String query =
        "SELECT a.AppointmentID, p.PatientID, p.FirstName, p.LastName, "
        + "p.Gender, p.ContactNumber, s.ServiceID, s.ServiceName, "
        + "a.AppointmentSchedule, a.Status, a.Notes "
        + "FROM tbl_appointment a "
        + "JOIN tbl_patient p ON a.PatientID = p.PatientID "
        + "JOIN tbl_service s ON a.ServiceID = s.ServiceID "
        + "WHERE a.AppointmentSchedule = ?";

    ObservableList<appointment> appointments =
        FXCollections.observableArrayList();

    try (Connection connect = getConnection();
         PreparedStatement pstmt = connect.prepareStatement(query)) {
        pstmt.setString(1, schedule);
        try (ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                appointment appointment = new appointment(
                    rs.getInt("AppointmentID"), rs.getInt("PatientID"),
                    rs.getString("FirstName") + " " + rs.getString("LastName"),
                    rs.getString("Gender"), rs.getString("ContactNumber"),
                    rs.getInt("ServiceID"), rs.getString("ServiceName"),
                    rs.getString("AppointmentSchedule"), rs.getString("Status"),
                    rs.getString("Notes"));
                appointments.add(appointment);
            }
        }
    }
    return appointments;
}
}

    
    

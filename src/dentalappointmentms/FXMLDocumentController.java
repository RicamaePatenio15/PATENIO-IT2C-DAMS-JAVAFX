/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dentalappointmentms;

import config.Users;
import config.alertMessage;
import config.connectDB;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 *
 * @author ride1
 */
public class FXMLDocumentController implements Initializable {
    
    
    static String type="";
    static String status= "";
    public static boolean loginUser(String email, String pass){
        connectDB con= new connectDB();
        try{
        String query = "SELECT * FROM tbl_user WHERE email='" + email + "' AND pass='" + pass + "'";
        ResultSet resultSet=con.getData(query);
        if(resultSet.next()){
            type=resultSet.getString("u_type");
            status=resultSet.getString("status");       
            return true;
        }else{
            return false;      
        }
        
        }catch (SQLException ex){
            return false;
        }
        
    }
    
    public static String em;
    public boolean duplicateCheck() {
    connectDB con = new connectDB();

    try {
        String query = "SELECT * FROM tbl_user WHERE email='" + email.getText() + "'";
        ResultSet resultSet = con.getData(query);
        if (resultSet.next()) {  
            em = resultSet.getString("email");
            if (em.equals(email.getText())) {
                return true;
            }
        }
    } catch (SQLException ex) {
        System.out.println("Error: " + ex);
    }
    return false;
}   
    
    @FXML
    private TextField email;

    @FXML
    private TextField fname;

    @FXML
    private TextField lname;

    @FXML
    private Button login;

    @FXML
    private AnchorPane loginForm;

    @FXML
    private TextField login_email;

    @FXML
    private PasswordField login_pass;

    @FXML
    private Hyperlink login_register;

    @FXML
    private CheckBox login_showpass;

    @FXML
    private TextField login_textpass;

    @FXML
    private PasswordField pass;

    @FXML
    private TextField pass_text;

    @FXML
    private TextField phone_num;

    @FXML
    private AnchorPane registerForm;

    @FXML
    private Button register_button;

    @FXML
    private Hyperlink register_login;

    @FXML
    private CheckBox showpass_button;

    @FXML
    private ComboBox<String> u_type;
    

    @FXML
    public void initialize() {
      u_type.getItems().addAll("User", "Admin"); 
      
      
  }
    
    
    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;
    
    private alertMessage alert= new alertMessage();
    
    public void loginAccount(){
    if (login_textpass.isVisible()) {
        if (!login_textpass.getText().equals(login_pass.getText())) {
            login_textpass.setText(pass.getText());
        } else {
            if (!login_textpass.getText().equals(login_pass.getText())) {
                login_pass.setText(login_textpass.getText());
            }
        }
    }

if (email.getText().trim().isEmpty() || pass.getText().trim().isEmpty()) {

//    if (!status.equals("Active")) {
//        alert.successMessage("The account is not yet Active. Please contact the administrator");
//    } else {
        alert.successMessage("Login Successfully!");
        if (type.equals("Admin")) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("user/adminDashboard.fxml"));
                Stage stage = new Stage();
                stage.setTitle("Admin Dashboard");

                stage.setMinHeight(530);
                stage.setMinWidth(380);

                stage.setScene(new Scene(root));
                stage.show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (type.equals("User")) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("admin/userDashboard.fxml"));
                Stage stage = new Stage();
                stage.setTitle("User Dashboard");

                stage.setMinHeight(530);
                stage.setMinWidth(380);

                stage.setScene(new Scene(root));
                stage.show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            alert.errorMessage("No account type found, contact the Administrator.");
        }
//    }
} else {
    alert.errorMessage("Invalid Email/Password!"); 
}

    }
    public void loginShowPassword() {    
    if (login_showpass.isSelected()) {
        login_textpass.setText(login_pass.getText());
        login_textpass.setVisible(true);
        login_pass.setVisible(false);
    } else {
        login_pass.setText(login_textpass.getText());
        login_textpass.setVisible(false);
        login_pass.setVisible(true);
    }
    }

    public void registerAccount() {
        
        
    if (fname.getText().isEmpty() || lname.getText().isEmpty() || 
        phone_num.getText().isEmpty() || email.getText().isEmpty() || 
        u_type.getSelectionModel().getSelectedItem() == null || 
        pass.getText().isEmpty()) { 
        
        alert.errorMessage("Please fill all blank fields");
    }
    
    if(pass_text.isVisible()){
        if(!pass_text.getText().equals(pass.getText())){
            pass_text.setText(pass.getText());
        }
    }else{
        if(!pass_text.getText().equals(pass.getText())){
            pass.setText(pass_text.getText());
        }
        
    }
    
    if (pass.getText().length() < 8) {
        alert.errorMessage("Password must be at least 8 characters long.");
        pass.setText("");
    return;
    }
   if (duplicateCheck()) {
        alert.errorMessage("The email already exists! Try again.");
        email.setText(""); 
    return;
}
    connectDB con = new connectDB();
   int insertResult = con.insertData("INSERT INTO tbl_user (fname, lname, phone_num, email, pass, u_type, status) " 
        + "VALUES('" + fname.getText() + "', '" + lname.getText() + "', '" + phone_num.getText() + "', '" 
        + email.getText() + "', '" + pass.getText() + "', '" 
        + (u_type.getSelectionModel().getSelectedItem() == null ? "" : u_type.getSelectionModel().getSelectedItem()) + "', 'Pending')");



    if (insertResult > 0) { 
        alert.successMessage("Sign up successfully! Wait for the Admin's approval for your registration.");
        loginForm.setVisible(true);
        registerForm.setVisible(false);
    } else {
        alert.errorMessage("Invalid Account. Register first before loggin in.");
    }
    }
    
    public void registerClear(){
        fname.clear();
        lname.clear();
        phone_num.clear();
        email.clear();
        u_type.getSelectionModel().clearSelection();
        pass.clear();
    }
    
    public void registerShowPassword(){
        if(showpass_button.isSelected()){
            pass_text.setText(pass.getText());
            pass_text.setVisible(true);
            pass.setVisible(false);
        }else{
            pass.setText(pass_text.getText());
            pass_text.setVisible(false);
            pass.setVisible(true);
        }
    }
    

    
    public void userList() {

        List<String> listU = new ArrayList<>();

        for (String data : Users.user) {
            listU.add(data);
        }

        ObservableList listData = FXCollections.observableList(listU);
        u_type.setItems(listData);
    }

//    public void switchPage() {
//
//        if (u_type.getSelectionModel().getSelectedItem() == "Admin") {
//
//            try {
//
//                Parent root = FXMLLoader.load(getClass().getResource("adminDashboard.fxml"));
//                Stage stage = new Stage();
//
//                stage.setTitle("Hospital Management System");
//
//                stage.setMinHeight(530);
//                stage.setMinWidth(380);
//
//                stage.setScene(new Scene(root));
//                stage.show();
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//        } else if (u_type.getSelectionModel().getSelectedItem() == "User") {
//
//            try {
//
//                Parent root = FXMLLoader.load(getClass().getResource("userDashboard.fxml"));
//                Stage stage = new Stage();
//
//                stage.setTitle("Dental Appointment Management System");
//
//                stage.setMinWidth(340);
//                stage.setMinHeight(580);
//
//                stage.setScene(new Scene(root));
//                stage.show();
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//    }
    
    public void switchForm(ActionEvent event) {

        if (event.getSource() == login_register) {
            // REGISTRATION FORM WILL SHOW
            loginForm.setVisible(false);
            registerForm.setVisible(true);
        } else if (event.getSource() == register_login) {
            // LOGIN FORM WILL SHOW
            loginForm.setVisible(true);
            registerForm.setVisible(false);
        }

    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
      u_type.getItems().addAll("User", "Admin");  
    }    
    
}

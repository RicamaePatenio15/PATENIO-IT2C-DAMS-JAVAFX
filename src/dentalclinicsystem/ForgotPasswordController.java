
package dentalclinicsystem;

import config.alertMessage;
import config.connectDB;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.SecurityQuestion;

public class ForgotPasswordController implements Initializable {

    @FXML
    private TextField AnswerQuestion;

    @FXML
    private ComboBox<String> Questionnaire;

    @FXML
    private Button submitAnswer;

    private alertMessage alert = new alertMessage();

    private int uId;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Questionnaire.getItems().addAll(
                "What is your birthyear?",
                "What is your favorite food?",
                "What is your favorite color?"
        );

        Questionnaire.getSelectionModel().select(0);
    }

    @FXML
private void handleSubmitAnswer(ActionEvent event) {
    String userAnswer = AnswerQuestion.getText();
    String selectedQuestion = Questionnaire.getSelectionModel().getSelectedItem().toString();

    SecurityQuestion securityQuestion = retrieveSecurityQuestion(uId);

    if (securityQuestion != null) {
        boolean isAnswerCorrect = checkAnswer(securityQuestion, userAnswer);

        if (isAnswerCorrect) {
            alert.successMessage("Answer is correct. Proceeding to reset password...");
            navigateToResetPasswordScene(event);
        } else {
            alert.errorMessage("Incorrect answer. Please try again.");
        }
    } else {
        alert.errorMessage("Failed to retrieve security question.");
    }
}

    private SecurityQuestion retrieveSecurityQuestion(int uId) {
        connectDB db = new connectDB();
        return db.retrieveSecurityQuestion(uId);
    }

  private boolean checkAnswer(SecurityQuestion securityQuestion, String userAnswer) {
    return securityQuestion.getAnswer().equals(userAnswer);
}

    private void navigateToResetPasswordScene(ActionEvent event) {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("forgotChangePass.fxml"));
        Parent root = loader.load();
        ForgotChangePassController controller = loader.getController();
        controller.setuId(uId); // Pass the uId to the ForgotChangePassController
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    } catch (IOException e) {
        System.out.println("Error loading ResetPassword FXML: " + e.getMessage());
    }
}

    private void closeCurrentScene(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    public void setuId(int uId) {
        this.uId = uId;
    }
}
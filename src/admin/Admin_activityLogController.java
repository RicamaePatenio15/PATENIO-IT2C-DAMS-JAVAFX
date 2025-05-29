
package admin;

import config.connectDB;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import models.activitylog;

public class Admin_activityLogController implements Initializable {
    @FXML private TableColumn<activitylog, Integer> log_id;
    @FXML private TableColumn<activitylog, Integer> user_id;
    @FXML private TableColumn<activitylog, String> log_date;
    @FXML private TableColumn<activitylog, String> log_action;
    @FXML private TableView<activitylog> table_activitylog;
    @FXML private FontAwesomeIcon back_icon;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadActivityLogData();
    }

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
    private void handleBackIconClick(MouseEvent event) {
        // Get the current stage
        Stage stage = (Stage) back_icon.getScene().getWindow();
        // Close the stage
        stage.close();
    }
}

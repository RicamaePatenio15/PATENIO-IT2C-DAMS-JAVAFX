package dentalclinicsystem;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class DentalClinicSystem extends Application {
    private static Stage stg;
    @Override
    public void start(Stage primaryStage) throws Exception {
        stg = primaryStage;
        Parent root = FXMLLoader.load(
            getClass().getResource("/dentalclinicsystem/FXMLDocument.fxml"));
        Image image = new Image("/images/logo.png");
        stg.getIcons().add(image);
        Scene scene = new Scene(root);
        stg.setTitle("DAMS");
        stg.setScene(scene);
        stg.initStyle(StageStyle.UNDECORATED);
        stg.show();
    }

    public void changeScene(Class<?> getClass, Node node, String fxml)
        throws IOException {
        Stage stage = (Stage) node.getScene().getWindow();
        Parent pane = FXMLLoader.load(getClass.getResource(fxml));

        // Create a new Scene with the parent pane
        Scene newScene = new Scene(pane);

        // Set the new scene to the stage
        stage.setScene(newScene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
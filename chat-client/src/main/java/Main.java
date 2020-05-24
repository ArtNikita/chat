import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Chat");
        primaryStage.setScene(new Scene(root, 480, 620));
        primaryStage.setMinWidth(480);
        primaryStage.setMinHeight(480);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        Controller.send("/exit");
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

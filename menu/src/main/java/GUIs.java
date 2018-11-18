

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class GUIs extends Application {

    private Stage primaryStage;
    private StackPane stackPane;
    private Scene scene;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Menu");
        initWindow();
    }

    private void initWindow() {

        try {
            FXMLLoader loader = new FXMLLoader();
            System.out.println(this.getClass().getResource("/MenuWindow.fxml"));
            //loader.setLocation();
            loader.setLocation(this.getClass().getResource("/MenuWindow.fxml"));

            //stackPane = new StackPane();

            //stackPane.getChildren().clear();
            stackPane = loader.load();

            MenuWindowController.setGuIs(this);
            scene = new Scene(stackPane);
            primaryStage.setResizable(false);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Menu");
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void initServer() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ServerWindowController.class.getResource("ServerWindow.fxml"));
            System.out.println(this.getClass().getResource("ServerWindow.fxml"));

            StackPane sp = loader.load();

            scene = new Scene(sp);

            primaryStage.setScene(scene);
            primaryStage.show();
            primaryStage.setResizable(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package org.example.examenpruebalistview;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.examenpruebalistview.Connection.DBConnection;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/equipos.fxml"));
            Scene scene = new Scene(loader.load());
            primaryStage.setTitle("Liga de Fútbol");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() throws Exception {
        DBConnection.desconectar();
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

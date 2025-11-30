/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package doanJava;

/**
 *
 * @author hatua
 */

import doanJava.utils.SqliteHelper;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class TestMainSession extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            System.out.println("Dang khoi tao Database...");
            // 1. QUAN TRỌNG: Phải init DB trước, nếu không DAO sẽ lỗi kết nối
            SqliteHelper.initDatabase();

            System.out.println("Dang load giao dien Main...");
            // 2. Load file MainFXML.fxml
            // Lưu ý đường dẫn phải chính xác
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/doanJava/view/MainFXML.fxml")));

            Scene scene = new Scene(root);

            // 3. Nạp CSS (Nếu có)
            try {
                // Thử nạp style.css hoặc fxml.css tùy project của bạn
                String css = getClass().getResource("/styles/style.css").toExternalForm();
                scene.getStylesheets().add(css);
            } catch (Exception e) {
                System.out.println("Khong tim thay CSS, chay giao dien mac dinh.");
            }

            primaryStage.setTitle("TEST MODE - Main Dashboard");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Loi khi chay Test Main: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

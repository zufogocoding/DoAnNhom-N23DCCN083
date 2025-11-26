package doanJava;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            // 1. Load file giao diện (SỬA TÊN FILE .fxml CỦA BẠN VÀO ĐÂY)
            // Ví dụ file của bạn tên là "GiaoDien.fxml" thì điền y hệt vào
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/ProfileFXML.fxml"));

            // 2. Tạo Scene (Khung cảnh) từ giao diện đó
            Scene scene = new Scene(root);
            
            // 3. (Tùy chọn) Nếu CSS chưa nhận, bạn có thể add cứng ở đây để chắc chắn
            // scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

            // 4. Cấu hình cửa sổ chính
            primaryStage.setTitle("Quản Lý Dinh Dưỡng Sinh Viên");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false); // Không cho người dùng kéo giãn cửa sổ lung tung
            primaryStage.show();
            
        } catch (Exception e) {
            e.printStackTrace(); // In lỗi ra nếu không tìm thấy file
            System.out.println("Lỗi: Không tìm thấy file FXML hoặc sai đường dẫn!");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
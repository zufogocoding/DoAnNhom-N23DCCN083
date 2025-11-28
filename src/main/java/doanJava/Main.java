package doanJava;

import doanJava.utils.SqliteHelper; // <--- 1. IMPORT QUAN TRỌNG (Vì file này đã chuyển vào package utils)
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

// Sửa tên class thành "Main" (viết hoa chữ đầu) cho đúng chuẩn Java
public class Main extends Application { 

    @Override
    public void start(Stage primaryStage) {
        try {
            // --- 2. SỬA ĐƯỜNG DẪN RESOURCE ---
            // Cấu trúc mới trong resources là: doanJava/view/Profile.fxml
            // Dấu "/" ở đầu nghĩa là bắt đầu tìm từ thư mục "resources"
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/doanJava/view/Profile.fxml")));

            Scene scene = new Scene(root);
            
            // (Tùy chọn) Load CSS thủ công nếu trong FXML chưa nhận
            // String css = Objects.requireNonNull(getClass().getResource("/doanJava/css/style.css")).toExternalForm();
            // scene.getStylesheets().add(css);

            primaryStage.setTitle("Quản Lý Dinh Dưỡng Sinh Viên");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false); 
            primaryStage.show();
            
        } catch (NullPointerException e) {
            // Lỗi này xảy ra nếu đường dẫn file FXML bị sai
            System.err.println("Lỗi TRẦM TRỌNG: Không tìm thấy file FXML!");
            System.err.println("Hãy kiểm tra xem file Profile.fxml có nằm đúng trong 'src/main/resources/doanJava/view/' không?");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // --- 3. KHỞI TẠO DATABASE ---
        // Phải chạy dòng này để tạo file data.db và các bảng trước khi app hiện lên
        SqliteHelper.initDatabase();

        launch(args);
    }
}
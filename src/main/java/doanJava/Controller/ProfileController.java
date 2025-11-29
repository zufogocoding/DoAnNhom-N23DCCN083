package doanJava.Controller;

import doanJava.DAO.StudentDAO;
import doanJava.Model.Student;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class ProfileController {

    StudentDAO SD = new StudentDAO();

    // Khai báo khớp với fx:id trong ProfileFXML.fxml
    @FXML private TextField txtName;
    @FXML private TextField txtStudentId; // Bổ sung thêm cái này cho khớp FXML
    @FXML private TextField txtHeight;
    @FXML private TextField txtWeight;
    @FXML private TextField txtProtein;
    @FXML private TextField txtCarbs;
    @FXML private TextField txtFat;
    @FXML private TextField txtCalories;
    @FXML private Button btnSave; // Cần cái này để lấy cửa sổ (Stage) hiện tại

    @FXML
    private void handleSaveProfile() {
        try {
            // 1. Lấy dữ liệu từ giao diện
            String name = txtName.getText();
            
            // Validate cơ bản
            if (name.isEmpty()) {
                showAlert("Lỗi", "Vui lòng nhập Tên!");
                return;
            }

            // 2. Parse dữ liệu số
            // Lưu ý: Nếu ô để trống thì parse sẽ lỗi, bạn có thể thêm check empty trước
            int studentId = txtStudentId.getText().isEmpty() ? 0 : Integer.parseInt(txtStudentId.getText());
            double height = Double.parseDouble(txtHeight.getText());
            double weight = Double.parseDouble(txtWeight.getText());
            double protein = Double.parseDouble(txtProtein.getText());
            double carbs = Double.parseDouble(txtCarbs.getText());
            double fat = Double.parseDouble(txtFat.getText());
            double calories = Double.parseDouble(txtCalories.getText());

            // 3. Tạo đối tượng và lưu
            Student profile = new Student(studentId, name, height, weight, protein, carbs, fat, calories);
            
            SD.addStudent(profile); // Lưu vào DB
            
            System.out.println("Đã lưu hồ sơ: " + profile.toString());
            
            // Hiện thông báo thành công
            showAlert("Thành công", "Đã lưu hồ sơ! Đang chuyển đến trang chủ...");

            // 4. --- CHUYỂN CẢNH SANG MAIN LAYOUT (Phần mới thêm) ---
            switchToMainScreen();

        } catch (NumberFormatException e) {
            showAlert("Lỗi nhập liệu", "Vui lòng nhập số hợp lệ (không chứa chữ cái)!");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Lỗi", "Đã có lỗi xảy ra: " + e.getMessage());
        }
    }

    // Hàm chuyển cảnh sang Dashboard
    private void switchToMainScreen() {
        try {
            // Load file 
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/doanJava/view/MainFXML.fxml"));
            Parent root = loader.load();

            // Lấy cửa sổ (Stage) hiện tại từ nút Save
            Stage stage = (Stage) btnSave.getScene().getWindow();
            
            // Tạo cảnh mới (Scene)
            Scene scene = new Scene(root);
            
            // Thiết lập cảnh mới cho cửa sổ
            stage.setScene(scene);
            stage.setTitle("Healthy Kitchen - Dashboard");
            stage.centerOnScreen(); // Căn giữa màn hình cho đẹp
            
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Lỗi hệ thống", "Không tìm thấy file giao diện MainFXML.fxml!");
        }
    }

    // Hàm tiện ích để hiện thông báo
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
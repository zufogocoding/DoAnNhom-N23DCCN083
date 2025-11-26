/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package doanJava;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;

public class ProfileController {
    StudentDAO SD = new StudentDAO();
    // Khai báo các biến khớp với fx:id trong Scene Builder
    @FXML private TextField txtName;
    @FXML private TextField txtHeight;
    @FXML private TextField txtWeight;
    @FXML private TextField txtProtein;
    @FXML private TextField txtCarbs;
    @FXML private TextField txtFat;
    @FXML private TextField txtCalories;

    @FXML
    private void handleSaveProfile() {
        try {
            // 1. Lấy dữ liệu từ giao diện
            String name = txtName.getText();
            
            // Validate: Kiểm tra xem các trường có trống không
            if (name.isEmpty()) {
                showAlert("Lỗi", "Vui lòng nhập Tên!");
                return;
            }

            // 2. Parse dữ liệu số (Sử dụng try-catch để tránh lỗi nếu nhập chữ)
            double height = Double.parseDouble(txtHeight.getText());
            double weight = Double.parseDouble(txtWeight.getText());
            double protein = Double.parseDouble(txtProtein.getText());
            double carbs = Double.parseDouble(txtCarbs.getText());
            double fat = Double.parseDouble(txtFat.getText());
            double calories = Double.parseDouble(txtCalories.getText());

            // 3. Tạo đối tượng và lưu (Ở đây in ra console demo)
            Student profile = new Student(0, name, height, weight, protein, carbs, fat, calories);
            SD.addStudent(profile);
            System.out.println("Đã lưu hồ sơ: " + profile.toString());
            showAlert("Thành công", "Đã thêm hồ sơ dinh dưỡng cho: " + name);
            
            //code lưu vào Database hoặc List

        } catch (NumberFormatException e) {
            showAlert("Lỗi nhập liệu", "Vui lòng nhập số hợp lệ cho Chiều cao, Cân nặng và Dinh dưỡng!");
        } catch (Exception e) {
            System.err.println("Lỗi"+ "Đã có lỗi xảy ra: " + e.getMessage());
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
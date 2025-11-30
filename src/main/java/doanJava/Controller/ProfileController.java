package doanJava.Controller;

import doanJava.DAO.StudentDAO;
import doanJava.Model.Student;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable; // Import để dùng hàm initialize
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ProfileController implements Initializable {

    StudentDAO SD = new StudentDAO();
    private int selectedProfileId = 0;
    // Khai báo khớp với fx:id trong FXML
    @FXML private ComboBox<Student> cbSavedProfiles; // ComboBox chọn người cũ
    @FXML private TextField txtName;
    @FXML private TextField txtStudentId;
    @FXML private TextField txtHeight;
    @FXML private TextField txtWeight;
    @FXML private TextField txtProtein;
    @FXML private TextField txtCarbs;
    @FXML private TextField txtFat;
    @FXML private TextField txtCalories;
    @FXML private Button btnSave;

    // Hàm này chạy ngay khi form mở lên
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadSavedProfiles();
        
        cbSavedProfiles.setOnAction(e ->{
            Student selected = cbSavedProfiles.getValue();
            if (selected !=null) {
                selectedProfileId = selected.getStudentId();
                fillForm(selected);
            } else{
                selectedProfileId = 0;
            }
        });
    }

    // Logic load danh sách sinh viên cũ vào ComboBox
    private void loadSavedProfiles() {
        if (cbSavedProfiles == null) return; // Phòng trường hợp FXML chưa có ID này

        List<Student> students = SD.getAllStudents();
        cbSavedProfiles.setItems(FXCollections.observableArrayList(students));

        // Hiển thị Tên trong ComboBox
        cbSavedProfiles.setConverter(new StringConverter<Student>() {
            @Override
            public String toString(Student s) {
                return (s != null) ? s.getName() + " (ID: " + s.getStudentId() + ")" : "";
            }
            @Override
            public Student fromString(String string) { return null; }
        });

        // Khi chọn -> Tự điền dữ liệu
        cbSavedProfiles.setOnAction(e -> {
            Student selected = cbSavedProfiles.getValue();
            if (selected != null) {
                txtName.setText(selected.getName());
                txtStudentId.setText(String.valueOf(selected.getStudentId()));
                txtHeight.setText(String.valueOf(selected.getHeightCm()));
                txtWeight.setText(String.valueOf(selected.getWeightKg()));
                txtProtein.setText(String.valueOf(selected.getTargetProteinG()));
                txtCarbs.setText(String.valueOf(selected.getTargetCarbsG()));
                txtFat.setText(String.valueOf(selected.getTargetFatG()));
                txtCalories.setText(String.valueOf(selected.getTargetCalories()));
            }
        });
    }

    @FXML
    private void handleSaveProfile() {
        try {
            // 1. Validate cơ bản
            String name = txtName.getText();
            if (name.isEmpty()) {
                showAlert("Lỗi", "Vui lòng nhập Tên!");
                return;
            }

            // 2. Parse dữ liệu số
            // Nếu ô ID trống hoặc nhập chữ -> mặc định là 0 (để DB tự tăng hoặc xử lý sau)
            int studentId = 0;
            try {
                if (!txtStudentId.getText().isEmpty()) {
                    studentId = Integer.parseInt(txtStudentId.getText());
                }
            } catch (NumberFormatException e) {
                showAlert("Lỗi nhập liệu", "MSSV phải là số!");
                return;
            }

            double height = Double.parseDouble(txtHeight.getText());
            double weight = Double.parseDouble(txtWeight.getText());
            double protein = Double.parseDouble(txtProtein.getText());
            double carbs = Double.parseDouble(txtCarbs.getText());
            double fat = Double.parseDouble(txtFat.getText());
            double calories = Double.parseDouble(txtCalories.getText());

            // 3. Lưu vào DB
            if (selectedProfileId == 0){
                Student profile = new Student(studentId, name, height, weight, protein, carbs, fat, calories);
                SD.addStudent(profile);
            }
            else {
                Student existingStudent = new Student(selectedProfileId, name, height, weight, protein, carbs, fat,calories);
                SD.updateStudent(existingStudent);
                System.out.println("Đã cập nhật profile ID: " + selectedProfileId);
            }
            
            
            //System.out.println("Đã lưu hồ sơ: " + profile.toString());
            
            // 4. Chuyển cảnh
            switchToMainScreen();

        } catch (NumberFormatException e) {
            showAlert("Lỗi nhập liệu", "Vui lòng nhập đúng định dạng số cho các chỉ số!");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Lỗi", "Đã có lỗi xảy ra: " + e.getMessage());
        }
    }

    // Hàm chuyển cảnh sang Dashboard (Đã sửa đường dẫn)
    private void switchToMainScreen() {
        try {
            // Đường dẫn file FXML chính xác
            String fxmlPath = "/doanJava/view/MainFXML.fxml";
            
            // Kiểm tra xem file có tồn tại không trước khi load
            URL url = getClass().getResource(fxmlPath);
            if (url == null) {
                System.err.println("❌ KHÔNG TÌM THẤY FILE: " + fxmlPath);
                showAlert("Lỗi Hệ Thống", "Không tìm thấy file giao diện MainFXML.fxml!\nKiểm tra lại tên file trong thư mục resources/doanJava/view/");
                return;
            }

            FXMLLoader loader = new FXMLLoader(url);
            Parent root = loader.load();

            // Lấy cửa sổ hiện tại
            Stage stage = (Stage) btnSave.getScene().getWindow();
            
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Healthy Kitchen - Dashboard");
            stage.centerOnScreen();
            
        } catch (IOException e) {
            e.printStackTrace();
            // In lỗi chi tiết ra console để dễ sửa
            System.err.println("Lỗi khi load FXML: " + e.getMessage());
            showAlert("Lỗi Fatal", "Không thể mở màn hình chính: " + e.getMessage());
        }
    }

  
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.show();
    }
    
    private void fillForm(Student s) {
        if (s == null) return;

        txtName.setText(s.getName());
        

        txtStudentId.setText(String.valueOf(s.getStudentId()));
        txtHeight.setText(String.valueOf(s.getHeightCm()));
        txtWeight.setText(String.valueOf(s.getWeightKg()));
        
        txtProtein.setText(String.valueOf(s.getTargetProteinG()));
        txtCarbs.setText(String.valueOf(s.getTargetCarbsG()));
        txtFat.setText(String.valueOf(s.getTargetFatG()));
        txtCalories.setText(String.valueOf(s.getTargetCalories()));
    }
    
}
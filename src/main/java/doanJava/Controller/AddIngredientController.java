package doanJava.Controller;

import doanJava.DAO.IngredientDAO;
import doanJava.DAO.InventoryDAO;
import doanJava.Model.Ingredient;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class AddIngredientController {

    @FXML private TextField txtName, txtCalories, txtProtein, txtCarbs, txtFat;
    @FXML private TextField txtQuantity; // Ô mới thêm
    @FXML private ComboBox<String> cbUnit;
    @FXML private Button btnSave, btnCancel;

    private IngredientDAO ingredientDAO = new IngredientDAO();
    private InventoryDAO inventoryDAO = new InventoryDAO(); // DAO kho
    private int currentStudentId ; // ID giả lập

    @FXML
    
    public void setStudentId(int studentId){
        this.currentStudentId = studentId;
    }
    
    public void initialize() {
        cbUnit.getItems().addAll("g", "kg", "ml", "l", "cái", "quả", "lát");
        cbUnit.getSelectionModel().select(0); // Mặc định chọn g

        btnCancel.setOnAction(e -> closeWindow());
        btnSave.setOnAction(e -> saveIngredientToInventory());
    }

    private void saveIngredientToInventory() {
        try {
            // 1. Validate dữ liệu
            String name = txtName.getText();
            String unit = cbUnit.getValue();
            String qtyText = txtQuantity.getText();

            if (name.isEmpty() || qtyText.isEmpty()) {
                showAlert("Vui lòng nhập Tên và Số lượng đang có!");
                return;
            }
            
            if (currentStudentId == 0) {
                showAlert("Lỗi hệ thống: Chưa xác định được người dùng (ID=0).");
                return;
            }
            double quantity = Double.parseDouble(qtyText);
            
            // Lấy giá trị dinh dưỡng thô (User nhập theo bao bì)
            double rawCal = Double.parseDouble(txtCalories.getText());
            double rawPro = Double.parseDouble(txtProtein.getText());
            double rawCarb = Double.parseDouble(txtCarbs.getText());
            double rawFat = Double.parseDouble(txtFat.getText());

            // --- LOGIC TỰ ĐỘNG CHUẨN HÓA ĐƠN VỊ ---
            double finalCal = rawCal;
            double finalPro = rawPro;
            double finalCarb = rawCarb;
            double finalFat = rawFat;

            // Nếu đơn vị là g hoặc ml, ta hiểu user nhập cho 100g (chuẩn bao bì)
            // Nên ta chia 100 để lưu về dạng "per 1g"
            if (unit.equals("g") || unit.equals("ml")) {
                finalCal = rawCal / 100.0;
                finalPro = rawPro / 100.0;
                finalCarb = rawCarb / 100.0;
                finalFat = rawFat / 100.0;
            }

            // 2. Tạo hoặc Tìm Ingredient (Lưu với chỉ số đã chuẩn hóa)
            Ingredient ing = new Ingredient(0, name, unit, finalCal, finalPro, finalCarb, finalFat);
            
            // Thêm vào bảng Ingredient
            ingredientDAO.addIngredient(ing); 
            
            // Lấy ID vừa tạo (Để thêm vào kho)
            Ingredient savedIng = ingredientDAO.getIngredientByName(name); 
            int ingId = (savedIng != null) ? savedIng.getIngredientId() : -1;

            if (ingId != -1) {
                // 3. THÊM VÀO KHO
                inventoryDAO.addOrUpdateInventory(currentStudentId, ingId, quantity);
                
                showAlert("Thành công: Đã thêm " + quantity + " " + unit + " " + name + " vào kho!");
                closeWindow();
            } else {
                showAlert("Lỗi: Không lấy được ID nguyên liệu.");
            }

        } catch (NumberFormatException e) {
            showAlert("Lỗi: Các ô số liệu phải nhập số!");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Lỗi hệ thống: " + e.getMessage());
        }
    }

    private void closeWindow() {
        ((Stage) btnSave.getScene().getWindow()).close();
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(msg);
        alert.show();
    }
}
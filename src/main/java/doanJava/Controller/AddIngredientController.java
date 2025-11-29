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
    private int currentStudentId = 1; // ID giả lập

    @FXML
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

            double quantity = Double.parseDouble(qtyText);
            double cal = Double.parseDouble(txtCalories.getText());
            double pro = Double.parseDouble(txtProtein.getText());
            double carb = Double.parseDouble(txtCarbs.getText());
            double fat = Double.parseDouble(txtFat.getText());

            // 2. Tạo hoặc Tìm Ingredient
            // Logic: Thêm vào bảng Ingredient trước
            Ingredient ing = new Ingredient(0, name, unit, cal, pro, carb, fat);
            
            // Lưu ý: ingredientDAO.addIngredient cần trả về ID của nguyên liệu vừa tạo
            // Nếu bạn chưa sửa DAO trả về int, hãy sửa IngredientDAO giống FoodDAO hôm qua
            // Hoặc dùng tạm logic: add xong -> getByName để lấy ID
            ingredientDAO.addIngredient(ing); 
            
            // Lấy ID vừa tạo (Giả sử bạn viết hàm getIngredientByName hoặc add trả về ID)
            // Ở đây mình viết đoạn code "chữa cháy" lấy ID bằng tên nếu DAO bạn chưa trả về ID
            Ingredient savedIng = ingredientDAO.getIngredientByName(name); 
            int ingId = (savedIng != null) ? savedIng.getIngredientId() : -1;

            if (ingId != -1) {
                // 3. THÊM VÀO KHO (QUAN TRỌNG)
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
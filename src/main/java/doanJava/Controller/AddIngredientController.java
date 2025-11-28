/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package doanJava.Controller;

/**
 *
 * @author hatua
 */
import doanJava.DAO.IngredientDAO;
import doanJava.Model.Ingredient;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class AddIngredientController {

    @FXML private TextField txtName, txtCalories, txtProtein, txtCarbs, txtFat;
    @FXML private ComboBox<String> cbUnit;
    @FXML private Button btnSave, btnCancel;

    private IngredientDAO ingredientDAO = new IngredientDAO();

    @FXML
    public void initialize() {
        // Init combobox
        cbUnit.getItems().addAll("g", "kg", "ml", "l", "cái", "quả", "lát");

        btnCancel.setOnAction(e -> closeWindow());
        btnSave.setOnAction(e -> saveIngredient());
    }

    private void saveIngredient() {
        try {
            String name = txtName.getText();
            String unit = cbUnit.getValue();
            
            // Validate cơ bản
            if (name.isEmpty() || unit == null) {
                showAlert("Vui lòng nhập tên và chọn đơn vị!");
                return;
            }

            // Parse số (Có thể ném lỗi nếu nhập chữ)
            double cal = Double.parseDouble(txtCalories.getText());
            double pro = Double.parseDouble(txtProtein.getText());
            double carb = Double.parseDouble(txtCarbs.getText());
            double fat = Double.parseDouble(txtFat.getText());

            // Tạo object và lưu
            Ingredient ing = new Ingredient(0, name, unit, cal, pro, carb, fat);
            ingredientDAO.addIngredient(ing); // Hàm này bạn đã có trong DAO

            showAlert("Đã thêm thành công: " + name);
            closeWindow();

        } catch (NumberFormatException e) {
            showAlert("Lỗi: Các chỉ số dinh dưỡng phải là số!");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Lỗi database: " + e.getMessage());
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(msg);
        alert.show();
    }
}

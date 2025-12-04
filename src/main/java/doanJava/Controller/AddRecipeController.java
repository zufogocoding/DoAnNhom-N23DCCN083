/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package doanJava.Controller;

import doanJava.DAO.*;
import doanJava.Model.Food;
import doanJava.Model.Ingredient;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class AddRecipeController {

    @FXML private TextField txtFoodName, txtQuantity;
    @FXML private TextArea txtInstruction;
    @FXML private ComboBox<Ingredient> cbIngredients;
    @FXML private Label lblUnit;
    @FXML private Button btnAddIngredient, btnSaveAll, btnCancel;

    // TableView
    @FXML private TableView<IngredientEntry> tblRecipeIngredients;
    @FXML private TableColumn<IngredientEntry, String> colName;
    @FXML private TableColumn<IngredientEntry, String> colQty;
    @FXML private TableColumn<IngredientEntry, String> colUnit;
    @FXML private TableColumn<IngredientEntry, Void> colAction;

    // Data Models
    private ObservableList<Ingredient> allIngredients; // Dữ liệu cho ComboBox
    private ObservableList<IngredientEntry> tempRecipeList = FXCollections.observableArrayList(); // Dữ liệu cho TableView

    private IngredientDAO ingredientDAO = new IngredientDAO();
    private FoodDAO foodDAO = new FoodDAO();
    private RecipeDAO recipeDAO = new RecipeDAO();

    @FXML
    public void initialize() {
        // 1. Load danh sách nguyên liệu vào ComboBox
        allIngredients = FXCollections.observableArrayList(ingredientDAO.getAllIngredients());
        cbIngredients.setItems(allIngredients);

        // Hiển thị tên nguyên liệu trong ComboBox thay vì toString object
        cbIngredients.setConverter(new javafx.util.StringConverter<Ingredient>() {
            @Override public String toString(Ingredient object) { return object != null ? object.getName() : ""; }
            @Override public Ingredient fromString(String string) { return null; }
        });

        // Khi chọn nguyên liệu -> Cập nhật Label đơn vị
        cbIngredients.setOnAction(e -> {
            Ingredient selected = cbIngredients.getValue();
            if (selected != null) lblUnit.setText(selected.getUnit());
        });

        // 2. Setup TableView Columns
        colName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().ingredient.getName()));
        colQty.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().quantity)));
        colUnit.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().ingredient.getUnit()));
        
        // Thêm nút xóa vào bảng
        addDeleteButtonToTable();

        tblRecipeIngredients.setItems(tempRecipeList);

        // 3. Sự kiện nút bấm
        btnAddIngredient.setOnAction(e -> addIngredientToTable());
        btnSaveAll.setOnAction(e -> saveEverything());
        btnCancel.setOnAction(e -> ((Stage) btnCancel.getScene().getWindow()).close());
    }

    // Logic: Thêm vào bảng tạm
    private void addIngredientToTable() {
        Ingredient selected = cbIngredients.getValue();
        String qtyText = txtQuantity.getText();

        if (selected == null || qtyText.isEmpty()) {
            showAlert("Chưa chọn nguyên liệu hoặc thiếu số lượng!");
            return;
        }

        try {
            double qty = Double.parseDouble(qtyText);
            if (qty<=0) {
                showAlert("Số lượng phải là số dương!");
                return;
            }
            // Thêm vào list tạm
            tempRecipeList.add(new IngredientEntry(selected, qty));
            
            // Clear input
            txtQuantity.clear();
            cbIngredients.getSelectionModel().clearSelection();
        } catch (NumberFormatException e) {
            showAlert("Số lượng phải là số!");
        }
    }

    // Logic: Lưu tất cả xuống DB
    private void saveEverything() {
        // 1. Lấy dữ liệu từ giao diện (QUAN TRỌNG: Bạn thiếu bước này)
        String name = txtFoodName.getText();
        String instructions = txtInstruction.getText();

        // Validate: Kiểm tra rỗng
        if (name.isEmpty() || tempRecipeList.isEmpty()) {
            showAlert("Tên món và danh sách nguyên liệu không được để trống!");
            return;
        }

        try {
            // Bước 2: Tạo object Food (ID để 0 vì DB tự tăng)
            Food newFood = new Food(0, name, instructions);
            
            // Bước 3: GỌI HÀM VỪA SỬA: Lấy ngay ID mới về
            // (Đảm bảo FoodDAO.addFood trả về int nhé)
            int newFoodId = foodDAO.addFood(newFood); 

            if (newFoodId == -1) {
                showAlert("Lỗi: Không thể lưu món ăn vào CSDL!");
                return;
            }

            // Bước 4: Lưu Recipe Ingredients với ID vừa lấy được
            for (IngredientEntry entry : tempRecipeList) {
                recipeDAO.addIngredientToRecipe(newFoodId, entry.ingredient.getIngredientId(), entry.quantity);
            }

            showAlert("Đã lưu công thức thành công!");
            
            // Đóng cửa sổ hiện tại
            ((Stage) btnSaveAll.getScene().getWindow()).close();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Lỗi lưu dữ liệu: " + e.getMessage());
        }
    }

    // Helper class để hiển thị trên TableView dễ hơn
    public static class IngredientEntry {
        Ingredient ingredient;
        double quantity;

        public IngredientEntry(Ingredient ingredient, double quantity) {
            this.ingredient = ingredient;
            this.quantity = quantity;
        }
    }
    
    // Hàm tạo nút xóa trong TableView (Hơi phức tạp chút trong JavaFX)
    private void addDeleteButtonToTable() {
        colAction.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("X");

            {
                deleteButton.setStyle("-fx-background-color: #ff7675; -fx-text-fill: white;");
                deleteButton.setOnAction(event -> {
                    IngredientEntry data = getTableView().getItems().get(getIndex());
                    tempRecipeList.remove(data);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) setGraphic(null);
                else setGraphic(deleteButton);
            }
        });
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(msg);
        alert.show();
    }
}
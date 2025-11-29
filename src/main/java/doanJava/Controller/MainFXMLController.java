package doanJava.Controller;

import doanJava.Components.RecipeCard;
import doanJava.DAO.*;
import doanJava.Model.Food;
import doanJava.service.FoodService;
import doanJava.service.FoodService.NutritionInfo;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.FlowPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MainFXMLController implements Initializable {

    // --- KHAI BÁO CÁC ID KHỚP VỚI MainLayout.fxml ---
    @FXML private ListView<String> inventoryListView; // List bên trái
    @FXML private Button btnFindRecipes;              // Nút tìm món to (Sidebar)
    @FXML private Button btnAddIngredient;            // Nút thêm nguyên liệu nhỏ (Sidebar)
    @FXML private Button btnRecipes;                  // Nút Recipes trên Header (Mới thêm)
    @FXML private FlowPane recipesContainer;          // Khu vực chứa các Card món ăn
    
    // Các nhãn thống kê ở Sidebar (Analyze)
    @FXML private Label lblTotalCalories;
    @FXML private Label lblTotalProtein;
    @FXML private Label lblTotalCarbs;
    @FXML private Label lblTotalFat;

    // Services
    private FoodService foodService;
    private int currentStudentId = 1; // ID giả lập

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 1. Khởi tạo Service và DAO
        initServices();
        
        // 2. Load dữ liệu lên giao diện
        loadInventory();        // List bên trái
        loadSuggestedRecipes(); // List Card ở giữa + Tính toán dinh dưỡng
        
        // 3. Gắn sự kiện (Event Handler)
        
        // Bấm nút Find -> Reload lại gợi ý
        btnFindRecipes.setOnAction(e -> loadSuggestedRecipes());

        // Bấm nút Thêm Nguyên Liệu -> Mở Form nhập
        btnAddIngredient.setOnAction(e -> {
            openModal("/doanJava/view/AddIngredient.fxml", "Nhập Nguyên Liệu Vào Kho");
        });

        // Bấm nút Recipes trên Header -> Mở Form thêm món ăn mới
        // (Cần đảm bảo trong FXML nút Recipes có fx:id="btnRecipes")
        if (btnRecipes != null) {
            btnRecipes.setOnAction(e -> {
                openModal("/doanJava/view/AddRecipe.fxml", "Thêm Công Thức Mới");
            });
        }
    }

    private void initServices() {
        FoodDAO foodDAO = new FoodDAO();
        RecipeDAO recipeDAO = new RecipeDAO();
        InventoryDAO inventoryDAO = new InventoryDAO();
        IngredientDAO ingredientDAO = new IngredientDAO();
        
        this.foodService = new FoodService(foodDAO, recipeDAO, inventoryDAO, ingredientDAO);
    }

    // --- HÀM 1: Load danh sách kho (Sidebar) ---
    private void loadInventory() {
        inventoryListView.getItems().clear();
        // TODO: Sau này gọi inventoryDAO.getInventory(studentId)
        // Hiện tại Fake data cho giống Figma
        inventoryListView.getItems().addAll("Avocado", "Salmons", "Beefs", "Eggs", "Onion", "Cheese", "Tomato");
    }

    // --- HÀM 2: Load Card món ăn & Tính tổng dinh dưỡng ---
    private void loadSuggestedRecipes() {
        recipesContainer.getChildren().clear(); // Xóa card cũ
        
        // Lấy danh sách món gợi ý từ DB
        List<Food> foods = foodService.getSuggestedFoods(currentStudentId);
        
        // Nếu không có món nào (do chưa nhập kho), tạo dữ liệu giả để Test giao diện
        if (foods.isEmpty()) {
            foods.add(new Food(1, "Beefsteak", "Áp chảo..."));
            foods.add(new Food(2, "Carbonara", "Mì ý..."));
            foods.add(new Food(3, "Salmon Steak", "Cá hồi..."));
            foods.add(new Food(4, "Sandwich", "Bánh mì..."));
            foods.add(new Food(5, "Egg Soup", "Canh trứng..."));
        }

        // Biến tính tổng dinh dưỡng
        double sumCal = 0, sumPro = 0, sumCarb = 0, sumFat = 0;

        // Vòng lặp tạo Card
        for (Food food : foods) {
            // Lấy dinh dưỡng từng món
            NutritionInfo nutrition = foodService.getNutrition(food.getFoodId());
            
            // Nếu data fake chưa có dinh dưỡng -> Fake luôn số liệu
            if (nutrition.calories == 0) {
                nutrition = new NutritionInfo(350, 25, 15, 10);
            }

            // Cộng dồn vào tổng
            sumCal += nutrition.calories;
            sumPro += nutrition.protein;
            sumCarb += nutrition.carbs;
            sumFat += nutrition.fat;

            // --- TẠO CARD (Đã sửa lỗi add trùng lặp) ---
            RecipeCard card = new RecipeCard(food, nutrition, (mealType, selectedFood) -> {
                System.out.println("User chọn nấu món: " + selectedFood.getName() + " vào " + mealType);

                // TODO: Gọi MenuService để lưu vào Database tại đây
                // Ví dụ: menuService.addDailyMenu(currentStudentId, selectedFood.getId(), mealType, LocalDate.now());

                // Sau khi lưu xong, cập nhật lại biểu đồ dinh dưỡng bên dưới nếu cần
            });

            // Chỉ add 1 lần duy nhất!
            recipesContainer.getChildren().add(card);
        }

        // Cập nhật số liệu lên Sidebar (Analyze)
        updateAnalyzeLabels(sumCal, sumPro, sumCarb, sumFat);
    }

    // Hàm cập nhật nhãn thống kê
    private void updateAnalyzeLabels(double cal, double pro, double carb, double fat) {
        lblTotalCalories.setText(String.format("🔥 Kcal: %.0f", cal));
        lblTotalProtein.setText(String.format("🥩 Protein: %.0f g", pro));
        lblTotalCarbs.setText(String.format("🍞 Carbs: %.0f g", carb));
        lblTotalFat.setText(String.format("🥑 Fat: %.0f g", fat));
    }

    // Hàm mở Popup Form
    private void openModal(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL); // Chặn cửa sổ chính
            stage.showAndWait();
            
            // Sau khi đóng form nhập liệu -> Reload lại kho
            loadInventory(); 
            // loadSuggestedRecipes(); // Uncomment nếu muốn reload cả danh sách món
            
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Lỗi không tìm thấy file FXML: " + fxmlPath);
        }
    }
}
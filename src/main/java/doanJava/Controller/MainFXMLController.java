package doanJava.Controller;

import doanJava.Components.RecipeCard;
import doanJava.DAO.*;
import doanJava.Model.DailyMenu; // Import Model DailyMenu
import doanJava.Model.Food;
import doanJava.service.MenuService; // Import MenuService
import doanJava.service.FoodService;
import doanJava.service.FoodService.NutritionInfo;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert; // Import Alert
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
    @FXML private ListView<String> inventoryListView;
    @FXML private Button btnFindRecipes;
    @FXML private Button btnAddIngredient;
    @FXML private Button btnRecipes;
    @FXML private FlowPane recipesContainer;
    
    // Các nhãn thống kê ở Sidebar (Analyze)
    @FXML private Label lblTotalCalories;
    @FXML private Label lblTotalProtein;
    @FXML private Label lblTotalCarbs;
    @FXML private Label lblTotalFat;

    // Services
    private FoodService foodService;
    private MenuService menuService; // KHAI BÁO SERVICE MỚI
    private int currentStudentId = 1;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 1. Khởi tạo Service và DAO
        initServices();
        
        // 2. Load dữ liệu lên giao diện
        loadInventory();
        loadSuggestedRecipes();
        
        // 3. Cập nhật thống kê ngay khi mở app (Lấy dữ liệu thật từ DB)
        refreshAnalyzeSidebar();

        // 4. Gắn sự kiện
        btnFindRecipes.setOnAction(e -> loadSuggestedRecipes());

        btnAddIngredient.setOnAction(e -> {
            openModal("/doanJava/view/AddIngredient.fxml", "Nhập Nguyên Liệu Vào Kho");
        });

        if (btnRecipes != null) {
            btnRecipes.setOnAction(e -> {
                openModal("/doanJava/view/AddRecipe.fxml", "Thêm Công Thức Mới");
            });
        }
    }

    private void initServices() {
        // Khởi tạo tất cả các DAO cần thiết
        FoodDAO foodDAO = new FoodDAO();
        RecipeDAO recipeDAO = new RecipeDAO();
        InventoryDAO inventoryDAO = new InventoryDAO();
        IngredientDAO ingredientDAO = new IngredientDAO();
        
        // DAO cho MenuService (Master-Detail)
        DailyMenuDAO dailyMenuDAO = new DailyMenuDAO();
        MenuFoodDAO menuFoodDAO = new MenuFoodDAO();
        
        // Khởi tạo Services
        this.foodService = new FoodService(foodDAO, recipeDAO, inventoryDAO, ingredientDAO);
        
        // Khởi tạo MenuService với đầy đủ DAO phụ thuộc
        this.menuService = new MenuService(dailyMenuDAO, menuFoodDAO, recipeDAO, ingredientDAO);
    }

    private void loadInventory() {
        inventoryListView.getItems().clear();
        // TODO: Kết nối InventoryService sau này
        inventoryListView.getItems().addAll("Avocado", "Salmons", "Beefs", "Eggs", "Onion", "Cheese", "Tomato");
    }

    private void loadSuggestedRecipes() {
        recipesContainer.getChildren().clear();
        
        List<Food> foods = foodService.getSuggestedFoods(currentStudentId);
        
        if (foods.isEmpty()) {
            foods.add(new Food(1, "Beefsteak", "Áp chảo..."));
            foods.add(new Food(2, "Carbonara", "Mì ý..."));
            foods.add(new Food(3, "Salmon Steak", "Cá hồi..."));
            foods.add(new Food(4, "Sandwich", "Bánh mì..."));
            foods.add(new Food(5, "Egg Soup", "Canh trứng..."));
        }

        // Vòng lặp tạo Card
        for (Food food : foods) {
            NutritionInfo nutrition = foodService.getNutrition(food.getFoodId());
            if (nutrition.calories == 0) {
                nutrition = new NutritionInfo(350, 25, 15, 10);
            }

            // --- TẠO CARD VÀ XỬ LÝ SỰ KIỆN LƯU MENU ---
            RecipeCard card = new RecipeCard(food, nutrition, (mealType, selectedFood) -> {
                System.out.println("User chọn nấu món: " + selectedFood.getName() + " vào " + mealType);

                // 1. GỌI MENU SERVICE ĐỂ LƯU VÀO DB
                menuService.logMeal(currentStudentId, selectedFood.getFoodId(), mealType);

                // 2. Hiện thông báo thành công
                showAlert("Thành công", "Đã thêm món '" + selectedFood.getName() + "' vào thực đơn " + mealType);

                // 3. CẬP NHẬT LẠI THANH THỐNG KÊ (ANALYZE) NGAY LẬP TỨC
                refreshAnalyzeSidebar();
            });

            recipesContainer.getChildren().add(card);
        }
    }

    // --- HÀM MỚI: Lấy số liệu thực tế hôm nay để cập nhật Sidebar ---
    private void refreshAnalyzeSidebar() {
        if (menuService == null) return;

        // Gọi Service lấy thông tin dinh dưỡng hôm nay
        DailyMenu todayMenu = menuService.getTodayNutrition(currentStudentId);
        
        // Cập nhật lên giao diện
        updateAnalyzeLabels(
            todayMenu.getTotalCalories(), 
            todayMenu.getTotalProtein(), 
            todayMenu.getTotalCarbs(), 
            todayMenu.getTotalFat()
        );
    }

    private void updateAnalyzeLabels(double cal, double pro, double carb, double fat) {
        lblTotalCalories.setText(String.format("🔥 Kcal: %.0f", cal));
        lblTotalProtein.setText(String.format("🥩 Protein: %.0f g", pro));
        lblTotalCarbs.setText(String.format("🍞 Carbs: %.0f g", carb));
        lblTotalFat.setText(String.format("🥑 Fat: %.0f g", fat));
    }

    private void openModal(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            
            loadInventory(); 
            
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Lỗi không tìm thấy file FXML: " + fxmlPath);
        }
    }
    
    // Hàm hiển thị thông báo
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.show();
    }
}
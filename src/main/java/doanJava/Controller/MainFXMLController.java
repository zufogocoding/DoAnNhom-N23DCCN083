package doanJava.Controller;

import doanJava.Component.InventoryListCell;
import doanJava.Components.RecipeCard;
import doanJava.DAO.*;
import doanJava.Model.DailyMenu;
import doanJava.Model.Food;
import doanJava.Model.StudentInventory; // Import Model
import doanJava.service.MenuService;
import doanJava.service.FoodService;
import doanJava.service.FoodService.NutritionInfo;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;

public class MainFXMLController implements Initializable {

    // --- KHAI BÁO CÁC ID KHỚP VỚI MainLayout.fxml ---
    // 1. Sửa ListView<String> thành ListView<StudentInventory>
    @FXML private ListView<StudentInventory> inventoryListView; 
    @FXML private Button btnAddIngredient;
    @FXML private Button btnRecipes;
    @FXML private FlowPane recipesContainer;
    @FXML private Button btnSummary;
    @FXML private PieChart macroPieChart;
    
    // Các nhãn thống kê ở Sidebar (Analyze)
    @FXML private Label lblTotalCalories;
    @FXML private Label lblTotalProtein;
    @FXML private Label lblTotalCarbs;
    @FXML private Label lblTotalFat;

    // Services & DAOs
    private FoodService foodService;
    private MenuService menuService;
    private InventoryDAO inventoryDAO; // Khai báo DAO để dùng chung
    private int currentStudentId;
    
    public void setStudentId(int id){
        this.currentStudentId = id;
        System.out.println("Main Controller đã nhận  ID: " + id);
        
        loadInventory();
        loadSuggestedRecipes();
        refreshAnalyzeSidebar();
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 1. Khởi tạo Service và DAO
        initServices();
        btnAddIngredient.setOnAction(e -> {
            openModal("/doanJava/view/AddIngredient.fxml", "Nhập Nguyên Liệu Vào Kho");
        });

        if (btnRecipes != null) {
            btnRecipes.setOnAction(e -> {
                openModal("/doanJava/view/AddRecipe.fxml", "Thêm Công Thức Mới");
            });
            
         if (btnSummary != null) {
            btnSummary.setOnAction(e -> {
                openModal("/doanJava/view/DailySummary.fxml", "Lịch Sử Dinh Dưỡng");
            });
        }
       }
    }

    private void initServices() {
        // Khởi tạo tất cả các DAO
        FoodDAO foodDAO = new FoodDAO();
        RecipeDAO recipeDAO = new RecipeDAO();
        this.inventoryDAO = new InventoryDAO(); // Gán vào biến class
        IngredientDAO ingredientDAO = new IngredientDAO();
        
        DailyMenuDAO dailyMenuDAO = new DailyMenuDAO();
        MenuFoodDAO menuFoodDAO = new MenuFoodDAO();
        
        // Khởi tạo Services
        this.foodService = new FoodService(foodDAO, recipeDAO, this.inventoryDAO, ingredientDAO);
        this.menuService = new MenuService(dailyMenuDAO, menuFoodDAO, recipeDAO, ingredientDAO,this.inventoryDAO);
    }

    // --- HÀM 1: Load danh sách kho (ĐÃ SỬA DÙNG DỮ LIỆU THẬT & GIAO DIỆN ĐẸP) ---
    private void loadInventory() {
        // Cài đặt giao diện dòng (Cell Factory) dùng InventoryListCell
        inventoryListView.setCellFactory(param -> new InventoryListCell());

        // Xóa dữ liệu cũ
        inventoryListView.getItems().clear();
        
        // Lấy dữ liệu thật từ DB thông qua inventoryDAO
        List<StudentInventory> myInventory = inventoryDAO.getInventory(currentStudentId);
        
        if (myInventory != null && !myInventory.isEmpty()) {
            inventoryListView.getItems().addAll(myInventory);
        } else {
            System.out.println("Kho đang trống.");
        }
    }

    // --- HÀM 2: Load Card món ăn & Tính tổng dinh dưỡng ---
    private void loadSuggestedRecipes() {
        recipesContainer.getChildren().clear();
        
        List<Food> foods = foodService.getSuggestedFoods(currentStudentId);
        
        // (Giữ nguyên đoạn fake data nếu có)
        if (foods.isEmpty()) {
            foods.add(new Food(1, "Beefsteak", "Áp chảo..."));
            // ...
        }

        for (Food food : foods) {
            NutritionInfo nutrition = foodService.getNutrition(food.getFoodId());
            if (nutrition.calories == 0) {
                nutrition = new NutritionInfo(350, 25, 15, 10);
            }

            RecipeCard card = new RecipeCard(food, nutrition, (mealType, selectedFood) -> {
                System.out.println("User chọn nấu món: " + selectedFood.getName() + " vào " + mealType);

                // 1. GỌI SERVICE ĐỂ TRỪ KHO & LƯU MENU
                menuService.logMeal(currentStudentId, selectedFood.getFoodId(), mealType);

                // 2. Hiện thông báo
                showAlert("Thành công", "Đã nấu món '" + selectedFood.getName() + "'. Kho đã được cập nhật!");

                // 3. Cập nhật thống kê (Biểu đồ tròn)
                refreshAnalyzeSidebar();
                
                // 4. --- QUAN TRỌNG: CẬP NHẬT LẠI KHO TRÊN GIAO DIỆN ---
                loadInventory(); 
                
                // 5. Cập nhật lại danh sách gợi ý (Món nào hết nguyên liệu sẽ tự ẩn đi)
                loadSuggestedRecipes();
            });

            recipesContainer.getChildren().add(card);
        }
    }

    // --- HÀM 3: Cập nhật Sidebar thống kê ---
    private void refreshAnalyzeSidebar() {
        if (menuService == null) return;
        DailyMenu todayMenu = menuService.getTodayNutrition(currentStudentId);
        updateAnalyzeLabels(
            todayMenu.getTotalCalories(), 
            todayMenu.getTotalProtein(), 
            todayMenu.getTotalCarbs(), 
            todayMenu.getTotalFat()
        );
    }

    private void updateAnalyzeLabels(double cal, double pro, double carb, double fat) {
        // 1. Cập nhật Text
        lblTotalCalories.setText(String.format("🔥 Kcal: %.0f", cal));
        lblTotalProtein.setText(String.format("🥩 Protein: %.0f g", pro));
        lblTotalCarbs.setText(String.format("🍞 Carbs: %.0f g", carb));
        lblTotalFat.setText(String.format("🥑 Fat: %.0f g", fat));

        // 2. Cập nhật PieChart
        if (macroPieChart != null) {
            ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList(
                new PieChart.Data("Protein", pro),
                new PieChart.Data("Carbs", carb),
                new PieChart.Data("Fat", fat)
            );
            macroPieChart.setData(pieData);
        }
    }
    private void openModal(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            
            // Chặn cửa sổ chính, bắt buộc xử lý xong form con mới được quay lại
            stage.initModality(Modality.APPLICATION_MODAL); 
            
            // Chờ cho đến khi cửa sổ con đóng lại
            stage.showAndWait();
            
            // --- SAU KHI ĐÓNG FORM CON ---
            // 1. Load lại kho (Để thấy nguyên liệu vừa nhập)
            loadInventory(); 
            
            // 2. Load lại gợi ý món ăn (QUAN TRỌNG: Để thấy món vừa thêm nếu đủ nguyên liệu)
            loadSuggestedRecipes();
            
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Lỗi không tìm thấy file FXML: " + fxmlPath);
            showAlert("Lỗi", "Không tìm thấy file: " + fxmlPath);
        }
    }
    
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.show();
    }
}
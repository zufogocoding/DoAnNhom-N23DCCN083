package doanJava.Controller;

import doanJava.DAO.*;
import doanJava.Model.DailyMenu;
import doanJava.service.MenuService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.List;

public class DailySummaryController {

    @FXML private DatePicker datePicker;
    @FXML private ListView<String> lvMeals;
    @FXML private PieChart summaryPieChart;
    @FXML private Label lblSumCal, lblSumPro, lblSumCarb, lblSumFat;
    @FXML private Button btnClose;

    private MenuService menuService;
    private int currentStudentId = 1;

    @FXML
    public void initialize() {
        // Khởi tạo Service (giống MainController)
        initServices();

        // Mặc định chọn ngày hôm nay
        datePicker.setValue(LocalDate.now());
        loadDataForDate(LocalDate.now());

        // Sự kiện khi đổi ngày
        datePicker.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                loadDataForDate(newVal);
            }
        });

        btnClose.setOnAction(e -> ((Stage) btnClose.getScene().getWindow()).close());
    }

    private void initServices() {
        // Khởi tạo lại bộ Service/DAO (để đơn giản)
        // Trong dự án lớn nên dùng Dependency Injection để tránh new nhiều lần
        FoodDAO f = new FoodDAO();
        RecipeDAO r = new RecipeDAO();
        InventoryDAO i = new InventoryDAO();
        IngredientDAO ing = new IngredientDAO();
        DailyMenuDAO dm = new DailyMenuDAO();
        MenuFoodDAO mf = new MenuFoodDAO();
        this.menuService = new MenuService(dm, mf, r, ing, i);
    }

    private void loadDataForDate(LocalDate date) {
        // 1. Lấy danh sách món ăn
        List<String> mealHistory = menuService.getHistoryByDate(currentStudentId, date);
        lvMeals.getItems().clear();
        if (mealHistory.isEmpty()) {
            lvMeals.getItems().add("Chưa có dữ liệu ăn uống cho ngày này.");
        } else {
            lvMeals.getItems().addAll(mealHistory);
        }

        // 2. Lấy chỉ số dinh dưỡng tổng
        // Lưu ý: Hàm getTodayNutrition của bạn đang hardcode LocalDate.now()
        // Bạn nên sửa MenuService để có thêm hàm getNutritionByDate(id, date)
        // Ở đây mình giả sử bạn dùng lại hàm findOrCreate của DAO để lấy data theo ngày bất kỳ
        DailyMenuDAO dao = new DailyMenuDAO(); // Gọi tạm DAO
        DailyMenu menu = dao.findOrCreate(currentStudentId, date.toString());

        if (menu != null) {
            updateChartAndLabels(menu.getTotalCalories(), menu.getTotalProtein(), menu.getTotalCarbs(), menu.getTotalFat());
        } else {
            updateChartAndLabels(0, 0, 0, 0);
        }
    }

    private void updateChartAndLabels(double cal, double pro, double carb, double fat) {
        // Update Text
        lblSumCal.setText(String.format("🔥 Calories: %.0f", cal));
        lblSumPro.setText(String.format("🥩 Protein: %.0f g", pro));
        lblSumCarb.setText(String.format("🍞 Carbs: %.0f g", carb));
        lblSumFat.setText(String.format("🥑 Fat: %.0f g", fat));

        // Update Chart
        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList(
                new PieChart.Data("Protein", pro),
                new PieChart.Data("Carbs", carb),
                new PieChart.Data("Fat", fat)
        );
        summaryPieChart.setData(pieData);
    }
}
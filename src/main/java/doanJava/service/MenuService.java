package doanJava.service;

import doanJava.DAO.DailyMenuDAO;
import doanJava.DAO.IngredientDAO;
import doanJava.DAO.MenuFoodDAO;
import doanJava.DAO.RecipeDAO;
import doanJava.Model.Ingredient;
import doanJava.Model.RecipeIngredient;
import doanJava.Model.DailyMenu;
import java.time.LocalDate;
import java.util.List;

public class MenuService {
    private final DailyMenuDAO dailyMenuDAO;
    private final MenuFoodDAO menuFoodDAO;
    private final RecipeDAO recipeDAO;
    private final IngredientDAO ingredientDAO;
    
    public MenuService(DailyMenuDAO dailyMenuDAO, MenuFoodDAO menuFoodDAO, RecipeDAO recipeDAO, IngredientDAO ingredientDAO) {
        this.dailyMenuDAO = dailyMenuDAO;
        this.menuFoodDAO = menuFoodDAO;
        this.recipeDAO = recipeDAO;
        this.ingredientDAO = ingredientDAO;
    }
    
    // Đổi thành public để Controller gọi được
    public void logMeal(int studentID, int foodId, String mealType){
        String today = LocalDate.now().toString();
        
        // 1. Lấy menu hiện tại (để lấy số liệu cũ)
        DailyMenu menu = dailyMenuDAO.findOrCreate(studentID, today);
        if (menu == null) {
            System.err.println("Error: can not find or create menu");
            return;
        }

        // 2. Thêm món vào bảng chi tiết
        menuFoodDAO.addFood(menu.getMenuId(), foodId, mealType);
        
        // 3. Tính dinh dưỡng của món MỚI thêm
        double foodCal = 0, foodPro = 0, foodCarb = 0, foodFat = 0;
        
        List<RecipeIngredient> recipe = recipeDAO.getIngredients(foodId);
        for(RecipeIngredient req: recipe){
            Ingredient ing  = ingredientDAO.getIngredient(req.getIngredientId());
            if (ing != null) {
                double quantity = req.getQuantity();
                foodCal  += ing.getCaloriesPerUnit() * quantity;
                foodPro  += ing.getProteinPerUnit() * quantity;
                foodCarb += ing.getCarbsPerUnit() * quantity;
                foodFat  += ing.getFatPerUnit() * quantity;
            }
        }

        // 4. CỘNG DỒN VÀO TỔNG CŨ (Fix lỗi logic ở đây)
        double newTotalCal = menu.getTotalCalories() + foodCal;
        double newTotalPro = menu.getTotalProtein() + foodPro;
        double newTotalCarb = menu.getTotalCarbs() + foodCarb;
        double newTotalFat = menu.getTotalFat() + foodFat;

        // 5. Cập nhật lại Database
        dailyMenuDAO.updateNutrition(menu.getMenuId(), newTotalCal, newTotalPro, newTotalCarb, newTotalFat);
        
        System.out.println("Đã ghi lại món ăn " + foodId + " vào bữa " + mealType);
    }
    
    public DailyMenu getTodayNutrition(int studentId) {
        String today = LocalDate.now().toString();
        // Sửa lại chỗ này một chút: findOrCreate luôn để đảm bảo không null
        DailyMenu menu = dailyMenuDAO.findOrCreate(studentId, today); 
        return menu;
    }
}
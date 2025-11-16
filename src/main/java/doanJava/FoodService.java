package doanJava;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FoodService {

    private final FoodDAO foodDAO;
    private final RecipeDAO recipeDAO;
    private final InventoryDAO inventoryDAO;
    private final IngredientDAO ingredientDAO; 

    public FoodService(FoodDAO foodDAO, RecipeDAO recipeDAO, InventoryDAO inventoryDAO, IngredientDAO ingredientDAO) {
        this.foodDAO = foodDAO;
        this.recipeDAO = recipeDAO;
        this.inventoryDAO = inventoryDAO;
        this.ingredientDAO = ingredientDAO;
    }

    public List<Food> getSuggestedFoods(int studentId) {
        // Map<IngredientId, Quantity>
        Map<Integer, Double> inventoryMap = new HashMap<>();
        List<StudentInventory> inventory = inventoryDAO.getInventory(studentId);
        for (StudentInventory item : inventory) {
            inventoryMap.put(item.getIngredientId(), item.getQuantity());
        }
        List<Food> allFoods = foodDAO.getAllFoods();
        List<Food> suggestions = new ArrayList<>();
        for (Food food : allFoods) {
            boolean canCook = true;

            List<RecipeIngredient> requiredIngredients = recipeDAO.getIngredients(food.getFoodId());
            for (RecipeIngredient req : requiredIngredients) {
                int requiredId = req.getIngredientId();
                double requiredQuantity = req.getQuantity();
                if (!inventoryMap.containsKey(requiredId)) {
                    canCook = false; 
                    break; 
                }
                if (inventoryMap.get(requiredId) < requiredQuantity) {
                    canCook = false;
                    break; 
                }
            }
            if (canCook) {
                suggestions.add(food); 
            }
        }

        return suggestions;
    }

    public Food addFood(String name, String instructions) {
        Food newFood = new Food(0, name, instructions);
        return foodDAO.addFood(newFood);
    }

    public void addRecipe(int foodId, int ingredientId, double quantity) {
        recipeDAO.addIngredientToRecipe(foodId, ingredientId, quantity);
    }

    public NutritionInfo getNutrition(int foodId) {
        List<RecipeIngredient> recipe = recipeDAO.getIngredients(foodId);
        List<Ingredient> allIngredients = ingredientDAO.getAllIngredients(); 

        Map<Integer, Ingredient> ingredientMap = new HashMap<>();
        for (Ingredient ing : allIngredients) {
            ingredientMap.put(ing.getIngredientId(), ing);
        }

        double totalCalories = 0;
        double totalProtein = 0;
        double totalCarbs = 0;
        double totalFat = 0;

        for (RecipeIngredient req : recipe) {
            Ingredient ing = ingredientMap.get(req.getIngredientId());
            if (ing != null) {
                double quantity = req.getQuantity();
                totalCalories += ing.getCaloriesPerUnit() * quantity;
                totalProtein += ing.getProteinPerUnit() * quantity;
                totalCarbs += ing.getCarbsPerUnit() * quantity;
                totalFat += ing.getFatPerUnit() * quantity;
            }
        }
        return new NutritionInfo(totalCalories, totalProtein, totalCarbs, totalFat);
    }
 
    public static class NutritionInfo {
        public final double calories;
        public final double protein;
        public final double carbs;
        public final double fat;

        public NutritionInfo(double calories, double protein, double carbs, double fat) {
            this.calories = calories;
            this.protein = protein;
            this.carbs = carbs;
            this.fat = fat;
        }
    }
}

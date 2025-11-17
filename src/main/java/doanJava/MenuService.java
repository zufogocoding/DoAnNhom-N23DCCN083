/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package doanJava;

import java.time.LocalDate;
import java.util.*;
/**
 *
 * @author phamt
 */
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
    
    private void logMeal(int studentID,int foodId,String mealType){
        String today = LocalDate.now().toString();
        DailyMenu menu = dailyMenuDAO.findOrCreate(studentID, today);
        if (menu==null) {
            System.err.println("Error: can not find or create menu");
            return;
        }
        menuFoodDAO.addFood(menu.getMenuId(), foodId, mealType);
        double calories = 0,protein = 0,carbs = 0, fat = 0;
        
        List<RecipeIngredient> recipe = recipeDAO.getIngredients(foodId);
        for(RecipeIngredient req: recipe){
            Ingredient ing  = ingredientDAO.getIngredient(req.getIngredientId());
            if (ing!=null) {
                double quantity = req.getQuantity();
                calories += ing.getCaloriesPerUnit() *quantity;
                protein += ing.getProteinPerUnit() *quantity;
                carbs += ing.getCarbsPerUnit()*quantity;
                fat += ing.getFatPerUnit()*quantity;
            }
        }
        dailyMenuDAO.updateNutrition(menu.getMenuId(), calories, protein, carbs, fat);
        System.out.println("Da ghi lai mon an "+foodId+"as "+mealType);
    }
}

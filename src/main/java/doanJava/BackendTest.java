/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package doanJava;

import doanJava.DAO.DailyMenuDAO;
import doanJava.DAO.FoodDAO;
import doanJava.DAO.IngredientDAO;
import doanJava.DAO.InventoryDAO;
import doanJava.DAO.MenuFoodDAO;
import doanJava.DAO.RecipeDAO;
import doanJava.DAO.StudentDAO;
import doanJava.Model.DailyMenu;
import doanJava.Model.Student;
import java.util.ArrayList;
import java.sql.SQLException;

public class BackendTest {

    public static void main(String[] args) {
        System.out.println("......");

        StudentDAO studentDAO = new StudentDAO();
        IngredientDAO ingredientDAO = new IngredientDAO();
        FoodDAO foodDAO = new FoodDAO();
        RecipeDAO recipeDAO = new RecipeDAO();
        InventoryDAO inventoryDAO = new InventoryDAO();
        DailyMenuDAO dailyMenuDAO = new DailyMenuDAO();
        MenuFoodDAO menuFoodDAO = new MenuFoodDAO();

        try {

            //  Task 1:   
            // 1.1 Tạo Student
            Student st = new Student(0, "Tu IT Test", 170, 65, 2000, 150, 200, 60);
            Student savedSt = studentDAO.addStudent(st);
            int studentId = savedSt.getStudentId();
            System.out.println("[Task 1] Đã tạo Student ID: " + studentId);

            // 1.2 Khai báo các ID 
            int eggId = 1;       
            int riceId = 2;     
            int friedRiceId = 1;

            // 1.3 Tạo Công thức
            recipeDAO.addIngredientToRecipe(friedRiceId, eggId, 2.0);
            recipeDAO.addIngredientToRecipe(friedRiceId, riceId, 200.0);
            System.out.println("[Task 1]" + friedRiceId);

            // Task 2: Thêm đồ vào kho
            //inventoryDAO.addStock(studentId, eggId, 10.0);
            //inventoryDAO.addStock(studentId, riceId, 5000.0);
            System.out.println("[Task 2] ");

            // ✅ Task 4: Log Meal 
            String today = "2025-11-25";
            //haha
            // 4.1 Tìm hoặc tạo Menu 
            DailyMenu menu = dailyMenuDAO.findOrCreate(studentId, today);
            System.out.println("[Task 4]" + menu.getMenuId());
            
            // 4.2 Log món ăn
            menuFoodDAO.addFood(menu.getMenuId(), friedRiceId, "Breakfast");
            menuFoodDAO.addFood(menu.getMenuId(), friedRiceId, "Lunch");
            menuFoodDAO.addFood(menu.getMenuId(), friedRiceId, "Dinner");
            
            System.out.println("[Task 4]");

            // 4.3 Update dinh dưỡng tổng
            dailyMenuDAO.updateNutrition(menu.getMenuId(), 1500, 60, 180, 45);
            System.out.println("[Task 4]");
            System.out.println("END");
        } catch (Exception e) {
            System.err.println(" CÓ LỖI XẢY RA: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
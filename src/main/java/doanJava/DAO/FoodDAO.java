/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package doanJava.DAO;

import doanJava.Model.Food;
import doanJava.util.SqliteHelper;
import java.util.*;
import java.sql.*;
/**
 *
 * @author phamt
 */
public class FoodDAO {
    /*          CREATE TABLE IF NOT EXISTS Food (
            food_id INTEGER PRIMARY KEY AUTOINCREMENT,
            name TEXT NOT NULL UNIQUE,
            instructions TEXT
            );*/
    public Food addFood(Food food){
        String sql = "INSERT INTO Food(name, instructions) VALUES(?,?)";
        
        try (Connection conn = SqliteHelper.getConnection(); PreparedStatement pstmt= conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)){
            pstmt.setString(1,food.getName());
            pstmt.setString(2,food.getInstructions());
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()){
                food.setFoodId(rs.getInt(1));
            }
            return food;
        } catch (SQLException e){
            System.err.println("Error adding food "+e.getMessage());
            return null;
        }
    }
    
    public List<Food> getAllFoods() {
        List<Food> foods = new ArrayList<>();
        String sql = "SELECT * FROM Food";
        
        try (Connection conn = SqliteHelper.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                foods.add(mapResultSetToFood(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error Fetching food: " + e.getMessage());
        }
        return foods;
    }
    
    private Food mapResultSetToFood(ResultSet rs) throws SQLException{
            return new Food(
                rs.getInt("food_id"),
                rs.getString("name"),
                rs.getString("instructions")
            );
        }
}

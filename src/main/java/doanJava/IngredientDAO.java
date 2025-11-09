/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package doanJava;
import java.sql.*;
import java.util.*;
/**
 *
 * @author phamt
 */
public class IngredientDAO {
    public Ingredient addIngredient(Ingredient ingredient){
        String sql = "INSERT INTO Ingredient(name,unit,caloriesPerUnit,proteinPerUnit,carbsPerUnit,fatPerUnit) VALUES(?,?,?,?,?,?,?)";
        try (Connection conn = SqliteHelper.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1,ingredient.getName());
            pstmt.setString(2, ingredient.getUnit());
            pstmt.setDouble(3, ingredient.getCaloriesPerUnit());
            pstmt.setDouble(4, ingredient.getCaloriesPerUnit());
            pstmt.setDouble(5, ingredient.getCaloriesPerUnit());
            pstmt.setDouble(6, ingredient.getCaloriesPerUnit());
            pstmt.executeUpdate();
            
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next())  ingredient.setIngredientId(rs.getInt(1));
            return ingredient;    
        } catch(SQLException e) {
            System.err.println("Error adding ingredient");
            return null;  
        } 
    }
}

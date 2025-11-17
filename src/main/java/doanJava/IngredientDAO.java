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
        String sql = "INSERT INTO Ingredient(name,unit,calories_per_unit,protein_per_unit,carbs_per_Unit,fat_Per_Unit) VALUES(?,?,?,?,?,?)";
        try (Connection conn = SqliteHelper.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1,ingredient.getName());
            pstmt.setString(2, ingredient.getUnit());
            pstmt.setDouble(3, ingredient.getCaloriesPerUnit());
            pstmt.setDouble(4, ingredient.getProteinPerUnit());
            pstmt.setDouble(5, ingredient.getCarbsPerUnit());
            pstmt.setDouble(6, ingredient.getFatPerUnit());
            pstmt.executeUpdate();
            
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next())  ingredient.setIngredientId(rs.getInt(1));
            //System.out.println("Adding xong roif  ne");
            return ingredient;    
        } catch(SQLException e) {
            System.err.println("Error adding ingredient "+e.getMessage());
            return null;  
        } 
    }
    public Ingredient getIngredient(int id){
        String sql = "SELECT * FROM Ingredient WHERE IngredientID = ?";
        try (Connection conn = SqliteHelper.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return MapResultSetToIngredient(rs);
        }
        catch(SQLException e){
            System.err.println("Error fetching ingredient " + e.getMessage());
        }
        return null;
    }
    
    public ArrayList<Ingredient> getAllIngredients(){
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        String sql = "SELECT * FROM Ingredient";
        try (Connection conn = SqliteHelper.getConnection(); Statement stmt = conn.createStatement();ResultSet rs = stmt.executeQuery(sql)){
            while(rs.next()) ingredients.add(MapResultSetToIngredient(rs));
        }
        catch(SQLException e){
            System.err.println("Error fetching Ingredients");
        }
        return ingredients;
    }
    /*
            CREATE TABLE IF NOT EXISTS Ingredient (
                ingredient_id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL UNIQUE,
                unit TEXT NOT NULL,
                calories_per_unit REAL,
                protein_per_unit REAL,
                carbs_per_unit REAL,
                fat_per_unit REAL
            );
            */
    
    private Ingredient MapResultSetToIngredient(ResultSet rs) throws SQLException{
        return new Ingredient(
        rs.getInt("ingredient_id"), rs.getString("name"), rs.getString("unit"), 
        rs.getDouble("calories_per_unit"),rs.getDouble("protein_per_unit"),rs.getDouble("carbs_per_unit"),rs.getDouble("fat_per_unit"));
    }
    
}

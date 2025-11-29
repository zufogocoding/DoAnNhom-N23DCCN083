package doanJava.DAO;

import doanJava.Model.Ingredient;
import doanJava.utils.SqliteHelper;
import java.sql.*;
import java.util.*;

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
            return ingredient;    
        } catch(SQLException e) {
            // Nếu lỗi do trùng tên, không in lỗi đỏ mà trả về null để Controller xử lý
            if (!e.getMessage().contains("UNIQUE constraint failed")) {
                System.err.println("Error adding ingredient "+e.getMessage());
            }
            return null;  
        } 
    }

    public Ingredient getIngredient(int id){
        String sql = "SELECT * FROM Ingredient WHERE ingredient_id = ?"; // Lưu ý: Sửa IngredientID thành ingredient_id cho khớp DB
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

    // --- ĐOẠN CODE MỚI THÊM VÀO ĐÂY ---
    public Ingredient getIngredientByName(String name) {
        String sql = "SELECT * FROM Ingredient WHERE name = ?";
        try (Connection conn = SqliteHelper.getConnection(); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return MapResultSetToIngredient(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching ingredient by name: " + e.getMessage());
        }
        return null;
    }
    // ----------------------------------

    private Ingredient MapResultSetToIngredient(ResultSet rs) throws SQLException{
        return new Ingredient(
        rs.getInt("ingredient_id"), rs.getString("name"), rs.getString("unit"), 
        rs.getDouble("calories_per_unit"),rs.getDouble("protein_per_unit"),rs.getDouble("carbs_per_unit"),rs.getDouble("fat_per_unit"));
    }
}
package doanJava.DAO;

import doanJava.Model.Ingredient;
import doanJava.utils.SqliteHelper;
import java.sql.*;
import java.util.*;

public class IngredientDAO {
    
    public Ingredient getIngredientByName(String nameToCheck) { 
        String sql = "SELECT * FROM Ingredient";
        String searchName = nameToCheck.trim(); 

        try (Connection conn = SqliteHelper.getConnection(); 
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                String dbName = rs.getString("name");
                if (dbName.trim().equalsIgnoreCase(searchName)) {
                    return MapResultSetToIngredient(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi tìm kiếm tên: " + e.getMessage());
        }
        return null; // Không tìm thấy
    }

    // --- 2. THÊM NGUYÊN LIỆU (Nâng cấp: Kiểm tra trước khi thêm) ---
    public Ingredient addIngredient(Ingredient ingredient){
        // BƯỚC A: Kiểm tra xem tên này đã có trong DB chưa
        Ingredient existing = getIngredientByName(ingredient.getName());
        
        if (existing != null) {
            System.out.println("Nguyên liệu '" + ingredient.getName() + "' đã tồn tại (ID: " + existing.getIngredientId() + "). Sử dụng lại ID cũ.");
            // Trả về luôn đối tượng cũ (để lấy ID) chứ không thêm mới
            return existing;
        }

        // BƯỚC B: Nếu chưa có thì mới INSERT
        String sql = "INSERT INTO Ingredient(name, unit, calories_per_unit, protein_per_unit, carbs_per_Unit, fat_Per_Unit) VALUES(?,?,?,?,?,?)";
        
        try (Connection conn = SqliteHelper.getConnection(); 
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, ingredient.getName().trim()); // Trim tên cho sạch
            pstmt.setString(2, ingredient.getUnit());
            pstmt.setDouble(3, ingredient.getCaloriesPerUnit());
            pstmt.setDouble(4, ingredient.getProteinPerUnit());
            pstmt.setDouble(5, ingredient.getCarbsPerUnit());
            pstmt.setDouble(6, ingredient.getFatPerUnit());
            
            pstmt.executeUpdate();
            
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                ingredient.setIngredientId(rs.getInt(1));
            }
            return ingredient;
            
        } catch(SQLException e) {
            System.err.println("Lỗi thêm nguyên liệu: "+e.getMessage());
            return null;  
        } 
    }

    public Ingredient getIngredient(int id){
        String sql = "SELECT * FROM Ingredient WHERE ingredient_id = ?";
        try (Connection conn = SqliteHelper.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return MapResultSetToIngredient(rs);
        }
        catch(SQLException e){ e.printStackTrace(); }
        return null;
    }
    
    public ArrayList<Ingredient> getAllIngredients(){
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        String sql = "SELECT * FROM Ingredient";
        try (Connection conn = SqliteHelper.getConnection(); Statement stmt = conn.createStatement();ResultSet rs = stmt.executeQuery(sql)){
            while(rs.next()) ingredients.add(MapResultSetToIngredient(rs));
        } catch(SQLException e){ e.printStackTrace(); }
        return ingredients;
    }

    private Ingredient MapResultSetToIngredient(ResultSet rs) throws SQLException{
        return new Ingredient(
        rs.getInt("ingredient_id"), rs.getString("name"), rs.getString("unit"), 
        rs.getDouble("calories_per_unit"),rs.getDouble("protein_per_unit"),rs.getDouble("carbs_per_unit"),rs.getDouble("fat_per_unit"));
    }
}
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package doanJava;
/**     
 *
 * @author congt
 */
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class RecipeDAO {
    public void addIngredientToRecipe(int foodId, int ingredientId, double quantity) {
        String sql = "INSERT OR REPLACE INTO Recipe_Ingredient (food_id, ingredient_id, quantity) VALUES (?, ?, ?)";
        try (Connection conn = SqliteHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, foodId);
            pstmt.setInt(2, ingredientId);
            pstmt.setDouble(3, quantity);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println(" Loi khi them nguyen lieu vao cong thuc: " + e.getMessage());
        }
    }
    public List<RecipeIngredient> getIngredients(int foodId) {
        List<RecipeIngredient> ingredients = new ArrayList<>();
        String sql = "SELECT ingredient_id, quantity FROM Recipe_Ingredient WHERE food_id = ?";
        try (Connection conn = SqliteHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, foodId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                RecipeIngredient ri = new RecipeIngredient();
                ri.setFoodId(foodId);
                ri.setIngredientId(rs.getInt("ingredient_id"));
                ri.setQuantity(rs.getDouble("quantity"));
                ingredients.add(ri);
            }
        } catch (SQLException e) {
            System.err.println("Loi khi lay nguyen lieu cua cong thuc: " + e.getMessage());
        }
        return ingredients;
    }
    
    private RecipeIngredient mapResultSetToRecipe(ResultSet rs)  throws SQLException{
        return new RecipeIngredient(
                rs.getInt("food_id"),
                rs.getInt("ingredient_id"),
                rs.getDouble("quantity")
        );
    }
    
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package doanJava.DAO;

/**
 *
 * @author congt
 */
import doanJava.Model.MenuFood;
import doanJava.utils.SqliteHelper;
import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;

public class MenuFoodDAO {
    public MenuFood addFood(int menuId, int foodId, String mealType) {
        String sql = "INSERT INTO Menu_Food(menu_id, food_id, meal_type, log_time) VALUES(?,?,?,?)";

        try (Connection conn = SqliteHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, menuId);
            pstmt.setInt(2, foodId);
            pstmt.setString(3, mealType);
            pstmt.setString(4, LocalTime.now().toString());
            pstmt.executeUpdate();
            // no need to retry tu oi
        } catch (SQLException e) {
            System.err.println("Loi khi them mon an vao menu: " + e.getMessage());
        }
        return null; 
    }
    public ArrayList<MenuFood> getFoods(int menuId) {
        ArrayList<MenuFood> foods = new ArrayList<>();
        String sql = "SELECT * FROM Menu_Food WHERE menu_id = ?";

        try (Connection conn = SqliteHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, menuId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                foods.add(mapResultSetToMenuFood(rs));
            }

        } catch (SQLException e) {
            System.err.println("Loi khi lay danh sach mon an cua menu: " + e.getMessage());
        }
        return foods;
    }
    private MenuFood mapResultSetToMenuFood(ResultSet rs) throws SQLException {
        return new MenuFood(
                rs.getInt("log_id"),
                rs.getInt("menu_id"),
                rs.getInt("food_id"),
                rs.getString("meal_type"),
                rs.getString("log_time")
        );
    }
}

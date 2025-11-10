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

public class MenuFoodDAO {
    public MenuFood addFood(int menuId, int foodId, String mealType, Time logTime) {
        String sql = "INSERT INTO Menu_Food(menu_id, food_id, meal_type, log_time) VALUES(?,?,?,?)";

        try (Connection conn = SqliteHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, menuId);
            pstmt.setInt(2, foodId);
            pstmt.setString(3, mealType);
            pstmt.setTime(4, logTime);
            
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    int logId = rs.getInt(1);
                    MenuFood newEntry = new MenuFood();
                    newEntry.setLogId(logId);
                    newEntry.setMenuId(menuId);
                    newEntry.setFoodId(foodId);
                    newEntry.setMealType(mealType);
                    newEntry.setLogTime(logTime);
                    return newEntry;
                }
            }

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
        MenuFood entry = new MenuFood();
        entry.setLogId(rs.getInt("log_id"));
        entry.setMenuId(rs.getInt("menu_id"));
        entry.setFoodId(rs.getInt("food_id"));
        entry.setMealType(rs.getString("meal_type"));
        entry.setLogTime(rs.getTime("log_time"));
        return entry;
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package doanJava.DAO;

import doanJava.Model.Food;
import doanJava.utils.SqliteHelper;
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
    public int addFood(Food food) {
    String sql = "INSERT INTO Food(name, instructions) VALUES(?, ?)";
    int generatedId = -1; // Mặc định là -1 (Lỗi)

    // Lưu ý: Thêm tham số Statement.RETURN_GENERATED_KEYS
    try (Connection conn = DriverManager.getConnection("jdbc:sqlite:src/doanJava/data.db"); // Hoặc dùng class Connection của bạn
         PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

        pstmt.setString(1, food.getName());
        pstmt.setString(2, food.getInstructions()); // Giả sử model Food có getInstructions

        int affectedRows = pstmt.executeUpdate();

        if (affectedRows > 0) {
            // Lấy ID vừa sinh ra
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedId = rs.getInt(1); // Lấy cột đầu tiên (chính là ID)
                }
            }
        }
    } catch (SQLException e) {
        System.out.println("Lỗi thêm món ăn: " + e.getMessage());
        e.printStackTrace();
    }

    return generatedId; // Trả về ID (ví dụ: 1, 2, 3...) hoặc -1 nếu lỗi
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

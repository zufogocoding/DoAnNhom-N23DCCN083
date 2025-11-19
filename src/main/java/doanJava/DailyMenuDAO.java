/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package doanJava;
/**
 *
 * @author phamt
 */
import java.sql.*;

public class DailyMenuDAO {
    public DailyMenu findOrCreate(int studentId, String date) {
        String selectSql = "SELECT * FROM Daily_Menu WHERE student_id = ? AND date = ?";
        String insertSql = "INSERT INTO Daily_Menu(student_id, date) VALUES(?,?)";

        try (Connection conn = SqliteHelper.getConnection()) {
            try (PreparedStatement selectStmt = conn.prepareStatement(selectSql)) {
                selectStmt.setInt(1, studentId);
                selectStmt.setString(2, date);
                ResultSet rs = selectStmt.executeQuery();
                if (rs.next()) {
                    return mapResultSetToDailyMenu(rs);
                }
            }
            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
                insertStmt.setInt(1, studentId);
                insertStmt.setString(2, date);
                insertStmt.executeUpdate();
                
                ResultSet rs = insertStmt.getGeneratedKeys();
                if (rs.next()) {
                    int newMenuId = rs.getInt(1);
                    return new DailyMenu(newMenuId, studentId, date, 0, 0, 0, 0);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error findOrCreate DailyMenu: " + e.getMessage());
        }
        return null;
    }
    
    public void updateNutrition(int menuId, double calories, double protein, double carbs, double fat) {
        String sql = "UPDATE Daily_Menu SET total_calories = ?, total_protein = ?, total_carbs = ?, total_fat = ? WHERE menu_id = ?";

        try (Connection conn = SqliteHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, calories);
            pstmt.setDouble(2, protein);
            pstmt.setDouble(3, carbs);
            pstmt.setDouble(4, fat);
            pstmt.setInt(5, menuId);
            
            int rowsUpdated = pstmt.executeUpdate();
            /*if (rowsUpdated > 0) {
                System.out.println("Da cap nhat dinh duong cho menu_id: " + menuId);
            }*/

        } catch (SQLException e) {
            System.err.println("Loi khi cap nhat dinh duong DailyMenu: " + e.getMessage());
        }
    }
    public DailyMenu getMenu(int menuId,String date) {
        String sql = "SELECT * FROM Daily_Menu WHERE menu_id = ? AND date = ?";
        
        try (Connection conn = SqliteHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, menuId);
            pstmt.setString(2,date);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToDailyMenu(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Loi khi lay DailyMenu: " + e.getMessage());
        }
        return null;
    }
    private DailyMenu mapResultSetToDailyMenu(ResultSet rs) throws SQLException {
        return new DailyMenu(
                rs.getInt("menu_id"),
                rs.getInt("student_id"),
                rs.getString("date"),
                rs.getDouble("total_calories"),
                rs.getDouble("total_protein"),
                rs.getDouble("total_carbs"),
                rs.getDouble("total_fat")
        );
    }
}

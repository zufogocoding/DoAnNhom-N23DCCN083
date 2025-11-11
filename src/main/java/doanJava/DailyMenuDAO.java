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
        String findSql = "SELECT * FROM Daily_Menu WHERE student_id = ? AND date = ?";
        
        try (Connection conn = SqliteHelper.getConnection();
             PreparedStatement findStmt = conn.prepareStatement(findSql)) {
            findStmt.setInt(1, studentId);
            findStmt.setString(2, date);
            ResultSet rs = findStmt.executeQuery();

            if (rs.next()) {
                System.out.println("Da tim thay menu cho ngay: " + date);
                return mapResultSetToDailyMenu(rs);
            } else {
                System.out.println("Khong tim thay, dang tao menu moi cho ngay: " + date);
                return createNewMenu(conn, studentId, date);
            }

        } catch (SQLException e) {
            System.err.println("Loi khi tim hoac tao DailyMenu: " + e.getMessage());
            return null;
        }
    }
    private DailyMenu createNewMenu(Connection conn, int studentId, String date) throws SQLException {
        String createSql = "INSERT INTO Daily_Menu(student_id, date, total_calories, total_protein, total_carbs, total_fat) VALUES(?,?, 0, 0, 0, 0)";
        
        try (PreparedStatement createStmt = conn.prepareStatement(createSql, Statement.RETURN_GENERATED_KEYS)) {
            
            createStmt.setInt(1, studentId);
            createStmt.setString(2, date);
            int rowsAffected = createStmt.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet generatedKeys = createStmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int newMenuId = generatedKeys.getInt(1);
                    DailyMenu newMenu = new DailyMenu();
                    newMenu.setMenuId(newMenuId);
                    newMenu.setStudentId(studentId);
                    newMenu.setDate(date); 
                    newMenu.setTotalCalories(0);
                    newMenu.setTotalProtein(0);
                    newMenu.setTotalCarbs(0);
                    newMenu.setTotalFat(0);
                    return newMenu;
                }
            }
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
            if (rowsUpdated > 0) {
                System.out.println("Da cap nhat dinh duong cho menu_id: " + menuId);
            }

        } catch (SQLException e) {
            System.err.println("Loi khi cap nhat dinh duong DailyMenu: " + e.getMessage());
        }
    }
    public DailyMenu getMenu(int menuId) {
        String sql = "SELECT * FROM Daily_Menu WHERE menu_id = ?";
        
        try (Connection conn = SqliteHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, menuId);
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
        DailyMenu menu = new DailyMenu();
        menu.setMenuId(rs.getInt("menu_id"));
        menu.setStudentId(rs.getInt("student_id"));
        menu.setDate(rs.getString("date"));
        menu.setTotalCalories(rs.getDouble("total_calories"));
        menu.setTotalProtein(rs.getDouble("total_protein"));
        menu.setTotalCarbs(rs.getDouble("total_carbs"));
        menu.setTotalFat(rs.getDouble("total_fat"));
        return menu;
    }
}

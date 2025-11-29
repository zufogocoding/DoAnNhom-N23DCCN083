package doanJava.DAO;

import doanJava.Model.StudentInventory;
import doanJava.utils.SqliteHelper;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InventoryDAO {

    // 1. Lấy danh sách kho (Hiển thị lên Sidebar)
    public List<StudentInventory> getInventory(int studentId) {
        List<StudentInventory> list = new ArrayList<>();
        String sql = "SELECT * FROM Student_Inventory WHERE student_id = ?";

        try (Connection conn = SqliteHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                // Constructor mới chỉ có 3 tham số
                list.add(new StudentInventory(
                    rs.getInt("student_id"),
                    rs.getInt("ingredient_id"),
                    rs.getDouble("quantity")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi lấy Inventory: " + e.getMessage());
        }
        return list;
    }

    // 2. Thêm hoặc Cập nhật kho (Logic Thông minh)
    public void addOrUpdateInventory(int studentId, int ingredientId, double quantityToAdd) {
        
        // Bước A: Kiểm tra xem đã có chưa
        String checkSql = "SELECT quantity FROM Student_Inventory WHERE student_id = ? AND ingredient_id = ?";
        double currentQty = -1;

        try (Connection conn = SqliteHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(checkSql)) {
            
            pstmt.setInt(1, studentId);
            pstmt.setInt(2, ingredientId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                currentQty = rs.getDouble("quantity");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        

        // Bước B: Xử lý
        if (currentQty >= 0) {
            // CÓ RỒI -> UPDATE cộng dồn
            double newQty = currentQty + quantityToAdd;
            String updateSql = "UPDATE Student_Inventory SET quantity = ? WHERE student_id = ? AND ingredient_id = ?";
            
            try (Connection conn = SqliteHelper.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(updateSql)) {
                pstmt.setDouble(1, newQty);
                pstmt.setInt(2, studentId);
                pstmt.setInt(3, ingredientId);
                pstmt.executeUpdate();
                System.out.println("DEBUG: Updated Inventory Item " + ingredientId + ": " + newQty);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            // CHƯA CÓ -> INSERT mới
            String insertSql = "INSERT INTO Student_Inventory(student_id, ingredient_id, quantity) VALUES(?,?,?)";
            try (Connection conn = SqliteHelper.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
                pstmt.setInt(1, studentId);
                pstmt.setInt(2, ingredientId);
                pstmt.setDouble(3, quantityToAdd);
                pstmt.executeUpdate();
                System.out.println("DEBUG: Inserted New Inventory Item " + ingredientId);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
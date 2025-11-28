/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package doanJava.DAO;

import doanJava.Model.StudentInventory;
import doanJava.util.SqliteHelper;
import java.util.*;
import java.sql.*;
/**
 *
 * @author phamt
 */
public class InventoryDAO {
   public ArrayList<StudentInventory> getInventory(int studentId){
       ArrayList<StudentInventory> inventory = new ArrayList<>();
       String sql = "SELECT * FROM Student_Inventory WHERE student_id = ?";
       
       try (Connection conn = SqliteHelper.getConnection();PreparedStatement pstmt = conn.prepareStatement(sql)){
           pstmt.setInt(1,studentId);
           ResultSet rs = pstmt.executeQuery();
           while (rs.next()){
               inventory.add(mapResultSetToInventory(rs));
           }
       } catch(SQLException e) {
            System.err.println("Error getting inventory "+e.getMessage());
       }
       return inventory;
}
   
   public void addStock(int studentId, int ingredientId, double quantityToAdd) {
        String checkSql = "SELECT quantity FROM Student_Inventory WHERE student_id = ? AND ingredient_id = ?";
        String updateSql = "UPDATE Student_Inventory SET quantity = ? WHERE student_id = ? AND ingredient_id = ?";
        String insertSql = "INSERT INTO Student_Inventory(student_id, ingredient_id, quantity) VALUES(?,?,?)";

        try (Connection conn = SqliteHelper.getConnection()) {
            double currentQuantity = 0;
            boolean exists = false;
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setInt(1, studentId);
                checkStmt.setInt(2, ingredientId);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    exists = true;
                    currentQuantity = rs.getDouble("quantity");
                }
            }

            if (exists) {
                try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                    updateStmt.setDouble(1, currentQuantity + quantityToAdd);
                    updateStmt.setInt(2, studentId);
                    updateStmt.setInt(3, ingredientId);
                    updateStmt.executeUpdate();
                }
            } 
            else {
                try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                    insertStmt.setInt(1, studentId);
                    insertStmt.setInt(2, ingredientId);
                    insertStmt.setDouble(3, quantityToAdd);
                    insertStmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            System.err.println("Error adding stock: " + e.getMessage());
        }
    }
   
   
   private StudentInventory mapResultSetToInventory(ResultSet rs) throws SQLException {
        return new StudentInventory(
                rs.getInt("student_id"),
                rs.getInt("ingredient_id"),
                rs.getDouble("quantity")
        );
    }
}

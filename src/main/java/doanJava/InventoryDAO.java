/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package doanJava;

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
   
   private StudentInventory mapResultSetToInventory(ResultSet rs) throws SQLException {
        return new StudentInventory(
                rs.getInt("student_id"),
                rs.getInt("ingredient_id"),
                rs.getDouble("quantity")
        );
    }
}

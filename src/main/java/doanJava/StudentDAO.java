/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package doanJava;
import java.sql.*;
import java.util.*;
/**
 *
 * @author phamt
 */
public class StudentDAO {
    public Student addStudent(Student student){
        String sql = "INSERT INTO Student(name,height_cm,weight_kg,target_calories,target_protein_g,target_carbs_g,target_fat_g) VALUES(?,?,?,?,?,?,?)";
        
        try (Connection conn =  SqliteHelper.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)){
            pstmt.setString(1, student.getName());
            pstmt.setDouble(2, student.getHeightCm());
            pstmt.setDouble(3, student.getWeightKg());
            pstmt.setInt(4, student.getTargetCalories());
            pstmt.setInt(5, student.getTargetProteinG());
            pstmt.setInt(6, student.getTargetCarbsG());
            pstmt.setInt(7, student.getTargetFatG());
            pstmt.executeUpdate();
            
            ResultSet rs = pstmt.getGeneratedKeys();
            if(rs.next()) {
                student.setStudentId(rs.getInt(1));}
            return student;
        } catch (SQLException e) {
            System.err.println("Error adding Student: "+e.getMessage());
            return null;
        }
    }
}

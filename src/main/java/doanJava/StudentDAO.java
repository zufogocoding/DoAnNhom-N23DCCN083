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

    public Student addStudent(Student student) {
        String sql = "INSERT INTO Student(name,height_cm,weight_kg,target_calories,target_protein_g,target_carbs_g,target_fat_g) VALUES(?,?,?,?,?,?,?)";

        try (Connection conn = SqliteHelper.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, student.getName());
            pstmt.setDouble(2, student.getHeightCm());
            pstmt.setDouble(3, student.getWeightKg());
            pstmt.setDouble(4, student.getTargetCalories());
            pstmt.setDouble(5, student.getTargetProteinG());
            pstmt.setDouble(6, student.getTargetCarbsG());
            pstmt.setDouble(7, student.getTargetFatG());
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                student.setStudentId(rs.getInt(1));
            }
            return student;
        } catch (SQLException e) {
            System.err.println("Error adding Student: " + e.getMessage());
            return null;
        }
    }
        

    public Student getStudent(int id) {
        String sql = "SELECT * FROM Student WHERE student_id = ?";
        try (Connection conn = SqliteHelper.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToStudent(rs);
            }

        } catch (SQLException e) {
            System.err.println("Error getting student: " + e.getMessage());
        }
        return null;
    }
    
    public boolean updateStudent(Student student) {
    String sql = "UPDATE Student SET name = ?, height_cm = ?, weight_kg = ?, " +
                 "target_calories = ?, target_protein_g = ?, target_carbs_g = ?, target_fat_g = ? " +
                 "WHERE student_id = ?";

    try (Connection conn = SqliteHelper.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        pstmt.setString(1, student.getName());
        pstmt.setDouble(2, student.getHeightCm());
        pstmt.setDouble(3, student.getWeightKg());
        pstmt.setDouble(4, student.getTargetCalories());
        pstmt.setDouble(5, student.getTargetProteinG());
        pstmt.setDouble(6, student.getTargetCarbsG());
        pstmt.setDouble(7, student.getTargetFatG());
        pstmt.setInt(8, student.getStudentId()); 

        return pstmt.executeUpdate() > 0; 
        
    } catch (SQLException e) {
        System.err.println("Error update student: " + e.getMessage());
        return false;
    }
}

    public ArrayList<Student> getAllStudents() {
        ArrayList<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM Student";

        try (Connection conn = SqliteHelper.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                students.add(mapResultSetToStudent(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error Fetching students: " + e.getMessage());
        }
        return students;
    }

    private Student mapResultSetToStudent(ResultSet rs) throws SQLException {
        return new Student(
                rs.getInt("student_id"),
                rs.getString("name"),
                rs.getDouble("height_cm"),
                rs.getDouble("weight_kg"),
                rs.getInt("target_calories"),
                rs.getInt("target_protein_g"),
                rs.getInt("target_carbs_g"),
                rs.getInt("target_fat_g")
        );
    }

}

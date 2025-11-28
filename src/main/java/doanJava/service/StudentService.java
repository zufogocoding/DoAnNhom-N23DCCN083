/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package doanJava.service;

/**
 *
 * @author phamt
 */
import doanJava.DAO.StudentDAO;
import doanJava.Model.Student;
import java.util.List;

public class StudentService {

    private final StudentDAO studentDAO;

    public StudentService(StudentDAO studentDAO) {
        this.studentDAO = studentDAO;
    }
    public Student createOrUpdateProfile(Student student) {
        if (student.getStudentId() > 0) {
            boolean success = studentDAO.updateStudent(student);
            if (success) {
                return student;
            } else {
                System.err.println("Fail update profile.");
                return null;
            }
        } else {
            return studentDAO.addStudent(student);
        }
    }

    public Student getProfile(int studentId) {
        return studentDAO.getStudent(studentId);
    }

    public List<Student> getAllProfiles() {
        return studentDAO.getAllStudents();
    }
}

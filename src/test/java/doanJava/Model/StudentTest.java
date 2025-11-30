package doanJava.Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StudentTest {
    
    private Student student;
    
    @BeforeEach
    public void setUp() {
        student = new Student(1, "Nguyễn Văn A", 170, 65, 2000, 150, 200, 60);
    }
    
    @Test
    public void testStudentCreation() {
        assertEquals(1, student.getStudentId());
        assertEquals("Nguyễn Văn A", student.getName());
        assertEquals(170, student.getHeightCm());
        assertEquals(65, student.getWeightKg());
    }
    
    @Test
    public void testStudentNutritionTargets() {
        assertEquals(2000, student.getTargetCalories());
        assertEquals(150, student.getTargetProteinG());
        assertEquals(200, student.getTargetCarbsG());
        assertEquals(60, student.getTargetFatG());
    }
    
    @Test
    public void testStudentNameNotEmpty() {
        student.setName("Tên hợp lệ");
        assertFalse(student.getName().isEmpty());
        assertNotNull(student.getName());
    }
    
    @Test
    public void testStudentWeightValidation() {
        // Weight phải lớn hơn 0
        student.setWeightKg(70);
        assertTrue(student.getWeightKg() > 0);
        assertEquals(70, student.getWeightKg());
    }
    
    @Test
    public void testStudentHeightValidation() {
        // Height phải lớn hơn 0
        student.setHeightCm(175);
        assertTrue(student.getHeightCm() > 0);
        assertEquals(175, student.getHeightCm());
    }
    
    @Test
    public void testStudentTargetCaloriesPositive() {
        student.setTargetCalories(2500);
        assertTrue(student.getTargetCalories() > 0);
    }
}
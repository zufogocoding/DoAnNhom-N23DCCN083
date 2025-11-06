/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package doanJava;

/**
 *
 * @author phamt
 */
public class Student {
    private int studentId;
    private String name;
    private int age;
    private double heightCm;
    private double weightKg;
    private int activityLevel;
    private int targetCalories;
    private int targetProteinG;
    private int targetCarbsG;
    private int targetFatG;

    // 2. Constructor (Hàm khởi tạo)
    public Student() {
        // Constructor rỗng
    }

    // 3. Các hàm Getters và Setters
    // (Bắt buộc phải có để lấy và gán dữ liệu)
    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getHeightCm() {
        return heightCm;
    }

    public void setHeightCm(double heightCm) {
        this.heightCm = heightCm;
    }

    public double getWeightKg() {
        return weightKg;
    }

    public void setWeightKg(double weightKg) {
        this.weightKg = weightKg;
    }

    public int getActivityLevel() {
        return activityLevel;
    }

    public void setActivityLevel(int activityLevel) {
        this.activityLevel = activityLevel;
    }

    public int getTargetCalories() {
        return targetCalories;
    }

    public void setTargetCalories(int targetCalories) {
        this.targetCalories = targetCalories;
    }

    public int getTargetProteinG() {
        return targetProteinG;
    }

    public void setTargetProteinG(int targetProteinG) {
        this.targetProteinG = targetProteinG;
    }

    public int getTargetCarbsG() {
        return targetCarbsG;
    }

    public void setTargetCarbsG(int targetCarbsG) {
        this.targetCarbsG = targetCarbsG;
    }

    public int getTargetFatG() {
        return targetFatG;
    }

    public void setTargetFatG(int targetFatG) {
        this.targetFatG = targetFatG;
    }

    @Override
    public String toString() {
        return "Student{" + 
               "id=" + studentId + 
               ", name='" + name + '\'' + 
               ", age=" + age + 
               '}';
    }

}

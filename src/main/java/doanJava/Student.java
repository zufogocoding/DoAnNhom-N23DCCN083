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
    private double heightCm;
    private double weightKg;
    private double targetCalories;
    private double targetProteinG;
    private double targetCarbsG;
    private double targetFatG;
    //file word em viet sai xin loi Tu
    // Constructor rỗng
    public Student() {
    }

    // Constructor đầy đủ

    public Student(int studentId, String name, double heightCm, double weightKg, double targetCalories, double targetProteinG, double targetCarbsG, double targetFatG) {
        this.studentId = studentId;
        this.name = name;
        this.heightCm = heightCm;
        this.weightKg = weightKg;
        this.targetCalories = targetCalories;
        this.targetProteinG = targetProteinG;
        this.targetCarbsG = targetCarbsG;
        this.targetFatG = targetFatG;
    }
    

    // Getters and Setters
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

    public double getTargetCalories() {
        return targetCalories;
    }

    public void setTargetCalories(double targetCalories) {
        this.targetCalories = targetCalories;
    }

    public double getTargetProteinG() {
        return targetProteinG;
    }

    public void setTargetProteinG(double targetProteinG) {
        this.targetProteinG = targetProteinG;
    }

    public double getTargetCarbsG() {
        return targetCarbsG;
    }

    public void setTargetCarbsG(double targetCarbsG) {
        this.targetCarbsG = targetCarbsG;
    }

    public double getTargetFatG() {
        return targetFatG;
    }

    public void setTargetFatG(double targetFatG) {
        this.targetFatG = targetFatG;
    }

  
    @Override
    public String toString() {
        return "Student{" +
                "studentId=" + studentId +
                ", name='" + name + '\'' +
                '}';
    }
}

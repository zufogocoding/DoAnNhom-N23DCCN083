/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package doanJava;

/**
 *
 * @author phamt
 */
public class Ingredient {
    
    private int ingredientId;
    private String name;
    private String unit;
    private double caloriesPerUnit;
    private double proteinPerUnit;
    private double carbsPerUnit;
    private double fatPerUnit;

    public Ingredient() {
    }

    public int getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(int ingredientId) {
        this.ingredientId = ingredientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double getCaloriesPerUnit() {
        return caloriesPerUnit;
    }

    public void setCaloriesPerUnit(double caloriesPerUnit) {
        this.caloriesPerUnit = caloriesPerUnit;
    }

    public double getProteinPerUnit() {
        return proteinPerUnit;
    }

    public void setProteinPerUnit(double proteinPerUnit) {
        this.proteinPerUnit = proteinPerUnit;
    }

    public double getCarbsPerUnit() {
        return carbsPerUnit;
    }

    public void setCarbsPerUnit(double carbsPerUnit) {
        this.carbsPerUnit = carbsPerUnit;
    }

    public double getFatPerUnit() {
        return fatPerUnit;
    }

    public void setFatPerUnit(double fatPerUnit) {
        this.fatPerUnit = fatPerUnit;
    }
}

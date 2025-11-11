/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package doanJava;

/**
 *
 * @author phamt
 */
public class RecipeIngredient {
    
    private int foodId;
    private int ingredientId;
    private double quantity;

    public RecipeIngredient() {
    }

    public RecipeIngredient(int foodId, int ingredientId, double quantity) {
        this.foodId = foodId;
        this.ingredientId = ingredientId;
        this.quantity = quantity;
    }
    
    

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public int getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(int ingredientId) {
        this.ingredientId = ingredientId;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

}

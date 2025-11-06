/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package doanJava;

import java.sql.Time;

/**
 *
 * @author phamt
 */
public class MenuFood {
    private int logId;
    private int menuId;
    private int foodId;
    private String mealType;
    private Time logTime;

    public MenuFood() {
    }

    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public String getMealType() {
        return mealType;
    }

    public void setMealType(String mealType) {
        this.mealType = mealType;
    }

    public Time getLogTime() {
        return logTime;
    }

    public void setLogTime(Time logTime) {
        this.logTime = logTime;
    }
}

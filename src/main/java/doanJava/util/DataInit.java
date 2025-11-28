/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package doanJava.util;

import doanJava.DAO.IngredientDAO;
import doanJava.Model.Ingredient;
import org.json.*;
/**
 *
 * @author phamt
 */
import java.nio.file.*;
import java.io.*;

public class DataInit {

    private static IngredientDAO iDAO = new IngredientDAO();

    public static void main(String[] args) {
        String fileName = "ingredients.json";

        try {
            String jsonText = new String(Files.readAllBytes(Paths.get(fileName)));
            JSONArray ingredientsArray = new JSONArray(jsonText);
            int count = 0;
            for (int i = 0; i < ingredientsArray.length(); i++) {
                JSONObject obj = ingredientsArray.getJSONObject(i);
                Ingredient ig = new Ingredient();
                ig.setName(obj.getString("name"));
                ig.setUnit(obj.getString("unit"));

                ig.setCaloriesPerUnit(safeParseDouble(obj, "calories_per_unit"));
                ig.setProteinPerUnit(safeParseDouble(obj, "protein_per_unit"));
                ig.setCarbsPerUnit(safeParseDouble(obj, "carbs_per_unit"));
                ig.setFatPerUnit(safeParseDouble(obj, "fat_per_unit"));

                iDAO.addIngredient(ig);
                count++;
            }
            System.out.println("SUCCESS Adding" + count + " Ingredients to databesasds");
        } catch (IOException e) {
            System.err.println("Error: file not found");
            System.err.println("Please make sure you read the manual");
        } catch (JSONException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    private static double safeParseDouble(JSONObject obj,String key){
        try{
            Object value = obj.get(key);
            if (value instanceof String && value.equals("N/A")){
                return 0.0;
            }
            if (value instanceof Number){
                return ((Number) value).doubleValue();
            }
            return Double.parseDouble(value.toString());
        } catch(Exception e){
            return 0.0;
        }
    } 
}

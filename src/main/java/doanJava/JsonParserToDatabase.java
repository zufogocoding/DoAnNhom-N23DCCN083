/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package doanJava;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author phamt
 */
public class JsonParserToDatabase {

    // Map target nutrient names to the output labels we want
    private static final Map<String, String> TARGETS = new HashMap<>();

    static {
        TARGETS.put("Energy (Atwater General Factors)", "calories");
        TARGETS.put("Protein", "protein");
        TARGETS.put("Carbohydrate, by difference", "carbs");
        TARGETS.put("Total lipid (fat)", "fat");
    }

    public static void main(String[] args) throws Exception {
        // Lấy file từ resources (ví dụ: src/main/resources/food.json)
        InputStream is = JsonParserToDatabase.class
                .getClassLoader()
                .getResourceAsStream("FoodData_Central_foundation_food_json_2025-04-24.json");

        if (is == null) {
            System.err.println("Không tìm thấy file food.json trong resources!");
            System.exit(1);
        }

        JsonFactory factory = new JsonFactory();
        try (JsonParser jp = factory.createParser(is)) {

            Map<String, String> result = new HashMap<>();
            String currentNutrientName = null;
            String currentAmount = null;

            while (!jp.isClosed()) {
                JsonToken token = jp.nextToken();
                if (token == null) break;

                if (token == JsonToken.FIELD_NAME) {
                    String fieldName = jp.currentName();

                    if ("foodNutrients".equals(fieldName)) {
                        token = jp.nextToken(); // START_ARRAY
                        if (token == JsonToken.START_ARRAY) {
                            while (jp.nextToken() != JsonToken.END_ARRAY) {
                                if (jp.currentToken() == JsonToken.START_OBJECT) {
                                    currentNutrientName = null;
                                    currentAmount = null;

                                    while (jp.nextToken() != JsonToken.END_OBJECT) {
                                        if (jp.currentToken() == JsonToken.FIELD_NAME) {
                                            String fn = jp.currentName();
                                            jp.nextToken();

                                            if ("nutrient".equals(fn) && jp.currentToken() == JsonToken.START_OBJECT) {
                                                while (jp.nextToken() != JsonToken.END_OBJECT) {
                                                    if (jp.currentToken() == JsonToken.FIELD_NAME) {
                                                        String nf = jp.currentName();
                                                        jp.nextToken();
                                                        if ("name".equals(nf)) {
                                                            currentNutrientName = jp.getValueAsString();
                                                        } else {
                                                            jp.skipChildren();
                                                        }
                                                    }
                                                }
                                            } else if ("amount".equals(fn)) {
                                                currentAmount = jp.getValueAsString();
                                            } else {
                                                jp.skipChildren();
                                            }
                                        }
                                    }

                                    if (currentNutrientName != null &&
                                        TARGETS.containsKey(currentNutrientName) &&
                                        currentAmount != null) {
                                        String key = TARGETS.get(currentNutrientName);
                                        result.put(key, currentAmount);
                                    }
                                } else {
                                    jp.skipChildren();
                                }
                            }
                        } else {
                            jp.skipChildren();
                        }
                    } else {
                        jp.skipChildren();
                    }
                }
            }

            System.out.println("calories: " + result.getOrDefault("calories", "not found"));
            System.out.println("protein: " + result.getOrDefault("protein", "not found") + " g");
            System.out.println("carbs: " + result.getOrDefault("carbs", "not found") + " g");
            System.out.println("fat: " + result.getOrDefault("fat", "not found") + " g");
        }
    }
}

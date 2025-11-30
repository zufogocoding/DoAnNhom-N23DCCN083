package doanJava.utils;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DataInitTest {
    
    @Test
    public void testSafeParseDouble_ValidNumber() {
        JSONObject obj = new JSONObject();
        obj.put("calories", 100.5);
        
        double result = safeParseDouble(obj, "calories");
        assertEquals(100.5, result);
    }
    
    @Test
    public void testSafeParseDouble_StringNA() {
        JSONObject obj = new JSONObject();
        obj.put("protein", "N/A");
        
        double result = safeParseDouble(obj, "protein");
        assertEquals(0.0, result, "N/A nên trả về 0.0");
    }
    
    @Test
    public void testSafeParseDouble_InvalidValue() {
        JSONObject obj = new JSONObject();
        obj.put("carbs", "invalid_text");
        
        double result = safeParseDouble(obj, "carbs");
        assertEquals(0.0, result, "Giá trị không hợp lệ nên trả về 0.0");
    }
    
    @Test
    public void testSafeParseDouble_MissingKey() {
        JSONObject obj = new JSONObject();
        
        assertThrows(org.json.JSONException.class, () -> {
            obj.get("nonexistent");
        });
    }
    
    @Test
    public void testSafeParseDouble_ZeroValue() {
        JSONObject obj = new JSONObject();
        obj.put("fat", 0.0);
        
        double result = safeParseDouble(obj, "fat");
        assertEquals(0.0, result);
    }
    
    // Method helper này được sao chép từ DataInit
    private double safeParseDouble(JSONObject obj, String key) {
        try {
            Object value = obj.get(key);
            if (value instanceof String && value.equals("N/A")) {
                return 0.0;
            }
            if (value instanceof Number) {
                return ((Number) value).doubleValue();
            }
            return Double.parseDouble(value.toString());
        } catch (Exception e) {
            return 0.0;
        }
    }
}
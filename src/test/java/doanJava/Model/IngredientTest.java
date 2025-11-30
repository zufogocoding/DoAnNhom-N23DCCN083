package doanJava.Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class IngredientTest {
    
    private Ingredient ingredient;
    
    @BeforeEach
    public void setUp() {
        // Khởi tạo trước mỗi test
        ingredient = new Ingredient();
    }
    
    @Test
    public void testIngredientCreation() {
        // Test tạo ingredient
        ingredient.setName("Trứng gà");
        ingredient.setUnit("cái");
        ingredient.setCaloriesPerUnit(70.0);
        ingredient.setProteinPerUnit(6.3);
        ingredient.setCarbsPerUnit(0.6);
        ingredient.setFatPerUnit(5.0);
        
        // Kiểm tra các giá trị
        assertEquals("Trứng gà", ingredient.getName());
        assertEquals("cái", ingredient.getUnit());
        assertEquals(70.0, ingredient.getCaloriesPerUnit());
        assertEquals(6.3, ingredient.getProteinPerUnit());
    }
    
    @Test
    public void testIngredientNutritionValidation() {
        // Test kiểm tra giá trị dinh dưỡng
        ingredient.setName("Gạo");
        ingredient.setUnit("g");
        ingredient.setCaloriesPerUnit(130.0);
        ingredient.setProteinPerUnit(2.7);
        ingredient.setCarbsPerUnit(28.0);
        ingredient.setFatPerUnit(0.3);
        
        // Giá trị phải >= 0
        assertTrue(ingredient.getCaloriesPerUnit() >= 0);
        assertTrue(ingredient.getProteinPerUnit() >= 0);
    }
    
    @Test
    public void testIngredientUnitNotEmpty() {
        // Test unit không được để trống
        ingredient.setName("Cà chua");
        ingredient.setUnit("kg");
        
        assertFalse(ingredient.getUnit().isEmpty());
        assertEquals("kg", ingredient.getUnit());
    }
    
    @Test
    public void testIngredientNameNotNull() {
        ingredient.setName("Chuối");
        assertNotNull(ingredient.getName());
    }
}
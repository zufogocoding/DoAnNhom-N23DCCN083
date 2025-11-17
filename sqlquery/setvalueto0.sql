UPDATE Ingredient
SET carbs_per_unit = 0
WHERE carbs_per_unit < 0;
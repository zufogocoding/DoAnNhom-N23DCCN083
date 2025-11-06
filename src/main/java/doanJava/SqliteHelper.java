package doanJava;

import java.sql.*;
import java.io.*;

/**
 * Utility class for managing SQLite connection and database initialization.
 */
public class SqliteHelper {

    // Path to the database file.
    // "jdbc:sqlite:data.db" means the file "data.db" will be created
    // in the project's root directory.
    private static final String DB_URL = "jdbc:sqlite:data.db";

    /**
     * Get a new connection to the database.
     * @return a Connection object
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public static void initDatabase() {
        
        // Array of 7 SQL statements to create tables
        String[] createTableStatements = {
            
            // 1. Student
            """
            CREATE TABLE IF NOT EXISTS Student (
                student_id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                height_cm REAL,
                weight_kg REAL,
                target_calories INTEGER,
                target_protein_g INTEGER,
                target_carbs_g INTEGER,
                target_fat_g INTEGER
            );
            """,
            
            // 2. Ingredient
            """
            CREATE TABLE IF NOT EXISTS Ingredient (
                ingredient_id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL UNIQUE,
                unit TEXT NOT NULL,
                calories_per_unit REAL,
                protein_per_unit REAL,
                carbs_per_unit REAL,
                fat_per_unit REAL
            );
            """,

            // 3. Student_Inventory
            """
            CREATE TABLE IF NOT EXISTS Student_Inventory (
                student_id INTEGER,
                ingredient_id INTEGER,
                quantity REAL NOT NULL,
                PRIMARY KEY (student_id, ingredient_id),
                FOREIGN KEY (student_id) REFERENCES Student(student_id),
                FOREIGN KEY (ingredient_id) REFERENCES Ingredient(ingredient_id)
            );
            """,

            // 4. Food
            """
            CREATE TABLE IF NOT EXISTS Food (
                food_id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL UNIQUE,
                instructions TEXT
            );
            """,

            // 5. Recipe_Ingredient
            """
            CREATE TABLE IF NOT EXISTS Recipe_Ingredient (
                food_id INTEGER,
                ingredient_id INTEGER,
                quantity REAL NOT NULL,
                PRIMARY KEY (food_id, ingredient_id),
                FOREIGN KEY (food_id) REFERENCES Food(food_id),
                FOREIGN KEY (ingredient_id) REFERENCES Ingredient(ingredient_id)
            );
            """,

            // 6. Daily_Menu
            """
            CREATE TABLE IF NOT EXISTS Daily_Menu (
                menu_id INTEGER PRIMARY KEY AUTOINCREMENT,
                student_id INTEGER,
                date TEXT NOT NULL,
                total_calories REAL DEFAULT 0,
                total_protein REAL DEFAULT 0,
                total_carbs REAL DEFAULT 0,
                total_fat REAL DEFAULT 0,
                FOREIGN KEY (student_id) REFERENCES Student(student_id)
            );
            """,

            // 7. Menu_Food
            """
            CREATE TABLE IF NOT EXISTS Menu_Food (
                log_id INTEGER PRIMARY KEY AUTOINCREMENT,
                menu_id INTEGER,
                food_id INTEGER,
                meal_type TEXT NOT NULL,
                log_time TEXT,
                FOREIGN KEY (menu_id) REFERENCES Daily_Menu(menu_id),
                FOREIGN KEY (food_id) REFERENCES Food(food_id)
            );
            """
        };
        
        // Use try-with-resources to ensure Connection and Statement are closed
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            
            for (String sql : createTableStatements) {
                stmt.execute(sql);
            }
            
            System.out.println("Database created successfully (or already exists).");

        } catch (SQLException e) {
            System.err.println("Error creating database: " + e.getMessage());
        }
    }

    // Main test
    /*public static void main(String[] args) {
        System.out.println("Creating database...");
        initDatabase();
        File f = new File("data.db");
        System.out.println("Absolute path: " + f.getAbsolutePath());
        System.out.println("Exists: " + f.exists());
    }*/
}

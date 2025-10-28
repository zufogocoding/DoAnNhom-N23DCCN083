import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Lớp tiện ích để quản lý kết nối và khởi tạo cơ sở dữ liệu SQLite.
 */
public class SqliteHelper {

    // Đường dẫn đến file database.
    // "jdbc:sqlite:data.db" có nghĩa là file "data.db" sẽ được tạo
    // ngay tại thư mục gốc của dự án của bạn.
    private static final String DB_URL = "jdbc:sqlite:data.db";

    /**
     * Lấy một kết nối mới đến cơ sở dữ liệu.
     * @return một đối tượng Connection
     * @throws SQLException nếu kết nối thất bại
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    /**
     * Khởi tạo 7 bảng cần thiết cho dự án.
     * Sử dụng "CREATE TABLE IF NOT EXISTS" để an toàn khi gọi lại.
     */
    public static void initDatabase() {
        
        // Mảng chứa 7 câu lệnh SQL để tạo bảng
        String[] createTableStatements = {
            
            // 1. Bảng Student
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
            
            // 2. Bảng Ingredient
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

            // 3. Bảng Student_Inventory
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

            // 4. Bảng Food
            """
            CREATE TABLE IF NOT EXISTS Food (
                food_id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL UNIQUE,
                instructions TEXT
            );
            """,

            // 5. Bảng Recipe_Ingredient
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

            // 6. Bảng Daily_Menu
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

            // 7. Bảng Menu_Food
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
        
        // Sử dụng try-with-resources để đảm bảo Connection và Statement được đóng
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            
            // Chạy từng câu lệnh CREATE TABLE
            for (String sql : createTableStatements) {
                stmt.execute(sql);
            }
            
            System.out.println("Cơ sở dữ liệu và các bảng đã được tạo thành công (hoặc đã tồn tại).");

        } catch (SQLException e) {
            System.err.println("Lỗi khi khởi tạo database: " + e.getMessage());
        }
    }

    /**
     * Hàm main để chạy và tạo file data.db.
     * Đây là cách bạn hoàn thành mục "Chạy initDatabase() 1 lần".
     */
    public static void main(String[] args) {
        System.out.println("Đang tiến hành khởi tạo database...");
        initDatabase();
    }
}
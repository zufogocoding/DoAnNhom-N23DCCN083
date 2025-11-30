package doanJava.utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SqliteHelperTest {
    
    @Test
    public void testDatabaseConnection() {
        try {
            Connection conn = SqliteHelper.getConnection();
            assertNotNull(conn, "Connection không nên là null");
            assertFalse(conn.isClosed(), "Connection phải mở");
            conn.close();
        } catch (SQLException e) {
            fail("Kết nối database thất bại: " + e.getMessage());
        }
    }
    
    @Test
    public void testDatabaseInitialization() {
        // Test khởi tạo database
        assertDoesNotThrow(() -> {
            SqliteHelper.initDatabase();
        }, "initDatabase không nên throw exception");
    }
    
    @Test
    public void testConnectionIsValid() {
        try {
            Connection conn = SqliteHelper.getConnection();
            assertTrue(conn.isValid(2), "Connection phải hợp lệ trong 2 giây");
            conn.close();
        } catch (SQLException e) {
            fail("Không thể kiểm tra connection validity: " + e.getMessage());
        }
    }
    
    @Test
    public void testTablesExistAfterInit() {
        try {
            SqliteHelper.initDatabase();
            Connection conn = SqliteHelper.getConnection();
            Statement stmt = conn.createStatement();
            
            // Kiểm tra xem bảng Student tồn tại
            assertDoesNotThrow(() -> {
                stmt.executeQuery("SELECT * FROM Student LIMIT 1");
            });
            
            conn.close();
        } catch (SQLException e) {
            fail("Lỗi khi kiểm tra tables: " + e.getMessage());
        }
    }
}
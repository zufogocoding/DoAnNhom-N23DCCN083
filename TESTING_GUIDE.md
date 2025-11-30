# 🧪 Unit Testing Guide

Hướng dẫn chạy Unit Tests cho dự án DoAnNhom-N23DCCN083

## 📋 Danh sách Unit Tests

### 1. **IngredientTest.java** (src/test/java/doanJava/Model/)
Kiểm tra các chức năng của class Ingredient:
- ✅ Tạo Ingredient
- ✅ Xác thực giá trị dinh dưỡng
- ✅ Kiểm tra unit không trống
- ✅ Kiểm tra name không null

### 2. **StudentTest.java** (src/test/java/doanJava/Model/)
Kiểm tra các chức năng của class Student:
- ✅ Tạo Student
- ✅ Xác thực mục tiêu dinh dưỡng
- ✅ Kiểm tra tên hợp lệ
- ✅ Xác thực trọng lượng
- ✅ Xác thực chiều cao
- ✅ Xác thực calorories hợp lệ

### 3. **SqliteHelperTest.java** (src/test/java/doanJava/utils/)
Kiểm tra các chức năng của database:
- ✅ Kết nối Database
- ✅ Khởi tạo Database
- ✅ Kiểm tra Connection hợp lệ
- ✅ Kiểm tra Tables tồn tại

### 4. **DataInitTest.java** (src/test/java/doanJava/utils/)
Kiểm tra xử lý JSON parsing:
- ✅ Parse số hợp lệ
- ✅ Xử lý "N/A" string
- ✅ Xử lý giá trị không hợp lệ
- ✅ Xử lý key missing
- ✅ Parse giá trị 0

## 🚀 Cách Chạy Tests

### Chạy tất cả tests:
```bash
mvn test
```

### Chạy test một class cụ thể:
```bash
mvn test -Dtest=StudentTest
```

### Chạy test một method cụ thể:
```bash
mvn test -Dtest=StudentTest#testStudentCreation
```

### Chạy tests với chi tiết output:
```bash
mvn test -X
```

## 📊 Coverage Report

### Tạo JaCoCo coverage report:
```bash
mvn clean test jacoco:report
```

### Xem coverage report:
Report sẽ được tạo tại:
```
target/site/jacoco/index.html
```
Mở file này trong browser để xem chi tiết coverage

## ✅ Expected Results

Khi chạy `mvn test`, bạn sẽ thấy output như sau:
```
-------------------------------------------------------
 T E S T S
-------------------------------------------------------
Running doanJava.Model.IngredientTest
Tests run: 4, Failures: 0, Errors: 0, Skipped: 0

Running doanJava.Model.StudentTest
Tests run: 6, Failures: 0, Errors: 0, Skipped: 0

Running doanJava.utils.SqliteHelperTest
Tests run: 4, Failures: 0, Errors: 0, Skipped: 0

Running doanJava.utils.DataInitTest
Tests run: 5, Failures: 0, Errors: 0, Skipped: 0

Results :
Tests run: 19, Failures: 0, Errors: 0, Skipped: 0
```

## 🔧 Thêm Tests Mới

Để thêm unit test mới:

1. Tạo file `YourClassTest.java` trong `src/test/java/doanJava/{package}/`
2. Kế thừa từ JUnit 5:
```java
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class YourClassTest {
    
    @BeforeEach
    public void setUp() {
        // Khởi tạo trước mỗi test
    }
    
    @Test
    public void testMethod() {
        // Viết test ở đây
        assertEquals(expected, actual);
    }
}
```
3. Chạy tests:
```bash
mvn test
```

## 📚 Tài Liệu Tham Khảo

- [JUnit 5 Documentation](https://junit.org/junit5/)
- [Mockito Documentation](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)
- [JaCoCo Documentation](https://www.jacoco.org/jacoco/)

## 💡 Best Practices

1. **Naming**: Tên test nên mô tả rõ: `test{MethodName}_{Condition}_{ExpectedResult}`
2. **AAA Pattern**: Arrange → Act → Assert
3. **Isolate**: Mỗi test độc lập với nhau
4. **Setup/Teardown**: Sử dụng @BeforeEach và @AfterEach
5. **One Assertion**: Cố gắng 1 assertion per test nếu có thể

---

**Created**: 2025-11-30
**Last Updated**: 2025-11-30
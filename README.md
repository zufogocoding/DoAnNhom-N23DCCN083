# 🍽️ Nutrition Management System (Hệ thống Quản lý Dinh dưỡng)

**Đồ án Lập trình Hướng Đối tượng - Giảng viên: Cô Tuyết Hải**

Một ứng dụng Java desktop được phát triển để quản lý thông tin dinh dưỡng của các nguyên liệu thực phẩm với giao diện người dùng thân thiện.

## 📌 Mô tả Dự án

Hệ thống này giúp:
- 📊 Quản lý cơ sở dữ liệu nguyên liệu thực phẩm và thông tin dinh dưỡng
- 🔍 Tìm kiếm và lọc thông tin chi tiết về các loại thực phẩm
- 📈 Xử lý và cập nhật dữ liệu dinh dưỡng
- 💾 Lưu trữ dữ liệu an toàn với SQLite database

## 🛠️ Công nghệ Sử dụng

| Công nghệ | Phiên bản | Mục đích |
|-----------|----------|---------|
| **Java** | 24 | Ngôn ngữ lập trình chính |
| **JavaFX** | 21.0.1 | Framework xây dựng giao diện (GUI) |
| **SQLite** | 3.46.0. 0 | Cơ sở dữ liệu nhúng |
| **Jackson** | 2.17.1 | Xử lý JSON   | //thật ra khi xử lý cho file Json lấy từ USDA em đã dùng Python :)
| **Maven** | - | Quản lý build và dependencies |
thật ra khi xử lý cho file Json lấy từ USDA em đã dùng Python :)
## 📂 Cấu trúc Dự án
DoAnNhom-N23DCCN083/
├── src/ 
│ └── main/ 
│ ├── java/ # Code Java chính 
│ │ └── doanJava/ 
│ │ └── Main.java # Entry point 
│ └── resources/ # Tài nguyên (CSS, FXML, v.v.) 
├── sqlquery/ # SQL queries 
│ └── setvalueto0.sql # Script xử lý dữ liệu 
├── ingredients.json # Dữ liệu nguyên liệu thực phẩm từ USDA
├── pom.xml # Maven configuration 
├── . gitignore # Git ignore rules 
└── README.md # Tài liệu này


## 🚀 Hướng dẫn Cài đặt và Chạy

### Yêu cầu
- **Java 24** trở lên
- **Maven 3.6+**

### Các bước cài đặt

1. **Clone repository**
2. Cài đặt Dependencies: mvn clean install
3. run: thông qua việc run file AppLauncher.java bằng Maven hoặc mở cả project bằng netbeans và run file AppLauncher.java

### Hướng dẫn init lại database trong trường hợp bạn cần:
bước 1: xóa file data.db
bước 2: chạy lại file AppLauncher hoặc sqliteHelper để tạo lại file data.db
bước 3: chạy file DataInit, sau khi chạy xong nó sẽ hiện cho bạn khoảng hơn trăm nguyên liệu bằng tiếng Anh trong table ingredient, bạn có thể kiểm tra lại bằng db browser.
bước 4: mở app lên và sử dụng như bình thường

### Bạn tưởng lỗi nhưng mà không
--Lần đầu bạn nhập dữ liệu đó vào database thì mới cần nhập đúng chính xác tpdd, kể từ lần sau bạn cứ nhập 0 0 0 0 là được bởi vì dữ liệu khởi tạo là dữ liệu cứng không cho phép sửa đổi để đảm bảo quá trình chạy không xảy ra thật nhiều lỗi :D

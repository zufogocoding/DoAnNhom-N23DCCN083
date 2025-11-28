package doanJava.Controller;

import doanJava.Model.Food;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class FoodCardController {

    @FXML private ImageView imgFood;
    @FXML private Label lblName;
    @FXML private Label lblInfo;
    @FXML private Button btnAction;

    private Food foodData; // Lưu đối tượng món ăn hiện tại

    // Hàm này sẽ được gọi từ bên ngoài để truyền dữ liệu vào thẻ
    public void setData(Food food) {
        this.foodData = food;

        // 1. Set tên
        lblName.setText(food.getName());

        // 2. Set thông tin (Giả sử class Food có hàm getCalories)
        // Bạn có thể nối chuỗi để hiển thị: "500 Kcal | 30 phút"
        //lblInfo.setText(food.getNutrition().calories + " Kcal");

        // 3. Set ảnh
        try {
            // Nếu bạn có đường dẫn ảnh trong DB, dùng dòng này:
            // Image image = new Image(getClass().getResourceAsStream(food.getImagePath()));
            
            // Tạm thời dùng ảnh mặc định nếu chưa có ảnh thật
            Image image = new Image(getClass().getResourceAsStream("/images/default_food.png"));
            imgFood.setImage(image);
        } catch (Exception e) {
            System.out.println("Không tìm thấy ảnh cho món: " + food.getName());
        }
        
        // 4. Xử lý sự kiện nút bấm tại chỗ (hoặc truyền callback)
        btnAction.setOnAction(event -> {
            System.out.println("Đã chọn nấu món: " + food.getName());
            // Tại đây bạn có thể gọi hàm mở Dialog chi tiết món ăn
        });
    }
}

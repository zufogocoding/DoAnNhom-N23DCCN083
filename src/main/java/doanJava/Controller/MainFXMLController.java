/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package doanJava.Controller;
import doanJava.Model.Food;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Pagination;
import javafx.scene.layout.TilePane;
import java.io.IOException;
import java.util.List;

public class MainFXMLController {
    @FXML private Pagination pagination;
    // Giả sử đây là danh sách món ăn lấy từ Database
    private List<Food> masterData; 
    // Số lượng món ăn muốn hiện trên 1 trang
    private static final int ITEMS_PER_PAGE = 6; 
    public void initialize() {
        // 1. Lấy dữ liệu (Ví dụ: Lấy từ FoodService)
        // masterData = foodService.getSuggestedFoods(studentId);
        // (Tạm thời mình giả lập dữ liệu mẫu để code không bị lỗi nhé)
        masterData = List.of(); // <--thay dòng này bằng dòng gọi service ở trên

        // 2. Tính tổng số trang
        // Công thức: (Tổng số món / Số món 1 trang) làm tròn lên
        int pageCount = (int) Math.ceil((double) masterData.size() / ITEMS_PER_PAGE);
        // Cài đặt số trang cho Pagination (Ít nhất là 1 trang nếu danh sách rỗng)
        pagination.setPageCount(pageCount > 0 ? pageCount : 1);
        // 3. Cài đặt "Nhà máy sản xuất trang" (Page Factory)
        // Mỗi khi người dùng bấm chuyển trang, hàm createPage sẽ được gọi
        pagination.setPageFactory(pageIndex -> createPage(pageIndex));
    }

    // --- TẠO GIAO DIỆN CHO 1 TRANG ---
    private Node createPage(int pageIndex) {
        // A. Tạo một cái khung để chứa các thẻ (TilePane tự động xuống dòng rất đẹp)
        TilePane pageBox = new TilePane();
        pageBox.setPrefColumns(3); // Muốn 3 cột
        pageBox.setHgap(20);       // Khoảng cách ngang
        pageBox.setVgap(20);       // Khoảng cách dọc
        
        // B. Tính toán xem trang này bắt đầu từ món nào đến món nào
        int fromIndex = pageIndex * ITEMS_PER_PAGE;
        int toIndex = Math.min(fromIndex + ITEMS_PER_PAGE, masterData.size());

        // C. Vòng lặp tạo thẻ
        for (int i = fromIndex; i < toIndex; i++) {
            Food food = masterData.get(i);
            
            try {
                // Load FoodCard.fxml
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/FoodCard.fxml"));
                Node cardNode = loader.load();

                // Lấy Controller của cái thẻ đó để truyền dữ liệu
                FoodCardController cardCtrl = loader.getController();
                cardCtrl.setData(food); // <--- Hàm này bạn phải viết bên FoodCardController

                // Thêm thẻ vào khung
                pageBox.getChildren().add(cardNode);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // D. Trả về cái khung đã chứa đầy thẻ
        return pageBox;
    }
}
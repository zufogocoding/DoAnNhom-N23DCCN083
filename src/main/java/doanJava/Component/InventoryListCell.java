package doanJava.Component;

import doanJava.Model.StudentInventory; // Đảm bảo bạn có Model này
import doanJava.DAO.IngredientDAO;     // Để lấy tên nguyên liệu từ ID
import doanJava.Model.Ingredient;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class InventoryListCell extends ListCell<StudentInventory> {

    private final HBox rootHBox = new HBox();
    private final Label lblName = new Label();
    private final Label lblQty = new Label();
    private final Circle statusDot = new Circle(4, Color.web("#8CC63F")); // Chấm tròn xanh trang trí
    
    // DAO để lấy thông tin chi tiết (Tên, Đơn vị) từ ID
    private IngredientDAO ingredientDAO = new IngredientDAO();

    public InventoryListCell() {
        // --- 1. Cấu trúc giao diện ---
        // VBox chứa Tên và Số lượng
        VBox vBox = new VBox(2); // Spacing 2
        vBox.getChildren().addAll(lblName, lblQty);
        vBox.setAlignment(Pos.CENTER_LEFT);

        // Style cho Tên
        lblName.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #2d3436;");
        
        // Style cho Số lượng
        lblQty.setStyle("-fx-font-size: 12px; -fx-text-fill: #636e72;");

        // HBox tổng: [Chấm xanh] - [Thông tin]
        rootHBox.setSpacing(10);
        rootHBox.setAlignment(Pos.CENTER_LEFT);
        rootHBox.setPrefHeight(50); // Chiều cao mỗi dòng
        rootHBox.setStyle("-fx-padding: 0 10 0 10; -fx-background-color: transparent; -fx-border-color: #dfe6e9; -fx-border-width: 0 0 1 0;"); // Gạch chân mờ
        
        rootHBox.getChildren().addAll(statusDot, vBox);
        HBox.setHgrow(vBox, Priority.ALWAYS); // Đẩy nội dung chiếm hết chỗ trống
    }

    @Override
    protected void updateItem(StudentInventory item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            // --- 2. Đổ dữ liệu thật vào ---
            
            // Lấy thông tin chi tiết nguyên liệu (Tên, Unit)
            Ingredient ing = ingredientDAO.getIngredient(item.getIngredientId());
            
            if (ing != null) {
                lblName.setText(ing.getName());
                // Hiển thị: "Còn lại: 500 g"
                lblQty.setText(String.format("Còn lại: %.0f %s", item.getQuantity(), ing.getUnit()));
            } else {
                lblName.setText("Unknown Item (ID: " + item.getIngredientId() + ")");
            }

            setGraphic(rootHBox);
        }
    }
}
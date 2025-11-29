package doanJava.Components;

import doanJava.Model.Food;
import doanJava.service.FoodService.NutritionInfo;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Optional;
import java.util.function.BiConsumer; // <--- Quan trọng: Import cái này để xử lý sự kiện

public class RecipeCard extends VBox {

    private final Food food;
    private final NutritionInfo nutrition;
    
    // Biến lưu hành động sẽ làm khi chọn xong bữa (Callback)
    private final BiConsumer<String, Food> onCookAction;

    // --- CONSTRUCTOR MỚI (3 THAM SỐ) ---
    // Bạn đang thiếu cái này nên bên Controller báo đỏ
    public RecipeCard(Food food, NutritionInfo nutrition, BiConsumer<String, Food> onCookAction) {
        this.food = food;
        this.nutrition = nutrition;
        this.onCookAction = onCookAction;
        initUI();
    }

    private void initUI() {
        this.setPrefWidth(220);
        this.setPrefHeight(310);
        this.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-cursor: hand;");
        this.setSpacing(10);
        this.setPadding(new Insets(0, 0, 15, 0));

        // Đổ bóng
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(0, 0, 0, 0.1));
        shadow.setRadius(10);
        shadow.setOffsetY(5);
        this.setEffect(shadow);

        // Ảnh món ăn
        ImageView imageView = new ImageView();
        imageView.setFitWidth(220);
        imageView.setFitHeight(140);
        imageView.setPreserveRatio(false);

        try {
            Image img = new Image(getClass().getResource("/images/default_food.png").toExternalForm());
            imageView.setImage(img);
        } catch (Exception e) {
            imageView.setStyle("-fx-background-color: #eee;");
        }

        Rectangle clip = new Rectangle(220, 140);
        clip.setArcWidth(15);
        clip.setArcHeight(15);
        imageView.setClip(clip);

        // Nội dung text
        VBox contentBox = new VBox(5);
        contentBox.setPadding(new Insets(5, 15, 0, 15));

        Label nameLabel = new Label(food.getName());
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: #2d3436;");
        nameLabel.setWrapText(true);
        nameLabel.setPrefHeight(45);

        String infoText = String.format("🔥 %d Kcal  •  🥩 %dg Pro", (int)nutrition.calories, (int)nutrition.protein);
        Label metaLabel = new Label(infoText);
        metaLabel.setStyle("-fx-text-fill: #757575; -fx-font-size: 12px;");

        contentBox.getChildren().addAll(nameLabel, metaLabel);

        // Nút Cook Now
        Button btnCook = new Button("Cook Now");
        btnCook.setPrefWidth(180);
        btnCook.setStyle("-fx-background-color: #8CC63F; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 20; -fx-cursor: hand;");
        
        // SỰ KIỆN: Khi bấm nút -> Gọi hàm hiện hộp thoại
        btnCook.setOnAction(e -> showMealSelectionDialog());

        HBox actionBox = new HBox(btnCook);
        actionBox.setAlignment(Pos.CENTER);
        actionBox.setPadding(new Insets(10, 0, 0, 0));

        this.getChildren().addAll(imageView, contentBox, actionBox);
        
        this.setOnMouseEntered(e -> this.setTranslateY(-3));
        this.setOnMouseExited(e -> this.setTranslateY(0));
    }

    // Hộp thoại chọn bữa Sáng/Trưa/Tối
    private void showMealSelectionDialog() {
        ChoiceDialog<String> dialog = new ChoiceDialog<>("Bữa Sáng", "Bữa Sáng", "Bữa Trưa", "Bữa Tối");
        dialog.setTitle("Chọn Bữa Ăn");
        dialog.setHeaderText("Bạn muốn nấu món '" + food.getName() + "' cho bữa nào?");
        dialog.setContentText("Chọn bữa:");

        Optional<String> result = dialog.showAndWait();
        
        // Nếu người dùng chọn xong -> Gửi dữ liệu về Controller
        result.ifPresent(selectedMeal -> {
            if (onCookAction != null) {
                onCookAction.accept(selectedMeal, food);
            }
        });
    }
}
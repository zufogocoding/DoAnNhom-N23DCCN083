package doanJava.Components;

import doanJava.Model.Food;
import doanJava.service.FoodService.NutritionInfo;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.Optional;
import java.util.function.BiConsumer;

public class RecipeCard extends VBox {

    private final Food food;
    private final NutritionInfo nutrition;
    private final BiConsumer<String, Food> onCookAction;

    public RecipeCard(Food food, NutritionInfo nutrition, BiConsumer<String, Food> onCookAction) {
        this.food = food;
        this.nutrition = nutrition;
        this.onCookAction = onCookAction;
        initUI();
    }

    private void initUI() {
        // 1. Setup Card Container
        this.setPrefWidth(210);
        this.setPrefHeight(290);
        this.setStyle("-fx-background-color: white; -fx-background-radius: 20; -fx-cursor: hand;");
        this.setSpacing(10);
        this.setPadding(new Insets(0, 0, 15, 0));

        // Đổ bóng nhẹ
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(0, 0, 0, 0.08));
        shadow.setRadius(15);
        shadow.setOffsetY(5);
        this.setEffect(shadow);

        // 2. --- TẠO HÌNH ĐẠI DIỆN THAY VÌ ẢNH THẬT ---
        StackPane imagePlaceholder = createAvatarImage(food.getName());

        // 3. Thông tin món ăn
        VBox contentBox = new VBox(5);
        contentBox.setPadding(new Insets(5, 15, 0, 15));

        Label nameLabel = new Label(food.getName());
        nameLabel.setStyle("-fx-text-fill: #2d3436;");
        nameLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        nameLabel.setWrapText(true);
        nameLabel.setPrefHeight(45);

        String infoText = String.format("🔥 %d Kcal  •  🥩 %dg Pro", (int)nutrition.calories, (int)nutrition.protein);
        Label metaLabel = new Label(infoText);
        metaLabel.setStyle("-fx-text-fill: #757575; -fx-font-size: 12px;");

        contentBox.getChildren().addAll(nameLabel, metaLabel);

        // 4. Nút Cook Now
        Button btnCook = new Button("Cook Now");
        btnCook.setPrefWidth(180);
        btnCook.setStyle("-fx-background-color: #8CC63F; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 20; -fx-cursor: hand;");
        
        btnCook.setOnAction(e -> showMealSelectionDialog());

        HBox actionBox = new HBox(btnCook);
        actionBox.setAlignment(Pos.CENTER);
        actionBox.setPadding(new Insets(10, 0, 0, 0));

        // Add tất cả vào VBox chính
        this.getChildren().addAll(imagePlaceholder, contentBox, actionBox);
        
        // Hiệu ứng Hover
        this.setOnMouseEntered(e -> this.setTranslateY(-3));
        this.setOnMouseExited(e -> this.setTranslateY(0));
    }

    // --- HÀM TẠO AVATAR MÀU SẮC ---
    private StackPane createAvatarImage(String name) {
        StackPane stack = new StackPane();
        stack.setPrefSize(210, 140);
        
        // A. Sinh màu ngẫu nhiên nhưng cố định theo Tên món
        // (Tên giống nhau sẽ ra màu giống nhau)
        int hash = name.hashCode(); 
        // Dùng hệ màu HSB để ra màu Pastel tươi sáng
        // Hue: dựa vào tên, Saturation: 0.6 (tươi), Brightness: 0.9 (sáng)
        Color dynamicColor = Color.hsb(Math.abs(hash) % 360, 0.6, 0.9);
        
        // B. Tạo nền màu
        Rectangle bg = new Rectangle(210, 140);
        bg.setFill(dynamicColor);
        // Bo góc trên
        bg.setArcWidth(20);
        bg.setArcHeight(20);
        
        // Clip để bo tròn chỉ 2 góc trên (Trick)
        Rectangle clip = new Rectangle(210, 140);
        clip.setArcWidth(20);
        clip.setArcHeight(20);
        stack.setClip(clip);

        // C. Tạo chữ cái đầu (Ví dụ: "Phở Bò" -> "P")
        String firstLetter = "";
        if (name != null && !name.isEmpty()) {
            firstLetter = name.substring(0, 1).toUpperCase();
            // Nếu muốn lấy 2 chữ cái đầu (VD: P B):
            // String[] parts = name.split(" ");
            // if (parts.length > 1) firstLetter += parts[1].substring(0, 1).toUpperCase();
        }

        Label letterLabel = new Label(firstLetter);
        letterLabel.setTextFill(Color.WHITE);
        letterLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 48)); // Chữ to đùng
        // Thêm bóng cho chữ để nổi bật trên nền sáng
        letterLabel.setEffect(new DropShadow(10, Color.rgb(0,0,0,0.2)));

        stack.getChildren().addAll(bg, letterLabel);
        return stack;
    }

    private void showMealSelectionDialog() {
        ChoiceDialog<String> dialog = new ChoiceDialog<>("Bữa Sáng", "Bữa Sáng", "Bữa Trưa", "Bữa Tối");
        dialog.setTitle("Chọn Bữa Ăn");
        dialog.setHeaderText("Bạn muốn nấu món '" + food.getName() + "' cho bữa nào?");
        dialog.setContentText("Chọn bữa:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(selectedMeal -> {
            if (onCookAction != null) onCookAction.accept(selectedMeal, food);
        });
    }
}
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package doanJava;

/**
 *
 * @author hatua
 */
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class TestForms extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Tạo giao diện menu đơn giản
        VBox root = new VBox(20); // Spacing 20
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-padding: 50; -fx-background-color: #f0f2f5;");

        Label label = new Label("MENU TEST GIAO DIỆN");
        label.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // Nút 1: Test thêm nguyên liệu
        Button btnTestIngredient = new Button("1. Test Form Nhập Nguyên Liệu");
        styleButton(btnTestIngredient);
        btnTestIngredient.setOnAction(e -> openForm("/doanJava/view/AddIngredient.fxml", "Thêm Nguyên Liệu"));

        // Nút 2: Test thêm món ăn
        Button btnTestRecipe = new Button("2. Test Form Nhập Món Ăn");
        styleButton(btnTestRecipe);
        btnTestRecipe.setOnAction(e -> openForm("/doanJava/view/AddRecipe.fxml", "Thêm Món Ăn (Master-Detail)"));

        root.getChildren().addAll(label, btnTestIngredient, btnTestRecipe);

        Scene scene = new Scene(root, 400, 300);
        primaryStage.setTitle("Test Environment");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Hàm tiện ích để mở Form Pop-up
    private void openForm(String fxmlPath, String title) {
        try {
            // Load FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            // Tạo Stage mới (Cửa sổ con)
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            
            // Chặn không cho bấm vào cửa sổ cha khi cửa sổ con đang mở
            stage.initModality(Modality.APPLICATION_MODAL); 
            
            stage.showAndWait(); // Chờ đóng form thì code mới chạy tiếp

        } catch (IOException e) {
            System.err.println("Lỗi không tìm thấy file FXML: " + fxmlPath);
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Lỗi logic hoặc Database:");
            e.printStackTrace();
        }
    }

    // Style cho nút bấm đẹp tí
    private void styleButton(Button btn) {
        btn.setPrefWidth(250);
        btn.setPrefHeight(40);
        btn.setStyle("-fx-background-color: #0984e3; -fx-text-fill: white; -fx-font-size: 14px; -fx-cursor: hand; -fx-background-radius: 5;");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
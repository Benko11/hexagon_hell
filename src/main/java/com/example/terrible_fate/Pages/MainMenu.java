package com.example.terrible_fate.Pages;

import com.example.terrible_fate.Components.CustomButton;
import com.example.terrible_fate.ENV;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MainMenu {
    public Scene render(Stage stage) {
        var pane = new Pane();

        var title = new Label("Hexagon Hell");
        title.setStyle("-fx-font-size: 4em; -fx-text-fill: rgb(255, 0, 0); -fx-font-family: \"SF Mono\", monospace"); // why no rem support? :(
        title.relocate(40, 40);

        var desc = new Label("Click one of the buttons below to start playing:");
        desc.setStyle("-fx-font-family: 'SF Mono', monospace; -fx-text-fill: #b30b02");
        desc.relocate(40, 130);

        final var init = 150;
        final var step = 50;

        var smallHexBtn = new CustomButton("Small Hexagon", 40, init);
        var medHexBtn = new CustomButton("Medium Hexagon", 40, init + step);
        var largeHexBtn = new CustomButton("Large Hexagon", 40, init + step * 2);

        var smallHexSq = new CustomButton("Small Square", 40, init + step * 3);
        var medHexSq = new CustomButton("Medium Square", 40, init + step * 4);
        var largeHexSq = new CustomButton("Large Square", 40, init + step * 5);

        smallHexBtn.setOnAction(e -> {
            stage.setScene(new HexagonField(4).render(stage));
        });

        medHexBtn.setOnAction(e -> {
            stage.setScene(new HexagonField(7).render(stage));
        });

        largeHexBtn.setOnAction(e -> {
            stage.setScene(new HexagonField(10).render(stage));
        });

        smallHexSq.setOnAction(e -> stage.setScene(new SquareField(6).render(stage)));
        medHexSq.setOnAction(e -> stage.setScene(new SquareField(10).render(stage)));
        largeHexSq.setOnAction(e -> stage.setScene(new SquareField(14).render(stage)));

        pane.getChildren().setAll(title, desc, smallHexBtn, medHexBtn, largeHexBtn, smallHexSq, medHexSq, largeHexSq);

        var scene = new Scene(pane, ENV.WIDTH, ENV.HEIGHT);
        return scene;
    }
}


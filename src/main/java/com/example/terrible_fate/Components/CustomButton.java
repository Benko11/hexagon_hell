package com.example.terrible_fate.Components;

import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;

public class CustomButton extends Button {
    public CustomButton(String label, int posX, int posY) {
        super();

        var styles = "-fx-background-color: none; -fx-border-width: 3; -fx-border-color: #ff0000; -fx-font-size: 20px; -fx-font-family: \"SF Mono\", monospace; -fx-cursor: hand";
        this.setText(label);
        this.setStyle(styles);
        this.setPrefSize(300, 30);
        this.setOnMouseEntered(e -> this.setStyle("-fx-background-color: #ff0000; -fx-border-width: 3; -fx-border-color: #ff0000; -fx-font-size: 20px; -fx-font-family: \"SF Mono\", monospace; -fx-text-fill: #ffffff; -fx-cursor: hand"));
        this.setOnMouseExited(e -> this.setStyle(styles));
        this.relocate(posX, posY);
    }
}

package com.example.terrible_fate.Components;

import javafx.scene.control.Button;

/**
 * Wrapper class around the default buttons with added default styling and ease of use features.
 */
public class CustomButton extends Button {
    /**
     * Creates a button that is ready to be displayed in the pane.
     * @param label Text displayed in the button
     * @param posX  X coordinate of the button
     * @param posY  Y coordinate of the button
     */
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

    /**
     *
     * @param label      Text displayed in the button
     * @param posX       X coordinate of the button
     * @param posY       Y coordinate of the button
     * @param prefSizeX  Override default button size on X axis
     * @param prefSizeY  Override default button size on Y axis
     */
    public CustomButton(String label, int posX, int posY, int prefSizeX, int prefSizeY) {
        super();

        var styles = "-fx-background-color: none; -fx-border-width: 3; -fx-border-color: #ff0000; -fx-font-size: 20px; -fx-font-family: \"SF Mono\", monospace; -fx-cursor: hand";
        this.setText(label);
        this.setStyle(styles);
        this.setPrefSize(prefSizeX, prefSizeY);
        this.setOnMouseEntered(e -> this.setStyle("-fx-background-color: #ff0000; -fx-border-width: 3; -fx-border-color: #ff0000; -fx-font-size: 20px; -fx-font-family: \"SF Mono\", monospace; -fx-text-fill: #ffffff; -fx-cursor: hand"));
        this.setOnMouseExited(e -> this.setStyle(styles));
        this.relocate(posX, posY);
    }
}

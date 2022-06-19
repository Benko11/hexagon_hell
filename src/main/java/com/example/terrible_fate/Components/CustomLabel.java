package com.example.terrible_fate.Components;

import com.example.terrible_fate.ENV;
import javafx.scene.control.Label;

/**
 * Wrapper class around the default labels with added default styling and ease of use features.
 */
public class CustomLabel extends Label {
    /**
     * Creates a label that is ready to be displayed in the pane.
     * @param text       Text displayed in the label
     * @param hexColour  Colour of the label
     * @param posX       X coordinate of the label
     * @param posY       Y coordinate of the button
     */
    public CustomLabel(String text, String hexColour, double posX, double posY) {
        super();
        setText(text);
        setStyle("-fx-background-color: none; -fx-border-width: 3; -fx-text-fill: " + hexColour + "; -fx-font-size: 20px; -fx-font-family: \"SF Mono\", monospace");
        relocate(posX, posY);
    }

    /**
     * Creates a label that is ready to be displayed in the pane and allows to specify the font size.
     * @param text       Text displayed in the label
     * @param hexColour  Colour of the label
     * @param posX       X coordinate of the label
     * @param posY       Y coordinate of the button
     * @param fontSize   Override the default font size of the label
     */
    public CustomLabel(String text, String hexColour, double posX, double posY, int fontSize) {
        super();
        setText(text);
        setStyle("-fx-background-color: none; -fx-border-width: 3; -fx-text-fill: " + hexColour + "; -fx-font-size: "+ fontSize + "px; -fx-font-family: \"SF Mono\", monospace");
        relocate(posX, posY);
    }
}

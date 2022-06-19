package com.example.terrible_fate.Pages;

import com.example.terrible_fate.Components.CustomButton;
import com.example.terrible_fate.Components.CustomLabel;
import com.example.terrible_fate.ENV;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;

/**
 * Main Menu screen, where user can select the type and size of the field they want to play on.
 */
public class MainMenu {
    /**
     * Renders the main menu screen for the user, is called from Index
     * @param stage Stage used for potential redrawing.
     * @return      The scene for the stage for the pane.
     */
    public Scene render(Stage stage) {
        var pane = new Pane();

        var title = new CustomLabel(ENV.APP_NAME, "#ff0000", 40, 40, 60);
        var desc = new CustomLabel("Click one of the buttons below to start playing:", "#b30b02", 40, 130, 12);

        final var init = 150;
        final var step = 50;

        var smallHexBtn = new CustomButton("Small Hexagon", 40, init);
        var medHexBtn = new CustomButton("Medium Hexagon", 40, init + step);
        var largeHexBtn = new CustomButton("Large Hexagon", 40, init + step * 2);

        var smallHexSq = new CustomButton("Small Square", 40, init + step * 3);
        var medHexSq = new CustomButton("Medium Square", 40, init + step * 4);
        var largeHexSq = new CustomButton("Large Square", 40, init + step * 5);

        var toggleAI = new CheckBox("Play against AI");
        toggleAI.relocate(600, init + 50);

        var load = new CustomButton("Load", 600, init, 100, 30);

        smallHexBtn.setOnAction(e -> {
            stage.setScene(new HexagonField(ENV.SMALL_HEX_SIZE, toggleAI.isSelected()).render(stage));
        });

        medHexBtn.setOnAction(e -> {
            stage.setScene(new HexagonField(ENV.MEDIUM_HEX_SIZE, toggleAI.isSelected()).render(stage));
        });

        largeHexBtn.setOnAction(e -> {
            stage.setScene(new HexagonField(ENV.LARGE_HEX_SIZE, toggleAI.isSelected()).render(stage));
        });

        smallHexSq.setOnAction(e -> stage.setScene(new SquareField(ENV.SMALL_SQ_SIZE, toggleAI.isSelected()).render(stage)));
        medHexSq.setOnAction(e -> stage.setScene(new SquareField(ENV.MEDIUM_SQ_SIZE, toggleAI.isSelected()).render(stage)));
        largeHexSq.setOnAction(e -> stage.setScene(new SquareField(ENV.LARGE_SQ_SIZE, toggleAI.isSelected()).render(stage)));
        load.setOnAction(e -> stage.setScene(new Load().render(stage)));

        pane.getChildren().setAll(title, desc, smallHexBtn, medHexBtn, largeHexBtn, smallHexSq, medHexSq, largeHexSq, load, toggleAI);


        return new Scene(pane, ENV.WIDTH, ENV.HEIGHT);
    }
}


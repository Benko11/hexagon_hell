package com.example.terrible_fate.Pages;

import com.example.terrible_fate.Components.CustomButton;
import com.example.terrible_fate.Components.CustomLabel;
import com.example.terrible_fate.ENV;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * End screen when the corruption has reached ENV.VICTORY value (presumed to be 0.7 or 70%)
 */
public class EndGame {
    private boolean player1Wins;

    /**
     * @param player1Wins Inherited from the previous scene, determines which text is displayed.
     */
    public EndGame(boolean player1Wins) {
        this.player1Wins = player1Wins;
    }

    /**
     * Render the end screen page with the option to go back to the main menu.
     * @param stage Stage used for potential redrawing.
     * @return      The scene for the stage for the pane.
     */
    public Scene render(Stage stage) {
        var pane = new Pane();
        var message = player1Wins ? "Player 1 wins!" : "Player 2 wins!";
        var msg = new CustomLabel(message, player1Wins ? ENV.PLAYER1_COLOUR : ENV.PLAYER2_COLOUR, 50, ENV.HEIGHT / 2.0 - 100, 60);
        var mainMenuBtn = new CustomButton("Back to Main Menu", 50, ENV.HEIGHT - 50);

        mainMenuBtn.setOnAction(e -> stage.setScene(new MainMenu().render(stage)));

        pane.getChildren().add(msg);
        pane.getChildren().add(mainMenuBtn);

        return new Scene(pane, ENV.WIDTH, ENV.HEIGHT);
    }
}
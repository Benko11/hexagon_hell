package com.example.terrible_fate;

import com.example.terrible_fate.Pages.MainMenu;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * The entry point for the application. Application should be run from this file.
 */
public class Index extends Application {
    /**
     * Launches the Java applet.
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Initializes the stage on which graphical elements will be rendered.
     * @param stage      The stage used for initializing the graphical payloads.
     * @throws Exception Generic exception when rendering does not function.
     */
    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new MainMenu().render(stage));
        stage.setTitle(ENV.APP_NAME);
        stage.show();
    }
}


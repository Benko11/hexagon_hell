package com.example.terrible_fate;

import com.example.terrible_fate.Pages.MainMenu;
import javafx.application.Application;
import javafx.stage.Stage;


public class Index extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new MainMenu().render(stage));
        stage.setTitle("Hexagon Hell");
        stage.show();
    }
}


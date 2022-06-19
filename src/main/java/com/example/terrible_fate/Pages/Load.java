package com.example.terrible_fate.Pages;

import com.example.terrible_fate.Components.CustomButton;
import com.example.terrible_fate.ENV;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class Load {
    public Scene render(Stage stage) {
        var pane = new Pane();

        var files = new ArrayList<File>();

        for (var file: Objects.requireNonNull(new File("src/main/saves").listFiles())) {
            if (file.isFile()) files.add(file);
        }

        var listedFiles = new ComboBox<String>();
        listedFiles.setStyle("-fx-font-size: 20px; -fx-font-family: \"SF Mono\", monospace");
        listedFiles.relocate(50, 50);
        for (var file: files) {
            listedFiles.getItems().add(file.toString());
        }


        var load = new CustomButton("Load", 30, ENV.HEIGHT - 60, 100, 30);
        load.setOnAction(e -> {
            String selectedFile = listedFiles.getValue();

            System.out.println(selectedFile);
            if (selectedFile == null) {
                stage.setScene(new MainMenu().render(stage));
                return;
            }

            try {
                var readFile = new File(selectedFile);
                var scanner = new Scanner(readFile);

                boolean isHex = false;
                int sideLength = -1;
                ArrayList<Integer> player1Corruption = new ArrayList<>();
                ArrayList<Integer> player2Corruption = new ArrayList<>();
                ArrayList<Integer> stages = new ArrayList<>();
                boolean player1Turn = false;
                boolean AIMode = false;

                int i = 0;
                while (scanner.hasNextLine()) {
                    var currentLine = scanner.nextLine();
                    if (i == 0) {
                        isHex = currentLine.equals("hex");
                    } else if (i == 1) {
                        sideLength = Integer.parseInt(currentLine);
                    } else if (i == 2) {
                        System.out.println(currentLine);
                        var split = currentLine.split(",");
                        for (var item: split) {
                            player1Corruption.add(Integer.parseInt(item.trim()));
                        }
                    } else if (i == 3) {
                        System.out.println(currentLine);
                        var split = currentLine.split(",");
                        for (var item: split) {
                            player2Corruption.add(Integer.parseInt(item.trim()));
                        }
                    } else if (i == 4) {
                        System.out.println(currentLine);
                        var split = currentLine.split(",");
                        for (var item: split) {
                            stages.add(Integer.parseInt(item.trim()));
                        }
                    } else if (i == 5) {
                        player1Turn = currentLine.equals("true");
                    } else if (i == 6) {
                        AIMode = currentLine.equals("true");
                    }

                    i++;
                }

                System.out.println("isHex: " + isHex);
                System.out.println("sideLength: " + sideLength);
                System.out.println("player1Corruption: " + player1Corruption);
                System.out.println("player2Corruption: " + player2Corruption);
                System.out.println("stages: " + stages);
                System.out.println("player1Turn: " + player1Turn);
                System.out.println("AIMode: " + AIMode);

                if (isHex) {
                    stage.setScene(new HexagonField(sideLength, player1Corruption, player2Corruption, stages, player1Turn, AIMode).load(stage));
                    return;
                }

                stage.setScene(new SquareField(sideLength, player1Corruption, player2Corruption, stages, player1Turn, AIMode).load(stage));
            } catch (FileNotFoundException er) {
                er.printStackTrace();
            }
        });

        var back = new CustomButton("Back", ENV.WIDTH - 120, ENV.HEIGHT - 60, 100, 30);
        back.setOnAction(e -> stage.setScene(new MainMenu().render(stage)));

        pane.getChildren().setAll(listedFiles, back, load);

        return new Scene(pane, ENV.WIDTH, ENV.HEIGHT);
    }
}

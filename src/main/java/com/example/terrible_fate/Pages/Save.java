package com.example.terrible_fate.Pages;

import com.example.terrible_fate.Components.CustomButton;
import com.example.terrible_fate.Components.CustomLabel;
import com.example.terrible_fate.Components.Vector;
import com.example.terrible_fate.ENV;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Save {
    private ArrayList<Integer> player1Corruption;
    private ArrayList<Integer> player2Corruption;
    private ArrayList<Integer> states;
    private boolean isHexagon;
    private int sideLength;
    private boolean player1Turn;
    private boolean AIMode;

    public Save(ArrayList<Integer> player1Corruption, ArrayList<Integer> player2Corruption, ArrayList<Integer> states, boolean isHexagon, int sideLength, boolean player1Turn, boolean AIMode) {
        this.player1Corruption = player1Corruption;
        this.player2Corruption = player2Corruption;
        this.states = states;
        this.isHexagon = isHexagon;
        this.sideLength = sideLength;
        this.player1Turn = player1Turn;
        this.AIMode = AIMode;
    }
    
    public String generateFileContents() {
        StringBuilder str = new StringBuilder();
        if (isHexagon)
            str.append("hex\n");
        else str.append("sq\n");
        str.append(sideLength).append("\n");

        for (int i = 0; i < player1Corruption.size(); i++) {
            str.append(player1Corruption.get(i));
            if (i + 1 < player1Corruption.size()) str.append(", ");
        }
        str.append("\n");
        for (int i = 0; i < player2Corruption.size(); i++) {
            str.append(player2Corruption.get(i));
            if (i + 1 < player2Corruption.size()) str.append(", ");
        }
        str.append("\n");
        for (int i = 0; i < states.size(); i++) {
            str.append(states.get(i));
            if (i + 1 < states.size()) str.append(", ");
        }
        str.append("\n");
        str.append(player1Turn).append("\n");
        str.append(AIMode);

        return str.toString();
    }

    public Scene render(Stage stage) {
        var pane = new Pane();
        System.out.println(generateFileContents());

        try {
            var name = System.currentTimeMillis();
            var file = new File("src/main/saves/" + name + ".txt");
            var fw = new FileWriter(file);
            fw.write(generateFileContents());
            fw.close();

            var msg = new CustomLabel("Save " + name + " generated","#000", 20, 400);
            var button = new CustomButton("Main Menu", 20, ENV.HEIGHT - 60);
            button.setOnAction(e -> stage.setScene(new MainMenu().render(stage)));

            pane.getChildren().add(msg);
            pane.getChildren().add(button);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new Scene(pane, ENV.WIDTH, ENV.HEIGHT);
    }
}

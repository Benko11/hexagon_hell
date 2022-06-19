package com.example.terrible_fate.Pages;

import com.example.terrible_fate.Components.CustomLabel;
import com.example.terrible_fate.Components.Hexagon;
import com.example.terrible_fate.Components.ReactivePolygon;
import com.example.terrible_fate.ENV;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

abstract public class Field {
    protected int sideLength;
    protected Canvas canvas;
    protected ArrayList<Hexagon> hexagons;
    protected ArrayList<ReactivePolygon> polygons;
    protected boolean player1Turn;
    protected ArrayList<Integer> player1Corruption;
    protected ArrayList<Integer> player2Corruption;
    protected int player1Start;
    protected int player2Start;
    protected boolean AIMode = false;
    protected ArrayList<Integer> stages;
    protected Pane pane;

    public Field(int sideLength) {
        this.sideLength = sideLength;
        this.canvas = new Canvas(ENV.WIDTH, ENV.HEIGHT);
        this.hexagons = new ArrayList<>();
        this.polygons = new ArrayList<>();
        this.player1Turn = true;
        this.player1Corruption = new ArrayList<>();
        this.player2Corruption = new ArrayList<>();
        this.pane = new Pane(canvas);
    }

    public Field(int sideLength, boolean AIMode) {
        this.sideLength = sideLength;
        this.canvas = new Canvas(ENV.WIDTH, ENV.HEIGHT);
        this.hexagons = new ArrayList<>();
        this.polygons = new ArrayList<>();
        this.player1Turn = true;
        this.player1Corruption = new ArrayList<>();
        this.player2Corruption = new ArrayList<>();
        this.pane = new Pane(canvas);
        this.AIMode = AIMode;
    }

    public Field(int sideLength, ArrayList<Integer> player1Corruption, ArrayList<Integer> player2Corruption, ArrayList<Integer> stages, boolean player1Turn, boolean AIMode) {
        this.sideLength = sideLength;
        this.player1Corruption = player1Corruption;
        this.player2Corruption = player2Corruption;
        this.player1Turn = player1Turn;
        this.AIMode = AIMode;
        this.stages = stages;

        this.canvas = new Canvas(ENV.WIDTH, ENV.HEIGHT);
        this.hexagons = new ArrayList<>();
        this.polygons = new ArrayList<>();
        this.pane = new Pane(canvas);
    }

    abstract public void initField(double initX, double initY);
    abstract public ArrayList<Hexagon> getAdjacentHexagons(Hexagon hex);
    abstract public Scene render(Stage stage);
    abstract public Scene load(Stage stage);
    abstract protected int getFieldSize();

    /**
     * We assume that the hexagons and the polygons share the same indexing.
     * @param p
     */
    protected void handleCorruption(ReactivePolygon p, ReactivePolygon prev) {
        var polygonIndex = polygons.indexOf(p);
        var newState = p.getState();
        var hexagon = hexagons.get(polygonIndex);

        var relevantHexagon = getAdjacentHexagons(hexagon).get(newState - 1);
        if (relevantHexagon == null) return; // hexagon does not exist, nothing to corrupt

        var relevantHexagonIndex = hexagons.indexOf(relevantHexagon);
        polygons.get(relevantHexagonIndex).corrupt(player1Turn); // visually corrupt the field

        if (player1Turn) {
            if (!player1Corruption.contains(relevantHexagonIndex))
                player1Corruption.add(relevantHexagonIndex);
            player2Corruption.remove(Integer.valueOf(relevantHexagonIndex));
        } else {
            player1Corruption.remove(Integer.valueOf(relevantHexagonIndex));
            if (!player2Corruption.contains(relevantHexagonIndex))
                player2Corruption.add(relevantHexagonIndex);
        }

        System.out.println("Hexagon: " + hexagons.get(polygonIndex));
        System.out.println("Relevant hexagon: " + relevantHexagon);
    }

    protected String updateScore(boolean player1) {
        if (player1)
            return Math.round((double) player1Corruption.size() / getFieldSize() * 100) + "%";

        return (int) Math.round((double) player2Corruption.size() / getFieldSize() * 100) + "%";
    }

    protected void updateTurn() {
        player1Turn = !player1Turn;
    }

    /**
     * Generates a state for the initial configurations of ReactivePolygon objects in the field.
     * @param hexagon Hexagon upon which the decision is made.
     * @return        State number that puts the given hexagon in its respective place.
     */
    protected int generateState(Hexagon hexagon) {
        if (hexagons.indexOf(hexagon) == player1Start) {
            return ENV.PLAYER1_STATE_START;
        }

        if (hexagons.indexOf(hexagon) == player2Start) {
            return ENV.PLAYER2_STATE_START;
        }

        return new Random().nextInt(6) + 1;
    }

    protected void update(ReactivePolygon p, CustomLabel player1Score, CustomLabel player2Score, Stage stage) {
        // remove the old line to replace with the new one
        pane.getChildren().remove(p.getLine());

        // update the state and rotate the line accordingly
        p.updateState();
        p.rotateLine();

        handleCorruption(p, null);

        updateTurn();

        // redraw the line back
        pane.getChildren().add(p.getLine());

        // update corruption scores
        player1Score.setText(updateScore(true));
        player2Score.setText(updateScore(false));

        // check to see if game should end
        if ((double) player1Corruption.size() / getFieldSize() >= ENV.VICTORY) {
            stage.setScene(new EndGame(true).render(stage));
        } else if ((double) player2Corruption.size() / getFieldSize() >= ENV.VICTORY) {
            stage.setScene(new EndGame(false).render(stage));
        }
    }
}

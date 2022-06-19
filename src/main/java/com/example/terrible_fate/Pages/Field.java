package com.example.terrible_fate.Pages;

import com.example.terrible_fate.Components.CustomLabel;
import com.example.terrible_fate.Components.Hexagon;
import com.example.terrible_fate.Components.ReactivePolygon;
import com.example.terrible_fate.ENV;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Random;

/**
 * A template for HexagonField and SquareField.
 * Implements the common attributes and methods that are then inherited from.
 * If AI is present, it is always assumed to be Player 2.
 */
abstract public class Field {
    // side length in hexagons
    protected int sideLength;
    // standard canvas
    protected Canvas canvas;
    // list of drawn hexagons
    protected ArrayList<Hexagon> hexagons;
    // list of interactive polygons (same bounds as the hexagons)
    protected ArrayList<ReactivePolygon> polygons;
    // determiner of players' turns
    protected boolean player1Turn;
    // list of corrupted fields by player 1 (integers used for identification)
    protected ArrayList<Integer> player1Corruption;
    // list of corrupted fields by player 2 (integers used for identification)
    protected ArrayList<Integer> player2Corruption;
    // starting index of the hexagon for player 1
    protected int player1Start;
    // starting index of the hexagon for player 2
    protected int player2Start;
    // determiner of AI/1v1 player mode
    protected boolean AIMode;
    // used in loading to keep track of the states of each hexagon (1-6)
    protected ArrayList<Integer> stages;
    // standard pane
    protected Pane pane;

    /**
     * Standard constructor accepting only the side length for a given field, AI is automatically false
     * @param sideLength length in hexagons of one side in HexagonField, and the shorter side in SquareField
     */
    public Field(int sideLength) {
        this.sideLength = sideLength;
        this.canvas = new Canvas(ENV.WIDTH, ENV.HEIGHT);
        this.hexagons = new ArrayList<>();
        this.polygons = new ArrayList<>();
        this.player1Turn = true;
        this.player1Corruption = new ArrayList<>();
        this.player2Corruption = new ArrayList<>();
        this.pane = new Pane(canvas);
        this.AIMode = false;
    }

    /**
     *
     * @param sideLength length in hexagons of one side in HexagonField, and the shorter side in SquareField
     * @param AIMode     overriding the AI mode default option (false)
     */
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

    /**
     * Constructor used for loading, all data is explicitly passed to the constructor and dealt with in the load() method accordingly
     * @param sideLength         length in hexagons of one side in HexagonField, and the shorter side in SquareField
     * @param player1Corruption  list of corrupted fields by player 1 (integers used for identification)
     * @param player2Corruption  list of corrupted fields by player 2 (integers used for identification)
     * @param stages             used in loading to keep track of the states of each hexagon (1-6)
     * @param player1Turn        determiner of players' turns
     * @param AIMode             determiner of AI/1v1 player mode
     */
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

    /**
     * Initializes (but does draw) the hexagons the field consists of
     * @param initX X coordinate of the initial hexagon
     * @param initY Y coordinate of the initial hexagon
     */
    abstract public void initField(double initX, double initY);

    /**
     * Get the list of hexagons adjacent to the given hexagon, uses vectors for determining the results
     * @param hex the hexagon from which the adjacent list is generated
     * @return    the list of all hexagons adjacent to the given hexagon (ones that do not exist may occur, they are filtered later)
     */
    abstract public ArrayList<Hexagon> getAdjacentHexagons(Hexagon hex);

    /**
     * Method that runs when a player starts a new game.
     * @param stage Stage used for potential redrawing
     * @return      The scene for the stage for the pane.
     */
    abstract public Scene render(Stage stage);

    /**
     * Method that runs when a player loads a saved game.
     * @param stage Stage used for potential redrawing
     * @returnv     The scene for the stage for the pane.
     */
    abstract public Scene load(Stage stage);

    /**
     * Returns the number of hexagons in the field.
     * @return The cardinality of all the hexagons present and playable in the field.
     */
    abstract protected int getFieldSize();

    /**
     * We assume that the hexagons and the polygons share the same indexing.
     * The double arguments are used in recursion, and if states of p and prev cancel out, the recursion terminates.
     * @param p    current polygon that is being evaluated
     * @param prev previous polygon that was evaluated (null, during first evaluation)
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
    }

    /**
     * Updates the visual percentage score that is displayed during gameplay.
     * @param player1 Determines which score counter to update
     * @return        Rounded percentage result of occupied hexagons in the field in relation to the total field size.
     */
    protected String updateScore(boolean player1) {
        if (player1)
            return Math.round((double) player1Corruption.size() / getFieldSize() * 100) + "%";

        return (int) Math.round((double) player2Corruption.size() / getFieldSize() * 100) + "%";
    }

    /**
     * Helper method for changing player turns.
     */
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

    protected void AITurn(Stage stage, CustomLabel player1Score, CustomLabel player2Score) {
        if (player1Turn) return;

        var randomFieldIndex = player2Corruption.get(new Random().nextInt(player2Corruption.size()));

        pane.getChildren().remove(polygons.get(randomFieldIndex).getLine());

        polygons.get(randomFieldIndex).updateState();
        polygons.get(randomFieldIndex).rotateLine();
        handleCorruption(polygons.get(randomFieldIndex), null);

        pane.getChildren().add(polygons.get(randomFieldIndex).getLine());

        updateTurn();

        // check to see if game should end
        if ((double) player1Corruption.size() / getFieldSize() >= ENV.VICTORY) {
            stage.setScene(new EndGame(true).render(stage));
        } else if ((double) player2Corruption.size() / getFieldSize() >= ENV.VICTORY) {
            stage.setScene(new EndGame(false).render(stage));
        }

        player1Score.setText(updateScore(true));
        player2Score.setText(updateScore(false));
    }
}

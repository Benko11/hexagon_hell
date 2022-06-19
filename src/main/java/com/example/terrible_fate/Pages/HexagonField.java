package com.example.terrible_fate.Pages;

import com.example.terrible_fate.Components.*;
import com.example.terrible_fate.ENV;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

/**
 * Draws a field where hexagons are arranged in one large hexagon.
 * Adjustable size.
 */
public class HexagonField extends Field {
    /**
     * Initializes the basic values used in the field.
     * @param sideLength the length of one side of the large hexagon (equilateral type)
     */
    public HexagonField(int sideLength) {
        super(sideLength);
        this.player1Start = 0;
        this.player2Start = 2 * sideLength - 2;
    }

    public HexagonField(int sideLength, boolean AIMode) {
        super(sideLength);
        this.player1Start = 0;
        this.player2Start = 2 * sideLength - 2;
        this.AIMode = AIMode;
    }

    public HexagonField(int sideLength, ArrayList<Integer> player1Corruption, ArrayList<Integer> player2Corruption, ArrayList<Integer> stages, boolean player1Turn, boolean AIMode) {
        super(sideLength, player1Corruption, player2Corruption, stages, player1Turn, AIMode);
    }

    /**
     * Prepares hexagons to be drawn into the field.
     * @param initX the X coordinate of the topmost hexagon's upper-left point
     * @param initY the Y coordinate of the topmost hexagon's upper-left point
     */
    @Override
    public void initField(double initX, double initY) {
        final int largest = sideLength * 2 - 1;
        int indexX = largest - 1;
        int indexY = sideLength - 1;
        for (int i = 0; i < largest; i++) {
            var hexagon = new Hexagon(initX, initY + ENV.APOTHEM * 2 * i, canvas.getGraphicsContext2D(), new Vector(indexX, indexY));
            hexagons.add(hexagon);
            indexX--;
        }

        var aux = Math.sqrt(ENV.HEXAGON_RADIUS * 2 + ENV.APOTHEM * 2);

        // left side
        for (int i = 1; i < sideLength; i++) {
            indexY--;
            indexX = largest - 1 - i;
            for (int j = 0; j < largest - i; j++) {
                var leftHexagon = new Hexagon(initX - i * (ENV.HEXAGON_RADIUS + aux), initY + i * ENV.APOTHEM + ENV.APOTHEM * 2 * j, canvas.getGraphicsContext2D(), new Vector(indexX, indexY));
                hexagons.add(leftHexagon);
                indexX--;
            }
        }

        // right side
        indexY = sideLength - 1;
        for (int i = 1; i < sideLength; i++) {
            indexY++;
            indexX = largest - 1 - i;

            for (int j = 0; j < largest - i; j++) {
                var rightHexagon = new Hexagon(initX + i * (ENV.HEXAGON_RADIUS + aux), initY + i * ENV.APOTHEM + ENV.APOTHEM * 2 * j, canvas.getGraphicsContext2D(), new Vector(indexX, indexY));
                hexagons.add(rightHexagon);
                indexX--;
            }
        }
    }

    /**
     * Returns the list of hexagons adjacent to a particular hexagon in the field.
     * @param hex The hexagon from which the neighbouring hexagons are sought.
     * @return    The list of hexagons of adjacent hexagons to a particular hexagon hex.
     */
    @Override
    public ArrayList<Hexagon> getAdjacentHexagons(Hexagon hex) {
        ArrayList<Hexagon> adjacent = new ArrayList<>();
        final var coords = hex.getVector();
        final var middle = sideLength - 1;

        ArrayList<Vector> vectors;
        if (coords.getY() == middle) {
            vectors = getFromMiddle(hex.getVector());
        } else if (coords.getY() < middle) {
            vectors = getFromLeft(hex.getVector());
        } else {
            vectors = getFromRight(hex.getVector());
        }

        for (var vector: vectors) {
            Hexagon found = Hexagon.findByVector(hexagons, vector);

            adjacent.add(found);
        }

        return adjacent;
    }

    /**
     * Helper method used to find the neighbouring hexagons for the hexagon in the middle column.
     * @param v Vector from which neighbouring coordinates are deduced.
     * @return  Return the list of identifying vectors for the hexagons.
     */
    private ArrayList<Vector> getFromMiddle(Vector v) {
        ArrayList<Vector> adjacent = new ArrayList<>();
        adjacent.add(new Vector(v.getX() + 1, v.getY()));
        adjacent.add(new Vector(v.getX(), v.getY() + 1));
        adjacent.add(new Vector(v.getX() - 1, v.getY() + 1));
        adjacent.add(new Vector(v.getX() - 1, v.getY()));
        adjacent.add(new Vector(v.getX() - 1, v.getY() - 1));
        adjacent.add(new Vector(v.getX(), v.getY() - 1));
        return adjacent;
    }

    /**
     * Helper method used to find the neighbouring hexagons for the hexagon to the left of the middle column.
     * @param v Vector from which neighbouring coordinates are deduced.
     * @return  Return the list of identifying vectors for the hexagons.
     */
    private ArrayList<Vector> getFromLeft(Vector v) {
        ArrayList<Vector> adjacent = new ArrayList<>();
        adjacent.add(new Vector(v.getX() + 1, v.getY()));
        adjacent.add(new Vector(v.getX() + 1, v.getY() + 1));
        adjacent.add(new Vector(v.getX(), v.getY() + 1));
        adjacent.add(new Vector(v.getX() - 1, v.getY()));
        adjacent.add(new Vector(v.getX() - 1, v.getY() - 1));
        adjacent.add(new Vector(v.getX(), v.getY() - 1));

        return adjacent;
    }

    /**
     * Helper method used to find the neighbouring hexagons for the hexagon to the right of the middle column.
     * @param v Vector from which neighbouring coordinates are deduced.
     * @return  Return the list of identifying vectors for the hexagons.
     */
    private ArrayList<Vector> getFromRight(Vector v) {
        ArrayList<Vector> adjacent = new ArrayList<>();
        adjacent.add(new Vector(v.getX() + 1, v.getY()));
        adjacent.add(new Vector(v.getX(), v.getY() + 1));
        adjacent.add(new Vector(v.getX() - 1, v.getY() + 1));
        adjacent.add(new Vector(v.getX() - 1, v.getY()));
        adjacent.add(new Vector(v.getX(), v.getY() - 1));
        adjacent.add(new Vector(v.getX() + 1, v.getY() - 1));

        return adjacent;
    }

    /**
     * Generates the number of hexagons in the field.
     * @return The cardinality of the set of hexagons in the field.
     */
    protected int getFieldSize() {
        int count = 1;
        for (int i = 1; i < sideLength; i++) {
            count += 6 * i;
        }

        return count;
    }

    public Scene load(Stage stage) {
        var scene = new Scene(pane, ENV.WIDTH, ENV.HEIGHT);
        initField(ENV.WIDTH / 2.0 - ENV.HEXAGON_RADIUS / 2.0, 30);

        var player1Score = new CustomLabel(Math.round((double) player1Corruption.size() / getFieldSize() * 100) + "%", ENV.PLAYER1_COLOUR, 20, 20);
        var player2Score = new CustomLabel(Math.round((double) player2Corruption.size() / getFieldSize() * 100) + "%", ENV.PLAYER2_COLOUR, 20, 40);
        pane.getChildren().add(player1Score);
        pane.getChildren().add(player2Score);

        var exitBtn = new CustomButton("Exit", 680, 550, 100, 30);
        exitBtn.setOnAction(e -> stage.setScene(new MainMenu().render(stage)));
        pane.getChildren().add(exitBtn);

        var saveBtn = new CustomButton("Save", 20, 550, 100, 30);
        saveBtn.setOnAction(e -> {
            var states = new ArrayList<Integer>();
            for (var p: polygons) {
                states.add(p.getState());
            }
            stage.setScene(new Save(player1Corruption, player2Corruption, states, true, sideLength, player1Turn, AIMode).render(stage));
        });
        pane.getChildren().add(saveBtn);

        for (var hexagon: hexagons) {
            Polygon pol = hexagon.draw();
            var p = new ReactivePolygon(stages.get(hexagons.indexOf(hexagon)));
            p.addPolygon(pol);

            polygons.add(p);

            pane.getChildren().add(polygons.get(polygons.size() - 1).getPolygon());
            pane.getChildren().add(polygons.get(polygons.size() - 1).getCircle());
            pane.getChildren().add(polygons.get(polygons.size() - 1).getLine());
        }

        for (var item: player1Corruption) {
            polygons.get(item).corrupt(true);
        }

        for (var item: player2Corruption) {
            polygons.get(item).corrupt(false);
        }

        for (var p: polygons) {
            var polygonIndex = polygons.indexOf(p);
            if (polygonIndex == player1Start) {
                p.corrupt(true);
            } else if (polygonIndex == player2Start) {
                p.corrupt(false);
            }

            p.getPolygon().setOnMouseClicked(e -> {
                // making sure turns are taken properly
                if (player1Turn && !player1Corruption.contains(polygonIndex)) return;
                if (!player1Turn && !player2Corruption.contains(polygonIndex)) return;

                Platform.runLater(() -> {
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
                });
            });
        }

        return scene;
    }

    /**
     * Renders the entire field and contains an event listener for updates on the scene.
     * @param stage Stage used for potential redrawing.
     * @return      The scene for the stage for the pane.
     */
    public Scene render(Stage stage) {
        var scene = new Scene(pane, ENV.WIDTH, ENV.HEIGHT);
        initField(ENV.WIDTH / 2.0 - ENV.HEXAGON_RADIUS / 2.0, 30);

        var player1Score = new CustomLabel(Math.round(1.0 / getFieldSize() * 100) + "%", ENV.PLAYER1_COLOUR, 20, 20);
        var player2Score = new CustomLabel(Math.round(1.0 / getFieldSize() * 100) + "%", ENV.PLAYER2_COLOUR, 20, 40);
        pane.getChildren().add(player1Score);
        pane.getChildren().add(player2Score);

        var exitBtn = new CustomButton("Exit", 680, 550, 100, 30);
        exitBtn.setOnAction(e -> stage.setScene(new MainMenu().render(stage)));
        pane.getChildren().add(exitBtn);

        var saveBtn = new CustomButton("Save", 20, 550, 100, 30);
        saveBtn.setOnAction(e -> {
            var states = new ArrayList<Integer>();
            for (var p: polygons) {
                states.add(p.getState());
            }
            stage.setScene(new Save(player1Corruption, player2Corruption, states, true, sideLength, player1Turn, AIMode).render(stage));
        });
        pane.getChildren().add(saveBtn);

        for (var hexagon: hexagons) {
            int state = generateState(hexagon);

            Polygon pol = hexagon.draw();
            var p = new ReactivePolygon(state);
            p.addPolygon(pol);

            polygons.add(p);

            pane.getChildren().add(polygons.get(polygons.size() - 1).getPolygon());
            pane.getChildren().add(polygons.get(polygons.size() - 1).getCircle());
            pane.getChildren().add(polygons.get(polygons.size() - 1).getLine());
        }

        player1Corruption.add(player1Start);
        player2Corruption.add(player2Start);

        for (var p: polygons) {
            var polygonIndex = polygons.indexOf(p);
            if (polygonIndex == player1Start) {
                p.corrupt(true);
            } else if (polygonIndex == player2Start) {
                p.corrupt(false);
            }

            p.getPolygon().setOnMouseClicked(e -> {
                // making sure turns are taken properly
                if (player1Turn && !player1Corruption.contains(polygonIndex)) return;
                if (!player1Turn && !player2Corruption.contains(polygonIndex)) return;

                Platform.runLater(() -> {
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
                });
            });
        }

        return scene;
    }
}

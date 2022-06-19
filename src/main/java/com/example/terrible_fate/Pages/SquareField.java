package com.example.terrible_fate.Pages;

import com.example.terrible_fate.Components.*;
import com.example.terrible_fate.ENV;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Random;

/**
 * Draws a field where hexagons are arranged in one large square.
 * Adjustable size.
 */
public class SquareField extends Field {
    /**
     * Initializes the basic values used in the field.
     * @param sideLength the length of the vertical side of the hexagon (shorter one)
     */
    public SquareField(int sideLength) {
        super(sideLength);
        player1Start = 0;
    }

    public SquareField(int sideLength, ArrayList<Integer> player1Corruption, ArrayList<Integer> player2Corruption, ArrayList<Integer> stages, boolean player1Turn, boolean AIMode) {
        super(sideLength, player1Corruption, player2Corruption, stages, player1Turn, AIMode);
    }

    public SquareField(int sideLength, boolean AIMode) {
        super(sideLength);
        this.player1Start = 0;
        this.player2Start = 2 * sideLength - 2;
        this.AIMode = AIMode;
    }

    /**
     * Prepares hexagons to be drawn into the field.
     * @param initX the X coordinate of the leftmost hexagon's upper-left point
     * @param initY the Y coordinate of the leftmost hexagon's upper-left point
     */
    public void initField(double initX, double initY) {
        var gc = canvas.getGraphicsContext2D();
        final var original = initY;

        int setX = 0;

        for (int i = 0; i < sideLength + 1; i++) {
            int setY = 0;
            if (i % 2 == 1) setY = 1;

            for (int j = 0; j < sideLength; j++) {
                if (i % 2 == 0) {
                    var hexagon = new Hexagon(initX, initY + j * ENV.APOTHEM * 2, gc, new Vector(setX, setY));
//                    canvas.getGraphicsContext2D().setFont(new Font("SF Mono", 9));
//                    canvas.getGraphicsContext2D().fillText(setX + "," + setY, initX, initY + j * ENV.APOTHEM * 2 + ENV.HEXAGON_RADIUS);
                    hexagons.add(hexagon);
                    setY += 2;
                    continue;
                }

                if (j + 1 == sideLength) continue;

                var aux = Math.sqrt(ENV.HEXAGON_RADIUS * 2 + ENV.APOTHEM * 2);
                var hexagon = new Hexagon(initX + aux, initY + ENV.APOTHEM + j * ENV.APOTHEM * 2, gc, new Vector(setX, setY));
//                canvas.getGraphicsContext2D().setFont(new Font("SF Mono", 9));
//                canvas.getGraphicsContext2D().fillText(setX + "," + setY, initX + aux, initY + ENV.APOTHEM + j * ENV.APOTHEM * 2 + ENV.HEXAGON_RADIUS);
                hexagons.add(hexagon);
                setY += 2;
            }

            initY = original;
            initX += ENV.HEXAGON_RADIUS;
            if (i % 2 == 1) {
                initX += ENV.HEXAGON_RADIUS;
                setX++;
            }
        }

        player2Start = hexagons.size() - 1;
    }

    /**
     * Returns the list of hexagons adjacent to a particular hexagon in the field.
     * @param hex The hexagon from which the neighbouring hexagons are sought.
     * @return    The list of hexagons of adjacent hexagons to a particular hexagon hex.
     */
    public ArrayList<Hexagon> getAdjacentHexagons(Hexagon hex) {
        ArrayList<Hexagon> adjacent = new ArrayList<>();
        ArrayList<Vector> vectors;

        if (hex.getVector().getY() % 2 == 0) {
            vectors = getFromEven(hex.getVector());
        } else {
            vectors = getFromOdd(hex.getVector());
        }

        for (var vector: vectors) {
            Hexagon found = Hexagon.findByVector(hexagons, vector);

            adjacent.add(found);
        }
        return adjacent;
    }

    /**
     * Helper method used to find the neighbouring hexagons for the hexagon where the Y coordinate is an even number.
     * @param v Vector from which neighbouring coordinates are deduced.
     * @return  Return the list of identifying vectors for the hexagons.
     */
    private ArrayList<Vector> getFromEven(Vector v) {
        ArrayList<Vector> adjacent = new ArrayList<>();
        adjacent.add(new Vector(v.getX(), v.getY() - 2));
        adjacent.add(new Vector(v.getX(), v.getY() - 1));
        adjacent.add(new Vector(v.getX(), v.getY() + 1));
        adjacent.add(new Vector(v.getX(), v.getY() + 2));
        adjacent.add(new Vector(v.getX() - 1, v.getY() + 1));
        adjacent.add(new Vector(v.getX() - 1, v.getY() - 1));

        return adjacent;
    }

    /**
     * Helper method used to find the neighbouring hexagons for the hexagon where the Y coordinate is an odd number.
     * @param v Vector from which neighbouring coordinates are deduced.
     * @return  Return the list of identifying vectors for the hexagons.
     */
    private ArrayList<Vector> getFromOdd(Vector v) {
        ArrayList<Vector> adjacent = new ArrayList<>();
        adjacent.add(new Vector(v.getX(), v.getY() - 2));
        adjacent.add(new Vector(v.getX() + 1, v.getY() - 1));
        adjacent.add(new Vector(v.getX() + 1, v.getY() + 1));
        adjacent.add(new Vector(v.getX(), v.getY() + 2));
        adjacent.add(new Vector(v.getX(), v.getY() + 1));
        adjacent.add(new Vector(v.getX(), v.getY() - 1));
        return adjacent;
    }

    protected int getFieldSize() {
        return (int) Math.ceil((sideLength + 1) / 2.0) * sideLength + (int) Math.floor((sideLength + 1) / 2.0) * (sideLength - 1);
    }

    /**
     * Renders the entire field
     * @param stage Stage used for potential redrawing.
     * @return      The scene for the stage for the pane.
     */
    public Scene render(Stage stage) {
        System.out.println(AIMode);
        var pane = new Pane(canvas);

        var exitBtn = new CustomButton("Exit", 680, 550, 100, 30);
        exitBtn.setOnAction(e -> stage.setScene(new MainMenu().render(stage)));
        pane.getChildren().add(exitBtn);

        var saveBtn = new CustomButton("Save", 20, 550, 100, 30);
        saveBtn.setOnAction(e -> {
            var states = new ArrayList<Integer>();
            for (var p: polygons) {
                states.add(p.getState());
            }
            stage.setScene(new Save(player1Corruption, player2Corruption, states, false, sideLength, player1Turn, AIMode).render(stage));
        });
        pane.getChildren().add(saveBtn);

        var scene = new Scene(pane, ENV.WIDTH, ENV.HEIGHT);
        initField(50, 30);

        var player1Score = new CustomLabel(Math.round(1.0 / getFieldSize() * 100) + "%", ENV.PLAYER1_COLOUR, ENV.WIDTH - 80, 20);
        var player2Score = new CustomLabel(Math.round(1.0 / getFieldSize() * 100) + "%", ENV.PLAYER2_COLOUR, ENV.WIDTH - 80, 40);

        pane.getChildren().add(player1Score);
        pane.getChildren().add(player2Score);

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

    @Override
    public Scene load(Stage stage) {
        var pane = new Pane(canvas);

        var exitBtn = new CustomButton("Exit", 680, 550, 100, 30);
        exitBtn.setOnAction(e -> stage.setScene(new MainMenu().render(stage)));
        pane.getChildren().add(exitBtn);

        var saveBtn = new CustomButton("Save", 20, 550, 100, 30);
        saveBtn.setOnAction(e -> {
            var states = new ArrayList<Integer>();
            for (var p: polygons) {
                states.add(p.getState());
            }
            stage.setScene(new Save(player1Corruption, player2Corruption, states, false, sideLength, player1Turn, AIMode).render(stage));
        });
        pane.getChildren().add(saveBtn);

        var scene = new Scene(pane, ENV.WIDTH, ENV.HEIGHT);
        initField(50, 30);

        var player1Score = new CustomLabel(Math.round((double) player1Corruption.size() / getFieldSize() * 100) + "%", ENV.PLAYER1_COLOUR, ENV.WIDTH - 80, 20);
        var player2Score = new CustomLabel(Math.round((double) player2Corruption.size() / getFieldSize() * 100) + "%", ENV.PLAYER2_COLOUR, ENV.WIDTH - 80, 40);

        pane.getChildren().add(player1Score);
        pane.getChildren().add(player2Score);

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
}
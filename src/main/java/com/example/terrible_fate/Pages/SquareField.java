package com.example.terrible_fate.Pages;

import com.example.terrible_fate.Components.CustomButton;
import com.example.terrible_fate.Components.Hexagon;
import com.example.terrible_fate.Components.ReactivePolygon;
import com.example.terrible_fate.Components.Vector;
import com.example.terrible_fate.ENV;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
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
public class SquareField {
    private int sideLength;
    private Canvas canvas;
    private ArrayList<Hexagon> hexagons;
    private ArrayList<ReactivePolygon> polygons;

    /**
     * Initializes the basic values used in the field.
     * @param sideLength the length of the vertical side of the hexagon (shorter one)
     */
    public SquareField(int sideLength) {
        this.sideLength = sideLength;
        this.canvas = new Canvas(ENV.WIDTH, ENV.HEIGHT);
        this.hexagons = new ArrayList<>();
        this.polygons = new ArrayList<>();
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

    /**
     * Renders the entire field
     * @param stage Stage used for potential redrawing.
     * @return      The scene for the stage for the pane.
     */
    public Scene render(Stage stage) {
        var pane = new Pane(canvas);

        var exitBtn = new CustomButton("Exit", 680, 550);
        exitBtn.setPrefSize(100, 30);

        exitBtn.setOnAction(e -> stage.setScene(new MainMenu().render(stage)));

        pane.getChildren().add(exitBtn);

        var scene = new Scene(pane, ENV.WIDTH, ENV.HEIGHT);
        initField(50, 30);

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

        for (var p: polygons) {
            if (polygons.indexOf(p) == 0) {
                p.corrupt(true);
            } else if (polygons.indexOf(p) == polygons.size() - 1) {
                p.corrupt(false);
            }

            p.getPolygon().setOnMouseClicked(e -> {
                Platform.runLater(() -> {
                    pane.getChildren().remove(p.getLine());
                    p.updateState();
                    p.drawLine();
                    pane.getChildren().add(p.getLine());
                });
            });
        }

        return scene;
    }

    /**
     * Generates a state for the initial configurations of ReactivePolygon objects in the field.
     * @param hexagon Hexagon upon which the decision is made.
     * @return        State number that puts the given hexagon in its respective place.
     */
    private int generateState(Hexagon hexagon) {
        if (hexagons.indexOf(hexagon) == 0) {
            return 2;
        }

        if (hexagons.indexOf(hexagon) == hexagons.size() - 1) {
            return 5;
        }

        return new Random().nextInt(7 - 1) + 1;
    }
}
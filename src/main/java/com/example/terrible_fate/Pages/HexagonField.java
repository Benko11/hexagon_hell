package com.example.terrible_fate.Pages;

import com.example.terrible_fate.Components.CustomButton;
import com.example.terrible_fate.Components.Hexagon;
import com.example.terrible_fate.Components.ReactivePolygon;
import com.example.terrible_fate.Components.Vector;
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
public class HexagonField {
    private int sideLength;
    private Canvas canvas;
    private ArrayList<Hexagon> hexagons;
    private ArrayList<ReactivePolygon> polygons;

    /**
     * Initializes the basic values used in the field.
     * @param sideLength the length of one side of the large hexagon (equilateral type)
     */
    public HexagonField(int sideLength) {
        this.sideLength = sideLength;
        this.canvas = new Canvas(ENV.WIDTH, ENV.HEIGHT);
        this.hexagons = new ArrayList<>();
        this.polygons = new ArrayList<>();
    }

    /**
     * Prepares hexagons to be drawn into the field.
     * @param initX the X coordinate of the topmost hexagon's upper-left point
     * @param initY the Y coordinate of the topmost hexagon's upper-left point
     */
    public void initField(double initX, double initY) {
        final int largest = sideLength * 2 - 1;
        int indexX = largest - 1;
        int indexY = sideLength - 1;
        for (int i = 0; i < largest; i++) {
            var hexagon = new Hexagon(initX, initY + ENV.APOTHEM * 2 * i, canvas.getGraphicsContext2D(), new Vector(indexX, indexY));
//            canvas.getGraphicsContext2D().setFont(new Font("SF Mono", 9));
//            canvas.getGraphicsContext2D().fillText(indexX + "," + indexY, initX, initY + ENV.APOTHEM * 2 * i + ENV.HEXAGON_RADIUS);
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
//                canvas.getGraphicsContext2D().setFont(new Font("SF Mono", 9));
//                canvas.getGraphicsContext2D().fillText(indexX + "," + indexY, initX - i * (ENV.HEXAGON_RADIUS + aux), initY + i * ENV.APOTHEM + ENV.APOTHEM * 2 * j + ENV.HEXAGON_RADIUS);
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
//                canvas.getGraphicsContext2D().setFont(new Font("SF Mono", 9));
//                canvas.getGraphicsContext2D().fillText(indexX + "," + indexY, initX + i * (ENV.HEXAGON_RADIUS + aux), initY + i * ENV.APOTHEM + ENV.APOTHEM * 2 * j + ENV.HEXAGON_RADIUS);
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
     * Renders the entire field
     * @param stage Stage used for potential redrawing.
     * @return      The scene for the stage for the pane.
     */
    public Scene render(Stage stage) {
        var pane = new Pane(canvas);

        var scene = new Scene(pane, ENV.WIDTH, ENV.HEIGHT);
        initField(ENV.WIDTH / 2.0 - ENV.HEXAGON_RADIUS / 2.0, 30);

        var exitBtn = new CustomButton("Exit", 680, 550);
        exitBtn.setPrefSize(100, 30);
        exitBtn.setOnAction(e -> stage.setScene(new MainMenu().render(stage)));
        pane.getChildren().add(exitBtn);

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
            } else if (polygons.indexOf(p) == 2 * sideLength - 2) {
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

        System.out.println(hexagons.get(30));
        System.out.println(getAdjacentHexagons(hexagons.get(30)));

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

        if (hexagons.indexOf(hexagon) == 2 * sideLength - 2) {
            return 5;
        }

        return new Random().nextInt(7 - 1) + 1;
    }
}

package com.example.terrible_fate.Pages;

import com.example.terrible_fate.Components.CustomButton;
import com.example.terrible_fate.Components.Hexagon;
import com.example.terrible_fate.Components.Vector;
import com.example.terrible_fate.ENV;
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

public class HexagonField {
    private int sideLength;
    private Canvas canvas;
    private ArrayList<Hexagon> hexagons;
    private ArrayList<Polygon> polygons;

    public HexagonField(int sideLength) {
        this.sideLength = sideLength;
        this.canvas = new Canvas(ENV.WIDTH, ENV.HEIGHT);
        this.hexagons = new ArrayList<>();
        this.polygons = new ArrayList<>();
    }

    public void initField(double initX, double initY) {
        final int largest = sideLength * 2 - 1;
        int indexX = largest - 1;
        int indexY = sideLength - 1;
        for (int i = 0; i < largest; i++) {
            var hexagon = new Hexagon(initX, initY + ENV.APOTHEM * 2 * i, canvas.getGraphicsContext2D(), new Vector(indexX, indexY));
            canvas.getGraphicsContext2D().setFont(new Font("SF Mono", 9));
            canvas.getGraphicsContext2D().fillText(indexX + "," + indexY, initX, initY + ENV.APOTHEM * 2 * i + ENV.HEXAGON_RADIUS);
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
                canvas.getGraphicsContext2D().setFont(new Font("SF Mono", 9));
                canvas.getGraphicsContext2D().fillText(indexX + "," + indexY, initX - i * (ENV.HEXAGON_RADIUS + aux), initY + i * ENV.APOTHEM + ENV.APOTHEM * 2 * j + ENV.HEXAGON_RADIUS);
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
                canvas.getGraphicsContext2D().setFont(new Font("SF Mono", 9));
                canvas.getGraphicsContext2D().fillText(indexX + "," + indexY, initX + i * (ENV.HEXAGON_RADIUS + aux), initY + i * ENV.APOTHEM + ENV.APOTHEM * 2 * j + ENV.HEXAGON_RADIUS);
                hexagons.add(rightHexagon);
                indexX--;
            }
        }
    }

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

    public Scene render(Stage stage) {
        var pane = new Pane(canvas);

        var scene = new Scene(pane, ENV.WIDTH, ENV.HEIGHT);
        initField(ENV.WIDTH / 2.0 - ENV.HEXAGON_RADIUS / 2.0, 30);

        var exitBtn = new CustomButton("Exit", 680, 550);
        exitBtn.setPrefSize(100, 30);
        exitBtn.setOnAction(e -> stage.setScene(new MainMenu().render(stage)));

        for (var hexagon: hexagons) {
            var polygon = hexagon.draw();
            polygon.setOnMouseClicked(e -> System.out.println("Hexagon clicked"));
            pane.getChildren().addAll(new Group(polygon));
        }

        System.out.println(hexagons.get(30));
        System.out.println(getAdjacentHexagons(hexagons.get(30)));

        pane.getChildren().setAll(canvas, exitBtn);
        return scene;
    }
}

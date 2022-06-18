package com.example.terrible_fate.Pages;

import com.example.terrible_fate.Components.CustomButton;
import com.example.terrible_fate.Components.Hexagon;
import com.example.terrible_fate.Components.Vector;
import com.example.terrible_fate.ENV;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;

public class SquareField {
    private int sideLength;
    private Canvas canvas;
    private ArrayList<Hexagon> hexagons;
    private ArrayList<Polygon> polygons;

    public SquareField(int sideLength) {
        this.sideLength = sideLength;
        this.canvas = new Canvas(ENV.WIDTH, ENV.HEIGHT);
        this.hexagons = new ArrayList<>();
        this.polygons = new ArrayList<>();
    }

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
                    canvas.getGraphicsContext2D().setFont(new Font("SF Mono", 9));
                    canvas.getGraphicsContext2D().fillText(setX + "," + setY, initX, initY + j * ENV.APOTHEM * 2 + ENV.HEXAGON_RADIUS);
                    hexagons.add(hexagon);
                    setY += 2;
                    continue;
                }

                if (j + 1 == sideLength) continue;

                var aux = Math.sqrt(ENV.HEXAGON_RADIUS * 2 + ENV.APOTHEM * 2);
                var hexagon = new Hexagon(initX + aux, initY + ENV.APOTHEM + j * ENV.APOTHEM * 2, gc, new Vector(setX, setY));
                canvas.getGraphicsContext2D().setFont(new Font("SF Mono", 9));
                canvas.getGraphicsContext2D().fillText(setX + "," + setY, initX + aux, initY + ENV.APOTHEM + j * ENV.APOTHEM * 2 + ENV.HEXAGON_RADIUS);
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

    public Scene render(Stage stage) {
        var pane = new Pane(canvas);

        var exitBtn = new CustomButton("Exit", 680, 550);
        exitBtn.setPrefSize(100, 30);
        exitBtn.setOnAction(e -> stage.setScene(new MainMenu().render(stage)));

        var scene = new Scene(pane, ENV.WIDTH, ENV.HEIGHT);
        initField(50, 30);
        pane.getChildren().setAll(canvas, exitBtn);

        for (var hexagon: hexagons) {
            hexagon.draw();
        }

        System.out.println(hexagons.get(35));
        System.out.println(getAdjacentHexagons(hexagons.get(35)));

        return scene;
    }
}
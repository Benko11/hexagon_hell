package com.example.terrible_fate.Components;

import com.example.terrible_fate.ENV;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Polygon;

import java.util.ArrayList;

public class Hexagon extends Canvas {
    private double initX;
    private double initY;
    private GraphicsContext gc;
    private Vector vector;

    public Hexagon(double initX, double initY, GraphicsContext gc, Vector vector) {
        this.initX = initX;
        this.initY = initY;
        this.gc = gc;
        this.vector = vector;
    }

    public Polygon draw() {
        var x = new double[6];
        x[0] = initX;
        x[1] = initX + ENV.HEXAGON_RADIUS;
        x[2] = initX + 1.5 * ENV.HEXAGON_RADIUS;
        x[3] = x[1];
        x[4] = initX;
        x[5] = initX - ENV.HEXAGON_RADIUS / 2.0;

        var y = new double[6];
        y[0] = initY;
        y[1] = y[0];
        y[2] = initY + ENV.APOTHEM;
        y[3] = initY + ENV.APOTHEM * 2;
        y[4] = y[3];
        y[5] = y[2];

        Polygon p = new Polygon();
        p.getPoints().addAll(x[0], y[0],
                x[1], y[1],
                x[2], y[2],
                x[3], y[3],
                x[4], y[4],
                x[5], y[5]);

        gc.strokePolygon(x, y, 6);
        return p;
    }

    public Vector getVector() {
        return vector;
    }

    @Override
    public String toString() {
        return "Hexagon("+ initX +", "+initY +", Vector("+getVector().getX() +", "+getVector().getY()+"))";
    }

    public static Hexagon findByVector(ArrayList<Hexagon> hexagons, Vector vector) {
        for (var hexagon: hexagons) {
            if (hexagon.getVector().getX() == vector.getX() && hexagon.getVector().getY() == vector.getY()) {
                return hexagon;
            }
        }
        return null;
    }
}

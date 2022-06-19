package com.example.terrible_fate.Components;

import com.example.terrible_fate.ENV;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Rotate;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Wrapper class where the logic of object/hexagon interactions is handled.
 */
public class ReactivePolygon {
    private Polygon polygon;
    private Circle circle;
    private Line line;
    private int state;
    private Rotate rotate;

    /**
     * Initializes and encapsulates all the items reactive to the logical interaction element.
     * @param state (1-6) represents where the line in the hexagon is pointing, 1 is upper
     */
    public ReactivePolygon(int state) {
        polygon = new Polygon();
        circle = new Circle();
        line = new Line();
        rotate = new Rotate();
        this.state = state;
    }

    /**
     * Creates a polygon for further interactions
     * @param p Polygon upon which a polygon in the ReactivePolygon object is created.
     */
    public void addPolygon(Polygon p) {
        polygon.getPoints().addAll(p.getPoints());
        polygon.setFill(Color.TRANSPARENT);

        circle = new Circle(polygon.getPoints().get(0) + ENV.HEXAGON_RADIUS / 2, polygon.getPoints().get(1) + ENV.APOTHEM, 3);
        line = new Line(
                polygon.getPoints().get(0) + ENV.HEXAGON_RADIUS / 2,
                polygon.getPoints().get(1) + ENV.APOTHEM,
                polygon.getPoints().get(0) + ENV.HEXAGON_RADIUS / 2,
                polygon.getPoints().get(1) - ENV.APOTHEM + 15)
        ;
        line.getTransforms().addAll(rotate);

        rotate.setPivotX(polygon.getPoints().get(0) + ENV.HEXAGON_RADIUS / 2);
        rotate.setPivotY(polygon.getPoints().get(1) + ENV.APOTHEM);
        rotate.setAngle((state - 1) * 60);
    }

    /**
     * @return the polygon in the structure
     */
    public Polygon getPolygon() {
        return polygon;
    }

    /**
     * @return the line in the structure
     */
    public Line getLine() {
        return line;
    }

    /**
     * @return the circle in the structure
     */
    public Circle getCircle() {
        return circle;
    }

    /**
     * @return the current state of the structure (i.e., where the needle is pointing)
     */
    public int getState() {
        return state;
    }

    /**
     * Updates the state, making sure, only 1-6 values are used.
     */
    public void updateState() {
        var newValue = state + 1;
        if (newValue > 6) {
            state = newValue % 6;
            return;
        }
        state = newValue;
    }

    /**
     * Serves for redrawing lines during a rotation event.
     */
    public void rotateLine() {
        var newValue = (rotate.getAngle() + 60) % 360;
        rotate.setAngle(newValue);
    }

    /**
     * Mark the specified polygon based on who corrupted it.
     * @param player1 Determines if it should corrupt for player 1 or player 2.
     */
    public void corrupt(boolean player1) {
        if (player1) {
            polygon.setFill(Color.web(ENV.PLAYER1_COLOUR));
            return;
        }

        polygon.setFill(Color.web(ENV.PLAYER2_COLOUR));
    }
}

package com.example.terrible_fate.Components;

/**
 * Auxiliary structure used for uniquely identifying hexagons in the field.
 */
public class Vector {
    private int x;
    private int y;

    /**
     * Initializes the coordinates for the vector (read-only afterwards)
     * @param x X coordinate in the field
     * @param y Y coordinate in the field
     */
    public Vector(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @return the X coordinate of the vector
     */
    public int getX() {
        return x;
    }

    /**
     * @return the Y coordinate of the vector
     */
    public int getY() {
        return y;
    }

    /**
     * Used for debugging.
     * @return String representation of the object.
     */
    @Override
    public String toString() {
        return "Vector("+x+", "+y+")";
    }
}

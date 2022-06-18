package com.example.terrible_fate;

/**
 * Here is where the app-wide constants reside.
 * Inspiration comes from default Laravel projects, where the .env file is used for similar purpose,
 * except here we are not necessarily storing sensitive data.
 */
public class ENV {
    public static double HEXAGON_RADIUS = 15;
    public static double APOTHEM = Math.sqrt(3) / 2 * HEXAGON_RADIUS;
    public static int WIDTH = 800;
    public static int HEIGHT = 600;
    public static String APP_NAME = "Hexagon Hell";
}

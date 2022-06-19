package com.example.terrible_fate;

/**
 * Here is where the app-wide constants reside.
 * Inspiration comes from default Laravel projects, where the .env file is used for similar purpose,
 * except here we are not necessarily storing sensitive data.
 * It also helps avoid using magical constants in the code.
 */
public class ENV {
    public static double HEXAGON_RADIUS = 15;
    public static double APOTHEM = Math.sqrt(3) / 2 * HEXAGON_RADIUS;
    public static int WIDTH = 800;
    public static int HEIGHT = 600;
    public static String APP_NAME = "Hexagon Hell";
    public static String PLAYER1_COLOUR = "#ff0000";
    public static String PLAYER2_COLOUR = "#ff91af";
    public static double VICTORY = 0.2;
    public static int PLAYER1_STATE_START = 2;
    public static int PLAYER2_STATE_START = 5;
    public static int SMALL_HEX_SIZE = 4;
    public static int MEDIUM_HEX_SIZE = 7;
    public static int LARGE_HEX_SIZE = 10;
    public static int SMALL_SQ_SIZE = 6;
    public static int MEDIUM_SQ_SIZE = 10;
    public static int LARGE_SQ_SIZE = 14;
}

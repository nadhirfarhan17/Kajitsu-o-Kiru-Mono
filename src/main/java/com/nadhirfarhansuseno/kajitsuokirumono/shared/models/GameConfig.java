
package com.nadhirfarhansuseno.kajitsuokirumono.shared.models;

public final class GameConfig {

    // Window configuration
    public static final String WINDOW_TITLE = "Kajitsu o Kiru Mono";

    // Virtual resolution
    public static final int VIRTUAL_WIDTH = 1280;
    public static final int VIRTUAL_HEIGHT = 720;

    // Minimum window resolution
    public static final int MIN_WIDTH = 960;
    public static final int MIN_HEIGHT = 540;

    // Game loop configuration
    public static final int TARGET_FPS = 60;
    public static final long FRAME_TIME_NS = 1_000_000_000L / TARGET_FPS;

    // Window settings
    public static final boolean RESIZABLE = true;

    // Prevent object instantiation
    private GameConfig() {
    }
}

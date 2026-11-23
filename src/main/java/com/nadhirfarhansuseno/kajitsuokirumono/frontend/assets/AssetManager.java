
package com.nadhirfarhansuseno.kajitsuokirumono.frontend.assets;

import javax.imageio.ImageIO;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class AssetManager {

    private final Map<String, BufferedImage> images;
    private final Map<String, Font> fonts;

    public AssetManager() {
        images = new HashMap<>();
        fonts = new HashMap<>();
    }

    public BufferedImage loadImage(String path) {
        if (images.containsKey(path)) {
            return images.get(path);
        }

        try {
            BufferedImage image = ImageIO.read(
                    getClass().getResourceAsStream(path)
            );

            images.put(path, image);

            return image;

        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Failed to load image: " + path);
            return null;
        }
    }

    public Font loadFont(String path, float size) {
        String key = path + "_" + size;

        if (fonts.containsKey(key)) {
            return fonts.get(key);
        }

        try (InputStream input = getClass().getResourceAsStream(path)) {

            if (input == null) {
                throw new IOException("Font not found: " + path);
            }

            Font font = Font.createFont(
                    Font.TRUETYPE_FONT,
                    input
            ).deriveFont(size);

            fonts.put(key, font);

            return font;

        } catch (Exception e) {
            System.err.println("Failed to load font: " + path);
            return new Font("SansSerif", Font.PLAIN, (int) size);
        }
    }

    public void clear() {
        images.clear();
        fonts.clear();
    }
}

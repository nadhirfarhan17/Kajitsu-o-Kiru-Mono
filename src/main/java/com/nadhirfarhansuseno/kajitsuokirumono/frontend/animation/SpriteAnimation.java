
package com.nadhirfarhansuseno.kajitsuokirumono.frontend.animation;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class SpriteAnimation {

    private final BufferedImage[] frames;

    private final double frameDuration;

    private int currentFrame;

    private double elapsedTime;

    private boolean finished;

    public SpriteAnimation(
            BufferedImage spriteSheet,
            int frameWidth,
            int frameHeight,
            int totalFrames,
            double frameDuration
    ) {
        this.frames = new BufferedImage[totalFrames];
        this.frameDuration = frameDuration;

        this.currentFrame = 0;
        this.elapsedTime = 0;
        this.finished = false;

        extractFrames(
                spriteSheet,
                frameWidth,
                frameHeight,
                totalFrames
        );
    }

    private void extractFrames(
            BufferedImage spriteSheet,
            int frameWidth,
            int frameHeight,
            int totalFrames
    ) {
        int columns = spriteSheet.getWidth() / frameWidth;

        for (int i = 0; i < totalFrames; i++) {
            int column = i % columns;
            int row = i / columns;

            frames[i] = spriteSheet.getSubimage(
                    column * frameWidth,
                    row * frameHeight,
                    frameWidth,
                    frameHeight
            );
        }
    }

    public void update(double deltaTime) {
        if (finished) {
            return;
        }

        elapsedTime += deltaTime;

        while (elapsedTime >= frameDuration) {
            elapsedTime -= frameDuration;
            currentFrame++;

            if (currentFrame >= frames.length) {
                currentFrame = frames.length - 1;
                finished = true;
                break;
            }
        }
    }

    public void render(
            Graphics2D g2d,
            int x,
            int y,
            int width,
            int height
    ) {
        if (frames.length == 0) {
            return;
        }

        g2d.drawImage(
                frames[currentFrame],
                x,
                y,
                width,
                height,
                null
        );
    }

    public void reset() {
        currentFrame = 0;
        elapsedTime = 0;
        finished = false;
    }

    public boolean isFinished() {
        return finished;
    }

    public int getCurrentFrame() {
        return currentFrame;
    }
}

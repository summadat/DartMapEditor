package com.summadat.gfx;

import java.awt.image.BufferedImage;

/**
 * Created by Noah on 23-Sep-17.
 */
public class Animation extends Sprite {

    BufferedImage[] images;
    int frame = 0;
    int numframes;
    static int num = 0;
    public int id;
    double length;
    double frameLength;
    double time = 0;
    double last = System.nanoTime() / 1000000000.0;
    double elapsed = 0;
    boolean paused = false;

    public Animation(BufferedImage[] frames, double length) {
        images = frames;
        this.numframes = images.length;
        this.length = length;
        frameLength = this.length  / this.numframes;
        id = num++;
    }

    public Animation(BufferedImage frame) {
        images[0] = frame;
        this.numframes = 1;
        this.length = 1.0;
        frameLength = 1.0;
        id = num++;
    }

    public void pause() {
        paused = true;
    }

    public void unpause() {
        paused = false;
    }

    public BufferedImage getImage() {

        if (paused)
            return images[1];

        elapsed = System.nanoTime() / 1000000000.0 - last;

        if (elapsed >= frameLength) {
            frame++;
            elapsed = 0;
            last = System.nanoTime() / 1000000000.0;
        }

        if (frame >= numframes)
            frame = 0;

        return images[frame];
    }



}

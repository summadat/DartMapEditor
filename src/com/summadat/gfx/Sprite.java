package com.summadat.gfx;

import java.awt.image.BufferedImage;

/**
 * Created by Noah on 23-Sep-17.
 */
public class Sprite {

    BufferedImage image;
    public static int num = 0;
    private int id;

    public Sprite(BufferedImage i) {
        id = num;
        num++;
        image = i;
    }

    public int getID() {
        return id;
    }

    public BufferedImage getImage() {
        return image;
    }

}

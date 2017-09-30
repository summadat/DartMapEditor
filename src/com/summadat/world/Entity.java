package com.summadat.world;

import com.summadat.GamePanel;
import com.summadat.gfx.Animation;
import com.summadat.gfx.Camera;
import com.summadat.gfx.Location;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Noah on 23-Sep-17.
 */
public class Entity {

    Animation[] sprites;
    Location location;

    public int animID;

    public static final int UP = 0, DOWN = 3, LEFT = 1, RIGHT = 2;
    private int direction = UP;

    private int speedX = 0;
    private int speedY = 0;
    private static int NUM = 0;
    public int id = 0;
    boolean col = false;

    public Entity(Animation[] s) {
        this(s, new Location(200, 200));

    }

    public Entity(Animation[] s, Location loc) {
        location = loc;
        sprites = s;
        id = NUM++;
    }

    public Animation[] getAnimation() {
        return sprites;
    }

    public void setLocation(Location l) {
        location = l;
    }

    public void setAnimation(Animation[] s) {
        sprites = s;
    }

    public int calculateX(Camera c) {
        return location.getX() - c.getLocation().getX() + (GamePanel.WIDTH / 2);
    }

    public int calculateY(Camera c) {
        return location.getY() - c.getLocation().getY() + (GamePanel.HEIGHT / 2);
    }

    public Location getLocation() {
        return location;
    }

    public void setSpeed(int x, int y) {
        speedX = x;
        speedY = y;
    }

    public void setSpeedX(int speed) {
        this.speedX = speed;
    }

    public void setSpeedY(int speed) {
        this.speedY = speed;
    }

    public void update() {
    }

    public void draw(Graphics2D graphics, Camera camera) {
        graphics.drawImage(sprites[direction].getImage(), calculateX(camera), calculateY(camera), null);
    }

    public BufferedImage getImage() {
        return sprites[direction].getImage();
    }

}

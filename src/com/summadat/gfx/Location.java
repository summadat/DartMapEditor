package com.summadat.gfx;

import com.summadat.world.Tile;

/**
 * Created by Noah on 23-Sep-17.
 */
public class Location {

    private int x, y;

    public Location(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Location translate(int x, int y) {
        this.x += x;
        this.y += y;

        return this;
    }

    public void set(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean isWithin(Tile t, Camera c) {
        if (x > Math.abs(t.calculateX(c)) && x < Math.abs(t.calculateX(c)) + 32 && y > Math.abs(t.calculateY(c)) && y < Math.abs(t.calculateY(c)) + 32)
            return true;

        return false;
    }
}

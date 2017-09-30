package com.summadat.world;

import com.summadat.GamePanel;
import com.summadat.gfx.AnimationLoader;
import com.summadat.gfx.Camera;
import com.summadat.gfx.Location;
import com.summadat.gfx.Sprite;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Created by Noah on 23-Sep-17.
 */
public class Tile {

    Location loc;
    Sprite image;
    Sprite layer;
    boolean highlighted = false;
    boolean walkable = true;
    boolean hasLayer = false;

    public Tile(Location loc, Sprite img) {
        this.loc = loc;
        this.image = img;
    }

    public void setImage(Sprite s) {
        image = s;
    }

    public void setLayer(Sprite s) {
        layer = s;
        hasLayer = true;
    }

    public void removeLayer() {
        hasLayer = false;
    }

    public void setWalkable(boolean b) {
        walkable = b;
    }

    public int calculateX(Camera c) {
        return (loc.getX() * 32) - c.getLocation().getX() + (GamePanel.WIDTH / 2);
    }

    public int calculateY(Camera c) {
        return (loc.getY() * 32) - c.getLocation().getY() + (GamePanel.HEIGHT / 2);
    }

    public void draw(Graphics2D graphics, Camera camera) {
        graphics.drawImage(image.getImage(), calculateX(camera), calculateY(camera), null);

        if (hasLayer) {
            graphics.drawImage(layer.getImage(), calculateX(camera), calculateY(camera), null);
        }
        if (GamePanel.mouseLoc.isWithin(this, camera)) {
            graphics.setColor(Color.gray);
                graphics.drawRect(calculateX(camera), calculateY(camera), 31, 31);
        }
        if (!walkable) {
            graphics.setColor(Color.black);
            graphics.drawRect(calculateX(camera), calculateY(camera), 31, 31);
        }
    }
}

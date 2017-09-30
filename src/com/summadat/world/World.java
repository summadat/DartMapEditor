package com.summadat.world;

import com.summadat.GamePanel;
import com.summadat.gfx.AnimationLoader;
import com.summadat.gfx.Camera;

import java.awt.*;

/**
 * Created by Noah on 23-Sep-17.
 */
public class World {

    public Map map;
    AnimationLoader loader;

    public void draw(Graphics2D graphics, Camera camera) {
        graphics.setColor(Color.black);
        graphics.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT + 32);
        map.draw(graphics, camera);
    }

    public World() {
        loader = new AnimationLoader();
    }

    public void loadTiles(String s) {
        loader.loadTiles(s);
    }

    public AnimationLoader getLoader() {
        return loader;
    }

    public void generateMap() {
        map = new Map(64, 64);

        map = Map.load("maps/default.map");
    }

    public void update() {

    }

}

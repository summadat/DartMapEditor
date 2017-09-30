package com.summadat.world;

import com.summadat.EntityContainer;
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
    EntityContainer entities;

    public void draw(Graphics2D graphics, Camera camera) {
        graphics.setColor(Color.black);
        graphics.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT + 32);
        map.draw(graphics, camera);
    }

    public World() {
        loader = new AnimationLoader();

        entities = loader.loadAnimations("res/chars1.png");


        entities.add(loader.loadAnimations("res/chars2.png"));
        entities.add(loader.loadAnimations("res/chars3.png"));
        entities.add(loader.loadAnimations("res/chars4.png"));
        entities.add(loader.loadAnimations("res/chars5.png"));

        entities.add(loader.loadAnimations("res/Butterflies.png"));
        entities.add(loader.loadAnimations("res/animals.png"));
        entities.add(loader.loadAnimations("res/ducks.png"));
        entities.add(loader.loadAnimations("res/camels.png"));
        entities.add(loader.loadAnimations("res/horses.png"));
        entities.add(loader.loadAnimations("res/doggos.png"));
        entities.add(loader.loadAnimations("res/knights.png"));
    }

    public void loadTiles(String s) {
        loader.loadTiles(s);
    }

    public AnimationLoader getLoader() {
        return loader;
    }

    public EntityContainer getEntities() { return entities; }

    public void generateMap() {
        map = new Map(64, 64);

        map = Map.load("maps/default.map");
    }

    public void update() {

    }

}

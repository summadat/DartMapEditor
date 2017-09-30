package com.summadat.gfx;


import com.summadat.EntityContainer;
import com.summadat.world.Entity;
import com.summadat.world.Map;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by Noah on 24-Sep-17.
 */
public class AnimationLoader {

    public static ArrayList<Sprite> spriteCache;
    public static int num = 0;
    public static int numAnims = 0;

    public static Sprite block;

    public AnimationLoader() {

    }

    public void loadTiles(String s) {
        try {
            BufferedImage image = ImageIO.read(new File(s));

            int numX = (image.getWidth() / 32);
            int numY = (image.getHeight() / 32);

            spriteCache = new ArrayList<>();

            block = new Sprite(ImageIO.read(new File("res/block.png")));
            spriteCache.add(block);

            for (int x = 0; x < numX; x++) {
                for (int y = 0; y < numY; y++) {
                    spriteCache.add(new Sprite(image.getSubimage(x * 32, y * 32, 32, 32)));
                    num++;
                }
            }

        } catch (Exception e) {
            System.out.println("Failed to load sprites: " + s);
            e.printStackTrace();
        }
    }

    public EntityContainer loadAnimations(String url) {
        Animation[] sprites;
        EntityContainer entities = new EntityContainer();

        try {
            BufferedImage image = ImageIO.read(new File(url));

            int numX = (image.getWidth() / 32) / 3;
            int numY = (image.getHeight() / 32) / 4;

            BufferedImage i;
            BufferedImage[] frames;

            for (int y = 0; y < numY; y++) {

                for (int x = 0; x < numX; x++) {

                    sprites = new Animation[numX];

                    i = image.getSubimage(x * 32 * 3, y * 32 * 4, 32 * 3, 32 * 4);

                    for (int a = 0; a < 4; a++) {

                        frames = new BufferedImage[3];
                        int offset = 0;

                        for (int b = 0; b < 3; b++) {
                            frames[offset++] = i.getSubimage(b * 32, a * 32, 32, 32);
                        }
                        sprites[a] = new Animation(frames, 0.3);
                    }

                    if (sprites != null) {
                    } else {
                        System.out.println("fukk");
                    }
                    Entity e = new Entity(sprites);
                    e.animID = numAnims++;
                    entities.add(e);
                }
            }
            System.out.println("loaded " + numAnims);
            return entities;
        } catch (Exception e) {
            System.out.println("Failed to load anims: " + url);
            e.printStackTrace();
        }
        return null;
    }

    public Sprite get(int id) {
        return spriteCache.get(id);
    }

}

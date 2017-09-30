package com.summadat.gfx;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by Noah on 24-Sep-17.
 */
public class AnimationLoader {

    public static ArrayList<Sprite> spriteCache;
    public int num = 0;

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

    public Sprite get(int id) {
        return spriteCache.get(id);
    }

}

package com.summadat.world;

import com.summadat.GamePanel;
import com.summadat.gfx.AnimationLoader;
import com.summadat.gfx.Camera;
import com.summadat.gfx.Location;
import com.summadat.gfx.Sprite;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by Noah on 23-Sep-17.
 */
public class Map {

    Tile[][] tiles;
    public static int width, height;

    String name = "default";

    public String getName() {
        return name;
    }

    public Map(int w, int h) {
        width = w;
        height = h;
        tiles = new Tile[width][height];
    }

    public Map(String n, int w, int h) {
        name = n;
        width = w;
        height = h;
        tiles = new Tile[width][height];
    }

    public void setTile(int x, int y, Tile t) {
        tiles[x][y] = t;
    }

    public Tile tileAt(int x, int y) {
        return tiles[x][y];
    }

    public void save() {
        try {
            String fname = "maps/_" + name + "_.map";
            System.out.println(fname);
            File file = new File(fname);

            if (file.createNewFile()){
                System.out.println("File is created!");

                Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));

                writer.write(this.name + "\n");
                writer.write(this.width + ":" + this.height + "\n");
                for (Tile[] x : tiles) {
                    for (Tile t : x) {
                        writer.write((t.loc.getX() + ":" + t.loc.getY() + ":" + (t.image.getID())) + (t.hasLayer ? "@" + t.layer.getID() : "") + ":" + (t.walkable ? "true" : "false") + "/");
                    }
                    writer.write("\n");
                }
                writer.write("EOF");
                writer.flush();
                System.out.println("Flushed map!");
                writer.close();
            } else {
                System.out.println("File already exists!");
            }

        } catch (Exception e) {
            System.out.println("Failed writing map!");
            e.printStackTrace();
        }
    }

    public static Map load(String s) {
        try {
            Map m;
            BufferedReader reader = new BufferedReader(new FileReader(s));

            String nam = reader.readLine();

            String[] dims = reader.readLine().split(":");

            int wid = Integer.parseInt(dims[0]);
            int hei = Integer.parseInt(dims[1]);

            m = new Map(wid, hei);
            m.name = nam;

            for (int x = 0; x < 64; x++) {
                for (int y = 0; y < 64; y++) {
                    m.setTile(x, y, new Tile(new Location(x, y), AnimationLoader.block));
                }
            }

            String input;
            while ((input = reader.readLine()) != null) {
                if (input.length() > 4) {
                    String[] tiles = input.split("/");
                    for (String str : tiles) {
                        String data[] = str.split(":");

                        int x = Integer.parseInt(data[0]);
                        int y = Integer.parseInt(data[1]);

                        int spriteid = 0;
                        int layer = 0;

                        if (data[2].contains("@")) {
                            spriteid = Integer.parseInt(data[2].split("@")[0]);
                            layer = Integer.parseInt(data[2].split("@")[1]);
                        } else {
                            spriteid = Integer.parseInt(data[2]);
                        }
                        boolean walkable = true;

                        if (data.length > 3) {
                            if (data[3] != null)
                                walkable = data[3].equals("true");
                        }

                        Sprite sprite;
                        Sprite layerimg;

                        if (spriteid != 0) {
                            sprite = AnimationLoader.spriteCache.get(spriteid);
                            if (layer != 0) {
                                layerimg = AnimationLoader.spriteCache.get(layer);
                                m.tileAt(x, y).setLayer(layerimg);
                            }
                        } else
                            sprite = AnimationLoader.block;

                        m.tileAt(x, y).setImage(sprite);
                        m.tileAt(x, y).setWalkable(walkable);

                    }
                } else {
                    break;
                }
            }
            return m;
        } catch (Exception e) {
            System.out.println("Failed to load map!");
            e.printStackTrace();
        }
        return null;
    }

    public void draw(Graphics2D graphics, Camera camera) {

        int cameraX = camera.getLocation().getX() / 32;
        int cameraY = camera.getLocation().getY() / 32;

        int startX = -32 + cameraX - (GamePanel.WIDTH / 64);
        int startY = -32 + cameraY - (GamePanel.HEIGHT / 64);

        int endX = 32 + cameraX + GamePanel.WIDTH / 64;
        int endY = 32 + cameraY + GamePanel.HEIGHT / 64;

        for (int x = startX; x < endX; x++) {
            for (int y = startY; y < endY; y++) {
                if (x >= 0 && x < width && y >= 0 && y < height)
                    tiles[x][y].draw(graphics, camera);
            }
        }
    }

}

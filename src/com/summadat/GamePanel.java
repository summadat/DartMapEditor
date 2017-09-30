package com.summadat;

import com.summadat.gfx.Camera;
import com.summadat.gfx.Location;
import com.summadat.gfx.Sprite;
import com.summadat.world.Tile;
import com.summadat.world.World;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

/**
 * Created by Noah on 19-Sep-17.
 */
public class GamePanel extends JPanel implements Runnable, KeyListener, MouseListener, MouseMotionListener {

    public static final int WIDTH = 1080;
    public static final int HEIGHT = 720;


    private Camera camera;

    private Thread thread;
    private boolean running;
    public static  final int MAXFPS = 60;
    private final double TARGET_TIME = 1.0 / MAXFPS;

    public static Location mouseLoc;

    private BufferedImage image;
    private Graphics2D g;

    private World world;

    boolean layering = false;

    public static Sprite laying;
    public static Sprite layer;

    private int carouselOffset = 0;

    private boolean dragging = false;
    private Location dragStart;

    public GamePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT ));
        setFocusable(true);
        requestFocus();
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    private void init() {
        running = true;
        image = new BufferedImage(WIDTH, HEIGHT, 1);
        g = (Graphics2D) image.getGraphics();
        world = new World();
        world.loadTiles("res/shee.png");
        world.generateMap();
        laying = world.getLoader().block;
        layer = world.getLoader().block;
        camera = new Camera(new Location(WIDTH / 2, HEIGHT / 2));
        mouseLoc = new Location(0,0);
    }

    public void start() {
        thread = new Thread(this);
        thread.run();
    }

    public void stop() {
        running = false;
    }

    public void run() {

        init();

        double start = 0;
        double last = System.nanoTime() / 1000000000.0;
        double elapsed = 0;
        double unprocessed = 0;

        double frameTime = 0;
        int frames = 0;
        int fps = 0;

        boolean render;

        while(running) {

            render = false;

            start = System.nanoTime() / 1000000000.0;
            elapsed = start - last;
            last = start;

            unprocessed += elapsed;
            frameTime += elapsed;

            while (unprocessed >= TARGET_TIME) {

                unprocessed -= TARGET_TIME;
                render = true;

                update();

                if (frameTime >= 1.0) {
                    frameTime = 0;
                    fps = frames;
                    frames = 0;
                }
            }

            if (render) {
                drawWorld();
                drawCarousel();
                flipBuffer();
                frames++;
            } else {
                try {
                    Thread.sleep(1);
                } catch (Exception e) {

                }
            }
        }
    }

    private void update() {
        world.update();
        updateCamera();
    }

    private void updateCamera() {

        camera.update();

        int x = camera.getLocation().getX();
        int y = camera.getLocation().getY();

        if (x <= (WIDTH / 2))
            x = (WIDTH / 2);

        else if (x > ((world.map.width) * 32) - WIDTH / 2)
            x = ((world.map.width)* 32) - WIDTH / 2;

        if (y <= (HEIGHT / 2))
            y = (HEIGHT / 2);

        else if (y > ((world.map.height ) * 32) - HEIGHT / 2)
            y = ((world.map.height) * 32) - HEIGHT / 2;

        camera.setLocation(x, y);

    }

    private void drawWorld() {
        world.draw(g, camera);
    }

    private void drawCarousel() {
        g.fillRect(0, HEIGHT - 32, WIDTH, 32);
        g.drawImage(laying.getImage(), 0, HEIGHT - 32, null);
        g.drawImage(layer.getImage(), 32, HEIGHT - 32, null);
        g.setColor(Color.CYAN);
        g.drawRect(layering ? 32 : 0, HEIGHT - 32, 31, 31);
        for (int i = 2; i < (WIDTH / 32); i++) {
            g.drawImage(world.getLoader().get(((i + (carouselOffset * 32)) % world.getLoader().num)).getImage(), i * 32, HEIGHT - 32, null);
        }
    }

    private void flipBuffer() {
        Graphics g2 = getGraphics();
        g2.drawImage(image, 0, 0, WIDTH, HEIGHT, null);
        g2.dispose();
    }

    public void addNotify() {
        super.addNotify();
        if(thread == null) {
            addKeyListener(this);
            thread = new Thread(this);
            thread.start();
        }
    }

    public void keyTyped(KeyEvent e) {

    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            camera.setSpeed(-6, 0);
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            camera.setSpeed(6, 0);
        } else if (e.getKeyCode() == KeyEvent.VK_UP) {
            camera.setSpeed(0, -6);
        } else if ( e.getKeyCode() == KeyEvent.VK_DOWN) {
            camera.setSpeed(0, 6);
        }

        if ( e.getKeyCode() == KeyEvent.VK_S) {
            world.map.save();
        }
        if ( e.getKeyCode() == KeyEvent.VK_L) {
            layering = !layering;
            System.out.println("Layering: " + layering);
        }
    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            camera.setSpeedX(0);
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            camera.setSpeedX(0);
        } else if (e.getKeyCode() == KeyEvent.VK_UP) {
            camera.setSpeedY(0);
        } else if ( e.getKeyCode() == KeyEvent.VK_DOWN) {
            camera.setSpeedY(0);
        }

        if (e.getKeyCode() == KeyEvent.VK_Z) {
            if (carouselOffset > 0)
                carouselOffset--;
        }
        if (e.getKeyCode() == KeyEvent.VK_X) {
            if (carouselOffset + 1 < world.getLoader().num)
            carouselOffset++;
        }

    }

    public void mouseClicked(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();

        if (mouseX > 0 && mouseX < WIDTH) {
            if (mouseY > 0) {
                if (mouseY < HEIGHT - 32) {
                    int tileX = ((mouseX + camera.getLocation().getX() - (GamePanel.WIDTH / 2)) / 32);
                    int tileY = ((mouseY + camera.getLocation().getY() - (GamePanel.HEIGHT / 2)) / 32);

                    Tile t = world.map.tileAt(tileX, tileY);

                    if (t != null) {
                        if (e.isControlDown()) {
                            if (!layering)
                                t.setImage(world.getLoader().block);
                            else
                                t.removeLayer();
                        }
                        if (e.isShiftDown()) {
                            t.setWalkable(false);
                        }
                        else {
                            if (!layering)
                                t.setImage(laying);
                            else
                                t.removeLayer();
                        }
                    }

                } else if (mouseY < HEIGHT) {
                    if (!layering)
                        laying = world.getLoader().get(((mouseX / 32) + (carouselOffset * 32)) % world.getLoader().num);
                    else
                        layer = world.getLoader().get(((mouseX / 32) + (carouselOffset * 32)) % world.getLoader().num);
                }
            }
        }
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {

    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }

    public void mouseDragged(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();
        if (mouseX > 0 && mouseX < WIDTH) {
            if (mouseY > 0) {
                if (mouseY < HEIGHT - 32) {

                    int tileX = ((mouseX + camera.getLocation().getX() - (GamePanel.WIDTH / 2)) / 32);
                    int tileY = ((mouseY + camera.getLocation().getY() - (GamePanel.HEIGHT / 2)) / 32);

                    if (e.isControlDown()) {
                        if (!layering)
                            world.map.tileAt(tileX, tileY).setImage(world.getLoader().block);
                        else
                            world.map.tileAt(tileX, tileY).removeLayer();
                    } else if (e.isAltDown()) {
                        world.map.tileAt(tileX, tileY).setWalkable(true);
                    } else if (e.isShiftDown()) {
                        world.map.tileAt(tileX, tileY).setWalkable(false);
                    } else {
                        if (!layering)
                            world.map.tileAt(tileX, tileY).setImage(laying);
                        else
                            world.map.tileAt(tileX, tileY).setLayer(layer);
                    }

                } else if (mouseY < HEIGHT) {
                    if (!layering)
                        laying = world.getLoader().get(((mouseX / 32) + (carouselOffset * 32)) % world.getLoader().num);
                    else
                        layer = world.getLoader().get(((mouseX / 32) + (carouselOffset * 32)) % world.getLoader().num);
                }
            }
        }
    }

    public void mouseMoved(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();

        mouseLoc.set(mouseX, mouseY);
    }
}

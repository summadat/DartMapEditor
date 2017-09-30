package com.summadat.gfx;

import com.summadat.world.Map;

/**
 * Created by Noah on 23-Sep-17.
 */
public class Camera {

    int speedX = 0;
    int speedY = 0;

    private Location location;

    public Camera() {
        this.location = new Location(450, 450);
    }

    public Camera(Location location) {
        this.location = location;
    }

    public void setSpeedX(int speed) {
        this.speedX = speed;
    }

    public void setSpeedY(int speed) {
        this.speedY = speed;
    }

    public void setSpeed(int x, int y) {
        speedX = x;
        speedY = y;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(int x, int y) {
        location.set(x, y);
    }

    public void update() {
        if ((speedX != 0 || speedY != 0)) {

            if (location.getX() + speedX > (Map.width - 1) * 32) {
                speedX = (Map.width - 1) * 32 - location.getX();
            } else if (location.getX() + speedX < 0) {
                speedX = -location.getX();
            }

            if (location.getY() + speedY > (Map.height - 1) * 32) {
                speedY = (Map.height - 1) * 32 - location.getY();
            } else if (location.getY() + speedY < 0) {
                speedY = -location.getY();
            }

            location.translate(speedX, speedY);
        }
    }

}


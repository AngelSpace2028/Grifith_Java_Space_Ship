package griffin;

import processing.core.PApplet;
import processing.core.PVector;

public class Bullet {
    private PVector pos;
    private PVector vel;
    private int life;

    public Bullet(PVector start, PVector velocity) {
        pos = start.copy();
        vel = velocity.copy();
        life = 80;
    }

    public void update() {
        pos.add(vel);
        life--;
    }

    public boolean isDead() {
        return life <= 0 || pos.mag() > 1500;
    }

    public void render(PApplet p) {
        p.pushMatrix();
        p.translate(pos.x, pos.y, pos.z);
        p.fill(255, 200, 0);
        p.noStroke();
        p.box(6);
        p.popMatrix();
    }
}

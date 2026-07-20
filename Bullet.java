package ie.Griffith;

import processing.core.PApplet;
import processing.core.PVector;

public class Stars {
    private PVector pos;
    private float size;

    public Stars() {
        pos = new PVector(
            PApplet.random(-1200, 1200),
            PApplet.random(-900, 900),
            PApplet.random(-800, 800)
        );
        size = PApplet.random(1, 4);
    }

    public void update(PVector shipPos) {
        float dz = pos.z - shipPos.z;
        if (dz > 500) {
            pos.x = PApplet.random(-1200, 1200);
            pos.y = PApplet.random(-900, 900);
            pos.z = shipPos.z - 500 - PApplet.random(300);
            size = PApplet.random(1, 4);
        }
    }

    public void render(PApplet p) {
        p.pushMatrix();
        p.translate(pos.x, pos.y, pos.z);
        p.fill(255);
        p.noStroke();
        p.box(size * 0.5f);
        p.popMatrix();
    }
}

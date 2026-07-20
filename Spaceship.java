package griffin;

import processing.core.PApplet;
import processing.core.PVector;

public class Spaceship {
    private PVector pos;
    private float yaw, pitch, roll;
    private boolean nitroActive = false;

    public Spaceship() {
        pos = new PVector(0, 0, 0);
        yaw = pitch = roll = 0;
    }

    public boolean isNitroActive() {
        return nitroActive;
    }

    public void setNitro(boolean active) {
        this.nitroActive = active;
    }

    public PVector getOrientation() {
        float cosY = PApplet.cos(yaw);
        float sinY = PApplet.sin(yaw);
        float cosP = PApplet.cos(pitch);
        float sinP = PApplet.sin(pitch);
        float cosR = PApplet.cos(roll);
        float sinR = PApplet.sin(roll);

        PVector forward = new PVector(0, 0, -1);
        // Apply yaw
        float x = forward.x * cosY - forward.z * sinY;
        float z = forward.x * sinY + forward.z * cosY;
        forward.x = x; forward.z = z;
        // Apply pitch
        float y = forward.y * cosP - forward.z * sinP;
        z = forward.y * sinP + forward.z * cosP;
        forward.y = y; forward.z = z;
        return forward;
    }

    public void update() {
        pitch = PApplet.constrain(pitch, -PApplet.PI/3, PApplet.PI/3);
    }

    public void yaw(float delta) { yaw += delta * 0.01f; }
    public void pitch(float delta) { pitch += delta * 0.01f; }
    public void roll(float delta) { roll += delta; }

    public void thrustForward(float speed) {
        PVector dir = getOrientation();
        // Apply Nitro boost (triple speed)
        float multiplier = nitroActive ? 3.0f : 1.0f;
        pos.add(PVector.mult(dir, speed * multiplier));
    }

    public void thrustBackward(float speed) {
        PVector dir = getOrientation();
        pos.sub(PVector.mult(dir, speed));
    }

    public void strafeLeft(float speed) {
        PVector up = new PVector(0, 1, 0);
        PVector forward = getOrientation();
        PVector right = forward.cross(up).normalize();
        pos.sub(PVector.mult(right, speed));
    }

    public void strafeRight(float speed) {
        PVector up = new PVector(0, 1, 0);
        PVector forward = getOrientation();
        PVector right = forward.cross(up).normalize();
        pos.add(PVector.mult(right, speed));
    }

    public void render(PApplet p) {
        p.pushMatrix();
        p.translate(pos.x, pos.y, pos.z);
        p.rotateY(yaw);
        p.rotateX(pitch);
        p.rotateZ(roll);

        p.noStroke();
        p.fill(180, 180, 220);
        p.box(20, 10, 40);

        p.fill(0, 200, 255, 150);
        p.translate(0, -5, 10);
        p.box(12, 6, 20);

        p.fill(200, 150, 100);
        p.translate(0, 5, -10);
        p.box(30, 2, 20);

        // Engine glow - changes with Nitro
        if (nitroActive) {
            p.fill(255, 255, 200, 250);
            p.translate(0, 0, -25);
            p.box(28, 12, 10);  // bigger & brighter
            p.fill(255, 200, 0, 150);
            p.translate(0, 0, -10);
            p.box(36, 16, 8);   // outer glow
        } else {
            p.fill(255, 100, 0, 200);
            p.translate(0, 0, -25);
            p.box(18, 8, 5);
        }

        p.popMatrix();
    }

    public float getX() { return pos.x; }
    public float getY() { return pos.y; }
    public float getZ() { return pos.z; }
    public PVector getPos() { return pos; }
    public float getYaw() { return yaw; }
    public float getPitch() { return pitch; }
    public float getRoll() { return roll; }
}

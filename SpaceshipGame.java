package ie.Griffith;

import processing.core.PApplet;
import processing.core.PVector;
import ddf.minim.Minim;
import ddf.minim.AudioPlayer;
import java.util.ArrayList;
import java.util.Iterator;

public class SpaceshipGame extends PApplet {

    // Constants
    final int STAR_COUNT = 1000;
    final float THRUST_SPEED = 10;
    final float STRAFE_SPEED = 6;
    final float BULLET_SPEED = 30;
    final int BULLET_LIFE = 80;

    // Game objects
    Spaceship ship;
    ArrayList<Stars> stars;
    ArrayList<Bullet> bullets;

    // Audio
    Minim minim;
    AudioPlayer music;
    boolean musicStarted = false;

    // Controls
    boolean[] keys = new boolean[1024];

    // Mouse look
    boolean mouseLocked = false;

    // ----------------------------------------------
    @Override
    public void settings() {
        size(1024, 768, P3D);
    }

    @Override
    public void setup() {
        ship = new Spaceship();
        stars = new ArrayList<>();
        for (int i = 0; i < STAR_COUNT; i++) {
            stars.add(new Stars());
        }
        bullets = new ArrayList<>();

        minim = new Minim(this);
        music = minim.loadFile("background.mp3");
        if (music != null) {
            music.loop();
            musicStarted = true;
        }

        directionalLight(200, 200, 200, 0, 0, -1);
        ambientLight(50, 50, 50);

        noCursor();
        mouseLocked = true;
    }

    @Override
    public void draw() {
        background(0);

        // ----- Update ship orientation with mouse -----
        if (mouseLocked) {
            float sensitivity = 0.005f;
            ship.yaw(mouseX - width/2);
            ship.pitch(mouseY - height/2);
            ship.roll(0);
        }

        // Keyboard movement (relative to ship facing)
        if (keys['w'] || keys['W']) ship.thrustForward(THRUST_SPEED);
        if (keys['s'] || keys['S']) ship.thrustBackward(THRUST_SPEED);
        if (keys['a'] || keys['A']) ship.strafeLeft(STRAFE_SPEED);
        if (keys['d'] || keys['D']) ship.strafeRight(STRAFE_SPEED);
        if (keys['q'] || keys['Q']) ship.roll(-0.05f);
        if (keys['e'] || keys['E']) ship.roll(0.05f);
        
        // Nitro boost (key N)
        if (keys['n'] || keys['N']) {
            ship.setNitro(true);
        } else {
            ship.setNitro(false);
        }

        ship.update();

        // Stars (parallax)
        for (Stars s : stars) {
            s.update(ship.getPos());
        }

        // Bullets
        Iterator<Bullet> it = bullets.iterator();
        while (it.hasNext()) {
            Bullet b = it.next();
            b.update();
            if (b.isDead()) it.remove();
        }

        // ----- Render (camera follows ship) -----
        pushMatrix();
        translate(-ship.getX(), -ship.getY(), -ship.getZ());
        rotateY(-ship.getYaw());
        rotateX(-ship.getPitch());
        rotateZ(-ship.getRoll());

        for (Stars s : stars) {
            s.render(this);
        }

        for (Bullet b : bullets) {
            b.render(this);
        }

        ship.render(this);
        popMatrix();

        // ----- UI -----
        fill(255);
        textAlign(LEFT, TOP);
        textSize(16);
        text("W/S: thrust  A/D: strafe  Q/E: roll  Mouse: look  SPACE: fire  N: NITRO", 10, 10);
        text("Bullets: " + bullets.size(), 10, 30);
        if (ship.isNitroActive()) {
            fill(255, 200, 0);
            text(">>> NITRO BOOST ACTIVE <<<", 10, 50);
        }
        if (!musicStarted) {
            fill(255);
            text("Music not found (background.mp3)", 10, 70);
        }

        if (mouseLocked) {
            warpMouse(width/2, height/2);
        }
    }

    // ----------------------------------------------
    @Override
    public void keyPressed() {
        keys[keyCode] = true;
        if (key == ' ') {
            fireBullet();
        }
        if (key == 'f' || key == 'F') {
            mouseLocked = !mouseLocked;
            if (mouseLocked) noCursor();
            else cursor();
        }
    }

    @Override
    public void keyReleased() {
        keys[keyCode] = false;
    }

    @Override
    public void mousePressed() {
        mouseLocked = true;
        noCursor();
        warpMouse(width/2, height/2);
    }

    // ----------------------------------------------
    void fireBullet() {
        PVector start = new PVector(ship.getX(), ship.getY(), ship.getZ());
        PVector dir = new PVector(0, 0, -1);
        dir = ship.getOrientation().mult(dir);
        dir.mult(BULLET_SPEED);
        bullets.add(new Bullet(start, dir));
    }
}

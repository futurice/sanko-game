package com.futurice.sankogame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Bullet {
    static private final String TEXTURE_PATH = "images/Bullet.png";

    private Texture texture;
    private Rectangle boundingBox;
    private double x;
    private double y;
    private double vx;
    private double vy;
    private float screenWidth = 1000000f;

    public boolean canDestroy;

    public Bullet(final double initialX, final double initialY) {
        canDestroy = false;
        texture = new Texture(Gdx.files.internal(TEXTURE_PATH));
        boundingBox = new Rectangle();
        x = initialX;
        y = initialY;
        vy = -GamePlayParams.BULLET_SPEED;
        updateBoundingBox();
    }

    public void redraw(final SpriteBatch batch) {
        x += vx;
        y += vy;
        if (y - texture.getHeight() < 0) {
            canDestroy = true;
        }
        updateBoundingBox();
        draw(batch);
    }

    private void updateBoundingBox() {
        boundingBox.set(
            (float) x - texture.getWidth()*0.5f,
            (float) y - texture.getHeight()*0.5f,
            texture.getWidth(),
            texture.getHeight()
        );
    }

    private void draw(final SpriteBatch batch) {
        batch.draw(
            texture,
            (float) x - texture.getWidth()*0.5f,
            (float) y - texture.getHeight()*0.5f
        );
    }
}


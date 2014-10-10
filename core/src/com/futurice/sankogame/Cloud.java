package com.futurice.sankogame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Cloud {
    static private final String TEXTURE_PATH = "images/CloudBig1.png";
    static public final float SPEED_X_BIG = 4f;
    static public final float SPEED_X_MEDIUM = 8f;
    static public final float SPEED_X_SMALL = 16f;

    private Texture texture;
    private Rectangle boundingBox;
    private int x;
    private int y;
    private float vx;
    private float vy = 3f;
    private Size size;
    private float screenWidth = 1000000f;

    public boolean canDestroy;

    static public enum Size {
        SMALL, MEDIUM, BIG
    }

    public Cloud(Size size) {
        canDestroy = false;
        this.size = size;
        texture = new Texture(Gdx.files.internal(TEXTURE_PATH));
        boundingBox = new Rectangle();
        updateBoundingBox();
    }

    private float getVelocityXforSize() {
        switch (size) {
            case SMALL:
                return SPEED_X_SMALL;
            case MEDIUM:
                return SPEED_X_MEDIUM;
            case BIG:
                return SPEED_X_BIG;
            default:
                return SPEED_X_BIG;
        }
    }

    public void spawnFromScreenBorder(final float screenWidth, final float screenHeight) {
        this.screenWidth = screenWidth;
        final float velocityX = getVelocityXforSize();
        final boolean fromLeft = true;
        if (fromLeft) {
            x = (int) (- texture.getWidth()*1.1f);
            vx = velocityX;
        }
        else {
            x = (int) (screenWidth + texture.getWidth()*1.1f);
            vx = -velocityX;
        }
        y = 0;
    }

    public void redraw(final SpriteBatch batch) {
        x += vx;
        y += vy;
        if (x > screenWidth && vx > 0) {
            canDestroy = true;
        }
        if (x-texture.getWidth() < 0 && vx < 0) {
            canDestroy = true;
        }
        updateBoundingBox();
        draw(batch);
    }

    private void updateBoundingBox() {
        boundingBox.set(x, y, texture.getWidth(), texture.getHeight());
    }

    private void draw(final SpriteBatch batch) {
        batch.draw(texture,
            x, y,
            texture.getWidth()*0.5f, texture.getHeight()*0.5f, // origin
            texture.getWidth(), texture.getHeight(), // width,height
            1f, 1f, // scale
            0f, // rotation
            0, 0, // source anchor
            texture.getWidth(), texture.getHeight(), // source size
            false, true // flip horiz/vertical
        );
    }
}

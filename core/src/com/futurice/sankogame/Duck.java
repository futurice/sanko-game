package com.futurice.sankogame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

public class Duck {
    static private final String TEXTURE_PATH = "images/Duck.png";

    private Texture texture;
    private Rectangle boundingBox;
    public double x;
    public double y;
    public double spawnY;
    public double vx;
    private boolean goingToRight;

    public boolean canDestroy;

    public Duck() {
        canDestroy = false;
        texture = new Texture(Gdx.files.internal(TEXTURE_PATH));
        boundingBox = new Rectangle();
        updateBoundingBox();
    }

    public static Duck spawnFromScreenBorder(final float screenWidth, final float screenHeight) {
        final Duck duck = new Duck();
        duck.goingToRight = Math.random() > 0.5;
        if (duck.goingToRight) {
            duck.vx = GamePlayParams.DUCK_SPEED_X;
        } else {
            duck.vx = -GamePlayParams.DUCK_SPEED_X;
        }
        duck.x = duck.goingToRight ? -duck.getBoundingBox().getWidth() : screenWidth;
        duck.spawnY = screenHeight - duck.getBoundingBox().getHeight()*(2f + GamePlayParams.DUCK_SPAN_Y*0.5f);
        return duck;
    }

    public void redraw(final SpriteBatch batch) {
        x += vx;
        long time = TimeUtils.nanoTime();
        final float maxspan = this.getBoundingBox().getHeight()*GamePlayParams.DUCK_SPAN_Y;
        y = spawnY +  Math.sin(((double)time)*0.000000004) * maxspan;
        updateBoundingBox();
        draw(batch);
    }

    public Rectangle getBoundingBox() {
        return boundingBox;
    }

    private void updateBoundingBox() {
        boundingBox.set((float) x, (float) y, texture.getWidth(), texture.getHeight());
    }

    private void draw(final SpriteBatch batch) {
        batch.draw(texture,
            (float) x, (float) y,
            texture.getWidth()*0.5f, texture.getHeight()*0.5f, // origin
            texture.getWidth(), texture.getHeight(), // width,height
            1f, 1f, // scale
            0f, // rotation
            0, 0, // source anchor
            texture.getWidth(), texture.getHeight(), // source size
            !goingToRight, true // flip horiz/vertical
        );
    }
}

package com.futurice.sankogame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class Cloud {
    static private final String TEXTURE_PATH = "images/CloudBig1.png";

    private Texture texture;
    private Rectangle boundingBox;
    public double x;
    public double y;
    public double vx;
    private boolean goingToRight;
    private double vy = GamePlayParams.ENVIRONMENT_INITIAL_SPEED_Y;
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

    private static float getVelocityXforSize(Size size) {
        switch (size) {
            case SMALL:
                return GamePlayParams.CLOUD_SMALL_SPEED_X;
            case MEDIUM:
                return GamePlayParams.CLOUD_MEDIUM_SPEED_X;
            case BIG:
                return GamePlayParams.CLOUD_BIG_SPEED_X;
            default:
                return GamePlayParams.CLOUD_BIG_SPEED_X;
        }
    }

    public static Cloud spawnFromScreenBorder(final Size size, final float screenWidth) {
        final float velocityX = getVelocityXforSize(size);
        final Cloud cloud = new Cloud(size);
        cloud.goingToRight = Math.random() > 0.5;
        if (cloud.goingToRight) {
            cloud.vx = velocityX;
        }
        else {
            cloud.vx = -velocityX;
        }
        cloud.x = (int) (screenWidth*Math.random() - cloud.getBoundingBox().getWidth()*0.5);
        cloud.y = (int) (-cloud.getBoundingBox().getHeight());
        return cloud;
    }

    public List<Cloud> split() {
        if (this.size == Size.SMALL) {
            return new ArrayList<Cloud>();
        }
        ArrayList<Cloud> list = new ArrayList<Cloud>();

        Size newSize = Size.MEDIUM;
        if (this.size == Size.BIG) {
            newSize = Size.MEDIUM;
        } else if (this.size == Size.MEDIUM) {
            newSize = Size.SMALL;
        }
        final float velocityX = getVelocityXforSize(newSize);

        final Cloud cloud1 = new Cloud(newSize);
        cloud1.goingToRight = true;
        cloud1.vx = velocityX;
        cloud1.x = this.x + getBoundingBox().getWidth()*0.25;
        cloud1.y = this.y;

        final Cloud cloud2 = new Cloud(newSize);
        cloud2.goingToRight = false;
        cloud2.vx = -velocityX;
        cloud2.x = this.x - getBoundingBox().getWidth()*0.25;
        cloud2.y = this.y;

        list.add(cloud1);
        list.add(cloud2);
        return list;
    }

    public void redraw(final SpriteBatch batch) {
        x += vx;
        y += vy;
        updateBoundingBox();
        draw(batch);
    }

    public Rectangle getBoundingBox() {
        return boundingBox;
    }

    private void updateBoundingBox() {
        final float scale = getScale();
        boundingBox.set((float) x, (float) y, texture.getWidth()*scale, texture.getHeight()*scale);
    }

    private float getScale() {
        float scale = 1.0f;
        if (this.size == Size.BIG) {
            scale = 1.0f;
        } else if (this.size == Size.MEDIUM) {
            scale = 0.75f;
        } else if (this.size == Size.SMALL) {
            scale = 0.5f;
        }
        return scale;
    }

    private void draw(final SpriteBatch batch) {
        final float scale = getScale();
        batch.draw(texture,
            (float) x, (float) y,
            texture.getWidth()*0.5f, texture.getHeight()*0.5f, // origin
            texture.getWidth(), texture.getHeight(), // width,height
            scale, scale, // scale
            0f, // rotation
            0, 0, // source anchor
            texture.getWidth(), texture.getHeight(), // source size
            !goingToRight, true // flip horiz/vertical
        );
    }
}

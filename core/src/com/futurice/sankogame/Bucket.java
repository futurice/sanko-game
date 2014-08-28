package com.futurice.sankogame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Bucket {
    static private final float BUCKET_SIZE = 0.15f; // % of screen width

    private Texture texture;
    private Rectangle boundingBox;
    private float health;
    private final float xOffsetPercent;
    private final float yOffsetPercent;
    private int x;
    private int y;

    public Bucket(final String texturePath, final float xOffsetPercent, final float yOffsetPercent) {
        texture = new Texture(Gdx.files.internal(texturePath));
        boundingBox = new Rectangle();
        health = 100f;
        this.xOffsetPercent = xOffsetPercent;
        this.yOffsetPercent = yOffsetPercent;
    }

    public void takeDamage(final float damage) {
        health -= damage;
        health = Math.max(health, 0);
    }

    public void update(final SpriteBatch batch, final int screenWidth, final int screenHeight) {
        if (Gdx.input.justTouched() && boundingBox.contains(Gdx.input.getX(), Gdx.input.getY())) {
            takeDamage(15f);
        }
        this.x = (int) (screenWidth*xOffsetPercent - getBucketSize(screenWidth)*0.5);
        this.y = (int) (screenHeight*yOffsetPercent + getBucketSize(screenWidth)*(1 - health*0.01f));
        updateBoundingBox(screenWidth);
        drawBucket(batch, screenWidth);
    }

    private int getBucketSize(final int screenWidth) {
        return (int) (screenWidth * BUCKET_SIZE);
    }

    private void updateBoundingBox(final int screenWidth) {
        final int bucketSize = getBucketSize(screenWidth);
        boundingBox.set(x, y, bucketSize, bucketSize*health*0.01f);
    }

    private void drawBucket(final SpriteBatch batch, final int screenWidth) {
        final int bucketSize = getBucketSize(screenWidth);
        batch.draw(texture, x, y, bucketSize, bucketSize*health*0.01f);
    }
}

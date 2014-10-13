package com.futurice.sankogame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.futurice.sankogame.effects.MyParticleEffect;

public class Hero {
    static private final String TEXTURE_PATH = "images/Hero.png";

    private MyParticleEffect particleEffect;
    private Texture texture;
    private Rectangle boundingBox;
    public double x;
    public double y;
    private double vx;

    public Hero(final int screenWidth, final int screenHeight) {
        // Build stuff
        texture = new Texture(Gdx.files.internal(TEXTURE_PATH));
        boundingBox = new Rectangle();
        makeParticleEffect();

        // Initial setup
        resetToInitialPosition(screenWidth, screenHeight);
        updateBoundingBox();
    }

    private void makeParticleEffect() {
        particleEffect = new MyParticleEffect();
        particleEffect.load(Gdx.files.internal("effects/thrust.p"), Gdx.files.internal("images"));
        setParticleEffectPosition();
        particleEffect.start();
    }

    private void setParticleEffectPosition() {
        particleEffect.setPosition(
            (float) x,
            (float) (y + texture.getHeight() * 0.5f)
        );
    }

    public void resetToInitialPosition(final int screenWidth, final int screenHeight) {
        x = (int) (screenWidth*0.5f);
        y = (int) (screenHeight - texture.getHeight()*2.5f);
        setParticleEffectPosition();
    }

    public void redraw(final SpriteBatch batch, double delta) {
        updateBoundingBox();
        draw(batch, delta);
    }

    public void setVelocityX(double value) {
        vx = value;
        particleEffect.setParentVelocityX((float) (value*0.9));
    }

    public double getVelocityX() {
        return vx;
    }

    public Rectangle getBoundingBox() {
        return boundingBox;
    }

    private void updateBoundingBox() {
        boundingBox.set(
            (float) (x - texture.getWidth()*0.5f),
            (float) (y - texture.getHeight()*0.5f),
            texture.getWidth(),
            texture.getHeight()
        );
    }

    private void draw(final SpriteBatch batch, double delta) {
        batch.draw(texture,
            (float) (x - texture.getWidth() * 0.5f), (float) (y - texture.getHeight() * 0.5f),
            texture.getWidth() * 0.5f, texture.getHeight() * 0.5f, // origin
            texture.getWidth(), texture.getHeight(), // width,height
            1f, 1f, // scale
            (float) vx, // rotation
            0, 0, // source anchor
            texture.getWidth(), texture.getHeight(), // source size
            false, true // flip horiz/vertical
        );
        setParticleEffectPosition();
        particleEffect.draw(batch, (float) delta);
    }

    public void dispose() {
        if (particleEffect != null) {
            particleEffect.dispose();
        }
    }
}


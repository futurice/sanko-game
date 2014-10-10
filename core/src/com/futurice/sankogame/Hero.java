package com.futurice.sankogame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.futurice.sankogame.effects.MyParticleEffect;

public class Hero {
    static private final String TEXTURE_PATH = "images/Hero.png";

    private MyParticleEffect particleEffect;
    private Texture texture;
    private Rectangle boundingBox;
    private double x;
    private double y;
    private double vx;
    private int screenWidth;

    public Hero(final int screenWidth, final int screenHeight) {
        // Build stuff
        texture = new Texture(Gdx.files.internal(TEXTURE_PATH));
        boundingBox = new Rectangle();
        makeParticleEffect();

        // Initial setup
        resetToInitialPosition(screenWidth, screenHeight);
        updateBoundingBox();
    }

    public void makeParticleEffect() {
        particleEffect = new MyParticleEffect();
        particleEffect.load(Gdx.files.internal("effects/thrust.p"), Gdx.files.internal("images"));
        setParticleEffectPosition();
        particleEffect.start();
    }

    public void resetToInitialPosition(final int screenWidth, final int screenHeight) {
        x = (int) (screenWidth*0.5f);
        y = (int) (screenHeight - texture.getHeight()*2.5f);
        this.screenWidth = screenWidth;
        setParticleEffectPosition();
    }

    public void setParticleEffectPosition() {
        particleEffect.setPosition(
            (float) x,
            (float) (y + texture.getHeight()*0.5f)
        );
    }

    public void update(final SpriteBatch batch, double delta) {
        updatePhysics();
        updateBoundingBox();
        draw(batch, delta);
    }

    public void updatePhysics() {
        // User inputs
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            vx = -GamePlayParams.HERO_MOVE_SPEED_X;
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            vx = GamePlayParams.HERO_MOVE_SPEED_X;
        } else {
            vx *= GamePlayParams.HERO_MOVE_DECELERATION_X;
        }
        x += vx;
        particleEffect.setParentVelocityX((float) vx);

        // Environment restrictions
        if (x < texture.getWidth()) {
            x = texture.getWidth();
            particleEffect.setParentVelocityX(0);
        } else if (x > screenWidth-texture.getWidth()) {
            x = screenWidth-texture.getWidth();
            particleEffect.setParentVelocityX(0);
        }
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
            (float) (x - texture.getWidth()*0.5f), (float) (y - texture.getHeight()*0.5f),
            texture.getWidth()*0.5f, texture.getHeight()*0.5f, // origin
            texture.getWidth(), texture.getHeight(), // width,height
            1f, 1f, // scale
            0f, // rotation
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


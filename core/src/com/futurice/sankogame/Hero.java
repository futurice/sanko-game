package com.futurice.sankogame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.futurice.sankogame.effects.MyParticleEffect;

public class Hero {
    static private final String TEXTURE_PATH = "images/Hero.png";

    private MyParticleEffect particleEffect;
    private Texture texture;
    private Rectangle boundingBox;
    private int x;
    private int y;
    private float vx;


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
        particleEffect.setPosition(x, y);
        particleEffect.start();
    }

    public void resetToInitialPosition(final int screenWidth, final int screenHeight) {
        x = (int) ((screenWidth*0.5) - texture.getWidth()*0.5f);
        y = (int) (screenHeight - texture.getHeight()*3.0f);
        particleEffect.setPosition(x + texture.getWidth()*0.5f, y + texture.getHeight());
    }

    public void update(final SpriteBatch batch, double delta) {
        updateBoundingBox();
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            vx = -GamePlayParams.HERO_MOVE_SPEED_X;
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            vx = GamePlayParams.HERO_MOVE_SPEED_X;
        } else {
            vx *= 0.4;
        }
        particleEffect.setParentVelocityX(vx);
        x += vx;
        draw(batch, delta);
    }

    private void updateBoundingBox() {
        boundingBox.set(x, y, texture.getWidth(), texture.getHeight());
    }

    private void draw(final SpriteBatch batch, double delta) {
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
        particleEffect.setPosition(x + texture.getWidth() * 0.5f, y + texture.getHeight());
        particleEffect.draw(batch, (float) delta);
    }

    public void dispose() {
        if (particleEffect != null) {
            particleEffect.dispose();
        }
    }
}


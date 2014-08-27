package com.futurice.sankogame;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class SankoGame implements ApplicationListener {
    private Texture redBucketImage;
    private Texture greenBucketImage;
    private Texture blueBucketImage;
    private Texture yellowBucketImage;
    private SpriteBatch batch;
    private OrthographicCamera camera;

    private double redFill;
    private double greenFill;
    private double blueFill;
    private double yellowFill;

    private int screenWidth = 800;
    private int screenHeight = 480;

    Array<ParticleEffectPool.PooledEffect> effects;

    @Override
    public void create() {
        // load the images for the droplet and the bucket, 64x64 pixels each
//        dropImage = new Texture(Gdx.files.internal("droplet.png"));
        redBucketImage = new Texture(Gdx.files.internal("red.png"));
        greenBucketImage = new Texture(Gdx.files.internal("green.png"));
        blueBucketImage = new Texture(Gdx.files.internal("blue.png"));
        yellowBucketImage = new Texture(Gdx.files.internal("yellow.png"));

        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(true, screenWidth, screenHeight);
        batch = new SpriteBatch();

        ParticleEffectPool bombEffectPool;
        effects = new Array();
        ParticleEffect bombEffect = new ParticleEffect();
        bombEffect.load(Gdx.files.internal("blueparticles.p"), Gdx.files.internal("redparticle"));
        bombEffectPool = new ParticleEffectPool(bombEffect, 1, 2);
        // Create effect:
        ParticleEffectPool.PooledEffect effect = bombEffectPool.obtain();
        effect.setPosition(100, 50);
        effects.add(effect);
    }

    @Override
    public void render() {
        // clear the screen with a dark blue color. The
        // arguments to glClearColor are the red, green
        // blue and alpha component in the range [0,1]
        // of the color to be used to clear the screen.
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // tell the camera to update its matrices.
        camera.update();

        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        batch.setProjectionMatrix(camera.combined);

        // begin a new batch and draw the buckets
        int bucketSize = (int) (screenWidth*0.15f);
        batch.begin();
        batch.draw(redBucketImage, (int)(screenWidth*0.2f - bucketSize*0.5), 50, bucketSize, bucketSize);
        batch.draw(blueBucketImage, (int)(screenWidth*0.4f - bucketSize*0.5), 50, bucketSize, bucketSize);
        batch.draw(greenBucketImage, (int)(screenWidth*0.6f - bucketSize*0.5), 50, bucketSize, bucketSize);
        batch.draw(yellowBucketImage, (int)(screenWidth*0.8f - bucketSize*0.5), 50, bucketSize, bucketSize);

        // Update and draw effects:
        for (int i = effects.size - 1; i >= 0; i--) {
            ParticleEffectPool.PooledEffect effect = effects.get(i);
            effect.draw(batch);
            if (effect.isComplete()) {
                effect.free();
                effects.removeIndex(i);
            }
        }

        batch.end();
    }

    @Override
    public void dispose() {
        // dispose of all the native resources
        batch.dispose();

        // Reset all effects:
        for (int i = effects.size - 1; i >= 0; i--) {
            effects.get(i).free();
        }
        effects.clear();
    }

    @Override
    public void resize(final int width, final int height) {
        screenWidth = width;
        screenHeight = height;
        camera.setToOrtho(true, screenWidth, screenHeight);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }
}
package com.futurice.sankogame;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;

/**
 * The game.
 */
public class SankoGame implements ApplicationListener {
    private static final float BLACK_R = 33f/255f;
    private static final float BLACK_G = 15f/255f;
    private static final float BLACK_B = 0f/255f;

    private SpriteBatch batch;
    private OrthographicCamera camera;
    private int screenWidth = 800;
    private int screenHeight = 480;
    private Aim aim;
    private Hero hero;
    private Cloud cloud;
    private long lastTime;

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        batch = new SpriteBatch();
        //aim = new Aim();
        hero = new Hero(screenWidth, screenHeight);
        cloud = new Cloud(Cloud.Size.BIG);

        camera = new OrthographicCamera();
        camera.setToOrtho(true, screenWidth, screenHeight);

        lastTime = TimeUtils.nanoTime();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(BLACK_R, BLACK_G, BLACK_B, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        final double delta = calculateDelta();

        // tell the camera to update its matrices.
        camera.update();

        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        batch.setProjectionMatrix(camera.combined);

        updatePhysics();

        // begin a new batch and draw the buckets
        batch.begin();
        cloud.redraw(batch);
        hero.redraw(batch, delta);
        //aim.update(camera, batch, screenWidth, screenHeight, gameTick);
        batch.end();
    }

    public void updatePhysics() {
        // User inputs
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            hero.setVelocityX(-GamePlayParams.HERO_MOVE_SPEED_X);
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            hero.setVelocityX(GamePlayParams.HERO_MOVE_SPEED_X);
        } else {
            hero.setVelocityX(hero.getVelocityX() * GamePlayParams.HERO_MOVE_DECELERATION_X);
        }
        hero.x += hero.getVelocityX();

        // Environment restrictions
        if (hero.x < hero.getBoundingBox().getWidth()) {
            hero.x = hero.getBoundingBox().getWidth();
            hero.setVelocityX(0);
        } else if (hero.x > screenWidth-hero.getBoundingBox().getWidth()) {
            hero.x = screenWidth-hero.getBoundingBox().getWidth();
            hero.setVelocityX(0);
        }
    }

    private double calculateDelta() {
        final long now = TimeUtils.nanoTime();
        final double delta = (double) (now - lastTime) * 0.000000001;
        lastTime = now;
        return delta;
    }

    @Override
    public void dispose() {
        // dispose of all the native resources
        batch.dispose();
        hero.dispose();
    }

    @Override
    public void resize(final int width, final int height) {
        screenWidth = width;
        screenHeight = height;
        camera.setToOrtho(true, screenWidth, screenHeight);
        cloud.spawnFromScreenBorder(screenWidth, screenHeight);
        hero.resetToInitialPosition(width, height);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }
}
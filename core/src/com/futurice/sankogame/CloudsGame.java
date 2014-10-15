package com.futurice.sankogame;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.mappings.Ouya;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * The game.
 */
public class CloudsGame implements ApplicationListener {
    private static final float BLACK_R = 33f/255f;
    private static final float BLACK_G = 15f/255f;
    private static final float BLACK_B = 0f/255f;

    private SpriteBatch batch;
    private OrthographicCamera camera;
    private int screenWidth = 800;
    private int screenHeight = 480;
    private Hero hero;
    private Score score;
    private List<Bullet> bullets;
    private List<Cloud> clouds;
    private Duck duck;
    private long lastTime;
    private long lastTimeSpawnedCloud;
    private long lastTimeSpawnedDuck;
    private boolean bulletJustShot;
    private Controller controller;

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        batch = new SpriteBatch();
        hero = new Hero(screenWidth, screenHeight);
        score = new Score(screenWidth, screenHeight);
        bullets = new ArrayList<Bullet>();
        clouds = new ArrayList<Cloud>();
        if (Controllers.getControllers().size > 0) {
            controller = Controllers.getControllers().first();
        }

        camera = new OrthographicCamera();
        camera.setToOrtho(true, screenWidth, screenHeight);

        lastTime = TimeUtils.nanoTime();
        lastTimeSpawnedCloud = lastTime;
        lastTimeSpawnedDuck = lastTime;
    }

    private void resetGame() {
        hero.resetToInitialPosition(screenWidth, screenHeight);
        bullets.clear();
        clouds.clear();
        score.reset();
        duck = null;
        long now = TimeUtils.nanoTime();
        lastTimeSpawnedDuck = now;
        lastTimeSpawnedCloud = now;
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

        handlePlayerInputs();
        updatePhysics();
        redrawAll(delta);
    }

    private void handleOuyaInputs() {
        float leftXAxis = controller.getAxis(Ouya.AXIS_LEFT_X);
        boolean oButton = controller.getButton(Ouya.BUTTON_O);

        if (oButton) {
            if (!bulletJustShot) {
                bullets.add(new Bullet(hero.x, hero.y));
                bulletJustShot = true;
            }
        } else {
            bulletJustShot = false;
        }

        // Fix for 'neutral' threshold
        if (Math.abs(leftXAxis) < 0.1) {
            leftXAxis = 0;
        }
        // Decelerate
        if (leftXAxis == 0) {
            hero.setVelocityX(hero.getVelocityX() * GamePlayParams.HERO_MOVE_DECELERATION_X);
        }
        // Move sideways
        else {
            hero.setVelocityX(leftXAxis*GamePlayParams.HERO_MOVE_SPEED_X);
        }
    }

    private void handleKeyboardInputs() {
        // Shoot
        if (Gdx.input.isKeyPressed(Input.Keys.X)) {
            if (!bulletJustShot) {
                bullets.add(new Bullet(hero.x, hero.y));
                bulletJustShot = true;
            }
        } else {
            bulletJustShot = false;
        }

        // Move sideways
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            hero.setVelocityX(-GamePlayParams.HERO_MOVE_SPEED_X);
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            hero.setVelocityX(GamePlayParams.HERO_MOVE_SPEED_X);
        } else {
            hero.setVelocityX(hero.getVelocityX() * GamePlayParams.HERO_MOVE_DECELERATION_X);
        }
    }

    private void handlePlayerInputs() {
        if(controller != null && controller.getName().equals(Ouya.ID)) {
            handleOuyaInputs();
        } else {
            handleKeyboardInputs();
        }
    }

    public void updatePhysics() {
        // Physics updated
        hero.x += hero.getVelocityX();

        // Environment restrictions
        if (hero.x < hero.getBoundingBox().getWidth()) {
            hero.x = hero.getBoundingBox().getWidth();
            hero.setVelocityX(0);
        } else if (hero.x > screenWidth-hero.getBoundingBox().getWidth()) {
            hero.x = screenWidth-hero.getBoundingBox().getWidth();
            hero.setVelocityX(0);
        }

        // Spawn clouds
        final long now = TimeUtils.nanoTime();
        if (now - lastTimeSpawnedCloud > GamePlayParams.CLOUD_SPAWN_INTERVAL*1000000) {
            clouds.add(Cloud.spawnFromScreenBorder(Cloud.Size.BIG, screenWidth));
            lastTimeSpawnedCloud = now;
        }
        // Spawn duck
        if (now - lastTimeSpawnedDuck > GamePlayParams.DUCK_SPAWN_INTERVAL*1000000) {
            duck = Duck.spawnFromScreenBorder(screenWidth, screenHeight);
            lastTimeSpawnedDuck = now;
        }

        // Resolve cloud-bullet collisions
        List<Cloud> newSplittedClouds = new ArrayList<Cloud>();
        for (Bullet b : bullets) {
            for (Cloud c : clouds) {
                if (b.getBoundingBox().overlaps(c.getBoundingBox())) {
                    b.canDestroy = true;
                    c.canDestroy = true;
                    newSplittedClouds.addAll(c.split());
                    score.add(1);
                }
            }
        }
        clouds.addAll(newSplittedClouds);

        // Resolve bullet-duck collisions
        if (duck != null) {
            for (Bullet b : bullets) {
                if (b.getBoundingBox().overlaps(duck.getBoundingBox())) {
                    duck.canDestroy = true;
                    b.canDestroy = true;
                    score.add(100);
                }
            }
        }

        // Resolve hero collisions
        boolean heroDied = false;
        for (Cloud c : clouds) {
            if (c.getBoundingBox().overlaps(hero.getBoundingBox())) {
                heroDied = true;
            }
        }
        if (duck != null && duck.getBoundingBox().overlaps(hero.getBoundingBox())) {
            heroDied = true;
        }
        if (heroDied) {
            resetGame();
        }

        // Remove old bullets
        for (Bullet b : bullets) {
            if (b.y - b.getBoundingBox().getHeight() < 0) {
                b.canDestroy = true;
            }
        }
        // Remove old clouds
        for (Cloud c : clouds) {
            if (c.x > screenWidth && c.vx > 0) {
                c.canDestroy = true;
            }
            if (c.x+c.getBoundingBox().getWidth() < 0 && c.vx < 0) {
                c.canDestroy = true;
            }
        }
        // Remove dead duck
        if (duck != null) {
            if (duck.x > screenWidth + duck.getBoundingBox().getWidth()
            || duck.x < 0 - duck.getBoundingBox().getWidth())
            {
                duck.canDestroy = true;
            }
            if (duck.canDestroy) {
                duck = null;
            }
        }
    }

    public void updateBullets() {
        Iterator<Bullet> it = bullets.iterator();
        while (it.hasNext()) {
            Bullet bullet = it.next();
            bullet.redraw(batch);
            if (bullet.canDestroy) {
                it.remove();
            }
        }
    }

    public void updateClouds() {
        Iterator<Cloud> it = clouds.iterator();
        while (it.hasNext()) {
            Cloud cloud = it.next();
            cloud.redraw(batch);
            if (cloud.canDestroy) {
                it.remove();
            }
        }
    }

    private double calculateDelta() {
        final long now = TimeUtils.nanoTime();
        final double delta = (double) (now - lastTime) * 0.000000001;
        lastTime = now;
        return delta;
    }

    private void redrawAll(double delta) {
        batch.begin();
        hero.redraw(batch, delta);
        updateBullets();
        updateClouds();
        score.redraw(batch);
        if (duck != null) {
            duck.redraw(batch);
        }
        batch.end();
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
        hero.resetToInitialPosition(screenWidth, screenHeight);
        score.setScreenHeight(screenHeight);
        score.setScreenWidth(screenWidth);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }
}
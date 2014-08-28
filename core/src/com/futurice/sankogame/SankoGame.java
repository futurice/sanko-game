package com.futurice.sankogame;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;


/**
 * The game.
 *
 * Basic colors are:
 * red    #FF5455
 * green  #60E670
 * blue   #38BFFA
 * yellow #FFF454
 *
 */
public class SankoGame implements ApplicationListener {
    private Texture aimTexture;
    private ParticleEffect particleEffect;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private int screenWidth = 800;
    private int screenHeight = 480;
    private Bucket redBucket;
    private Bucket greenBucket;
    private Bucket blueBucket;
    private Bucket yellowBucket;
    private long gameTick = 0L;
    private long lastTime;

    @Override
    public void create() {
        batch = new SpriteBatch();
        aimTexture = new Texture(Gdx.files.internal("images/aim.png"));
        redBucket = new Bucket("images/red.png", 0.2f, 0.2f);
        greenBucket = new Bucket("images/green.png", 0.4f, 0.2f);
        blueBucket = new Bucket("images/blue.png", 0.6f, 0.2f);
        yellowBucket = new Bucket("images/yellow.png", 0.8f, 0.2f);
        particleEffect = new ParticleEffect();
        particleEffect.load(Gdx.files.internal("effects/red-drops.p"), Gdx.files.internal("images"));
        particleEffect.setPosition(180f, 200f);
        particleEffect.start();

        camera = new OrthographicCamera();
        camera.setToOrtho(true, screenWidth, screenHeight);

        lastTime = TimeUtils.nanoTime();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        gameTick++;
        final double delta = calculateDelta();

        // tell the camera to update its matrices.
        camera.update();

        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        batch.setProjectionMatrix(camera.combined);

        // begin a new batch and draw the buckets
        batch.begin();
        redBucket.update(batch, screenWidth, screenHeight);
        greenBucket.update(batch, screenWidth, screenHeight);
        blueBucket.update(batch, screenWidth, screenHeight);
        yellowBucket.update(batch, screenWidth, screenHeight);
        drawAim(batch);
        particleEffect.draw(batch, (float) delta);
        batch.end();
    }

    private double calculateDelta() {
        final long now = TimeUtils.nanoTime();
        final double delta = (double) (now - lastTime) * 0.000000001;
        lastTime = now;
        return delta;
    }

    private Vector3 getAimPosition() {
        final int inputX = Gdx.input.getX();
        final int inputY = Gdx.input.getY();
        if (inputX == 0 || inputY == 0) {
            return new Vector3(screenWidth*0.5f, screenHeight*0.5f, 0);
        }
        else {
            Vector3 touchPos = new Vector3();
            touchPos.set(inputX, inputY, 0);
            return camera.unproject(touchPos);
        }
    }

    private void drawAim(final SpriteBatch batch) {
        final Vector3 aimPosition = getAimPosition();
        int aimSize = (int) (screenWidth*0.04f);
        batch.draw(aimTexture,
            aimPosition.x - aimSize*0.5f, aimPosition.y - aimSize*0.5f,
            aimSize*0.5f, aimSize*0.5f,
            aimSize, aimSize,
            1f, 1f,
            (gameTick * 3) % 360,
            0, 0,
            200, 200,
            false, false
        );
    }

    @Override
    public void dispose() {
        // dispose of all the native resources
        batch.dispose();
        if (particleEffect != null) {
            particleEffect.dispose();
        }
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
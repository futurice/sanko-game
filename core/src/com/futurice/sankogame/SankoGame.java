package com.futurice.sankogame;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class SankoGame implements ApplicationListener {
    private Texture redBucketTexture;
    private Texture greenBucketTexture;
    private Texture blueBucketTexture;
    private Texture yellowBucketTexture;
    private Texture aimTexture;
    private ParticleEffect particleEffect;

    private SpriteBatch batch;
    private OrthographicCamera camera;

    private float redFill = 100f;
    private float greenFill = 100f;
    private float blueFill = 100f;
    private float yellowFill = 100f;

    private int screenWidth = 800;
    private int screenHeight = 480;
    private long gameTick = 0L;

    static private final float BUCKET_SIZE = 0.15f; // % of screen width
    static private final float BUCKET_Y = 0.2f; // % of screen height

    @Override
    public void create() {
        batch = new SpriteBatch();
        loadTextures();
        camera = new OrthographicCamera();
        camera.setToOrtho(true, screenWidth, screenHeight);

//        particleEffect = new ParticleEffect();
//        particleEffect.load(Gdx.files.internal("effects/drop.p"), Gdx.files.internal("images"));
//        particleEffect.setPosition(180f, 180f);
//        particleEffect.start();
    }

    private void loadTextures() {
        redBucketTexture = new Texture(Gdx.files.internal("images/red.png"));
        greenBucketTexture = new Texture(Gdx.files.internal("images/green.png"));
        blueBucketTexture = new Texture(Gdx.files.internal("images/blue.png"));
        yellowBucketTexture = new Texture(Gdx.files.internal("images/yellow.png"));
        aimTexture = new Texture(Gdx.files.internal("images/aim.png"));
    }

    @Override
    public void render() {
        // clear the screen with a dark blue color. The
        // arguments to glClearColor are the red, green
        // blue and alpha component in the range [0,1]
        // of the color to be used to clear the screen.
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        gameTick++;

        // tell the camera to update its matrices.
        camera.update();

        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        batch.setProjectionMatrix(camera.combined);

        // begin a new batch and draw the buckets
        batch.begin();
        drawBuckets(batch);
        drawAim(batch);
//        particleEffect.draw(batch);
        batch.end();
    }

    private void drawBuckets(final SpriteBatch batch) {
        drawBucket(batch, redBucketTexture, 0.2f, redFill);
        drawBucket(batch, blueBucketTexture, 0.4f, blueFill);
        drawBucket(batch, greenBucketTexture, 0.6f, greenFill);
        drawBucket(batch, yellowBucketTexture, 0.8f, yellowFill);
    }

    /**
     * @param batch
     * @param texture
     * @param x percentage of the screen width
     * @param height [0,100] as a percentage of the bucket original height
     */
    private void drawBucket(final SpriteBatch batch, final Texture texture, final float x, final float height) {
        final int bucketSize = (int) (screenWidth * BUCKET_SIZE);
        final int bucketY = (int) (screenHeight * BUCKET_Y);
        batch.draw(
            texture,
            (int)(screenWidth*x - bucketSize*0.5),
            bucketY,
            bucketSize,
            bucketSize*height*0.01f
        );
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
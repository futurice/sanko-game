package com.futurice.sankogame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class Aim {

    private Texture aimTexture;

    public Aim() {
        aimTexture = new Texture(Gdx.files.internal("images/aim.png"));
    }

    private Vector3 getAimPosition(final OrthographicCamera camera,
        final float screenWidth, final float screenHeight)
    {
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

    public void  update(final OrthographicCamera camera, final SpriteBatch batch,
        final float screenWidth, final float screenHeight, long gameTick)
    {
        final Vector3 aimPosition = getAimPosition(camera, screenWidth, screenHeight);
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
}

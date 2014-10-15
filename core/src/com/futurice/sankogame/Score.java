package com.futurice.sankogame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
/**
 * Created by amed on 15.10.14.
 */
public class Score {

    private int screenWidth;
    private int screenHeight;
    private BitmapFont scoreBitmapFont;

    private long score = 0;

    public Score(int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(
            Gdx.files.internal("fonts/SourceSansPro-Italic.ttf")
        );
        FreeTypeFontGenerator.FreeTypeFontParameter parameters =
            new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameters.flip = true;
        parameters.size = 22;
        scoreBitmapFont = generator.generateFont(parameters);
        generator.dispose();
        scoreBitmapFont.setColor(Color.WHITE);
        scoreBitmapFont.scale(1f);
    }

    public void add(long scoreAddition) {
        this.score += scoreAddition;
    }

    public void reset() {
        this.score = 0;
    }

    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    public void redraw(final SpriteBatch batch) {
        String scoreStr = String.format("Score: %d", this.score);
        scoreBitmapFont.draw(
            batch,
            scoreStr,
            screenWidth*0.5f - scoreBitmapFont.getSpaceWidth()*((float)scoreStr.length()),
            50
        );
    }
}

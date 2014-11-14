package com.futurice.sankogame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class FinalScore {

    private int screenWidth;
    private int screenHeight;
    private BitmapFont scoreBitmapFont;
    private float alpha = 0f;

    private long score = 0;

    public FinalScore(int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(
            Gdx.files.internal("fonts/SourceSansPro-Italic.ttf")
        );
        FreeTypeFontGenerator.FreeTypeFontParameter parameters =
            new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameters.flip = true;
        parameters.size = 40;
        scoreBitmapFont = generator.generateFont(parameters);
        generator.dispose();
        scoreBitmapFont.setColor(Color.WHITE);
        scoreBitmapFont.scale(1f);
    }

    public void setValue(long scoreAddition) {
        this.score = scoreAddition;
        this.alpha = 1.0f;
    }

    public void reset() {
        this.score = 0;
    }

    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }

    public long getValue() {
        return score;
    }

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    public void redraw(final SpriteBatch batch) {
        String scoreStr = String.format("You scored: %d", this.score);
        alpha -= 0.003f;
        alpha = (alpha < 0) ? 0 : alpha;
        scoreBitmapFont.setColor(1.0f, 1.0f, 1.0f, alpha);
        scoreBitmapFont.draw(
            batch,
            scoreStr,
            screenWidth*0.5f - scoreBitmapFont.getSpaceWidth()*((float)scoreStr.length()),
            screenHeight*0.5f
        );
    }
}

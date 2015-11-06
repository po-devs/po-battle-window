package com.mygdx.game.battlewindow;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class SpriteAnimation extends Animation {

    public SpriteAnimation(float frameDuration, Array<? extends TextureRegion> keyFrames) {
        super(frameDuration, keyFrames);
        region = getKeyFrame(0f);
    }

    public SpriteAnimation(float frameDuration, Array<? extends TextureRegion> keyFrames, PlayMode playMode) {
        super(frameDuration, keyFrames, playMode);
        region = getKeyFrame(0f);
    }

    public SpriteAnimation(float frameDuration, TextureRegion... keyFrames) {
        super(frameDuration, keyFrames);
        region = getKeyFrame(0f);
    }

    public void dispose() {
        getKeyFrame(0f).getTexture().dispose();
    }

    public boolean paused = false;
    public boolean visible = true;

    private TextureRegion region;

    public void update() {
        if (visible) {
            if (!paused) {
                region = getKeyFrame(ContinuousGameFrame.elapsedTime);
            }
        }
    }

    public void loadSub(boolean side) {
        region = ContinuousGameFrame.instance.substitutes[(side ? 0 : 1)];
    }

    public TextureRegion getRegion() {
        return region;
    }

    public Vector2 maxOff(float scale) {
        float maxOffX = 0;
        float maxOffY = 0;
        for (TextureRegion text : getKeyFrames()) {
            float offX = ((TextureAtlas.AtlasRegion) text).offsetX * scale;
            float offY = ((TextureAtlas.AtlasRegion) text).offsetY * scale;
            if (offX > maxOffX) maxOffX = offX;
            if (offY > maxOffY) maxOffY = offY;
        }
        return new Vector2(maxOffX, maxOffY);
    }

    public Vector2 averageDimension() {
        float combinedWidth = 0;
        float combinedHeight = 0;
        for (TextureRegion text : getKeyFrames()) {
            combinedWidth  += text.getRegionWidth();
            combinedHeight += text.getRegionHeight();
        }
        return new Vector2(combinedWidth / (getSize() + 1), combinedHeight / (getSize() + 1));
    }

    public int getSize() {
        return getKeyFrames().length;
    }

    public float offX(float scale) {
        return ((TextureAtlas.AtlasRegion) region).offsetX * scale;
    }

    public float offY(float scale) {
        return ((TextureAtlas.AtlasRegion) region).offsetY * scale;
    }

    public float scaledWidth(float scale) {
        return region.getRegionWidth() * scale;
    }

    public float scaledHeight(float scale) {
        return region.getRegionHeight() * scale;
    }
}

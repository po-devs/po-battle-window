package com.mygdx.game.battlewindow;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class AnimatedActor extends Actor {
    SpriteAnimation sprite;
    boolean side;
    float originalScale;

    public AnimatedActor(boolean side) {
        this.side = side;
    }

    public void setSprite(SpriteAnimation sprite) {
        this.sprite = sprite;
        fit();
    }

    Color batchColor;
    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (isVisible()) {
            batchColor = batch.getColor();
            batch.setColor(getColor());
            if (hasActions()) {
                batch.draw(sprite.getRegion(),
                        getX() + sprite.offX(originalScale) + (sprite.scaledWidth(originalScale - getScaleX()) / 2f),
                        getY() + sprite.offY(originalScale),
                        sprite.scaledWidth(getScaleX()),
                        sprite.scaledHeight(getScaleY()));
            } else {
                batch.draw(sprite.getRegion(),
                        getX() + sprite.offX(getScaleX()),
                        getY() + sprite.offY(getScaleY()),
                        sprite.scaledWidth(getScaleX()),
                        sprite.scaledHeight(getScaleY()));
            }
            batch.setColor(batchColor);
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        sprite.update();
    }

    public void fit() {
        Rectangle rect;
        // Bottom left or top right?
        if (side) rect = Rectangle.tmp;
        else      rect = Rectangle.tmp2;

        float width  = sprite.getRegion().getRegionWidth();
        float height = sprite.getRegion().getRegionHeight();

        float scale;

        if (width > height) {
            // Width is bigger
            scale = rect.width / width;
        } else {
            // Height is bigger
            scale = rect.height / height;
        }

        // Too much scaling looks bad
        if (side) {if (scale > 2.0f) scale = 2.0f;}
        else      {if (scale > 1.7f) scale = 1.7f;}

        width  = width * scale;
        height = height * scale;

        // For Animations that move wildly
        Vector2 maxOffset = sprite.maxOff(scale);

        // To center the image
        float differenceX = rect.width - width;
        float differenceY = rect.height - height;

        float x = rect.x + (differenceX - maxOffset.x) / 2f;
        float y = rect.y + (differenceY - maxOffset.y) / 2f;

        setWidth(width);
        setHeight(height);
        setScale(scale);
        setX(x);
        setY(y);

        originalScale = scale;
    }

    public boolean paused() {
        return sprite.paused;
    }

    public void pause() {
        sprite.paused = true;
    }

    public void unpause() {
        sprite.paused = false;
    }

    public void pause(boolean paused) {
        sprite.paused = paused;
    }

    public boolean loaded() {
        return sprite != null;
    }
}

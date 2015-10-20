package com.mygdx.game.battlewindow;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;

public class FadeToAction extends TemporalAction {
    private float startAlpha;
    private float endAlpha;
    private Color color;

    @Override
    protected void begin() {
        startAlpha = target.getColor().a;
    }

    @Override
    protected void update(float percent) {
        float alpha = startAlpha + (endAlpha - startAlpha) * percent;
        color = target.getColor();
        color.a = alpha;
        target.setColor(color);
    }

    public void setAlpha(float alpha) {
        endAlpha = alpha;
    }
}

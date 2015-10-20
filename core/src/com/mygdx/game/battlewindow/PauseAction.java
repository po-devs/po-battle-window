package com.mygdx.game.battlewindow;

import com.badlogic.gdx.scenes.scene2d.Action;

public class PauseAction extends Action {
    private boolean paused;

    public boolean act (float delta) {
        ((AnimatedActor)target).pause(paused);

        return true;
    }

    public void setPaused (boolean paused) {
        this.paused = paused;
    }
}

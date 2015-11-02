package com.mygdx.game.battlewindow;

public abstract class Timer {
    float remaining;

    public Timer(float duration) {
        this.remaining = duration;
    }

    public void act(float delta) {
        remaining = remaining - delta;
        if (isCompleted()) {
            run();
        }
    }

    public boolean isCompleted() {
        return remaining <= 0;
    }

    public abstract void run();
}

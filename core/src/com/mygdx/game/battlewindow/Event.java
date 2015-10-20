package com.mygdx.game.battlewindow;

import com.badlogic.gdx.utils.Timer;

public abstract class Event {
    public EventListener listener;

    public void run(ContinuousGameFrame frame) {
        launch(frame);

        if (duration() == 0) {
            finish();
        } else if (duration() > 0) {
            Timer.instance().scheduleTask(new Timer.Task() {
                @Override
                public void run() {
                    finish();
                }
            }, duration()/1000f);
        } else {
            //Duration of -1, undetermined duration, subclass needs to call finish()
        }
    }

    public void finish() {
        if (listener != null) {
            listener.onEventFinished();
        }
    }

    public abstract void launch(ContinuousGameFrame frame);
    public abstract int duration();

    public interface EventListener {
        void onEventFinished();
    }
}

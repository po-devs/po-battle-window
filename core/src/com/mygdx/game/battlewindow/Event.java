package com.mygdx.game.battlewindow;

public abstract class Event {
    public EventListener listener;

    public void run(final ContinuousGameFrame frame) {
        launch(frame);

        if (duration() == 0) {
            finish();
        } else if (duration() > 0) {
            Timer timer = new Timer(duration() / 1000f) {
                @Override
                public void run() {
                    frame.removeTimer(this);
                    finish();
                }
            };
            frame.addTimer(timer);
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

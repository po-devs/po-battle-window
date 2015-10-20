package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.mygdx.game.battlewindow.ContinuousGameFrame;
import com.mygdx.game.battlewindow.Event;
import com.mygdx.game.battlewindow.TaskService;

import java.util.ArrayList;

public abstract class Bridge implements Event.EventListener {
    protected ContinuousGameFrame game;

    public ArrayList<Event> eventQueue = new ArrayList<Event>();

    public abstract void pause();
    public abstract void unpause();
    public abstract void finished();
    public abstract TaskService setGame(ContinuousGameFrame game);
    public abstract void alert(String message);
    public abstract TextureAtlas getAtlas(String path);
    public abstract Texture getTexture(String path);
    public abstract BitmapFont getFont(String path);
    public abstract void log(String text);
    public abstract boolean isDebug();

    synchronized public void addEvent(Event event) {
        //Logger.println("Adding event " + event.getClass().getName());
        if (eventQueue.isEmpty()) {
            pause();
        }
        eventQueue.add(event);
        event.listener = this;
        if (eventQueue.size() == 1) {
            processEvent(event);
        }
    }

    /* Process event immediately */
    synchronized public void processEvent(Event event) {
        //Logger.println("offering event" + event.getClass().getName());
        game.service.offer(event);
    }

    @Override
    synchronized public void onEventFinished() {
        eventQueue.remove(0);
        if (eventQueue.size() > 0) {
            game.service.offer(eventQueue.get(0));
        } else {
            unpause();
        }
    }
}

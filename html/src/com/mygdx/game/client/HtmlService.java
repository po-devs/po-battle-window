package com.mygdx.game.client;

import com.mygdx.game.battlewindow.Event;
import com.mygdx.game.battlewindow.TaskService;

import java.util.LinkedList;

public class HtmlService implements TaskService {
    private LinkedList<Event> tasks;
    private boolean busy;

    public HtmlService() {
        this.tasks = new LinkedList<Event>();
    }

    @Override
    public Event take() {
        if (busy) {
            return null;
        }
        return tasks.poll();
    }

    @Override
    public void offer(Event e) {
        busy = true;
        tasks.add(e);
        busy = false;
    }
}

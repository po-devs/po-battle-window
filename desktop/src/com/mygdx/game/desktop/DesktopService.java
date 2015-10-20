package com.mygdx.game.desktop;

import com.mygdx.game.battlewindow.Event;
import com.mygdx.game.battlewindow.TaskService;

import java.util.concurrent.ConcurrentLinkedQueue;

public class DesktopService implements TaskService {
    private ConcurrentLinkedQueue<Event> tasks;
    private boolean busy;

    public DesktopService() {
        this.tasks = new ConcurrentLinkedQueue<Event>();
    }

    public Event take() {
        while (busy) {
            try {
                Thread.sleep(10);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Event task = tasks.poll();
        return task;
    }

    public void offer(Event event) {
        busy = true;
        tasks.add(event);
        busy = false;
    }
}

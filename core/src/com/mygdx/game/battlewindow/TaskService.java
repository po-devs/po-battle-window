package com.mygdx.game.battlewindow;

public interface TaskService {
    public Event take();
    public void offer(Event e);
}

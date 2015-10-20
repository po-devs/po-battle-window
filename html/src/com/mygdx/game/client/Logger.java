package com.mygdx.game.client;

public class Logger {
    public static native void println(String line) /*-{
        console.log(line);
    }-*/;
}

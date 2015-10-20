package com.mygdx.game.client;

public interface DownloaderListener {
    void finished();
    void failure(String url);
}

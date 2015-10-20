package com.mygdx.game.client;


import com.mygdx.game.Bridge;
import com.mygdx.game.battlewindow.ContinuousGameFrame;
import com.mygdx.game.battlewindow.Event;
import com.mygdx.game.battlewindow.Events;

public class HtmlEvents {
    public static class DelayedEvent extends Event implements DownloaderListener, Event.EventListener {
        /* If an asset doesn't exist the client need to download from the server and when finished fire the event that requires the resource */
        private Event event;
        private Bridge bridge;
        // to handle Paired downloads
        private int queueSize = 0;
        private int type;

        public DelayedEvent(Event event, Bridge bridge, int type) {
            this.event = event;
            this.bridge = bridge;
            this.type = type;
        }

        public int duration() {
            return -1;
        }

        @Override
        public void launch(ContinuousGameFrame Frame) {
            switch (type) {
                case 0: {
                    break;
                }
                case 1: {
                    // Pokemon Sprite and Atlas;
                    queueSize = 2;
                    Events.SpriteChange sendevent = (Events.SpriteChange) this.event;
                    //Logger.println("Starting QueueEvent for atlas " + sendevent.getPath());
                    MyAssetChecker.checkText(sendevent.getPath() + ".atlas", this);
                    //Logger.println("Starting QueueEvent for image " + sendevent.getPath());
                    MyAssetChecker.checkImage(sendevent.getPath() + ".png", this);
                    break;
                }
                case 2: {
                    // Background Image
                    queueSize = 1;
                    Events.BackgroundChange backevent = (Events.BackgroundChange) this.event;
                    //Logger.println("Starting QueueEvent for background " + backevent.getId());
                    MyAssetChecker.checkImage("background/" + Integer.toString(backevent.getId()) + ".png", this);
                    break;
                }
                default: {
                    Logger.println("Unhandled Queue Event");
                }
            }
        }

        @Override
        public synchronized void finished() {
            queueSize--;
            //Logger.println("Queue remaining " + queueSize + " type: " + type);
            if (queueSize == 0) {
                event.listener = this;
                bridge.processEvent(event);
            }
        }

        @Override
        public void onEventFinished() {
            finish();
        }

        @Override
        public void failure(String url) {
            if (url.contains(".png")) {
                switch (type) {
                    case 1: {
                        // Could we revert the forme?
                        Events.SpriteChange sendevent = (Events.SpriteChange) this.event;
                        String path = sendevent.getPath();
                        if (path.contains("_")) {
                            // It is a forme
                            sendevent.setPath(path.substring(0, path.indexOf("_")));
                            // Try again without forme;
                            MyAssetChecker.checkText(sendevent.getPath() + ".atlas", this);
                            MyAssetChecker.checkImage(sendevent.getPath() + ".png", this);
                        } else {
                            Logger.println("404 Error " + path);
                        }
                        break;
                    }
                    default: {
                        Logger.println("404 Error");
                        break;
                    }
                }
            }
        }
    }

    public static class AnimatedHPEvent extends Event {
        private byte change;
        private int spot;
        private float duration;

        public AnimatedHPEvent(byte change, int spot, float duration) {
            this.change = change;
            this.spot = spot;
            this.duration = duration;
        }

        public int duration() {
            return (int) duration;
        }

        @Override
        public void launch(ContinuousGameFrame Frame) {
            //bridge.pause();
            Frame.getHUD(spot).setChangeHP(change, duration / 1000f);
        }
    }
}

package com.mygdx.game.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.badlogic.gdx.backends.gwt.preloader.Preloader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.mygdx.game.Bridge;
import com.mygdx.game.battlewindow.*;

public class HtmlLauncher extends GwtApplication {

    public GwtApplicationConfiguration getConfig () {
        GwtApplicationConfiguration config = new GwtApplicationConfiguration(600, 360);
        config.preferFlash = false;
        config.alpha = true;
        //Assets.init(getPreloaderBaseURL());
        return config;
    }

    @Override
    public ApplicationListener getApplicationListener () {
        HtmlBridge bridge = new HtmlBridge();
        return new ContinuousGameFrame(bridge, bridge.getMe() == 1);
    }


    public static class HtmlBridge extends Bridge {
        byte me, opp;
        boolean isBattle;

        public HtmlBridge() {
            me = (byte) getMe();
            opp = (byte) getOpp();

            Logger.println("Created bridge with me " + (int)me + " and opp " + (int)opp);
        }

        @Override
        public TextureAtlas getAtlas(String path) {
            return Assets.getAtlas(path);
        }

        @Override
        public Texture getTexture(String path) {
            return Assets.getTexture(path);
        }

        @Override
        public BitmapFont getFont(String path) {
            return Assets.getFont(path);
        }

        @Override
        public TaskService setGame(ContinuousGameFrame game) {
            this.game = game;
            try {
                setCallBacks();
            } catch (Exception e) {
                Window.alert("can't find battle");
            }
            return new HtmlService();
        }

        @Override
        public void log(String text) {
            Logger.println(text);
        }

        @Override
        public void pause() {
            pausebattle();
        }

        @Override
        public void unpause() {
            unpausebattle();
        }

        @Override
        public native boolean isDebug() /*-{
            return $wnd.battle.debug || false;
        }-*/;

        private native void pausebattle() /*-{
            console.log("html pause");
            $wnd.battle.pause();
        }-*/;

        private native void unpausebattle() /*-{
            console.log("html unpause");
            $wnd.battle.unpause();
        }-*/;

        private native int getBackgroundNum() /*-{
            return $wnd.battle.data.background || 0;
        }-*/;

        @Override
        public void alert(String message) {
            //Window.alert("Alert from: " + side);
            if (message.equals("true")) {
                //game.HUDs[0].updatePoke(JavaScriptPokemon.fromJS(getPoke(0, 1)));
                Window.alert(getPoke(this.me));
            } else if (message.equals("false")){
                //game.HUDs[1].updatePoke(JavaScriptPokemon.fromJS(getPoke(1, 1)));
                Window.alert(getPoke(this.opp));
                //addEvent(new Events.LogEvent(getBattle()));
            } else {
                //Window.alert(message);
            }
        }

        @Override
        public void onHover(int spot) {
            _onHover(spot);
        }

        private native void _onHover(int spot) /*-{
            $wnd.battle.trigger("battle-hover", spot);
        }-*/;

        @Override
        public void finished() {
            Logger.println("finished");
            HtmlEvents.DelayedEvent event = new HtmlEvents.DelayedEvent(new Events.BackgroundChange(getBackgroundNum()), this, 2);
            addEvent(event);

            Logger.println("added event for background");
            isBattle = getIsBattle();
            Logger.println("Is battle? " + isBattle);
            //Logger.println("My Pokemon " + getPoke(this.me));
            //Logger.println("Opp Pokemon " + getPoke(this.opp));

            String meStr = getPoke(this.me);
            Logger.println("me str " + meStr);
            JavaScriptPokemon me = JavaScriptPokemon.fromJS(meStr);
            Logger.println("got poke " + me);
            if (isBattle) {
                game.getHUD(this.me).updatePokeNonSpectating(me);
            } else {
                game.getHUD(this.me).updatePoke(me);
            }
            Logger.println("updated HUD");
            if (me.num() > 0) {
                Logger.println("Adding sprite change event");
                HtmlEvents.DelayedEvent eventme = new HtmlEvents.DelayedEvent(new Events.SpriteChange(me, this.me, true), this, 1);
                addEvent(eventme);
            }

            Logger.println("getting opp");
            JavaScriptPokemon opp = JavaScriptPokemon.fromJS(getPoke(this.opp));
            Logger.println("got opp " + opp);
            game.getHUD(this.opp).updatePoke(opp);
            Logger.println("updated opp hud");
            if (opp.num() > 0) {
                Logger.println("Adding sprite change event");
                HtmlEvents.DelayedEvent eventopp = new HtmlEvents.DelayedEvent(new Events.SpriteChange(opp, this.opp, false), this, 1);
                addEvent(eventopp);
            }

            Logger.println("finished finished");
        }

        /*
        public void newEvent(int args) {
            String s = "";
            if (args == 1) {
                s = "Send Out";
            }
            if (args == 2) {
                s = "Send Back";
            }
            //Event event = new Events.LogEvent(s);
            //addEvent(event);
        }
        */

        public void dealWithSendOut(int spot) {
            Logger.println("html sendout");
            JavaScriptPokemon poke = JavaScriptPokemon.fromJS(getPoke(spot));
            boolean myself = player(spot) == me;
            Event event;
            if (myself && isBattle) {
                event = new Events.HUDChangeBattling(poke, spot);
            } else {
                event = new Events.HUDChange(poke, spot);
            }
            addEvent(event);
            HtmlEvents.DelayedEvent event1 = new HtmlEvents.DelayedEvent(new Events.SpriteChange(poke, spot, myself), this, 1);
            addEvent(event1);
            addEvent(new Events.SendOut(spot, poke));
        }

        public void dealWithSpriteChange(int spot) {
            Logger.println("html spritechange");
            JavaScriptPokemon poke = JavaScriptPokemon.fromJS(getPoke(spot));
            boolean myself = player(spot) == me;

            HtmlEvents.DelayedEvent event1 = new HtmlEvents.DelayedEvent(new Events.SpriteChange(poke, spot, myself), this, 1);
            addEvent(event1);
        }

        public void dealWithSendBack(int spot) {
            Logger.println("html sendback");
            Event event = new Events.SendBack(spot);
            addEvent(event);
        }

        public void dealWithStatusChange(int spot, int status) {
            Logger.println("html statuschange");
            Event event = new Events.StatusChange(spot, status);
            addEvent(event);
        }

        public void dealWithHpChange(int spot, int change) {
            Logger.println("html hpchange");

            int duration = change;
            duration = Math.min(100, Math.abs(duration));

            /* Delay varying between 700ms and 1300 ms depending on how much hp */
            duration = 700 + duration*6;

            boolean side = player(spot) == me;
            if (side && isBattle) {
                dealWithHpChangeBattling(spot, duration);
            } else {
                Event event = new HtmlEvents.AnimatedHPEvent((byte) change, spot, duration);
                addEvent(event);
            }
        }

        private void dealWithHpChangeBattling(int spot, int duration) {
            JavaScriptPokemon my = JavaScriptPokemon.fromJS(getPoke(spot));
            Logger.println("new percent " + my.percent() + "  " + my.life());
            Event event = new Events.SetHPBattlingAnimated(spot, my.percent(), my.life(), duration/1000f);
            addEvent(event);
        }

        public void dealWithKo(int spot) {
            Logger.println("html KO");
            Event event = new Events.KO(spot);
            addEvent(event);
        }

        public void dealWithVanish(int spot) {
            addEvent(new Events.Visibility(spot, false));
        }

        public void dealWithReappear(int spot) {
            addEvent(new Events.Visibility(spot, true));
        }

        public void dealWithPlayWeather(int type) {
            Logger.println("DealWithWeather");
            if (type != WeatherAnimation.getType()) {
                addEvent(HtmlEvents.recursiveDelayedEvent(new Events.Weather(type), this, 3, 4));
            } else {
                // Already loaded no need to do a download check
                addEvent(new Events.Weather(type));
            }
        }
        public void dealWithDurationMultiplier(float multiplier) {
            /* make this instant! */
            processEvent(new Events.DurationMultiplier(multiplier));
        }

        private native void setCallBacks() /*-{
        console.log("setting callbacks");
        var that = this;
        $wnd.battle.on("sendout", function(spot) {
            that.@com.mygdx.game.client.HtmlLauncher.HtmlBridge::dealWithSendOut(I)(spot);
        });
        $wnd.battle.on("sendback", function(spot) {
            that.@com.mygdx.game.client.HtmlLauncher.HtmlBridge::dealWithSendBack(I)(spot);
        });
        $wnd.battle.on("ko", function(spot) {
            that.@com.mygdx.game.client.HtmlLauncher.HtmlBridge::dealWithKo(I)(spot);
        });
        $wnd.battle.on("statuschange", function(spot, status) {
            that.@com.mygdx.game.client.HtmlLauncher.HtmlBridge::dealWithStatusChange(II)(spot, status);
        });
        $wnd.battle.on("hpchange", function(spot, change) {
            that.@com.mygdx.game.client.HtmlLauncher.HtmlBridge::dealWithHpChange(II)(spot, change);
        });
        $wnd.battle.on("spritechange", function(spot) {
            that.@com.mygdx.game.client.HtmlLauncher.HtmlBridge::dealWithSpriteChange(I)(spot);
        });
        $wnd.battle.on("vanish", function(spot) {
            that.@com.mygdx.game.client.HtmlLauncher.HtmlBridge::dealWithVanish(I)(spot);
        });
        $wnd.battle.on("reappear", function(spot) {
            that.@com.mygdx.game.client.HtmlLauncher.HtmlBridge::dealWithReappear(I)(spot);
        });
        $wnd.battle.on("weather", function(type) {
            that.@com.mygdx.game.client.HtmlLauncher.HtmlBridge::dealWithPlayWeather(I)(type);
        });
        $wnd.battle.on("duration-multiplier", function(multiplier) {
            that.@com.mygdx.game.client.HtmlLauncher.HtmlBridge::dealWithDurationMultiplier(F)(multiplier);
        });
        console.log("callbacks sets");
        }-*/;

        private static native int battleId() /*-{
        return $wnd.battleId;
        }-*/;

        private static native String getPoke(int spot) /*-{
        var poke = $wnd.battle.pokes[spot];
            return JSON.stringify(poke || {});
        }-*/;

        private static int player(int spot){
            return spot%2;
        }

        private static int slot(int spot){
            return spot/2;
        }

        private static native String getBattle() /*-{
            var battle = $wnd.battle;
            return JSON.stringify(battle);
        }-*/;

        private static native String getTeam(int side) /*-{
            var team = $wnd.battle.teams[side];
            return JSON.stringify(team);
        }-*/;

        private static native int getMe() /*-{
            var me = $wnd.battle.myself;
            return me;
        }-*/;

        private static native int getOpp() /*-{
            var opp = $wnd.battle.opponent;
            return opp;
        }-*/;

        private static native boolean getIsBattle() /*-{
            var isBattle = $wnd.battle.isBattle();
            return isBattle;
        }-*/;
    }


    @Override
    public Preloader.PreloaderCallback getPreloaderCallback() {
        return new Preloader.PreloaderCallback() {
            @Override
            public void update(Preloader.PreloaderState state) {
                // like update(stateTime) but update(stat.getProgess())
            }

            @Override
            public void error(String file) {
                System.out.println("error: " + file);
            }
        };
    }



    /*
    public LoadingListener getLoadingListener() {
        return new LoadingListener() {
            @Override
            public void beforeSetup() {
                // Do something!
            }

            @Override
            public void afterSetup() {
                // Do something!
            }
        };
    }
    */


    @Override
    public String getPreloaderBaseURL() {
        return GWT.getHostPageBaseURL() + "public/battle/";
    }
}

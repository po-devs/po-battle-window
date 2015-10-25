package com.mygdx.game.battlewindow;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.actions.VisibleAction;
import com.mygdx.game.JSONPoke;

public class Events {
    private Events() {
    }

    public static class SetHPBattlingAnimated extends Event {
        byte percent;
        short HP;
        float duration;
        int spot;

        public SetHPBattlingAnimated(int spot, byte percent, short HP, float duration) {
            this.spot = spot;
            this.percent = percent;
            this.HP = HP;
            this.duration = duration;
        }

        public int duration() {
            return (int) (duration * 1000);
        }

        @Override
        public void launch(ContinuousGameFrame Frame) {
            Frame.getHUD(spot).setHPBattling(percent, HP, duration);
            //          Log.e("Event", "SetHPBattlingAnimated " +  log + " to " + Thread.currentThread().getName() + " took: " + time);
        }
    }

    public static class HUDChangeBattling extends InstantEvent {
        JSONPoke poke;
        int spot;

        public HUDChangeBattling(JSONPoke poke, int spot) {
            this.poke = poke;
            this.spot = spot;
        }

        @Override
        public void launch(ContinuousGameFrame Frame) {
            Frame.getHUD(spot).updatePokeNonSpectating(poke);
            if (poke.status() == 31) {
                //Frame.sprites[0].paused = true;
            }
            //Log.e("Event", log + " to " + Thread.currentThread().getName() + " took: " + time);
        }
    }

    public static class HUDChange extends InstantEvent {
        JSONPoke poke;
        int spot;


        public HUDChange(JSONPoke poke, int spot) {
            this.poke = poke;
            this.spot = spot;
        }

        @Override
        public void launch(ContinuousGameFrame Frame) {
            Frame.getHUD(spot).updatePoke(poke);
            //if (poke.statusColor() == 31) {
            //    Frame.sprites[(side ? 0 : 1)].paused = true;
            //}
            //Log.e("Event", log + " to " + Thread.currentThread().getName() + " took: " + time);
        }
    }

    public static class SpriteChange extends InstantEvent {
        JSONPoke poke;
        int spot;
        String path;

        public SpriteChange(JSONPoke poke, int spot, boolean back) {
            this.poke = poke;
            this.spot = spot;
            this.path = (back ? "back/" : "front/") + Short.toString(poke.num());
            if (poke.forme() > 0) {
                this.path = this.path + "_" + Byte.toString(poke.forme());
            }
        }

        @Override
        public void launch(ContinuousGameFrame Frame) {
//            Log.e("Event", "SpriteChange " + log + " to " + Thread.currentThread().getName() + " took: " + time);
            Frame.updateSprite(spot, path, false);
            handleStatus(spot, poke.status(), Frame);
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }

    private static void handleStatus(int spot, int status, ContinuousGameFrame Frame) {
        if (status >= 0 && status <= 6) {
            Frame.sprites[spot].setColor(new Color(Static.statusColor[status]));
            Frame.sprites[spot].pause(Static.statusPause[status]);
            Frame.sprites[spot].sprite.setFrameDuration(Static.statusFrameRate[status]);
        } else if (status == 31) {
            Frame.sprites[spot].pause();
        }
    }

    public static class StatusChange extends InstantEvent {
        int spot;
        int status;

        public StatusChange(int spot, int status) {
            this.spot = spot;
            this.status = status;
        }

        @Override
        public void launch(ContinuousGameFrame Frame) {
            Frame.getHUD(spot).updateStatus(status);
            handleStatus(spot, status, Frame);
        }
    }

    public static class BackgroundChange extends InstantEvent {
        int id;

        public BackgroundChange(int id) {
            this.id = id;
        }

        @Override
        public void launch(ContinuousGameFrame Frame) {
            Frame.setBackground(id);
        }

        public int getId() {
            return id;
        }
    }

    public static class KO extends Event {
        int spot;

        public KO(int spot) {
            this.spot = spot;
        }

        public int duration() {
            return 800;
        }

        @Override
        public void launch(ContinuousGameFrame Frame) {
            //Frame.sprites[(side ? 0 : 1)].paused = true;
            //Frame.sprites[(side ? 0 : 1)].startFade();

            FadeToAction fade = new FadeToAction();
            fade.setDuration(0.8f);
            fade.setAlpha(0f);

            VisibleAction visible  = new VisibleAction();
            visible.setVisible(false);

            Frame.getSprite(spot).pause();
            Frame.getSprite(spot).addAction(Actions.sequence(fade, visible));
            Frame.getHUD(spot).updateStatus(31); // 31 = Koed
        }
    }

    public static class SendOut extends Event {
        int spot;
        JSONPoke poke;

        public SendOut(int spot, JSONPoke poke) {
            this.poke = poke;
            this.spot = spot;
        }

        public int duration() {
            return 600;
        }

        @Override
        public void launch(ContinuousGameFrame Frame) {
            handleStatus(spot, poke.status(), Frame);

            ScaleToAction action = new ScaleToAction();
            action.setScale(Frame.getSprite(spot).originalScale);
            action.setInterpolation(Interpolation.pow2Out);
            action.setDuration(0.4f);

            FadeToAction fade = new FadeToAction();
            fade.setDuration(.2f);
            fade.setAlpha(1f);

            VisibleAction visible  = new VisibleAction();
            visible.setVisible(true);

            Frame.getSprite(spot).addAction(Actions.sequence(visible, fade, action));
        }
    }

    public static class SendBack extends Event {
        int spot;

        public SendBack(int spot) {
            this.spot = spot;
        }

        public int duration() {
            return 600;
        }

        @Override
        public void launch(ContinuousGameFrame Frame) {
            //Frame.sprites[(side ? 0 : 1)].paused = true;
            //Frame.sprites[(side ? 0 : 1)].startScale();
            ScaleToAction action = new ScaleToAction();
            action.setScale(0.5f * Frame.getSprite(spot).originalScale);
            action.setInterpolation(Interpolation.pow2In);
            action.setDuration(.4f);

            FadeToAction fade = new FadeToAction();
            fade.setDuration(.2f);
            fade.setAlpha(0f);

            VisibleAction visible  = new VisibleAction();
            visible.setVisible(false);

            Frame.getSprite(spot).pause();
            Frame.getSprite(spot).addAction(Actions.sequence(action,fade,visible));
        }
    }

    public static class Visibility extends InstantEvent {
        int spot;
        boolean visible;

        public Visibility(int spot, boolean visible) {
            this.spot = spot;
            this.visible = visible;
        }
        @Override
        public void launch(ContinuousGameFrame frame) {
            VisibleAction action = new VisibleAction();
            action.setVisible(this.visible);

            frame.getSprite(spot).addAction(action);
        }
    }

    public static class Weather extends Event {
        int type = 0;
        WeatherAnimation weather;

        public Weather(int type) {
            this.type = type;
        }

        public void process() {
            weather = new WeatherAnimation(Gdx.files.internal(getPath()));
        }

        @Override
        public void launch(ContinuousGameFrame frame) {
            if (WeatherAnimation.getType() != type) {
                frame.weather = this.weather;
                frame.weather.load();
                WeatherAnimation.setType(type);
            }
            frame.weather.start();
            this.weather = frame.weather;
        }

        @Override
        public int duration() {
            return 2000;
        }

        public String getPath() {
            return "weather/" + type + ".txt";
        }

        @Override
        public void finish() {
            super.finish();
            weather.finish();
        }

        public WeatherAnimation getWeather() {
            return weather;
        }
    }
 }

package com.mygdx.game.battlewindow;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.JSONPoke;

public class BattleInfoHUD {

    private boolean me;
    private boolean statusBool = false;
    private boolean displayRealHealth;
    private boolean holdingItem = false;

    private TextureAtlas atlas;
    private TextureRegion mainHUD;
    private TextureRegion bar;
    private BitmapFont font;
    private TextureRegion battleStatus;
    private TextureRegion holdItem;

    private HealthAction healthAction = null;

    private byte lifePercent = 100;
    private byte lastHealth = 100;
    private byte level = 0;

    private int bar_color = 1;

    private short realHealth;
    private short totalHeath;
    private short lastRealHealth = 0;

    private int statusInt = 0;

    private float scale;

    private float xHUD;
    private float yHUD;
    private float widthHUD;
    private float heightHUD;

    private float xBar;
    private float yBar;
    private float widthBar;
    private float heightBar;

    private float xLevel;
    private float yLevel;

    private float xName;
    private float yName;

    private float xStatus;
    private float yStatus;
    private float widthStatus;
    private float heightStatus;

    private float xCurrentHealth;
    private float yCurrentHealth;
    private float xTotalHealth;
    private float yTotalHealth;

    private String name = "";
    private String realHealthString = "";
    private String totalHealthString = "";

    public BattleInfoHUD(TextureAtlas atlas, boolean me, float scale, BitmapFont font) {
        this.me = me;
        this.atlas = atlas;
        this.scale = scale;
        this.font = font;
        healthAction = new HealthAction();
        setup();
        updateHealthBar();
    }

    public void updatePoke(JSONPoke poke) {
        //updateName(poke.rnick, poke.gender);
        updateName(poke.name(), poke.gender());
        level = poke.level();
        updateStatus(poke.status());
        //Log.e("HUD", poke.toString() + " HP to " + poke.lifePercent);
        setHPNonAnimated(poke.percent());
        lastHealth = lifePercent;
    }

    public void updatePokeNonSpectating(JSONPoke poke) {
        updateName(poke.name(), (byte) poke.gender());
        totalHeath = poke.totallife();
        totalHealthString = FontShifter.parseBlackNumber(totalHeath);
        level = poke.level();
        lifePercent = (byte) ((poke.life() * 100) / (poke.totallife()));
        lastHealth = lifePercent;
        updateStatus(poke.status());
        if (!displayRealHealth) {
            // 88/3
            xTotalHealth = Math.round(xHUD + (88 * scale * 1.7f));
            yTotalHealth = (yHUD + (1 * scale * 1.7f));

            // Each digit is 8 pixels wide except 1
            // rightmost 79/3
            yCurrentHealth = yTotalHealth;
            realHealth = 0;
            totalHeath = 0;

            displayRealHealth = true;
        }
        setHPNonAnimated(lifePercent);
        updateRealHealth(poke.life());
    }


    private void updateName(String pokeName, byte gender) {
        if (!displayRealHealth) {
            name = pokeName + (gender > 0 ? (gender == (byte) 1 ? (char) 11 : (char) 12) : "");
        } else {
            name = pokeName + (gender > 0 ? (gender == (byte) 1 ? (char) 11 : (char) 12) : "");
        }
    }


    public void updateStatus(int status) {
        if (statusInt != status) {
            statusInt = status;
            if (statusInt > 0 && statusInt < 7) {
                battleStatus = atlas.findRegion("battle_status" + statusInt);
                statusBool = true;
            } else if (statusInt == 31) {
                battleStatus = atlas.findRegion("battle_status31");
                statusBool = true;
            } else {
                statusBool = false;
            }
        }
    }

    public void setChangeHP(byte change, float duration) {
        int newpercent = lifePercent - change;
        if (newpercent > 100)   newpercent = 100; else
        if (newpercent < 0)     newpercent = 0;
        healthAction.newAction((byte) (newpercent), duration);
    }

    public void setHPBattling(byte newPercent, short newHP, float duration) {
        healthAction.newAction(newPercent, newHP, duration);
    }

    public void setHPNonAnimated(byte newPercent) {
        lifePercent = newPercent;
        if (lifePercent > 50) {
            bar_color = 1;
            bar = atlas.findRegion("green_bar");
        } else if (lifePercent <= 50 && lifePercent > 20) {
            bar_color = 2;
            bar = atlas.findRegion("yellow_bar");
        } else if (lifePercent <= 20 && lifePercent > 0) {
            bar_color = 3;
            bar = atlas.findRegion("red_bar");
        } else if (lifePercent == 0) {
            bar_color = 4;
            bar = atlas.findRegion("white_bar");
        }
        widthBar = (lifePercent / 100f) * 48f * scale * (me ? 1.7f : 1.5f);
    }

    public void act(float delta) {
        healthAction.update(delta);
    }

    private void setup() {
        // 400/240 1.5/1.5
        // Me 120x21
        // Opp 124/16
        bar = atlas.findRegion("green_bar");
        battleStatus = atlas.findRegion("battle_status6");
        holdItem = atlas.findRegion("hold_item");
        float mainWidth = scale * 400;
        float mainHeight = scale * 240;
        updateHealthBar();
        if (me) {
            mainHUD = atlas.findRegion("me_HUD");
            mainHUD.getTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
            widthHUD = Math.round(mainHUD.getRegionWidth() * scale * 1.7f);
            heightHUD = Math.round(mainHUD.getRegionHeight() * scale * 1.7f);

            xHUD = Math.round(mainWidth - widthHUD - 10 * scale);
            yHUD = Math.round(10 * scale);

            //widthBar = Math.round(48*scale*1.7f);
            heightBar = Math.round(2 * scale * 1.7f);

            xBar = Math.round(xHUD + (56 * scale * 1.7f));
            yBar = Math.round(yHUD + (10 * scale * 1.7f));

            xLevel = Math.round(xHUD + (89 * scale * 1.7f));
            yLevel = Math.round(yHUD + (14 * scale * 1.7f));

            xName = Math.round(xHUD + (14 * scale * 1.7f));
            yName = Math.round(yHUD + (14 * scale * 1.7f));

            // 20 wide
            // height 6
            // 38 = x
            // 9 = y
            xStatus = Math.round(xHUD + 18 * scale * 1.7f);
            yStatus = Math.round(yHUD + 8 * scale * 1.7f);

            widthStatus = Math.round(20 * scale * 1.7f);
            heightStatus = Math.round(6 * scale * 1.7f);

        } else {
            mainHUD = atlas.findRegion("opp_HUD");
            widthHUD = Math.round(mainHUD.getRegionWidth() * scale * 1.5f);
            heightHUD = Math.round(mainHUD.getRegionHeight() * scale * 1.5f);

            xHUD = Math.round(10 * scale);
            yHUD = Math.round(mainHeight - heightHUD - 10*scale);

            //widthBar = Math.round(48*scale*1.5f);
            heightBar = Math.round(2 * scale * 1.5f);
            xBar = Math.round(xHUD + (44 * scale * 1.5f));
            yBar = Math.round(yHUD + (5 * scale * 1.5f));

            xLevel = Math.round(xHUD + (93 * scale * 1.5f));
            yLevel = Math.round(yHUD + (9 * scale * 1.5f));

            xName = Math.round(xHUD + (5 * scale * 1.5f));
            yName = Math.round(yHUD + (9 * scale * 1.5f));

            xStatus = Math.round(xHUD + 6 * scale * 1.5f);
            yStatus = Math.round(yHUD + 3 * scale * 1.5f);

            widthStatus = Math.round(20 * scale * 1.5f);
            heightStatus = Math.round(6 * scale * 1.5f);
        }
    }

    /*
    public void draw(float delta, Batch batch) {
        //act(delta);
        batch.draw(mainHUD, xHUD, yHUD, widthHUD, heightHUD);
        batch.draw(bar, xBar, yBar, widthBar, heightBar);
        if (statusBool) batch.draw(battleStatus, xStatus, yStatus, widthStatus, heightStatus);
        if (holdingItem) batch.draw(holdItem, xHUD, yHUD, 6 * scale, 8 * scale);
        if (displayRealHealth) {
            font.draw(batch, realHealthString, xCurrentHealth, yCurrentHealth);
            font.draw(batch, totalHealthString, xTotalHealth, yTotalHealth);
            font.draw(batch, level + "", xLevel, yLevel);
        } else {
            font.draw(batch, level + "", xLevel, yLevel);
        }
        font.draw(batch, name, xName, yName);
    }*/


    public void draw(Batch batch) {
        batch.draw(mainHUD, xHUD, yHUD, widthHUD, heightHUD);
        batch.draw(bar, xBar, yBar, widthBar, heightBar);
        if (statusBool) batch.draw(battleStatus, xStatus, yStatus, widthStatus, heightStatus);
        if (holdingItem) batch.draw(holdItem, xHUD, yHUD, 6 * scale, 8 * scale);
        if (displayRealHealth) {
            font.draw(batch, realHealthString, xCurrentHealth, yCurrentHealth);
            font.draw(batch, totalHealthString, xTotalHealth, yTotalHealth);
            font.draw(batch, level + "", xLevel, yLevel);
        } else {
            font.draw(batch, level + "", xLevel, yLevel);
        }
        font.draw(batch, name, xName, yName);
    }

    /*
    public void updateRealHealth(byte percent, short health) {
        updateRealHealth(health);
        setHPNonAnimated(percent);
    }*/

    public void updateRealHealth(short health) {
        int length = Short.toString(health).length();
        realHealth = health;
        xCurrentHealth = Math.round(xHUD + (79 - 7 * length) * scale * 1.7f);
        realHealthString = FontShifter.parseBlackNumber(realHealth);
        lastRealHealth = realHealth;
    }

    private void updateRealHealthAnimator(short health) {
        int length = Short.toString(health).length();
        realHealth = health;
        xCurrentHealth = Math.round(xHUD + (79 - 7 * length) * scale * 1.7f);
        realHealthString = FontShifter.parseBlackNumber(realHealth);
    }

    public void updateHealthBar() {
        switch (bar_color) {
            case 1: // Green
                if (lifePercent <= 50) {
                    bar = atlas.findRegion("yellow_bar");
                    bar_color = 2;
                }
                widthBar = (lifePercent / 100f) * 48f * scale * (me ? 1.7f : 1.5f);
                break;
            case 2: // Yellow
                if (lifePercent <= 20) {
                    bar = atlas.findRegion("red_bar");
                    bar_color = 3;
                } else if (lifePercent > 50) {
                    bar = atlas.findRegion("green_bar");
                    bar_color = 1;
                }
                widthBar = (lifePercent / 100f) * 48f * scale * (me ? 1.7f : 1.5f);
                break;
            case 3: // Red
                if (lifePercent == 0) {
                    bar = atlas.findRegion("white_bar");
                    bar_color = 4;
                } else if (lifePercent > 20) {
                    bar = atlas.findRegion("red_bar");
                    bar_color = 2;
                }
                widthBar = (lifePercent / 100f) * 48f * scale * (me ? 1.7f : 1.5f);
                break;
            case 4: // White
                break;
        }
    }


    private class HealthAction {
        private byte endPercent;
        private float duration;
        private float remaining;
        private short endHP;
        private boolean running = false;

        public HealthAction() {
        }

        public void newAction(byte endPercent, float duration) {
            this.endPercent = endPercent;
            this.duration = duration;
            remaining = duration;
            if (running) {
                lastHealth = lifePercent;
            }
            running = true;
        }

        public void newAction(byte endPercent, short endHP, float duration) {
            this.endPercent = endPercent;
            this.endHP = endHP;
            this.duration = duration;
            remaining = duration;
            if (running) {
                lastHealth = lifePercent;
            }
            running = true;
        }

        private void update(float delta) {
            if (running) {
                if (displayRealHealth) {
                    if (delta > remaining) {
                        //delta = remaining;
                        running = false;
                        lifePercent = endPercent;
                        updateHealthBar();
                        lastHealth = lifePercent;
                        updateRealHealth(endHP);
                    } else {
                        remaining -= delta;
                        float percent = 1f - remaining / duration;
                        lifePercent = (byte) (lastHealth - (percent * (lastHealth - endPercent)));
                        updateHealthBar();
                        updateRealHealthAnimator((short) (lastRealHealth - (percent * (lastRealHealth - endHP))));
                    }
                } else {
                    if (delta > remaining) {
                        //delta = remaining;
                        running = false;
                        lifePercent = endPercent;
                        updateHealthBar();
                        lastHealth = lifePercent;
                    } else {
                        remaining -= delta;
                        float percent = 1f - remaining / duration;
                        lifePercent = (byte) (lastHealth - (percent * (lastHealth - endPercent)));
                        updateHealthBar();
                    }
                }
            }
        }
    }
}

package com.mygdx.game.battlewindow;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class WeatherAnimation {
    private boolean hasTexture = false;
    private boolean hasFilter = false;
    private boolean running = false;
    private static ParticleEffect effect;
    private Color colorFilter;
    private Texture texture;
    private static int type = 0;
    public String particleEffectPath = "";
    public String particleImagePath = "";
    public String textureImagePath = "";
    private int x = 0;
    private int y = 0;

    @Override
    public String toString() {
        String s = "";
        s += "Paths: " + particleEffectPath + " " + particleImagePath + textureImagePath + "\n";
        if (colorFilter != null) {
            s += colorFilter.toString() + "\n";
        }
        s += "type: " + type + " x:" + x + " y:" + y;
        return s;
    }

    public WeatherAnimation() {}

    public WeatherAnimation(FileHandle pack) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(pack.read()), 64);
        String line;
        int i = 0;
        try {
            while (true) {
                line = reader.readLine();
                if (line == null) break;
                switch (i) {
                    case 0: {
                        particleEffectPath = "weather/" + line;
                        break;
                    }
                    case 1: {
                        particleImagePath = "weather/" + line;
                        break;
                    }
                    case 2: {
                        if (line.equals("false")) {
                            hasTexture = false;
                        } else {
                            textureImagePath = "weather/" + line;
                            hasTexture = true;
                        }
                        break;
                    }
                    case 3: {
                        if (line.equals("false")) {
                            hasFilter = false;
                        } else {
                            int red = Integer.valueOf(line.substring(0, 2), 16);
                            int green = Integer.valueOf(line.substring(2, 4), 16);
                            int blue = Integer.valueOf(line.substring(4, 6), 16);;
                            int alpha = Integer.valueOf(line.substring(6, 8), 16);
                            colorFilter = new Color(red / 255f, green / 255f, blue / 255f, alpha / 255f);
                            hasFilter = true;
                        }
                        break;
                    }
                    case 4: {
                        x = Integer.parseInt(line);
                        break;
                    }
                    case 5: {
                        y = Integer.parseInt(line);
                        break;
                    }
                }
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (effect == null) {
                effect = new ParticleEffect();
            }
        }
    }

    public void load() {
        effect.load(Gdx.files.internal(particleEffectPath), Gdx.files.internal("weather/"));
        effect.setPosition(x, y);
        if (hasTexture) {
            texture = new Texture(Gdx.files.internal(textureImagePath));
        }
    }

    public void start() {
        running = true;
    }

    public void stop() {
        running = false;
    }


    public void draw(Batch batch, float delta) {
        if (running) {
            batch.begin();
            if (hasTexture) {
                batch.draw(texture, 0, 0, 600, 360);
            }
            effect.draw(batch, delta);
            batch.end();
        }
    }

    public void drawFilter(ShapeRenderer renderer) {
        if (running) {
            if (hasFilter) {
                Gdx.gl20.glEnable(GL20.GL_BLEND);
                Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
                renderer.begin(ShapeRenderer.ShapeType.Filled);
                renderer.setColor(colorFilter);
                renderer.rect(0, 0, 600, 360);
                renderer.end();
                Gdx.gl20.glDisable(GL20.GL_BLEND);
            }
        }
    }

    public static void setType(int t) {
        type = t;
    }

    public static int getType() {
        return type;
    }

    public void finish() {
        stop();
    }
}

package com.mygdx.game.battlewindow;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Bridge;

/*
    Core application.

    Uses a bridge (Html, Desktop or Android) to interface with particular platform.

 */
public class ContinuousGameFrame extends ApplicationAdapter implements InputProcessor {
    private static final String TAG = "ContinuousGameFrame";
    public TaskService service;

    /* I'm curious if these can be shared between frames*/
    private static BitmapFont font;
    private static TextureAtlas battleAtlas;

    public SpriteBatch batch;
    private Image background;
    public AnimatedActor[] sprites = new AnimatedActor[2];
    public TextureAtlas[] spriteAtlas = new TextureAtlas[2];
    public BattleInfoHUD[] HUDs = new BattleInfoHUD[2];
    public WeatherAnimation weather = new WeatherAnimation();

    private byte me = 0;
    private byte opp = 1;

    private final static int STATUS_INIT = 0;
    private final static int STATUS_RUNNING = 1;
    private final static int STATUS_PAUSED = 2;
    private int STATUS_CURRENT = STATUS_INIT;

    public static float elapsedTime = 0f;
    private float width;
    private float height;
    private float scaledX;
    private float scaledY;
    private float delta;

    private Bridge bridge;

    public ContinuousGameFrame(Bridge bridge) {
        this.bridge = bridge;
    }

    public ContinuousGameFrame(Bridge bridge, boolean reversed) {
        if (reversed) {
            setPlayers((byte)1, (byte)0);
        }
        this.bridge = bridge;
    }

    @Override
    public void create() {
        objectDebugger = new ShapeRenderer();
        if (bridge != null) {
            service = bridge.setGame(this);

            call();

            calculateGUI();

            //setBackground(backgroundId);

            sprites[me] = new AnimatedActor(true);
            sprites[opp] = new AnimatedActor(false);

            HUDs[me] = new BattleInfoHUD(battleAtlas, true, scaledX, font);
            HUDs[opp] = new BattleInfoHUD(battleAtlas, false, scaledX, font);

            Gdx.input.setInputProcessor(this);
            bridge.finished();
        }
    }

    public void setPlayers(byte me, byte opp) {
        this.me = me;
        this.opp = opp;
    }

    public  AnimatedActor getSprite(int spot) {
        return sprites[spot];
    }

    public  BattleInfoHUD getHUD(int spot) {
        return HUDs[spot];
    }

    protected void call() {
        // Handle in overridden extension
    }

    //private final static int FRAME_TARGET = 1000 / 32;
    //private int sleepTime;

    Event event;

    /* Main rendering loop */
    @Override
    public void render() {
        delta = Gdx.graphics.getDeltaTime();
        elapsedTime += delta;

        //beginTime = System.currentTimeMillis();

        switch (STATUS_CURRENT) {
            case STATUS_INIT: {
                Gdx.gl.glClearColor(0, 0, 0, 0);
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
                event = service.take();
                if (event != null) {
                    //Log.e(TAG, "Event Received");
                    event.run(this);
                }
                if (background != null) {
                    batch.begin();
                    background.draw(batch, 1f);
                    batch.end();
                }
                break;
            }
            case STATUS_RUNNING: {
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
                event = service.take();
                if (event != null) {
                    //Log.e(TAG, "Event Received");
                    event.run(this);
                }
                act();
                batch.begin();
                draw();
                batch.end();

                weather.drawFilter(objectDebugger);

                weather.draw(batch, delta);

                if (bridge.isDebug()) {
                    renderDebugObjects();
                }
                break;
            }
            case STATUS_PAUSED: {
                break;
            }
        }


        /*
        long timeDiff = System.currentTimeMillis() - beginTime;
        sleepTime = (int) (FRAME_TARGET - timeDiff);
        if (sleepTime > 0 && Gdx.graphics.getFramesPerSecond() > 25) {
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {

            }
        }
        */
    }

    /* Calculate sprite positions depending on dimensions of the window */
    private void calculateGUI() {
        batch = new SpriteBatch();

        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        scaledX = width / 400f;
        scaledY = height / 240;

        float xMe = 10 * scaledX;
        float yMe = 10 * scaledY;
        float widthMe = scaledX * 155;
        float heightMe = scaledY * 150;
        Rectangle.tmp.set(xMe, yMe, widthMe, heightMe);

        float xOpp = width - (scaledX * 135) - 10 * scaledX;
        float yOpp = height - (scaledY * 135) - 10 * scaledY;
        float widthOpp = scaledX * 135;
        float heightOpp = scaledY * 130;
        Rectangle.tmp2.set(xOpp, yOpp, widthOpp, heightOpp);

        if (battleAtlas == null) {
            battleAtlas = new TextureAtlas(Gdx.files.internal("battle3.txt"));
            //battleAtlas = bridge.getAtlas("battle3.txt");
        }

        if (font == null) {
            font = new BitmapFont(Gdx.files.internal("battle.fnt"), battleAtlas.findRegion("font"));
            font.getData().setScale(scaledX * 1.4f);
        }
    }

    private void draw() {
        if (background != null) {
            background.draw(batch, 1f);
        }

        drawPokemon(me);
        drawPokemon(opp);

        HUDs[me].draw(batch);
        HUDs[opp].draw(batch);

        //font.draw(batch, "" + Gdx.graphics.getFramesPerSecond(), 10, 10);
    }

    private void act() {
        HUDs[me].act(delta);
        HUDs[opp].act(delta);

        actPokemon(me);
        actPokemon(opp);
    }

    @Override
    public void resume() {
        super.resume();
        if (sprites[me] != null && sprites[opp] != null) {
            if (sprites[me].loaded() && sprites[opp].loaded()) {
                STATUS_CURRENT = STATUS_RUNNING;
                return;
            }
        }
        STATUS_CURRENT = STATUS_INIT;
    }

    @Override
    public void pause() {
        super.pause();
        STATUS_CURRENT = STATUS_PAUSED;
    }

    @Override
    public void dispose() {
        super.dispose();
        try {
            /*
            battleAtlas.dispose();
            font.dispose();
            batch.dispose();
            spriteAtlas[me].dispose();
            spriteAtlas[opp].dispose();
            ((TextureRegionDrawable) background.getDrawable()).getRegion().getTexture().dispose();
            */
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    private boolean checkForFemaleAsset(boolean side, String path) {
        return Gdx.files.internal("data/sheets/" + (!side ? "front/atlas/" : "back/atlas/") + path + "f" + ".zz").exists();
    }*/

    public void updateSprite(int spot, String path, boolean female) {
        //bridge.alert("Updating: " + path);
        if (!path.equals("0")) {
            //if (female && checkForFemaleAsset(true, path)) path = path + "f";
            //spriteAtlas[me] = GdxGZipAssetLoader.loadTextureAtlas(path, true);
            spriteAtlas[spot] = new TextureAtlas(Gdx.files.internal(path + ".atlas"));
            //spriteAtlas[me] = bridge.getAtlas(path);
            Array<TextureRegion> regions = new Array<TextureRegion>();
            if (spriteAtlas[spot].findRegion("001") == null) {
                for (int i = 1; i < spriteAtlas[spot].getRegions().size; i++) {
                    regions.add(spriteAtlas[spot].findRegion("" + i));
                }
            } else {
                for (int i = 1; i < spriteAtlas[spot].getRegions().size; i++) {
                    regions.add(spriteAtlas[spot].findRegion(StringFormat(i)));
                }
            }
            sprites[spot].setSprite(new SpriteAnimation(0.075f, regions, Animation.PlayMode.LOOP));

            if (STATUS_CURRENT == STATUS_INIT) {
                if (sprites[me] != null && sprites[opp] != null) {
                    if (sprites[me].loaded() && sprites[opp].loaded()) {
                        STATUS_CURRENT = STATUS_RUNNING;
                    }
                }
            }
        }
    }

    private void drawPokemon(byte player) {
        if (sprites[player] != null) {
            sprites[player].draw(batch, 1f);
        }
    }

    private void actPokemon(byte player) {
        if (sprites[player] != null) {
            sprites[player].act(delta);
        }
    }

    public void setBackground(int id) {
        background = new Image(bridge.getTexture("background/" + id + ".png"));
        background.setWidth(width);
        background.setHeight(height);
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        // Handle in overridden extension
        if (Rectangle.tmp.contains(screenX, height - screenY)) {
            bridge.alert("true");
            //activity.DialogLooper(BattleActivity.BattleDialog.MyDynamicInfo.ordinal());
        }
        if (Rectangle.tmp2.contains(screenX, height - screenY)) {
            bridge.alert("false");
            //activity.DialogLooper(BattleActivity.BattleDialog.OppDynamicInfo.ordinal());
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        if (Rectangle.tmp.contains(screenX, height-screenY)) {
            bridge.onHover(me);
        } else if (Rectangle.tmp2.contains(screenX, height-screenY)) {
            bridge.onHover(opp);
        } else {
            bridge.onHover(-1);
        }
        return false;
    }

    private String StringFormat(Integer i) {
        // Because String.format won't transfer to html version
        String string = i + "";
        while (string.length() < 3) {
            string = "0" + string;
        }
        return string;
    }

    public void drawRect(Rectangle rect) {
        objectDebugger.rect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
    }

    ShapeRenderer objectDebugger;
    public void renderDebugObjects() {
        objectDebugger.begin(ShapeRenderer.ShapeType.Line);
        objectDebugger.setColor(0, 1, 0, 1);
        drawRect(Rectangle.tmp);
        drawRect(Rectangle.tmp2);
        objectDebugger.end();
    }

}

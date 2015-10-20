package com.mygdx.game.client;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class Assets {
    private static AssetManager assets = new AssetManager();
    private static String path = "";//"/public/battle/";
    public static TextureAtlas getAtlas(String name) {
        assets.load(path + name, TextureAtlas.class);
        assets.finishLoading();
        return assets.get(path + name);
    }

    public static void bundleLoad(String[] args, Class[] type) {

    }

    public static Texture getTexture(String name) {
        assets.load(path + name, Texture.class);
        assets.finishLoading();
        return assets.get(path + name);
    }

    public static BitmapFont getFont(String name) {
        assets.load(path + name, BitmapFont.class);
        assets.finishLoading();
        return assets.get(path + name);
    }

    public static void free(String name) {
        assets.unload(path + name);
    }
}

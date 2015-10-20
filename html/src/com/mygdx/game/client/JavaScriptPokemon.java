package com.mygdx.game.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.mygdx.game.JSONPoke;

public class JavaScriptPokemon extends JavaScriptObject implements JSONPoke {

    protected JavaScriptPokemon() {}

    @Override
    public final boolean shiny() {
        return getShiny();
    }

    @Override
    public final byte level() {
        return getLevel();
    }

    @Override
    public final String name() {
        return getName();
    }

    @Override
    public final short num() {
        return getNum();
    }

    @Override
    public final byte percent() {
        return getPercent();
    }

    @Override
    public final byte gender() {
        return getGender();
    }

    @Override
    public final int status() {
        return getStatus();
    }

    @Override
    public final byte forme() {
        return getforme();
    }

    @Override
    public final short life() {
        return getLife();
    }

    @Override
    public final short totallife() {
        return getTotalLife();
    }

    private native byte getGender() /*-{
        return this.gender || 0;
    }-*/;

    private native byte getPercent() /*-{
        return this.percent || 0;
    }-*/;
    private native byte getLevel() /*-{
        return this.level || 0;
    }-*/;
    private native String getName() /*-{
        return this.name || "";
    }-*/;

    private native String pokemonName() /*-{
        return $wnd.pokeinfo.name(this);
    }-*/;

    private native short getNum() /*-{
        return this.sprite ? (this.sprite & ( (1<< 16) -1)) : this.num;
    }-*/;

    private native boolean getShiny() /*-{
        return this.shiny || false;
    }-*/;

    private native int getStatus() /*-{
        return this.status || 0;
    }-*/;

    private native byte getforme() /*-{
        return this.sprite ? (this.sprite >> 16) : this.forme;
    }-*/;

    private native short getTotalLife() /*-{
        return this.totalLife || 0;
    }-*/;

    private native short getLife() /*-{
        return this.life || 0;
    }-*/;

    public static native JavaScriptPokemon fromJS(String json) /*-{
        var ret = eval('(' + json + ')');

        console.log("got evaled");
        //Change the name if it contains illegal characters. A regex would probably do the same job
        var name = ret.name;
        for (var i in name) {
            var chr = name.charCodeAt(i);
            if (chr >= 32 && chr <= 126) {
                continue;
            }
            if (chr == 3 || chr == 4 || chr == 15 || (chr >= 11 && chr <= 13)
                    || chr == 166 || chr == 167 || chr == 173) {
                continue;
            }
            ret.name = $wnd.pokeinfo.name(ret);
            break;
        }
        console.log("got renamed");

        return ret;
    }-*/;

    public static native String pokemonName(int num) /*-{
        return $wnd.pokeinfo.name(num);
    }-*/;
}

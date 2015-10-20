package com.mygdx.game.desktop;

import com.mygdx.game.JSONPoke;

public class Poke implements JSONPoke {
    byte gender;
    String name = "";
    short num;
    byte level;
    boolean shiny;
    int status;
    byte percent;
    short life;
    short totallife;

    public Poke(byte level, String name, short num, byte gender, byte percent, int status) {
        this.level = level;
        this.name = name;
        this.num = num;
        this.gender = gender;
        this.percent = percent;
        this.status = status;
    }

    public Poke(byte level, String name, short num, byte gender, byte percent, int status, short life, short totallife) {
        this.level = level;
        this.name = name;
        this.num = num;
        this.gender = gender;
        this.percent = percent;
        this.status = status;
        this.life = life;
        this.totallife = life;
    }

    @Override
    public byte gender() {
        return gender;
    }

    @Override
    public byte level() {
        return level;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public short num() {
        return num;
    }

    @Override
    public byte percent() {
        return percent;
    }

    @Override
    public boolean shiny() {
        return shiny;
    }

    @Override
    public int status() {
        return status;
    }

    @Override
    public short life() {
        return life;
    }

    @Override
    public short totallife() {
        return totallife;
    }

    @Override
    public byte forme() {
        return 0;
    }
}

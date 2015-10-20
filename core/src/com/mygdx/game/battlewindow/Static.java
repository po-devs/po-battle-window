package com.mygdx.game.battlewindow;

public class Static {
    public static final int sandstorm = 0xDEAC007F;
    public static final int hailstorm = 0x9DCFCF7F;           //Fine        Paralysed   Asleep      Frozen      Burnt       Poisoned    Toxic
    public static final int[] statusColor =     new int[]       {0xFFFFFFFF,0xFFFF7FFF, 0x989898FF, 0x98D8D8FF, 0xFFB070FF, 0xF090F0FF, 0xF090F0FF};
    public static final boolean[] statusPause = new boolean[]   {false,     false,      false,      true,       false,      false,      false};
    public static final float[] statusFrameRate = new float[]   {0.075f,    0.150f,     0.150f,     0.075f,     0.075f,     0.075f,     0.075f};
}
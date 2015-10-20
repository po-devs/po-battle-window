package com.mygdx.game.battlewindow;

public class FontShifter {
    public static String parseBlackNumber(short in) {
        return parseBlackNumber(String.valueOf(in));
    }

    public static String parseBlackNumber(String in) {
        char[] chars = in.toCharArray();
        // 0 = 48
        // 1 = 49
        // 2 = 50
        // 3 = 51
        // 4 = 52
        // 5 = 53
        // 6 = 54
        // 7 = 55
        // 8 = 56
        // 9 = 57
        for (int i = 0; i < chars.length; i++) {
            int j = (int) chars[i];
            if (j > 47 && j < 58) {
                chars[i] = (char) (j + 127);
            }
        }
        return String.valueOf(chars);
    }
}

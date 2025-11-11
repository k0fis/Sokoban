package kfs.sokoban;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum Tile {
    BOX('B'),
    GOAL('G'),
    WALL('W'),
    PLAYER_L('P'),

    PLAYER_R('1'),
    PLAYER_U('2'),
    PLAYER_D('3'),
    FLOOR('F');

    public final char sym;

    private Tile(char sym) {
        this.sym = sym;
    }

    public char getCode() {
        return sym;
    }

    private static final Map<Character, Tile> BY_CODE =
        Arrays.stream(values())
            .collect(Collectors.toMap(Tile::getCode, e -> e));

    public static Tile fromCode(char c) {
        return BY_CODE.get(c);
    }
}

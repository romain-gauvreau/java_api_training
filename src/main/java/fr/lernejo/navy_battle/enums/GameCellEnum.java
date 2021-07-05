package fr.lernejo.navy_battle.enums;

public enum GameCellEnum {
    EMPTY("."),
    MISSED_FIRE("-"),
    SUCCESSFUL_FIRE("X"),
    BOAT("B");

    private final String letter;

    GameCellEnum(String letter) {
        this.letter = letter;
    }

}

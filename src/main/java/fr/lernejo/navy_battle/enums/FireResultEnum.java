package fr.lernejo.navy_battle.enums;

import java.util.Arrays;

public enum FireResultEnum {
    MISS("miss"), HIT("hit"), SUNK("sunk");

    private final String apiString;

    FireResultEnum(String res) {
        this.apiString = res;
    }


    public static FireResultEnum fromAPI(String value) {
        var res = Arrays.stream(FireResultEnum.values()).filter(f -> f.apiString.equals(value)).findFirst();

        if (res.isEmpty())
            throw new RuntimeException("Invalid value!");

        return res.get();
    }

    public String toAPI() {
        return apiString;
    }
}

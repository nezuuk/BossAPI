package ru.emrass.bossapi.bosses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.PackagePrivate;

import java.util.Arrays;
@AllArgsConstructor
@Getter
public enum BossType {
    BOSS("&4&l", "BOSS"),
    RAID_BOSS("&6&l", "RAID_BOSS");

    private final String color;
    private final String name;


}

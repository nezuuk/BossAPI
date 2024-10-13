package ru.emrass.bossapi.bosses;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BossProtectionType {

    POISON("poison","Защита от зельев"),
    PROJECTILE("projectaile","Защита от стрел"),
    ANTIKNOCKBACK("antiknockback","Защита от отталкивания");


    private final String name;
    private final String description;
}

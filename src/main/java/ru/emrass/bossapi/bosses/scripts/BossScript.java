package ru.emrass.bossapi.bosses.scripts;

import lombok.SneakyThrows;
import org.bukkit.entity.Player;
import ru.emrass.bossapi.bosses.CustomBoss;

import java.util.HashMap;
import java.util.Random;

public abstract class BossScript {
    public static HashMap<CustomBoss, HashMap<Player, Double>> killersBoss = new HashMap<>();
    protected static final Random r = new Random();
    @SneakyThrows
    public BossScript(String script) {
        script = script.toLowerCase();
        if (CustomBoss.scripts.containsKey(script))
            throw new IllegalArgumentException("error");

    }

    public abstract void onSpawn(CustomBoss entity);
    public abstract void onDeath(CustomBoss entity);
    public abstract void onDamageBoss(CustomBoss entity, Player p, double damage);


    public static void register(String script, BossScript script2){
        System.out.println(script +  " " + script2.getClass().getName() + " register ");
        CustomBoss.scripts.put(script,script2);
    }

    public static void loadScript(){

    }

}

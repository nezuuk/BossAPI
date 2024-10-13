package ru.emrass.bossapi;

import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import ru.emrass.bossapi.bosses.RespawnManager;
import ru.emrass.bossapi.bosses.mobevent.EntityDamage;
import ru.emrass.bossapi.bosses.mobevent.EventCanceller;
import ru.emrass.bossapi.bosses.scripts.BossScript;
import ru.emrass.bossapi.configuration.ConfigManager;
import ru.emrass.bossapi.database.DataBaseAPI;
import ru.emrass.bossapi.bosses.mobevent.EntityDeath;

public class BossApiCore extends JavaPlugin {

    @Getter
    public static BossApiCore instance;

    @Getter
    public ConfigManager configManager;

    @Getter
    public DataBaseAPI dataBase;

    @Override
    @SneakyThrows
    public void onEnable() {
        instance = this;
        configManager = new ConfigManager();
        dataBase = new DataBaseAPI();
        BossScript.loadScript();
        RespawnManager.load();
        Bukkit.getPluginManager().registerEvents(new EntityDeath(), this);
        Bukkit.getPluginManager().registerEvents(new EntityDamage(), this);
        Bukkit.getPluginManager().registerEvents(new EventCanceller(), this);
    }

    @Override
    public void onDisable() {
        Bukkit.getWorld("world").getEntities().forEach(entity -> {
            if (!(entity instanceof Player)) {
                entity.remove();
            }
        });
    }

}

package ru.emrass.bossapi.bosses.mobevent;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import ru.emrass.bossapi.BossApiCore;
import ru.emrass.bossapi.bosses.BossManagerAPI;
import ru.emrass.bossapi.bosses.CustomBoss;
import ru.emrass.bossapi.bosses.RespawnManager;
import ru.emrass.bossapi.bosses.scripts.BossScript;
import ru.emrass.bossapi.utils.ChatUtil;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class EntityDeath implements Listener {

    @EventHandler
    public void entityDeath(EntityDeathEvent e){
        CustomBoss boss = BossManagerAPI.getFromEntity(e.getEntity());
        if(boss != null){
            if(boss.isRespawn()) RespawnManager.spawn(BossManagerAPI.getBossFromID(boss.getId()),boss.getSpawnLocations(),boss.getRespawnSeconds());
            boss.getScript().onDeath(boss);
            Stream<Map.Entry<Player, Double>> finalMap = BossScript.killersBoss.get(boss).entrySet().stream().sorted(Map.Entry.<Player,Double>comparingByValue().reversed());
            PlayerDamager[] damager = new PlayerDamager[3];
            AtomicInteger i = new AtomicInteger(0);
            Bukkit.getOnlinePlayers().forEach(player -> {
                player.sendMessage(ChatUtil.alt(boss.getCustomName()) + " повержен!");
                BossApiCore.getInstance().getConfigManager().getLocalizationConfig().get("top_damage");
                player.sendMessage(ChatUtil.alt(BossApiCore.getInstance().getConfigManager().getLocalizationConfig().getString("top_damage")));
                finalMap.forEach(playerDoubleEntry -> {
                    if(i.get() != 3) {
                        player.sendMessage(playerDoubleEntry.getKey().getName() + ": " + playerDoubleEntry.getValue());
                        damager[i.getAndIncrement()] = new PlayerDamager(playerDoubleEntry.getKey().getName(), playerDoubleEntry.getValue());

                    }

                });
            });
            CustomBoss.cacheLE.remove(e.getEntity().getUniqueId());
            System.out.println(Arrays.toString(damager));
            BossApiCore.getInstance().getDataBase().addBossKill(boss.getId(),System.currentTimeMillis(), Arrays.toString(damager));

        }
    }
    public class PlayerDamager {

        private String name;
        private double damage;


        public PlayerDamager(String name, double damage){
            this.name = name;
            this.damage = damage;
        }
        @Override
        public String toString() {
            return "{" +
                    "name: " + name + ", damage:" + damage  + '}';
        }
    }
}

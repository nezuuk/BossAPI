package ru.emrass.bossapi.bosses;

import lombok.Data;
import lombok.experimental.PackagePrivate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import ru.emrass.bossapi.BossApiCore;

import java.util.PriorityQueue;
public class RespawnManager {

    private static final PriorityQueue<RespawnManager.RespawningEntity> queue = new PriorityQueue<>();
    private static boolean loaded = false;

    public static void load() {
        if(!loaded) {
            loaded = true;
            Bukkit.getScheduler().runTaskTimer(BossApiCore.getInstance(), () -> {
                long current = System.currentTimeMillis();

                while (!queue.isEmpty()) {
                    RespawnManager.RespawningEntity top = queue.peek();
                    if (top.ressurectionTime > current) return;

                    queue.remove(top);
                    top.boss.spawn(top.getLocation(),false,true);
                }
            }, 0L, 80L);
        }
    }

    public static void spawn(CustomBoss boss, Location location, int delayInSeconds) {
        queue.add(new RespawnManager.RespawningEntity(boss, location, System.currentTimeMillis() + (delayInSeconds * 1000L)));
    }


    @Data
    private static class RespawningEntity implements Comparable<RespawnManager.RespawningEntity> {
        private final CustomBoss boss;
        private final Location location;
        private final long ressurectionTime;

        public int compareTo(RespawnManager.RespawningEntity re) {
            return this.ressurectionTime > re.ressurectionTime ? 1 : -1;
        }
    }
}

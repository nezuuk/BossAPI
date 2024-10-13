package ru.emrass.bossapi.utils;
import lombok.experimental.UtilityClass;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;
@UtilityClass
public class LocationUtil {

    public String toString(Location location) {
        return location.getWorld().getName() + " " + location.getX() + " " + location.getY() + " " + location.getZ() + " " + location.getYaw() + " " + location.getPitch();
    }

    public Location fromString(String location) {
        String[] args = location.split(" ");
        if (args.length < 4) {
            throw new IllegalArgumentException("not parameters");
        }
        org.bukkit.World world = Bukkit.getWorld(args[0]);
        double x = Double.parseDouble(args[1]);
        double y = Double.parseDouble(args[2]);
        double z = Double.parseDouble(args[3]);
        float yaw = args.length >= 6 ? Float.parseFloat(args[4]) : 0f;
        float pitch = args.length >= 6 ? Float.parseFloat(args[5]) : 0f;
        return new Location(world, x, y, z, yaw, pitch);
    }


    public List<Player> getNearbyPlayer(Location l, int radius) {
        return l.getWorld().getNearbyEntities(l, radius, radius, radius).stream()
                .filter(entity -> entity.getType() == EntityType.PLAYER)
                .map(entity -> (Player) entity)
                .collect(Collectors.toList());
    }


    public List<Player> getNearbyPlayers(Location l, int radius) {
        return l.getWorld().getNearbyEntities(l, radius, radius, radius).stream()
                .filter(entity -> entity.getType() == EntityType.PLAYER)
                .map(entity -> (Player) entity)
                .filter(player -> player.getGameMode() == GameMode.SURVIVAL || player.getGameMode() == GameMode.ADVENTURE)
                .collect(Collectors.toList());
    }
}

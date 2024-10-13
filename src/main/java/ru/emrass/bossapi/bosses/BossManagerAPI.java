package ru.emrass.bossapi.bosses;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.sql.Connection;
import java.util.HashMap;

public class BossManagerAPI {


    public static void loadBosses(Connection connection){
        CustomBoss.loadAll(connection);
    }

    public static CustomBoss getBossFromID(int id){
        return CustomBoss.getBoss(id);
    }

    public static LivingEntity getLivingEntity(CustomBoss boss){
        return boss.getLe();
    }
    public static CustomBoss getFromLE(LivingEntity le){
        return CustomBoss.getFromLE(le);
    }

    public static CustomBoss getFromEntity(Entity e){
        return CustomBoss.get(e);
    }
    public static Entity spawn(CustomBoss boss,Location location) {
        return boss.spawn(location,false,false);
    }

    public static Entity spawn(CustomBoss boss,Location location,boolean save) {
        return boss.spawn(location,save,save);
    }

}

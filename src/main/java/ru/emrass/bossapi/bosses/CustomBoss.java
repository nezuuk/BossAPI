package ru.emrass.bossapi.bosses;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import ru.emrass.bossapi.BossApiCore;
import ru.emrass.bossapi.bosses.scripts.BossScript;
import ru.emrass.bossapi.utils.ChatUtil;
import ru.emrass.bossapi.utils.LocationUtil;



import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter

public class CustomBoss {
    private static Connection connection;
    protected static final HashMap<Integer, CustomBoss> cacheID = new HashMap<>();
    public static final ConcurrentHashMap<UUID, CustomBoss> cacheLE = new ConcurrentHashMap<>();
    public static final HashMap<String, BossScript> scripts = new HashMap<>();
    private int id;
    private String customName;
    private EntityType prototype;
    private int damage;
    private double health;
    private double maxHealth;
    private double movementSpeed;
    private int armor;
    private Location spawnLocations;
    private int respawnSeconds = 0;
    private Material handId;
    private Material hand2Id;
    private Material armor1Id;
    private Material armor2Id;
    private Material armor3Id;
    private Material armor4Id;
    private BossType type;
    private String script = "default";
    private LivingEntity le;
    private boolean respawn = false;
    private List<BossProtectionType> protectionTypeList = new ArrayList<>();

    private CustomBoss() {
        this.type = BossType.BOSS;
    }
    @SneakyThrows
    protected static void loadAll(Connection con){
        connection = con;
        Statement state = con.createStatement();
        ResultSet rs = state.executeQuery("SELECT * FROM bosses");
        while (rs.next()){
            CustomBoss customBoss = new CustomBoss();
            customBoss.setId(rs.getInt("id"));
            customBoss.setCustomName(rs.getString("customname"));
            customBoss.setDamage(rs.getInt("damage"));
            customBoss.setHealth(rs.getDouble("health"));
            customBoss.setMaxHealth(rs.getDouble("health"));
            customBoss.setMovementSpeed(rs.getDouble("movespeed"));
            customBoss.setArmor(rs.getInt("armor"));
            customBoss.setPrototype(EntityType.valueOf(rs.getString("prototype")));
            customBoss.setRespawnSeconds(rs.getInt("respawnseconds"));
            customBoss.setHandId(Material.valueOf(rs.getString("handid") == null ? "AIR" : rs.getString("handid")));
            customBoss.setHand2Id(Material.valueOf(rs.getString("hand2id") == null ? "AIR" : rs.getString("hand2id")));
            customBoss.setArmor1Id(Material.valueOf(rs.getString("armor1id")  == null ? "AIR" : rs.getString("armor1id")));
            customBoss.setArmor2Id(Material.valueOf(rs.getString("armor2id")   == null ? "AIR" : rs.getString("armor2id")));
            customBoss.setArmor3Id(Material.valueOf(rs.getString("armor3id")   == null ? "AIR" : rs.getString("armor3id")));
            customBoss.setArmor4Id(Material.valueOf(rs.getString("armor4id")   == null ? "AIR" : rs.getString("armor4id")));
            customBoss.setScript(rs.getString("script"));
//            ResultSet set = state.executeQuery("SELECT * FROM bossesspawn WHERE 'id' = " + customBoss.get);
//            if(set.next()) customBoss.setSpawnLocations(LocationUtil.fromString(set.getString("spawnlocation")));
            if (customBoss.script == null) customBoss.setScript("default");
            cacheID.put(customBoss.getId(), customBoss);
            System.out.println("BOSS " + customBoss.getCustomName() + " load");
        }
        spawnallmob();
    }
    @SneakyThrows
    private static void spawnallmob(){
        Statement state = connection.createStatement();
        ResultSet rs = state.executeQuery("SELECT * FROM bossesspawn");
        while (rs.next()){
            int id = rs.getInt("id");
            Location location = LocationUtil.fromString(rs.getString("spawnlocation"));
            getBoss(id).setSpawnLocations(location);
            getBoss(id).spawn(location,false,true);
        }
    }

    public BossScript getScript() {
        return scripts.get(this.script);
    }

    @SneakyThrows
    protected static CustomBoss getBoss(int id) {
        CustomBoss boss = null;
        if (cacheID.containsKey(id)) boss = cacheID.get(id);
        else {
            Statement state = connection.createStatement();
            ResultSet rs = state.executeQuery("SELECT * FROM bosses WHERE 'id' = " + id);
            while (rs.next()){
                boss = new CustomBoss();
                boss.setId(id);
                boss.setCustomName(rs.getString("customname"));
                boss.setDamage(rs.getInt("damage"));
                boss.setHealth(rs.getDouble("health"));
                boss.setMaxHealth(rs.getDouble("health"));
                boss.setMovementSpeed(rs.getDouble("movespeed"));
                boss.setArmor(rs.getInt("armor"));
                boss.setPrototype(EntityType.valueOf(rs.getString("prototype")));
                boss.setRespawnSeconds(rs.getInt("respawnseconds"));
                boss.setHandId(Material.valueOf(rs.getString("handid") == null ? "AIR" : rs.getString("handid")));
                boss.setHand2Id(Material.valueOf(rs.getString("hand2id") == null ? "AIR" : rs.getString("hand2id")));
                boss.setArmor1Id(Material.valueOf(rs.getString("armor1id")  == null ? "AIR" : rs.getString("armor1id")));
                boss.setArmor2Id(Material.valueOf(rs.getString("armor2id")   == null ? "AIR" : rs.getString("armor2id")));
                boss.setArmor3Id(Material.valueOf(rs.getString("armor3id")   == null ? "AIR" : rs.getString("armor3id")));
                boss.setArmor4Id(Material.valueOf(rs.getString("armor4id")   == null ? "AIR" : rs.getString("armor4id")));
                boss.setScript(rs.getString("script"));
                if (boss.getScript() == null) boss.setScript("default");
//                ResultSet set = state.executeQuery("SELECT * FROM bossesspawn WHERE 'id' = " + id);
//                if(set.next()) boss.setSpawnLocations(LocationUtil.fromString(set.getString("spawnlocation")));
                cacheID.put(boss.getId(), boss);
            }
        }
        return boss;
    }


    protected static CustomBoss get(Entity e) {
        return e instanceof LivingEntity ? getFromLE((LivingEntity) e) : null;
    }

    protected static CustomBoss getFromLE(LivingEntity le) {
        return cacheLE.get(le.getUniqueId());
    }

    protected Entity spawn(Location location,boolean save,boolean respawn){
        if(location.getChunk().isLoaded()) {
            this.setRespawn(respawn);
            this.setHealth(this.getMaxHealth());
            World world = location.getWorld();
            Entity entity = world.spawnEntity(location, this.getPrototype());
            LivingEntity le = (LivingEntity) entity;
            entity.customName(Component.text(ChatUtil.alt(this.getCustomName())));
            double moveSpeed = le.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getValue();
            le.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(moveSpeed * this.getMovementSpeed());
            le.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(this.getMaxHealth());
            le.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(this.getArmor());
            le.setHealth(this.getMaxHealth());
            if(this.prototype.equals(EntityType.ZOMBIE)) ((Zombie) entity).setBaby(false);
            entity.setCustomNameVisible(true);
            le.getEquipment().setItemInMainHand(new ItemStack(this.getHandId()));
            le.getEquipment().setItemInOffHand(new ItemStack(this.getHand2Id()));
            le.getEquipment().setHelmet(new ItemStack(this.getArmor1Id()));
            le.getEquipment().setChestplate(new ItemStack(this.getArmor2Id()));
            le.getEquipment().setLeggings(new ItemStack(this.getArmor3Id()));
            le.getEquipment().setBoots(new ItemStack(this.getArmor4Id()));
            le.getEquipment().setItemInMainHandDropChance(0.0F);
            le.getEquipment().setHelmetDropChance(0.0F);
            le.getEquipment().setChestplateDropChance(0.0F);
            le.getEquipment().setLeggingsDropChance(0.0F);
            le.getEquipment().setBootsDropChance(0.0F);
            this.setLe(le);
            if(this.getScript() != null) this.getScript().onSpawn(this);
            BossScript.killersBoss.put(this,new HashMap<>());

            cacheLE.put(entity.getUniqueId(), this);
//            RespawnManager.spawn(this,location,this.getRespawnSeconds());
            if(save){
                BossApiCore.getInstance().getDataBase().query(String.format("INSERT INTO bossesspawn VALUES('%s', '%s')",this.getId(), LocationUtil.toString(location)));
                this.setSpawnLocations(location);
            }
            return entity;
        }else{
            return null;
        }
    }
}

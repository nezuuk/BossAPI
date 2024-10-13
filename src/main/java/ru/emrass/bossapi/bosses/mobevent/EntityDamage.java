package ru.emrass.bossapi.bosses.mobevent;

import net.kyori.adventure.text.Component;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import ru.emrass.bossapi.bosses.BossManagerAPI;
import ru.emrass.bossapi.bosses.BossProtectionType;
import ru.emrass.bossapi.bosses.CustomBoss;
import ru.emrass.bossapi.bosses.scripts.BossScript;
import ru.emrass.bossapi.utils.ChatUtil;

public class EntityDamage implements Listener {


    @EventHandler
    public void entityDamage(EntityDamageByEntityEvent e) {
        Entity damager = e.getDamager();
        CustomBoss boss = BossManagerAPI.getFromEntity(e.getEntity());

        if (damager instanceof Player) {
            if (boss != null) {
                if(!BossScript.killersBoss.get(boss).containsKey((Player) damager)){
                    BossScript.killersBoss.get(boss).put((Player) damager,e.getDamage());
                }
                if(BossScript.killersBoss.get(boss).containsKey((Player) damager)){
                    double finaldamage = BossScript.killersBoss.get(boss).get((Player)damager) + e.getDamage();
                    if(finaldamage > boss.getMaxHealth()) finaldamage = boss.getMaxHealth();
                    BossScript.killersBoss.get(boss).put((Player) damager,finaldamage);
                }
                boss.getScript().onDamageBoss(boss, (Player) damager,e.getDamage());
                boss.setHealth(((LivingEntity) e.getEntity()).getHealth());
                if(boss.getHealth() > 0){
                    damager.sendActionBar(Component.text(String.format("§4♥ %s: §4%s", ChatUtil.replace(boss.getCustomName()), Math.round(boss.getHealth() * 100.0) / 100.0)));
                }
                if (boss.getProtectionTypeList().contains(BossProtectionType.ANTIKNOCKBACK)) {
                    ((LivingEntity) e.getEntity()).getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(1);
                } else
                    ((LivingEntity) e.getEntity()).getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(0);
                if (e.getCause().equals(EntityDamageEvent.DamageCause.POISON) || e.getCause().equals(EntityDamageEvent.DamageCause.PROJECTILE)) {
                    if (e.getCause().equals(EntityDamageEvent.DamageCause.POISON) && boss.getProtectionTypeList().contains(BossProtectionType.POISON))
                        e.setCancelled(true);
                    if (e.getCause().equals(EntityDamageEvent.DamageCause.PROJECTILE) && boss.getProtectionTypeList().contains(BossProtectionType.PROJECTILE))
                        e.setCancelled(true);
                }
            }
        }
    }
}


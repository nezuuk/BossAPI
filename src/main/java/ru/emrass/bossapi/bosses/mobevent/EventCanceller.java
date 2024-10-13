package ru.emrass.bossapi.bosses.mobevent;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;

public class EventCanceller implements Listener {



    @EventHandler
    public void onFireEntity(EntityCombustEvent event) {
        if (event.getEntity() != null) event.setCancelled(true);
    }
}

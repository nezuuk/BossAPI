package ru.emrass.bossapi.command.context;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;


public record ContextCommand(CommandSender sender, List<?> args) {

    public Object getArg(int index) {
        return hasArg(index) ? args.get(index) : null;
    }

    public boolean hasArg(int index) {
        return args.size() - 1 >= index;
    }

    public Player getPlayer() {
        return (Player) sender;
    }

}

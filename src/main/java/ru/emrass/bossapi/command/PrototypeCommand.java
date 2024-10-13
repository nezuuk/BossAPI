package ru.emrass.bossapi.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;


import java.util.List;

public class PrototypeCommand extends Command{

    protected Plugin plugin;
    protected final CommandExecutor owner;
    protected final String cmdName;
    protected final String description;
    protected final List<String> aliases;
    protected final String usage;
    protected final int neededArgs;



    protected PrototypeCommand(Plugin plugin, String name, CommandExecutor owner, List<String> aliases, String description, String usage,int neededArgs) {
        super(name, description, usage, aliases);
        this.plugin = plugin;
        this.owner = owner;
        this.cmdName = name;
        this.description = description;
        this.aliases = aliases;
        this.usage = usage;
        this.neededArgs = neededArgs;

    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String cmd, String[] args) {
        this.owner.onCommand(sender,this,cmd,args);
        return true;
    }

}

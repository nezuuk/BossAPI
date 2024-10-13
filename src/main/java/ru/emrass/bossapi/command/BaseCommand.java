package ru.emrass.bossapi.command;

import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import ru.emrass.bossapi.BossApiCore;
import ru.emrass.bossapi.command.context.ContextCommand;
import ru.emrass.bossapi.utils.ChatUtil;
import ru.emrass.bossapi.utils.ClassFinder;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public abstract class BaseCommand implements CommandExecutor {

    @Getter
    private static HashMap<String,PrototypeCommand> listCommand = new HashMap<>();
    public BaseCommand(String name, String description, String usage, List<String> aliases){
        registerMap(this,name,description,aliases,usage,-1);
    }
    public BaseCommand(String name, String description, String usage, List<String> aliases,int neededArgs){
        registerMap(this,name,description,aliases,usage,neededArgs);

    }
    public abstract void execute(ContextCommand ctx);

    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command command, @NotNull String label, String[] args){
        ContextCommand ctx = new ContextCommand(sender, Arrays.stream(args).toList());
        if(ctx.args().size() < listCommand.get(command.getName()).neededArgs){
            ChatUtil.sendMessage(sender,listCommand.get(command.getName()).usage);
            return false;
        }
        execute(ctx);
        return true;
    }

    @SneakyThrows
    public static void registerPackageCommand(String packages) {
        ClassFinder.find(packages).forEach(BaseCommand::registerCommand);

    }

    @SneakyThrows
    public static void registerCommand(Class<?> aClass) {
        System.out.println(aClass.getName() + " register command");
        aClass.getDeclaredConstructor().newInstance();
    }

    private void registerMap(CommandExecutor cxecutor, String name, String desc,List<String> aliases, String usage,int neededArgs) {
        try {
            PrototypeCommand prototypeCommand = new PrototypeCommand(BossApiCore.getInstance(),name,cxecutor,aliases,desc,usage,neededArgs);
            Field field = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            field.setAccessible(true);
            CommandMap map = (CommandMap) field.get(Bukkit.getServer());
            map.register(BossApiCore.getInstance().getDescription().getName(), prototypeCommand);
            listCommand.put(name,prototypeCommand);

        } catch (Exception e) {
            BossApiCore.getInstance().getLogger().warning("Произошла ошибочка эмммм %s".formatted(e.getCause()));
        }
    }
}

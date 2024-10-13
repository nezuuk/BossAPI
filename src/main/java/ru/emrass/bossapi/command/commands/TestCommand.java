package ru.emrass.bossapi.command.commands;

import org.bukkit.entity.Player;
import ru.emrass.bossapi.command.BaseCommand;
import ru.emrass.bossapi.command.context.ContextCommand;

import java.util.List;

public class TestCommand extends BaseCommand {
    public TestCommand() {
        super("test", "TEst", "/test text", List.of("t"));
    }

    @Override
    public void execute(ContextCommand ctx) {
        Player p = ctx.getPlayer();
        System.out.println("test!");

    }
}

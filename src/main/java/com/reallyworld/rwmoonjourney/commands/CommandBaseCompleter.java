package com.reallyworld.rwmoonjourney.commands;

import com.reallyworld.rwmoonjourney.constants.Commands;
import com.reallyworld.rwmoonjourney.constants.Permissions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CommandBaseCompleter implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        ArrayList<String> base = new ArrayList<>();
        base.add(Commands.StartEvent);
        base.add(Commands.StopEvent);
        base.add(Commands.Join);
        base.add(Commands.Time);
        base.add(Commands.BuyBreath);
        if(sender.hasPermission(Permissions.CommandMJAddChest))
            base.add(Commands.AddChest);
        if(sender.hasPermission(Permissions.CommandMJASpawnChests))
            base.add(Commands.SpawnChests);
        return base;
    }
}

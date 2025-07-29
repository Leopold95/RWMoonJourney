package com.reallyworld.rwmoonjourney.commands;

import com.reallyworld.rwmoonjourney.configs.Config;
import com.reallyworld.rwmoonjourney.constants.Commands;
import com.reallyworld.rwmoonjourney.constants.Permissions;
import lombok.var;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CommandBaseCompleter implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        var list = new ArrayList<String>();

        if(args.length == 1){
            list.add(Commands.StartEvent);
            list.add(Commands.StopEvent);
            list.add(Commands.Join);
            list.add(Commands.Time);
            list.add(Commands.BuyBreath);
            if(sender.hasPermission(Permissions.CommandMJAddChest))
                list.add(Commands.AddChest);
            if(sender.hasPermission(Permissions.CommandMJASpawnChests))
                list.add(Commands.SpawnChests);

            return list;
        }

        if(args.length == 2){
            if (args[0].equals(Commands.AddChest)){
                var configRarities = Config.getStringList("chest-rarity-list");
                return configRarities.stream().map(r -> r.split(":")[0]).collect(Collectors.toList());
            }
        }

        return list;
    }
}

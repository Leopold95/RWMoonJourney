package com.reallyworld.rwmoonjourney.commands;

import com.reallyworld.rwmoonjourney.constants.Commands;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandBaseCompleter implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        ArrayList<String> base = new ArrayList<>();
        base.add(Commands.StartEvent);
        base.add(Commands.StopEvent);
        return base;
    }
}

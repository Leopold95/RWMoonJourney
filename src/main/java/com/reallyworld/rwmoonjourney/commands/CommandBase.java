package com.reallyworld.rwmoonjourney.commands;

import com.reallyworld.rwmoonjourney.configs.Messages;
import com.reallyworld.rwmoonjourney.constants.Commands;
import com.reallyworld.rwmoonjourney.constants.Permissions;
import com.reallyworld.rwmoonjourney.core.EventManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CommandBase implements CommandExecutor {
    private EventManager eventManager;

    public CommandBase(EventManager eventManager){
        this.eventManager = eventManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(args.length < 1){
            sender.sendMessage(Messages.badArgs());
            return true;
        }

        switch (args[0]){
            case Commands.StartEvent:{
                if(!sender.hasPermission(Permissions.CommandMJStart)){
                    sender.sendMessage(Messages.noPerm());
                    return true;
                }

                eventManager.start();
            }

            case Commands.StopEvent:{
                if(!sender.hasPermission(Permissions.CommandMJStop)){
                    sender.sendMessage(Messages.noPerm());
                    return true;
                }

                eventManager.stop();
            }
        }

        return true;
    }
}

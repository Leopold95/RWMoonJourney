package com.reallyworld.rwmoonjourney.commands;

import com.reallyworld.rwmoonjourney.configs.Messages;
import com.reallyworld.rwmoonjourney.constants.Commands;
import com.reallyworld.rwmoonjourney.constants.Permissions;
import com.reallyworld.rwmoonjourney.core.EventManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandBase implements CommandExecutor {
    private final EventManager eventManager;

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

            case Commands.Join:{
                if(!(sender instanceof Player)){
                    sender.sendMessage(Messages.playerOnly());
                    return true;
                }

                if(!sender.hasPermission(Permissions.CommandMJJoin)){
                    sender.sendMessage(Messages.noPerm());
                    return true;
                }

                eventManager.join((Player) sender);
            }

            case Commands.Time:{
                if(!(sender instanceof Player)){
                    sender.sendMessage(Messages.playerOnly());
                    return true;
                }

                if(!sender.hasPermission(Permissions.CommandMJTime)){
                    sender.sendMessage(Messages.noPerm());
                    return true;
                }

                eventManager.time((Player) sender);
            }
        }

        return true;
    }
}

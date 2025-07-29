package com.reallyworld.rwmoonjourney.commands;

import com.reallyworld.rwmoonjourney.configs.ChestsConfig;
import com.reallyworld.rwmoonjourney.configs.Messages;
import com.reallyworld.rwmoonjourney.constants.Commands;
import com.reallyworld.rwmoonjourney.constants.Permissions;
import com.reallyworld.rwmoonjourney.core.EventChestManager;
import com.reallyworld.rwmoonjourney.core.EventManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class CommandBase implements CommandExecutor {
    private final EventManager eventManager;
    private final EventChestManager eventChestManager;
    private final Plugin plugin;

    public CommandBase(
            @NotNull Plugin plugin,
            @NotNull EventManager eventManager,
            @NotNull EventChestManager eventChestManager
    ){
        this.eventManager = eventManager;
        this.eventChestManager = eventChestManager;
        this.plugin = plugin;
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
                break;
            }

            case Commands.StopEvent:{
                if(!sender.hasPermission(Permissions.CommandMJStop)){
                    sender.sendMessage(Messages.noPerm());
                    return true;
                }

                eventManager.stop();
                break;
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
                break;
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
                break;
            }

            case Commands.BuyBreath:{
                if(!(sender instanceof Player)){
                    sender.sendMessage(Messages.playerOnly());
                    return true;
                }

                if(!sender.hasPermission(Permissions.CommandMJBuyBreath)){
                    sender.sendMessage(Messages.noPerm());
                    return true;
                }

                eventManager.buyBreath((Player) sender);
                break;
            }

            case Commands.AddChest:{
                if(!(sender instanceof Player)){
                    sender.sendMessage(Messages.playerOnly());
                    return true;
                }

                if(!sender.hasPermission(Permissions.CommandMJAddChest)){
                    sender.sendMessage(Messages.noPerm());
                    return true;
                }

                if(args.length < 2) {
                    sender.sendMessage(Messages.badArgs());
                    return true;
                }

                ChestsConfig.addChest(args[1], ((Player) sender).getLocation());
                sender.sendMessage(Messages.text("event.add-chest.done"));
                break;
            }

            case Commands.SpawnChests:{
                if(!(sender instanceof Player)){
                    sender.sendMessage(Messages.playerOnly());
                    return true;
                }

                if(!sender.hasPermission(Permissions.CommandMJASpawnChests)){
                    sender.sendMessage(Messages.noPerm());
                    return true;
                }

                Bukkit.getScheduler().runTask(plugin, eventChestManager::respawnAll);
                break;
            }
        }

        return true;
    }
}

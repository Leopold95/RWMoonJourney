package com.reallyworld.rwmoonjourney.commands;

import com.reallyworld.rwmoonjourney.configs.Messages;
import com.reallyworld.rwmoonjourney.constants.Commands;
import com.reallyworld.rwmoonjourney.constants.Permissions;
import com.reallyworld.rwmoonjourney.core.event.ChestService;
import com.reallyworld.rwmoonjourney.core.event.EventService;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class CommandBase implements CommandExecutor {
    private final EventService eventService;
    private final ChestService chestService;
    private final Plugin plugin;

    public CommandBase(
            @NotNull Plugin plugin,
            @NotNull EventService eventService,
            @NotNull ChestService chestService
    ){
        this.eventService = eventService;
        this.chestService = chestService;
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

                Bukkit.getScheduler().runTask(plugin, eventService::startJoining);
                break;
            }

            case Commands.StopEvent:{
                if(!sender.hasPermission(Permissions.CommandMJStop)){
                    sender.sendMessage(Messages.noPerm());
                    return true;
                }

                Bukkit.getScheduler().runTask(plugin, eventService::stop);
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

                Bukkit.getScheduler().runTask(plugin, () -> eventService.join((Player) sender));
                break;
            }

            case Commands.Leave:{
                if(!(sender instanceof Player)){
                    sender.sendMessage(Messages.playerOnly());
                    return true;
                }

                if(!sender.hasPermission(Permissions.CommandMJLeave)){
                    sender.sendMessage(Messages.noPerm());
                    return true;
                }

                Bukkit.getScheduler().runTask(plugin, () -> eventService.leave((Player) sender));
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

                Bukkit.getScheduler().runTask(plugin, () -> eventService.time((Player) sender));
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

                Bukkit.getScheduler().runTask(plugin, () -> eventService.buyBreath((Player) sender));
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

                Bukkit.getScheduler().runTask(plugin, () -> chestService.addChest(args[1], ((Player) sender)));
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

                Bukkit.getScheduler().runTask(plugin, chestService::respawnAll);
                break;
            }
        }

        return true;
    }
}

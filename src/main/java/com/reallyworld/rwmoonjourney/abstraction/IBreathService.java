package com.reallyworld.rwmoonjourney.abstraction;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface IBreathService {
    void add(@NotNull Player player);
    void remove(@NotNull Player player);
    boolean has(@NotNull Player player);
    void tryDamage(@NotNull Player player);
}

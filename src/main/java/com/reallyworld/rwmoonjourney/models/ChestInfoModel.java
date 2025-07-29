package com.reallyworld.rwmoonjourney.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.Location;

@Data
@AllArgsConstructor
public class ChestInfoModel {
    private String rarity;
    private int rarityCost;
    private Location location;
}

package com.reallyworld.rwmoonjourney.configs;

import com.reallyworld.rwmoonjourney.api.config.ConfigBase;
import net.kyori.adventure.text.Component;


public class Messages extends ConfigBase {
    public static String message(String path){
        return getString(path);
    }

    public static Component text(String path){
        return getText(path);
    }

    public static Component noPerm(){
        return getText("no-permission");
    }
    public static Component badArgs(){
        return getText("bad-args");
    }
    public static Component playerOnly(){ return getText("player-only");}
}

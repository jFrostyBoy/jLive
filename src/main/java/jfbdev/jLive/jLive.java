package jfbdev.jLive;

import jfbdev.jLive.Commands.JLiveReloadCommand;
import jfbdev.jLive.Commands.LiveCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class jLive extends JavaPlugin {

    @Override
    public void onEnable() {

        Objects.requireNonNull(getCommand("live")).setExecutor(new LiveCommand(this));
        Objects.requireNonNull(getCommand("jlivereload")).setExecutor(new JLiveReloadCommand(this));
        saveDefaultConfig();

    }
}
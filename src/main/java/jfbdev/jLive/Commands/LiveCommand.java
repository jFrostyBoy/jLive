package jfbdev.jLive.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

public class LiveCommand implements CommandExecutor {

    private final JavaPlugin plugin;
    private final HashMap<UUID, Long> cooldown;

    public LiveCommand(JavaPlugin plugin) {
        this.plugin = plugin;
        this.cooldown = new HashMap<>();
    }

    @Deprecated
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        String prefix = plugin.getConfig().getString("prefix", ".");

        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + plugin.getConfig().getString("messages.console", ".")));
            return true;
        }
        if (!player.hasPermission("jlive.use")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + plugin.getConfig().getString("messages.no-permission", ".")));
            return true;
        }
        if (args.length == 0) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + plugin.getConfig().getString("messages.usage", ".")));
            return true;
        }
        if (args.length > 1) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + plugin.getConfig().getString("messages.usage", ".")));
            return true;
        }
        long cooldownTime = plugin.getConfig().getLong("cooldown", 60) * 1000;
        UUID playerUUID = player.getUniqueId();
        long currentTime = System.currentTimeMillis();
        long lastUsed = cooldown.getOrDefault(playerUUID, 0L);

        if (lastUsed != 0 && currentTime < lastUsed + cooldownTime) {
            long timeLeft = (lastUsed + cooldownTime - currentTime) / 1000;
            String cooldownMessage = plugin.getConfig().getString("messages.cooldown-message", ".");
            cooldownMessage = cooldownMessage.replace("%cooldown%", String.valueOf(timeLeft));
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + cooldownMessage));
            return true;
        }
        cooldown.put(playerUUID, currentTime);
        String link = args[0];
        String linkMessage = plugin.getConfig().getString("messages.live-message", ".");
        linkMessage = linkMessage.replace("%link%", link)
                .replace("%player%", player.getName());
        plugin.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', linkMessage));
        return true;
    }
}
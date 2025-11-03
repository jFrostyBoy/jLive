package jfbdev.jLive.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

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

        String soundName = plugin.getConfig().getString("sound.name", "ENTITY_PLAYER_LEVELUP");
        float volume = (float) plugin.getConfig().getDouble("sound.volume", 1.0);
        float pitch = (float) plugin.getConfig().getDouble("sound.pitch", 1.0);

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
        String link = args[0];
        if (!link.matches("^(https?://).+")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + plugin.getConfig().getString("messages.link-format", ".")));
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
        String linkMessageConfig = plugin.getConfig().getString("messages.live-message", ".");
        String formattedMessage = linkMessageConfig
                .replace("%link%", link)
                .replace("%player%", player.getName());

        Component message = LegacyComponentSerializer.legacyAmpersand().deserialize(formattedMessage);
        Component clickableLink = Component.text(link)
                .color(NamedTextColor.BLUE)
                .clickEvent(ClickEvent.openUrl(link));

        message = message.replaceText(builder -> builder
                .matchLiteral(link)
                .once()
                .replacement(clickableLink)
        );
        plugin.getServer().broadcast(message);

        Sound sound = Sound.valueOf(soundName.toUpperCase());
        for (Player online : Bukkit.getOnlinePlayers()) {
            online.playSound(online.getLocation(), sound, volume, pitch);
            return true;
        }
        return true;
    }
}

package jfbdev.jlivestream.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class LiveCommand implements CommandExecutor {

    private final JavaPlugin plugin;
    private final HashMap<UUID, Long> cooldown;

    public LiveCommand(JavaPlugin plugin) {
        this.plugin = plugin;
        this.cooldown = new HashMap<>();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        String prefix = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("prefix", ""));

        String soundName = plugin.getConfig().getString("sound-start.name", "ENTITY_PLAYER_LEVELUP");
        float volume = (float) plugin.getConfig().getDouble("sound-start.volume", 1.0);
        float pitch = (float) plugin.getConfig().getDouble("sound-start.pitch", 2.0);

        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + plugin.getConfig().getString("messages.only-players", "")));
            return true;
        }
        if (!player.hasPermission("jlivestream.live")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + plugin.getConfig().getString("messages.no-permission", "")));
            return true;
        }

        if (player.hasPermission("jlivestream.online")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + plugin.getConfig().getString("messages.already-live", "")));
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + plugin.getConfig().getString("messages.live-usage", "")));
            return true;
        }

        if (args.length > 1) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + plugin.getConfig().getString("messages.live-usage", "")));
            return true;
        }

        String link = args[0];
        if (!link.matches("^(https?://).+")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + plugin.getConfig().getString("messages.invalid-link", "")));
            return true;
        }

        long cooldownTime = plugin.getConfig().getLong("cooldown-time", 60) * 1000;
        UUID playerUUID = player.getUniqueId();
        long currentTime = System.currentTimeMillis();
        long lastUsed = cooldown.getOrDefault(playerUUID, 0L);

        if (lastUsed != 0 && currentTime < lastUsed + cooldownTime) {
            long timeLeft = (lastUsed + cooldownTime - currentTime) / 1000;
            String cooldownMessage = plugin.getConfig().getString("messages.cooldown-message", "");
            cooldownMessage = cooldownMessage.replace("%cooldown%", String.valueOf(timeLeft));
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + cooldownMessage));
            return true;
        }

        cooldown.put(playerUUID, currentTime);
        List<String> lines = plugin.getConfig().getStringList("broadcast.live.started");
        Component finalMessage = Component.empty();

        for (String line : lines) {
            String formatted = line
                    .replace("%player%", player.getName())
                    .replace("%link%", link);

            Component component = LegacyComponentSerializer.legacyAmpersand().deserialize(formatted);

            if (line.contains("%link%")) {
                component = component.replaceText(builder -> builder
                        .matchLiteral(link)
                        .once()
                        .replacement(
                                Component.text(link)
                                        .color(NamedTextColor.BLUE)
                                        .clickEvent(ClickEvent.openUrl(link))
                        )
                );
            }
            if (finalMessage != Component.empty()) {
                finalMessage = finalMessage.append(Component.newline());
            }
            finalMessage = finalMessage.append(component);
        }
        plugin.getServer().broadcast(finalMessage);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + plugin.getConfig().getString("messages.live-started", "")));
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " permission set jlivestream.online");

        Sound sound = Sound.valueOf(soundName.toUpperCase());
        for (Player online : Bukkit.getOnlinePlayers()) {
            online.playSound(online.getLocation(), sound, volume, pitch);
            return true;
        }
        return true;
    }
}

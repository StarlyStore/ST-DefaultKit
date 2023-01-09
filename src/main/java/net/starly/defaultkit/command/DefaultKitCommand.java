package net.starly.defaultkit.command;

import net.starly.defaultkit.data.DefaultKitData;
import net.starly.defaultkit.data.PlayerDefaultKitData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Objects;

import static java.util.stream.Collectors.toList;
import static net.starly.defaultkit.DefaultKitMain.config;

public class DefaultKitCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage(config.getMessage("messages.only_player"));
            return true;
        }

        PlayerDefaultKitData data = new PlayerDefaultKitData(p);
        if (args.length == 0) {
            if (!p.hasPermission("starly.defaultkit." + config.getString("permissions.receive_kit"))) {
                p.sendMessage(config.getMessage("messages.no_permission"));
                return true;
            }
            if (data.isReceived()) {
                p.sendMessage(config.getMessage("messages.already_received"));
                return true;
            }
            if (Arrays.stream(p.getInventory().getContents()).filter(Objects::nonNull).toList().size() != 0) {
                p.sendMessage(config.getMessage("messages.inventory_not_empty"));
                return true;
            }

            new DefaultKitData().giveKit(p);
            data.setReceived(true);
            p.sendMessage(config.getMessage("messages.kit_received"));

            return true;
        }

        switch (args[0].toLowerCase()) {
            case "리로드", "reload", "rl" -> {
                if (!p.hasPermission("starly.defaultkit." + config.getString("permissions.reload"))) {
                    p.sendMessage(config.getMessage("messages.no_permission"));
                    return true;
                }

                config.reloadConfig();
                p.sendMessage(config.getMessage("messages.reload"));
                return true;
            }

            case "설정", "set" -> {
                if (!p.hasPermission("starly.defaultkit." + config.getString("permissions.set"))) {
                    p.sendMessage(config.getMessage("messages.no_permission"));
                    return true;
                }
                if (args.length != 1) {
                    p.sendMessage(config.getMessage("messages.wrong_command"));
                    return true;
                }

                new DefaultKitData().setKit(p.getInventory());
                p.sendMessage(config.getMessage("messages.kit_set"));

                return true;
            }

            default -> {
                p.sendMessage(config.getMessage("messages.wrong_command"));
                return true;
            }
        }
    }
}

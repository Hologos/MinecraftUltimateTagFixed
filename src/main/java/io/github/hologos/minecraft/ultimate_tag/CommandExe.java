package io.github.hologos.minecraft.ultimate_tag;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandExe implements CommandExecutor {
    public Main plugin;

    public CommandExe(Main plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (label.equalsIgnoreCase("tag")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("[Minecraft Ultimate Tag] This command can only be used by the player.");
                return true;
            }

            Player player = (Player) sender;
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("nextGame")) {
                    this.plugin.nextGame(player, false);
                    return true;
                }

                if (args[0].equalsIgnoreCase("test")) {
                    this.plugin.startGame(player.getName(), player.getName(), player, true);
                    return true;
                }

                if (args[0].equalsIgnoreCase("help")) {
                    this.plugin.helpMenu(player);
                    return true;
                }
            }

            if (args.length == 3 && args[0].equalsIgnoreCase("start")) {
                this.plugin.startGame(args[1], args[2], player, false);
                return true;
            }

            this.plugin.helpMenu(player);
        }

        return false;
    }
}

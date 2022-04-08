package io.github.hologos.minecraft.ultimate_tag;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class TabCommand implements TabCompleter {
    public TabCommand() {
    }

    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> list = new ArrayList();
        if (sender instanceof Player) {
            if (args.length == 1) {
                if (label.equalsIgnoreCase("tag")) {
                    list.clear();
                    if ("help".indexOf(args[0].toLowerCase()) == 0) {
                        list.add("help");
                    }

                    if ("nextGame".indexOf(args[0].toLowerCase()) == 0) {
                        list.add("nextGame");
                    }

                    if ("start".indexOf(args[0].toLowerCase()) == 0) {
                        list.add("start");
                    }

                    return list;
                }
            } else if (args.length == 2) {
                Player[] players;
                Player p;
                int var8;
                int var9;
                Player[] var10;
                if (label.equalsIgnoreCase("tag")) {
                    if (args[0].equalsIgnoreCase("start")) {
                        list.clear();
                        players = new Player[Bukkit.getServer().getOnlinePlayers().size()];
                        Bukkit.getServer().getOnlinePlayers().toArray(players);
                        var10 = players;
                        var9 = players.length;

                        for(var8 = 0; var8 < var9; ++var8) {
                            p = var10[var8];
                            if (p.getDisplayName().toLowerCase().indexOf(args[1].toLowerCase()) == 0) {
                                list.add(p.getDisplayName());
                            }
                        }

                        return list;
                    }
                } else if (args.length == 3 && label.equalsIgnoreCase("tag") && args[0].equalsIgnoreCase("start")) {
                    list.clear();
                    players = new Player[Bukkit.getServer().getOnlinePlayers().size()];
                    Bukkit.getServer().getOnlinePlayers().toArray(players);
                    var10 = players;
                    var9 = players.length;

                    for(var8 = 0; var8 < var9; ++var8) {
                        p = var10[var8];
                        if (p.getDisplayName().toLowerCase().indexOf(args[2].toLowerCase()) == 0) {
                            list.add(p.getDisplayName());
                        }
                    }

                    return list;
                }
            }
        }

        return new ArrayList();
    }
}

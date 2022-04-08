package io.github.hologos.minecraft.ultimate_tag;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
    public Player hunter;
    public Player not;
    public boolean inGame;
    public ItemStack[] notInv;
    public ItemStack[] hunterInv;
    public UUID hunterNum;
    public UUID notNum;
    public int x;
    public int z;
    public FileConfiguration config;

    public Main() {
    }

    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);
        this.inGame = false;
        this.makeInvs();
        this.getCommand("tag").setExecutor(new CommandExe(this));
        this.getCommand("tag").setTabCompleter(new TabCommand());
        this.hunter = null;
        this.not = null;
        this.hunterNum = null;
        this.notNum = null;
        this.x = 0;
        this.z = 0;
        this.config = this.getConfig();
        this.config.options().copyDefaults(true);
        this.saveConfig();
    }

    public void onDisable() {
        System.out.println("Goodbye!");
    }

    public void nextGame(final Player p) {
        if (!this.inGame || p.getUniqueId() != this.hunter.getUniqueId() && p.getUniqueId() != this.not.getUniqueId()) {
            p.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "You are not in a game!\nUse /tag start <hunter> <target> to start a game!");
        } else {
            this.getServer().getScheduler().cancelTasks(this);
            this.hunterNum = this.hunter.getUniqueId();
            this.notNum = this.not.getUniqueId();
            this.hunter = this.getServer().getPlayer(this.notNum);
            this.not = this.getServer().getPlayer(this.hunterNum);
            this.hunter.setHealth(20.0D);
            this.not.setHealth(20.0D);
            this.hunter.setFoodLevel(20);
            this.not.setFoodLevel(20);
            this.x = this.getRand();
            this.z = this.getRand();
            p.getWorld().getWorldBorder().setCenter((double)this.x, (double)this.z);
            p.getWorld().getWorldBorder().setSize(160.0D);
            this.hunter.teleport(p.getWorld().getHighestBlockAt(this.x + 80 - 1, this.z + 80 - 1).getLocation());
            this.not.teleport(p.getWorld().getHighestBlockAt(this.x + 80 - 1, this.z + 80 - 1).getLocation());
            this.hunter.getInventory().setContents(this.hunterInv);
            this.not.getInventory().setContents(this.notInv);
            this.getServer().getScheduler().runTaskLater(this, new Runnable() {
                public void run() {
                    Main.this.hunter.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "Start!");
                    Main.this.not.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "Start!");
                }
            }, 0L);
            this.getServer().getScheduler().runTaskLater(this, new Runnable() {
                public void run() {
                    Main.this.hunter.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "1 Minute Left!");
                    Main.this.not.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "1 Minute Left!");
                }
            }, 1200L);
            this.getServer().getScheduler().runTaskLater(this, new Runnable() {
                public void run() {
                    Main.this.hunter.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "30 Seconds Left!");
                    Main.this.not.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "30 Seconds Left!");
                }
            }, 1800L);
            this.getServer().getScheduler().runTaskLater(this, new Runnable() {
                public void run() {
                    Main.this.hunter.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "15 Seconds Left!");
                    Main.this.not.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "15 Seconds Left!");
                }
            }, 2100L);
            this.getServer().getScheduler().runTaskLater(this, new Runnable() {
                public void run() {
                    Main.this.hunter.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "10 Seconds Left!");
                    Main.this.not.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "10 Seconds Left!");
                }
            }, 2200L);
            this.getServer().getScheduler().runTaskLater(this, new Runnable() {
                public void run() {
                    Main.this.hunter.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "9 Seconds Left!");
                    Main.this.not.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "9 Seconds Left!");
                }
            }, 2220L);
            this.getServer().getScheduler().runTaskLater(this, new Runnable() {
                public void run() {
                    Main.this.hunter.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "8 Seconds Left!");
                    Main.this.not.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "8 Seconds Left!");
                }
            }, 2240L);
            this.getServer().getScheduler().runTaskLater(this, new Runnable() {
                public void run() {
                    Main.this.hunter.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "7 Seconds Left!");
                    Main.this.not.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "7 Seconds Left!");
                }
            }, 2260L);
            this.getServer().getScheduler().runTaskLater(this, new Runnable() {
                public void run() {
                    Main.this.hunter.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "6 Seconds Left!");
                    Main.this.not.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "6 Seconds Left!");
                }
            }, 2280L);
            this.getServer().getScheduler().runTaskLater(this, new Runnable() {
                public void run() {
                    Main.this.hunter.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "5 Seconds Left!");
                    Main.this.not.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "5 Seconds Left!");
                }
            }, 2300L);
            this.getServer().getScheduler().runTaskLater(this, new Runnable() {
                public void run() {
                    Main.this.hunter.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "4 Seconds Left!");
                    Main.this.not.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "4 Seconds Left!");
                }
            }, 2320L);
            this.getServer().getScheduler().runTaskLater(this, new Runnable() {
                public void run() {
                    Main.this.hunter.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "3 Seconds Left!");
                    Main.this.not.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "3 Seconds Left!");
                }
            }, 2340L);
            this.getServer().getScheduler().runTaskLater(this, new Runnable() {
                public void run() {
                    Main.this.hunter.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "2 Seconds Left!");
                    Main.this.not.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "2 Seconds Left!");
                }
            }, 2360L);
            this.getServer().getScheduler().runTaskLater(this, new Runnable() {
                public void run() {
                    Main.this.hunter.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "1 Seconds Left!");
                    Main.this.not.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "1 Seconds Left!");
                }
            }, 2380L);
            this.getServer().getScheduler().runTaskLater(this, new Runnable() {
                public void run() {
                    p.getWorld().getWorldBorder().reset();
                    Main.this.hunter.sendMessage("" + ChatColor.RED + ChatColor.BOLD + Main.this.not.getDisplayName() + " has won this round!");
                    Main.this.not.sendMessage("" + ChatColor.RED + ChatColor.BOLD + Main.this.not.getDisplayName() + " has won this round!");
                }
            }, 2400L);
        }

    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {
        if (this.hunter != null) {
            if (this.not != null) {
                if (e.getEntity() instanceof Player) {
                    if (e.getDamager() instanceof Player) {
                        Player p = (Player)e.getEntity();
                        Player d = (Player)e.getDamager();
                        if (p.getUniqueId() == this.not.getUniqueId()) {
                            if (d.getUniqueId() == this.hunter.getUniqueId()) {
                                this.getServer().getScheduler().cancelTasks(this);
                                p.getWorld().getWorldBorder().reset();
                                this.hunter.sendMessage("" + ChatColor.RED + ChatColor.BOLD + this.hunter.getDisplayName() + " has won this round!");
                                this.not.sendMessage("" + ChatColor.RED + ChatColor.BOLD + this.hunter.getDisplayName() + " has won this round!");
                            }
                        }
                    }
                }
            }
        }
    }

    public void helpMenu(Player p) {
        p.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "Minecraft Ultimate Tag Commands: \n" + ChatColor.RED + "-- /tag help" + ChatColor.WHITE + " - shows this\n" + ChatColor.RED + "-- /tag start <hunter> <target>" + ChatColor.WHITE + " - starts new game\n" + ChatColor.RED + "-- /tag nextRound" + ChatColor.WHITE + " - starts next round");
    }

    public void startGame(String h, String n, Player p) {
        if (this.getServer().getPlayer(h) == null) {
            p.sendMessage("" + ChatColor.RED + ChatColor.BOLD + h + "is not a player!");
            if (this.getServer().getPlayer(n) == null) {
                p.sendMessage("" + ChatColor.RED + ChatColor.BOLD + n + "is not a player!");
            }

        } else {
            this.hunter = this.getServer().getPlayer(n);
            this.not = this.getServer().getPlayer(h);
            this.inGame = true;
            this.nextGame(p);
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        if (this.inGame) {
            if (e.getPlayer().getUniqueId() == this.hunter.getUniqueId()) {
                this.inGame = false;
                return;
            }

            if (e.getPlayer().getUniqueId() == this.not.getUniqueId()) {
                this.inGame = false;
                return;
            }
        }

    }

    public int getRand() {
        return Math.random() > 0.5D ? (int)(Math.random() * 8000.0D) : (int)(Math.random() * -8000.0D);
    }

    public void makeInvs() {
        this.notInv = new ItemStack[4];
        this.notInv[0] = new ItemStack(Material.DIAMOND_PICKAXE, 1);
        this.notInv[1] = new ItemStack(Material.DIAMOND_AXE, 1);
        this.notInv[2] = new ItemStack(Material.DIAMOND_SHOVEL, 1);
        this.notInv[3] = new ItemStack(Material.COBBLESTONE, 16);
        this.hunterInv = new ItemStack[5];
        this.hunterInv[0] = new ItemStack(Material.COMPASS, 1);
        this.hunterInv[1] = new ItemStack(Material.DIAMOND_PICKAXE, 1);
        this.hunterInv[2] = new ItemStack(Material.DIAMOND_AXE, 1);
        this.hunterInv[3] = new ItemStack(Material.DIAMOND_SHOVEL, 1);
        this.hunterInv[4] = new ItemStack(Material.COBBLESTONE, 16);
    }

    @EventHandler
    public void onDeath(EntityDeathEvent e) {
        if (this.hunter != null) {
            if (this.not != null) {
                if (e.getEntity() instanceof Player) {
                    Player p = (Player)e.getEntity();
                    if (p.getUniqueId() == this.hunter.getUniqueId()) {
                        this.getServer().getScheduler().cancelTasks(this);
                        p.getWorld().getWorldBorder().reset();
                        this.hunter.sendMessage("" + ChatColor.RED + ChatColor.BOLD + this.not.getDisplayName() + " has won this round!");
                        this.not.sendMessage("" + ChatColor.RED + ChatColor.BOLD + this.not.getDisplayName() + " has won this round!");
                    } else {
                        if (p.getUniqueId() == this.not.getUniqueId()) {
                            this.getServer().getScheduler().cancelTasks(this);
                            p.getWorld().getWorldBorder().reset();
                            this.hunter.sendMessage("" + ChatColor.RED + ChatColor.BOLD + this.hunter.getDisplayName() + " has won this round!");
                            this.not.sendMessage("" + ChatColor.RED + ChatColor.BOLD + this.hunter.getDisplayName() + " has won this round!");
                        }

                    }
                }
            }
        }
    }

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        if (this.hunter != null) {
            if (this.not != null) {
                if (e.getPlayer().getUniqueId() == this.hunter.getUniqueId()) {
                    Player p = e.getPlayer();
                    if (e.getMaterial() == Material.COMPASS) {
                        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                            p.setCompassTarget(this.not.getLocation());
                            p.sendMessage(ChatColor.RED + "Compass now pointing to " + this.not.getDisplayName() + ".  y=" + this.not.getLocation().getBlockY());
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        if (this.hunter != null) {
            if (this.not != null) {
                if (e.getPlayer().getUniqueId() == this.hunter.getUniqueId() || e.getPlayer().getUniqueId() == this.not.getUniqueId()) {
                    e.setRespawnLocation(e.getPlayer().getWorld().getHighestBlockAt(this.x, this.z).getLocation());
                }
            }
        }
    }
}

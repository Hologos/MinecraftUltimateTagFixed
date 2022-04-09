package io.github.hologos.minecraft.ultimate_tag;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
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

    protected int playgroundSize;
    protected int maxStartingDistance;
    protected int minStartingDistance;

    protected int winScore;
    protected int winDifference;
    protected boolean activeRound;

    protected Map<UUID, Integer> scores = new HashMap<>();

    public Main() {
        this.playgroundSize = 160;
        this.minStartingDistance = 20;
        this.maxStartingDistance = 60;
        this.winScore = 3;
        this.winDifference = 2;
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

    public void nextGame(final Player p, boolean test) {
        if (!this.inGame || p.getUniqueId() != this.hunter.getUniqueId() && p.getUniqueId() != this.not.getUniqueId()) {
            p.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "You are not in a game!\nUse /tag start <hunter> <target> to start a game!");
            return;
        }

        this.getServer().getScheduler().cancelTasks(this);
        this.hunterNum = this.hunter.getUniqueId();
        this.notNum = this.not.getUniqueId();
        this.hunter = this.getServer().getPlayer(this.notNum);
        this.not = this.getServer().getPlayer(this.hunterNum);
        this.hunter.setHealth(20.0D);
        this.not.setHealth(20.0D);
        this.hunter.setFoodLevel(30);
        this.not.setFoodLevel(30);
        this.hunter.getInventory().setHeldItemSlot(4); // force to start with compass selected
        this.not.getInventory().setContents(this.notInv);
        this.hunter.getInventory().setContents(this.hunterInv);
        this.activeRound = true;

        this.preparePlayground(p, test);

        this.createMessageTask("" + ChatColor.RED + ChatColor.BOLD + "Start!", 0);
        this.createMessageTask("" + ChatColor.RED + ChatColor.BOLD + "1 Minute Left!", 60);
        this.createMessageTask("" + ChatColor.RED + ChatColor.BOLD + "30 Seconds Left!", 60 + 30);
        this.createMessageTask("" + ChatColor.RED + ChatColor.BOLD + "15 Seconds Left!", 60 + 45);
        this.createMessageTask("" + ChatColor.RED + ChatColor.BOLD + "10 Seconds Left!", 60 + 50);
        this.createMessageTask("" + ChatColor.RED + ChatColor.BOLD + "9 Seconds Left!", 60 + 51);
        this.createMessageTask("" + ChatColor.RED + ChatColor.BOLD + "8 Seconds Left!", 60 + 52);
        this.createMessageTask("" + ChatColor.RED + ChatColor.BOLD + "7 Seconds Left!", 60 + 53);
        this.createMessageTask("" + ChatColor.RED + ChatColor.BOLD + "6 Seconds Left!", 60 + 54);
        this.createMessageTask("" + ChatColor.RED + ChatColor.BOLD + "5 Seconds Left!", 60 + 55);
        this.createMessageTask("" + ChatColor.RED + ChatColor.BOLD + "4 Seconds Left!", 60 + 56);
        this.createMessageTask("" + ChatColor.RED + ChatColor.BOLD + "3 Seconds Left!", 60 + 57);
        this.createMessageTask("" + ChatColor.RED + ChatColor.BOLD + "2 Seconds Left!", 60 + 58);
        this.createMessageTask("" + ChatColor.RED + ChatColor.BOLD + "1 Seconds Left!", 60 + 59);
        this.createEndGameTask(p,60 + 60);
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {
        if (this.hunter == null || this.not == null) {
            return;
        }

        if (!(e.getEntity() instanceof Player) || !(e.getDamager() instanceof Player)) {
            return;
        }

        Player p = (Player) e.getEntity();
        Player d = (Player) e.getDamager();

        if (p.getUniqueId() != this.not.getUniqueId() || d.getUniqueId() != this.hunter.getUniqueId()) {
            return;
        }

        this.endRound(p, this.hunter, false, false);
    }

    public void helpMenu(Player p) {
        p.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "Minecraft Ultimate Tag Commands: \n"
            + ChatColor.RED + "-- /tag help" + ChatColor.WHITE + " - shows this\n"
            + ChatColor.RED + "-- /tag start <hunter> <target>" + ChatColor.WHITE + " - starts new game\n"
            + ChatColor.RED + "-- /tag nextRound" + ChatColor.WHITE + " - starts next round");
    }

    public void startGame(String h, String n, Player p, boolean test) {
        if (this.getServer().getPlayer(h) == null) {
            p.sendMessage("" + ChatColor.RED + ChatColor.BOLD + h + " is not a player!");
            return;
        }

        if (this.getServer().getPlayer(n) == null) {
            p.sendMessage("" + ChatColor.RED + ChatColor.BOLD + n + " is not a player!");
            return;
        }

        this.hunter = this.getServer().getPlayer(n);
        this.not = this.getServer().getPlayer(h);
        this.inGame = true;
        this.scores.clear();
        this.scores.put(this.getServer().getPlayer(h).getUniqueId(), 0);
        this.scores.put(this.getServer().getPlayer(n).getUniqueId(), 0);
        this.nextGame(p, test);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        if(!this.inGame) {
            return;
        }

        if(this.hunter == null || this.not == null) {
            return;
        }

        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();

        if (uuid != this.hunter.getUniqueId() && uuid != this.not.getUniqueId()) {
            return;
        }

        if (uuid == this.hunter.getUniqueId()) {
            this.endRound(p, this.not, true, false);
        } else if (uuid == this.not.getUniqueId()) {
            this.endRound(p, this.hunter, true, false);
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
        this.hunterInv[0] = new ItemStack(Material.DIAMOND_PICKAXE, 1);
        this.hunterInv[1] = new ItemStack(Material.DIAMOND_AXE, 1);
        this.hunterInv[2] = new ItemStack(Material.DIAMOND_SHOVEL, 1);
        this.hunterInv[3] = new ItemStack(Material.COBBLESTONE, 16);
        this.hunterInv[4] = new ItemStack(Material.COMPASS, 1);
    }

    @EventHandler
    public void onDeath(EntityDeathEvent e) {
        if(!this.inGame) {
            return;
        }

        if(!(e.getEntity() instanceof Player)) {
            return;
        }

        if(this.hunter == null || this.not == null) {
            return;
        }

        Player p = (Player) e.getEntity();
        UUID uuid = p.getUniqueId();

        if (uuid == this.hunter.getUniqueId()) {
            this.endRound(p, this.not, true, false);
        } else if (uuid == this.not.getUniqueId()) {
            this.endRound(p, this.hunter, true, false);
        }
    }

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        if(!this.inGame) {
            return;
        }

        if(this.hunter == null || this.not == null) {
            return;
        }

        if (e.getMaterial() != Material.COMPASS) {
            return;
        }

        Player p = e.getPlayer();

        if (p.getUniqueId() != this.hunter.getUniqueId()) {
            return;
        }

        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        p.setCompassTarget(this.not.getLocation());
        p.sendMessage(ChatColor.RED + "Compass now pointing to " + this.not.getDisplayName() + ".  y=" + this.not.getLocation().getBlockY());
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        if(!this.inGame) {
            return;
        }

        if(this.hunter == null || this.not == null) {
            return;
        }

        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();

        if (uuid != this.hunter.getUniqueId() && uuid != this.not.getUniqueId()) {
            return;
        }

        Location respawnLocation = p.getWorld().getHighestBlockAt(this.x, this.z).getLocation();
        e.setRespawnLocation(respawnLocation);
        p.getServer().broadcastMessage("DEBUG: Respawning " + p.getDisplayName()
                + " at x= " + respawnLocation.getX() + ", y=" + respawnLocation.getY() + ", z=" + respawnLocation.getZ());
    }

    protected void setPlayersPositions(final Player p) {
        Location[] locations = this.generatePlayersLocations(p, false);

        this.hunter.teleport(locations[0]);
        this.not.teleport(locations[1]);
    }

    protected Location[] generatePlayersLocations(final Player p, boolean test) {
        Location hl, tl;
        double distance, x1, x2, z1, z2;
        boolean satisfactoryDistance;

        // fix hunter's location in place
        hl = this.getRandomLocation(p);
        x1 = hl.getX();
        z1 = hl.getZ();

        if(test) {
            p.sendMessage("Hunter location: x=" + x1 + ", z=" + z1);
        }

        this.announceMessage(ChatColor.RED + "" + ChatColor.BOLD + "Searching for positions not too close and not too far. Expect lag!");

        do {
            tl = this.getRandomLocation(p);
            x2 = tl.getX();
            z2 = tl.getZ();
            distance = Math.hypot(Math.abs(z2 - z1), Math.abs(x2 - x1));

            satisfactoryDistance = distance < this.maxStartingDistance && distance > this.minStartingDistance;

            if(test) {
                p.sendMessage("Target location: x=" + x2 + ", z=" + z2);
            }

            if(satisfactoryDistance) {
                this.announceMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "Suitable location found (distance = " + ((int) distance) + ").");
            }
        } while (!satisfactoryDistance);

        return new Location[] { hl, tl };
    }

    protected Location getRandomLocation(final Player p) {
        int x = this.getRandomCoord(this.x);
        int z = this.getRandomCoord(this.z);
        Location location = p.getWorld().getHighestBlockAt(x, z).getLocation();
        location.setY(location.getY() + 1); // fixes spawning in the ground

        return location;
    }

    protected int getRandomCoord(int offset) {
        return offset + ((int) (Math.random() * this.playgroundSize)) - (this.playgroundSize / 2) - 1;
    }

    protected void setTestPositions(final Player p) {
        Location[] locations = this.generatePlayersLocations(p, true);

        this.hunter.teleport(locations[0]);
        p.getWorld().spawnEntity(locations[1], EntityType.BLAZE);
        p.setCompassTarget(locations[1]);
    }

    public void endRound(Player p, Player winner, boolean awardPoint, boolean endGame) {
        if(!this.activeRound) {
            return;
        }

        this.getServer().getScheduler().cancelTasks(this);
        p.getWorld().getWorldBorder().reset();

        UUID uuid = winner.getUniqueId();
        int score = this.scores.get(uuid);

        if(awardPoint) {
            score += 1;
            this.scores.put(uuid, score);
        } else {
            this.announceMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "Not awarding a point.");
        }

        int scoreHunter = this.scores.get(this.hunter.getUniqueId());
        int scoreNot = this.scores.get(this.not.getUniqueId());

        if(score >= this.winScore && Math.abs(scoreHunter - scoreNot) >= this.winDifference) {
            this.announceMessage(" ");
            this.announceMessage(" ");
            this.announceMessage(" ");
            this.announceMessage("" + ChatColor.RED + ChatColor.BOLD + winner.getDisplayName() + " has won this game!");
            endGame = true;
        } else {
            this.announceMessage(" ");
            this.announceMessage(" ");
            this.announceMessage(ChatColor.RED + "" + ChatColor.BOLD + winner.getDisplayName() + " has won this round!");
        }

        this.announceMessage(" ");
        this.announceMessage("\n" + ChatColor.BLUE + ChatColor.BOLD + "=== Score ===\n"
                + ChatColor.RED + this.hunter.getDisplayName() + ": " + scoreHunter + "\n"
                + ChatColor.GOLD + this.not.getDisplayName() + ": " + scoreNot + "\n \n");

        this.activeRound = false;

        if(endGame) {
            this.endGame(p);
        }
    }

    public void endGame(Player p) {
        this.inGame = false;
        this.activeRound = false;
    }

    protected void announceMessage(String message) {
        this.hunter.sendMessage(message);
        this.not.sendMessage(message);
    }

    protected long convertSecondsToTicks(int seconds) {
        return 20L * seconds;
    }

    protected void createMessageTask(String message, int secondsDelay) {
        this.getServer().getScheduler().runTaskLater(this, new Runnable() {
            public void run() {
                Main.this.announceMessage(message);
            }
        }, this.convertSecondsToTicks(secondsDelay));
    }

    private void createEndGameTask(Player p, int secondsDelay) {
        this.getServer().getScheduler().runTaskLater(this, new Runnable() {
            public void run() {
                Main.this.endRound(p, Main.this.not, true, false);
            }
        }, this.convertSecondsToTicks(secondsDelay));
    }

    protected void preparePlayground(Player p, boolean test) {
        this.x = this.getRand();
        this.z = this.getRand();

        p.getWorld().getWorldBorder().setCenter((double)this.x, (double)this.z);
        p.getWorld().getWorldBorder().setSize(this.playgroundSize);


        if(!test) {
            this.setPlayersPositions(p);
        } else {
            this.setTestPositions(p);
        }
    }
}

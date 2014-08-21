/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.meloncraft.league;

import com.meloncraft.league.Arena.ArenaHandler;
import com.meloncraft.league.Arena.Turret;
import com.meloncraft.league.Champions.Champion;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

/**
 *
 * @author Gary
 */
public class GeneralListeners implements Listener {
    public static Teams teams;
    //League plugin;
    public Location blueSwitcher, purpleSwitcher;
    public World world;
    Player player;
    League plugin;
    ArenaHandler arena;
    private Champion tempChampion;
    Location tempLoc;
    private int count;
    
    public GeneralListeners(League plug, ArenaHandler are, Teams tea) {
        plugin = plug;
        arena = are;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        teams = tea;
        count = 0;
        
        FileConfiguration config = plugin.getConfig();
        world = plugin.mainWorld;
        blueSwitcher = new Location(world,  plugin.getConfig().getDouble("blue-switcher.x"),  plugin.getConfig().getDouble("blue-switcher.y"),  plugin.getConfig().getDouble("blue-switcher.z"));
        purpleSwitcher = new Location(world,  plugin.getConfig().getDouble("purple-switcher.x"),  plugin.getConfig().getDouble("purple-switcher.y"),   plugin.getConfig().getDouble("purple-switcher.z"));
    }
    
    
    //Prevent Items from dropping
    @EventHandler
    public void onItemDrop(ItemSpawnEvent event) {
        
        //if (event.getEntityType() == EntityType.DROPPED_ITEM) {
            event.setCancelled(true);
        //}
        
    }
    
    @EventHandler
    public void onPickupItem(PlayerPickupItemEvent event) {
        event.setCancelled(true);
    }
    
    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        double respawnTime = 5;
        int totalRespawnTime = 5;
        respawnTime += teams.getChampion(event.getEntity()).getLevel() * 2.5;
        if (arena.clock > 20 * 60) {
            respawnTime = respawnTime + ((respawnTime / 50) * ((arena.clock / 60) - 20));
            totalRespawnTime = (int) respawnTime;
        }
        else {
            totalRespawnTime = (int) respawnTime;
        }
        
        teams.getChampion(event.getEntity()).setRespawnTime(totalRespawnTime);
        //begin respawn timer
    }
    
    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (arena.started) {
            //check if champion is respawning and prevent them from leaving spawn.
            if (teams.getChampion(event.getPlayer()).getRespawnTime() > 0) {
                
                if (teams.getTeam(event.getPlayer()).equals("blue")) {
                    if (event.getTo().distance(arena.blueSpawn) > plugin.getConfig().getDouble("spawn-radius")) {
                        if (count % 2 != 1) {
                            event.getPlayer().sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "[DEAD] " + ChatColor.GOLD + "You must wait " + ChatColor.GREEN + teams.getChampion(event.getPlayer()).getRespawnTime() + ChatColor.GOLD + " to exit Spawn! Go buy items!");
                            event.setTo(event.getFrom());
                            count++;
                        }
                    }
                }
                if (teams.getTeam(event.getPlayer()).equals("purple")) {
                    if (event.getTo().distance(arena.purpleSpawn) > plugin.getConfig().getDouble("spawn-radius")) {
                        if (count % 2 != 1) {
                            event.getPlayer().sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "[DEAD] " + ChatColor.GOLD + "You must wait " + ChatColor.GREEN + teams.getChampion(event.getPlayer()).getRespawnTime() + ChatColor.GOLD + " to exit Spawn! Go buy items!");
                            event.setTo(event.getFrom());
                        }
                    }
                }
            }
            
            if (teams.getChampion(event.getPlayer()).getRecalling()) {
                teams.getChampion(event.getPlayer()).setRecalling(false);
                event.getPlayer().sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "[WARNING]: " + ChatColor.GOLD + " Recalling Interrupted!");
            }
        }
    }
    
    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        if (arena.started) {
            if (teams.getTeam(event.getPlayer()).equals("blue")) {
                event.setRespawnLocation(arena.blueSpawn);
            }
            else if (teams.getTeam(event.getPlayer()).equals("purple")) {
                event.setRespawnLocation(arena.purpleSpawn);
            }
        }
    }
    
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        
        player = event.getPlayer();
        
        
        
        
        //_________
        //RIGHT CLICK ACTIONS
        
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
        //BLUE switcher clicked
            if (event.getClickedBlock().getLocation().getX() == blueSwitcher.getX() && event.getClickedBlock().getLocation().getY() == blueSwitcher.getY() && event.getClickedBlock().getLocation().getZ() == blueSwitcher.getZ()) {
                if (teams.getPurpleQueue().contains(player)) {
                    teams.removePurpleQueue(player);
                    player.sendMessage("You are no longer on the Join-Purple Queue!");
                }
                else {
                    if (teams.getTeamSize("purple") < 5) {
                        if (teams.blueChampions[teams.blueTeam.lastIndexOf(player)] != null) {
                                tempChampion = teams.getChampion(player);
                                teams.removeChampion(player);
                                teams.removePlayer(player);
                                teams.addPurple(player);
                                teams.setChampion(player, tempChampion);
                            }
                            //dont't set champion because no champ selected
                            else {
                                teams.removePlayer(player);
                                teams.addPurple(player);
                            }
                            
                        if (!teams.getBlueQueue().isEmpty()) {
                            //set champions
                            if (teams.purpleChampions[teams.purpleTeam.lastIndexOf(teams.getBlueQueue(0))] != null) {
                                tempChampion = teams.getChampion(teams.getBlueQueue(0));
                                teams.removeChampion(teams.getBlueQueue(0));
                                teams.getBlueQueue(0).sendMessage("You have been taken off the Queue and moved to BLUE!");
                                teams.addBlue(teams.removeBlueQueue());
                                teams.setChampion(teams.getBlueQueue(0), tempChampion);
                            }
                            //dont't set champion because no champ selected
                            else {
                                teams.getBlueQueue(0).sendMessage("You have been taken off the Queue and moved to BLUE!");
                                teams.addBlue(teams.removeBlueQueue());
                            }
                            
                        }
                    }
                    else {
                        player.sendMessage("The PURPLE team is full! You have been added to the switch queue.");
                    }
                }
            }

            //purple switcher clicked
            if (event.getClickedBlock().getLocation().getX() == purpleSwitcher.getX() && event.getClickedBlock().getLocation().getY() == purpleSwitcher.getY() && event.getClickedBlock().getLocation().getZ() == purpleSwitcher.getZ()) {
                if (teams.getPurpleQueue().contains(player)) {
                    teams.removePurpleQueue(player);
                    player.sendMessage("You are no longer on the Join-Purple Queue!");
                }
                else {
                    if (teams.getTeamSize("blue") < 5) {
                        if (teams.purpleChampions[teams.purpleTeam.lastIndexOf(player)] != null) {
                                tempChampion = teams.getChampion(player);
                                teams.removeChampion(player);
                                teams.removePlayer(player);
                                teams.addBlue(player);
                                teams.setChampion(player, tempChampion);
                            }
                            //dont't set champion because no champ selected
                            else {
                                teams.removePlayer(player);
                                teams.addBlue(player);
                            }
                            
                        if (!teams.getPurpleQueue().isEmpty()) {
                            //set champions
                            if (teams.blueChampions[teams.blueTeam.lastIndexOf(teams.getPurpleQueue(0))] != null) {
                                tempChampion = teams.getChampion(teams.getPurpleQueue(0));
                                teams.removeChampion(teams.getPurpleQueue(0));
                                teams.getPurpleQueue(0).sendMessage("You have been taken off the Queue and moved to PURPLE!");
                                teams.addPurple(teams.removePurpleQueue());
                                teams.setChampion(teams.getPurpleQueue(0), tempChampion);
                            }
                            //dont't set champion because no champ selected
                            else {
                                teams.getPurpleQueue(0).sendMessage("You have been taken off the Queue and moved to BLUE!");
                                teams.addPurple(teams.removePurpleQueue());
                            }
                        }
                    }
                    else {
                        player.sendMessage("The BLUE team is full! You have been added to the switch queue.");
                    }
                }
            }
        }



        //_______
        //LEFT CLICK ACTIONS
        
        else if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
            Turret turret = null;
            //boolean hitTurret = false;
            
            //--------------
            //check if tower is clicked
            if (arena.started) {
                if (teams.getTeam(player).equals("blue")) {
                    turret = arena.isPurpleTurret(event.getClickedBlock().getLocation());
                    if (turret != null) {
                        if (teams.getChampion(player) != null) {
                            turret.hit(teams.getChampion(player).getDamage());
                            tempLoc = event.getClickedBlock().getLocation().add(.5, 0, .5);
                            plugin.mainWorld.createExplosion(tempLoc, (float) 0.01, false);
                            //hitTurret = true;
                        }
                        player.sendMessage("YOU HIT THE TURRET");
                    }
                    if (arena.isBlueTurret(event.getClickedBlock().getLocation()) != null) {
                        player.sendMessage(ChatColor.GOLD + "That is a friendly tower! Attack " + ChatColor.LIGHT_PURPLE + "PURPLE " + ChatColor.GOLD + "towers!");
                    }
                }

                else if (teams.getTeam(player).equals("purple")) {
                    turret = arena.isBlueTurret(event.getClickedBlock().getLocation());
                    if (turret != null) {
                        if (teams.getChampion(player) != null) {
                            turret.hit(teams.getChampion(player).getDamage());
                            tempLoc = event.getClickedBlock().getLocation().add(.5, 0, .5);
                            plugin.mainWorld.createExplosion(tempLoc, (float) 0.01, false);
                            //hitTurret = true;
                        }
                        player.sendMessage("YOU HIT THE TURRET");
                    }
                    if (arena.isPurpleTurret(event.getClickedBlock().getLocation()) != null) {
                        player.sendMessage(ChatColor.GOLD + "That is a friendly tower! Attack " + ChatColor.BLUE + "BLUE " + ChatColor.GOLD + "towers!");
                    }
                }

                //---------------
                else {
                    //regular attack
                }
            }
        }
        
        //_______
        //RIGHT CLICK AIR ACTIONS
        
        if (event.getAction() == Action.RIGHT_CLICK_AIR) {
            
        }
    }
}
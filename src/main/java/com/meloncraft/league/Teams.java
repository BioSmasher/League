/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.meloncraft.league;

import com.meloncraft.league.Arena.Minions.MinionPopulation;
import com.meloncraft.league.Champions.Champion;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

/**
 *
 * @author Gary
 */
public class Teams {
    public List<Player> blueTeam;
    public List<Player> purpleTeam;
    public Champion[] blueChampions;
    public Champion[] purpleChampions;
    public int[] purpleRespawnTime, blueRespawnTime;
    private static List<Player> blueQueue, purpleQueue;
    private static double x, y, z;
    public static Location blueLobby, purpleLobby;
    public List<World> worlds;
    public World world;
    League plugin;
    public MinionPopulation population;
    
    public Teams(League plug) {
        blueTeam = new ArrayList<Player>();
        purpleTeam  = new ArrayList<Player>();
        blueQueue = new ArrayList<Player>();
        purpleQueue  = new ArrayList<Player>();
        blueChampions = new Champion[5];
        purpleChampions = new Champion[5];
        blueRespawnTime = new int[5];
        purpleRespawnTime = new int[5];
        int count = 0;
        plugin = plug;
        world = plugin.mainWorld;
        population = new MinionPopulation(plugin, this);
        setLobby(plugin.getConfig().getDouble("blue-lobby.x"), plugin.getConfig().getDouble("blue-lobby.y"), plugin.getConfig().getDouble("blue-lobby.z"), plugin.getConfig().getDouble("purple-lobby.x"), plugin.getConfig().getDouble("purple-lobby.y"), plugin.getConfig().getDouble("purple-lobby.z"), plugin.getConfig().getDouble("blue-lobby.pitch"), plugin.getConfig().getDouble("blue-lobby.yaw"), plugin.getConfig().getDouble("purple-lobby.pitch"), plugin.getConfig().getDouble("purple-lobby.yaw"), world);
    }
    
    public static void setLobby(double x1, double y1, double z1, double x2, double y2, double z2, double pitch1, double yaw1, double pitch2, double yaw2, World world) {
        blueLobby = new Location(world, x1, y1, z1, (float) yaw1, (float) pitch1);
        purpleLobby = new Location(world, x2, y2, z2, (float) yaw2, (float) pitch2);
    }
    
    public String getSmallerTeam() {
        if (getTeamSize("blue") <= getTeamSize("purple")) {
            return "blue";
        }
        else {
            return "purple";
        }
    }
    
    public int getTeamSize(String team) {
        if (team.equalsIgnoreCase("blue")) {
            return blueTeam.size();

        }
        else {
            return purpleTeam.size();
        }
    }
    
    public boolean addBlue(Player player) {
        if (getTeamSize("blue") < 5) {
            blueLobby.setWorld(player.getWorld());
            blueTeam.add(player);
            player.sendMessage("You have joined the Blue Team!");
            plugin.getLogger().info(player.getName() + " has Joined the Blue Team");
            player.teleport(blueLobby);
            return true;
        }
        else {
            return false;
        }
        
    }
    
    public boolean addPurple(Player player) {
        if (getTeamSize("purple") < 5) {
            purpleLobby.setWorld(plugin.mainWorld);
            purpleTeam.add(player);
            player.sendMessage("You have joined the Purple Team!");
            plugin.getLogger().info(player.getName() + " has Joined the Purple Team");
            player.teleport(purpleLobby);
            return true;
        }
        else {
            return false;
        }
        
    }
    
    public int getBlueSize() {
        return blueTeam.size();
    }
    
    public int getPurpleSize() {
        return purpleTeam.size();
    }
    
    public List<Player> getBlueTeam() {
        return blueTeam;
    }
    
    public List<Player> getPurpleTeam() {
        return purpleTeam;
    }
    
    public Player getBlueTeam(int num) {
        return blueTeam.get(num);
    }
    
    public Player getPurpleTeam(int num) {
        return purpleTeam.get(num);
    }
    
    public boolean removePlayer(Player player) {
        if (blueTeam.contains(player)) {
            blueTeam.remove(player);
            return true;
        }
        else if (purpleTeam.contains(player)) {
            purpleTeam.remove(player);
            return true;
        }
        else {
            return false;
        }
    }
    
    public String getTeam(Player player) {
        if (blueTeam.contains(player)) {
            return "blue";
        }
        else if (purpleTeam.contains(player)){
            return "purple";
        }
        else {
            return null;
        }
    }
    
    public String getTeam(Entity entity) {
        if (entity != null) {
            if (entity instanceof Player) {
                return getTeam((Player) entity);
            }
            else {
                return population.getTeam((LivingEntity) entity);
            }
        
        }
        return null;
        
    }
    
    
    
    
    
    
    
    
    
    
    
    //-------------------------
    //adds a player to the queue to join Blue
    public static void addBlueQueue(Player player) {
        blueQueue.add(player);
    }
    //adds a player to the queue to join Purple
    public static void addPurpleQueue(Player player) {
        purpleQueue.add(player);
    }
    //---------------------------
    public static List<Player> getBlueQueue() {
        return blueQueue;
    }
    public static List<Player> getPurpleQueue() {
        return purpleQueue;
    }
    //----------------------------
    public static Player removeBlueQueue() {
        return blueQueue.remove(0);
    }
    public static Player removePurpleQueue() {
        return purpleQueue.remove(0);
    }
    
    public static boolean removeBlueQueue(Player player) {
        return blueQueue.remove(player);
    }
    public static boolean removePurpleQueue(Player player) {
        return purpleQueue.remove(player);
    }
    //----------------------------
    public static Player getBlueQueue(int i) {
        return blueQueue.get(i);
    }
    public static Player getPurpleQueue(int i) {
        return purpleQueue.get(i);
    }
    
    
    
    
    
    
    //----------------------------
    //Champion storage
    public void setRandomChampion(Player player) {
        String champ = "";
            Random gen = new Random();
            
            switch (gen.nextInt(2)) {
                case 0: champ = "Ashe";
                    break;
                case 1: champ = "Master Yi";
                    break;
            }
            
            setChampion(player, champ);
    }
    
    public void setChampion(Player player, String champion) {
        if (getTeam(player).equals("blue")) {
            blueChampions[blueTeam.lastIndexOf(player)] = new Champion(player, champion, "blue", plugin);
            player.sendMessage("You have selected " + champion);
        }
        else {
            purpleChampions[purpleTeam.lastIndexOf(player)] = new Champion(player, champion, "purple", plugin);
            player.sendMessage("You have selected " + champion);
        }
    }
    
    public void setChampion(Player player, Champion champion) {
        if (getTeam(player).equals("blue")) {
            blueChampions[blueTeam.lastIndexOf(player)] = champion;
        }
        else {
            purpleChampions[purpleTeam.lastIndexOf(player)] = champion;
        }
    }
    
    public void removeChampion(Player player) {
        if (getTeam(player).equals("blue")) {
            blueChampions[blueTeam.lastIndexOf(player)] = null;
        }
        else {
            purpleChampions[purpleTeam.lastIndexOf(player)] = null;
        }
    }
    public Champion getChampion(Player player) {
        if (getTeam(player).equals("blue")) {
            if (blueTeam.lastIndexOf(player) != -1) {
                return blueChampions[blueTeam.lastIndexOf(player)];
            }
        }
        else {
            if (purpleTeam.lastIndexOf(player) != -1) {
                return purpleChampions[purpleTeam.lastIndexOf(player)];
            }
        }
        return null;
    }
    public Champion[] getBlueChampions() {
        return blueChampions;
    }
    
    public Champion[] getPurpleChampions() {
        return purpleChampions;
    }
}

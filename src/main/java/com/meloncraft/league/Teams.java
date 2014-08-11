/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.meloncraft.league;

import java.util.List;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 *
 * @author Gary
 */
public class Teams {
    private static Player[] blueTeam;
    private static Player[] purpleTeam;
    private static List<Player> blueQueue, purpleQueue;
    private static int count;
    private static int emptySlot, emptySlot2;
    private static double x, y, z;
    private static Location blueLobby, purpleLobby;
    public List<World> worlds;
    public World mainWorld;
    League plugin;
    
    
    public Teams() {
        blueTeam  = new Player[4];
        purpleTeam  = new Player[4];
        int count = 0;
        emptySlot = 0;
        
    }
    
    public static void setLobby(double x1, double y1, double z1, double x2, double y2, double z2, World world) {
        blueLobby = new Location(world, x1, y1, z1);
        purpleLobby = new Location(world, x2, y2, z2);
    }
    
    public static int getEmptySlot(String team) {
        count = 0;
        if (team.equalsIgnoreCase("blue")) {
            while (count < 5) {
                if (blueTeam[count] == null) {
                    emptySlot = count;
                    count = 10;
                }
                else {
                    count++;
                    emptySlot = 5;
                }
            }
            return emptySlot;
        }
        else {
            while (count < 5) {
                if (purpleTeam[count] == null) {
                    emptySlot = count;
                    count = 10;
                }
                else {
                    count++;
                    emptySlot = 5;
                }
            }
            return emptySlot;
        }
        
    }
    
    public static int getTeamSize(String team) {
        count = 0;
        emptySlot = 0;
        if (team.equalsIgnoreCase("blue")) {
            while (count < 5) {
                if (blueTeam[count] == null) {
                    count++;
                }
                else {
                    count++;
                    emptySlot++;
                }
            }
            return emptySlot;
        }
        else {
            while (count < 5) {
                if (purpleTeam[count] == null) {
                    count++;
                }
                else {
                    count++;
                    emptySlot++;
                }
            }
            return emptySlot;
        }
    }
    
    public static boolean addBlue(Player player) {
        emptySlot2 = getEmptySlot("blue");
        if (emptySlot2 < 5) {
            blueTeam[emptySlot2] = player;
            player.sendMessage("You have joined the Blue Team!");
            player.teleport(blueLobby);
            return true;
        }
        else {
            return false;
        }
        
    }
    
    public static boolean addPurple(Player player) {
        emptySlot2 = getEmptySlot("purple");
        if (emptySlot2 < 5) {
            purpleTeam[emptySlot2] = player;
            player.sendMessage("You have joined the Purple Team!");
            player.teleport(blueLobby);
            return true;
        }
        else {
            return false;
        }
        
    }
    
    public static int getBlueSize() {
        emptySlot2 = getTeamSize("blue");
        return emptySlot2;
    }
    
    public static int getPurpleSize() {
        emptySlot2 = getTeamSize("purple");
        return emptySlot2;
    }
    
    public static Player[] getBlueTeam() {
        return blueTeam;
    }
    
    public static Player[] getPurpleTeam() {
        return purpleTeam;
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
    
}

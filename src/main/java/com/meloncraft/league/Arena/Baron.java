/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.meloncraft.league.Arena;

import net.minecraft.server.v1_7_R4.EntityWither;
import net.minecraft.server.v1_7_R4.World;

/**
 *
 * @author Gary
 */
public class Baron extends EntityWither{
    World world;
    
    public Baron(World world) {
        super(world);
    }
}

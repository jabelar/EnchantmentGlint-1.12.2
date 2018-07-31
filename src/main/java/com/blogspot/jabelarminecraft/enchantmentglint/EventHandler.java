/**
    Copyright (C) 2017 by jabelar

    This file is part of jabelar's Minecraft Forge modding examples; as such,
    you can redistribute it and/or modify it under the terms of the GNU
    General Public License as published by the Free Software Foundation,
    either version 3 of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    For a copy of the GNU General Public License see <http://www.gnu.org/licenses/>.
*/

package com.blogspot.jabelarminecraft.enchantmentglint;

import com.blogspot.jabelarminecraft.enchantmentglint.init.ModConfig;

import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * This class contains many of the event handling methods for events posted to the event bus.
 * Registry events are contained in separate classes in the init subpackage. It is important that
 * this class is annotated as an event bus subscriber and that the methods should be all static.
 */
@EventBusSubscriber(modid = MainMod.MODID)
public class EventHandler
{
    /**
     * Event handling method that is called automatically by Forge. It handles when
     * the mod configuration fields are changed.
     *
     * @param event the event args
     */
    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public static void onEvent(OnConfigChangedEvent event)
    {
        // DEBUG
        System.out.println("OnConfigChangedEvent");
        if (event.getModID().equals(MainMod.MODID))
        {
            System.out.println("Syncing config for mod =" + event.getModID());
            ModConfig.config.save();
            ModConfig.syncConfig();
        }
    }
}

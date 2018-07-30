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
import com.blogspot.jabelarminecraft.enchantmentglint.proxy.IProxy;
import com.blogspot.jabelarminecraft.enchantmentglint.utilities.Utilities;

import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

// TODO: Auto-generated Javadoc
/**
 * This is the main file for the mod, as it has the mod annotation
 */
@Mod(   modid = MainMod.MODID,
        name = MainMod.MODNAME,
        version = MainMod.MODVERSION,
        guiFactory = "com.blogspot.jabelarminecraft." + MainMod.MODID + ".client.gui.GuiFactory",
        acceptedMinecraftVersions = "[1.12]",
        updateJSON = "https://raw.githubusercontent.com/jabelar/ExampleMod-1.12/master/src/main/resources/versionChecker.json"
        )
public class MainMod
{
    public static final String MODID = "enchantmentglint";
    public static final String MODNAME = "Jabelar's Truly Magical Enchantment Glint";
    public static final String MODVERSION = "1.0.0";
    public static final String MODDESCRIPTION = "Control the colors of your enchantments.";
    public static final String MODAUTHOR = "jabelar";
    public static final String MODCREDITS = "Jnaejnae";
    public static final String MODURL = "www.jabelarminecraft.blogspot.com";
    public static final String MODLOGO = "modconfigraphic.png";

    static
    {
        FluidRegistry.enableUniversalBucket();
    }

    // instantiate the mod
    @Instance(MODID)
    public static MainMod instance;

    // Says where the client and server 'proxy' code is loaded.
    @SidedProxy(clientSide = "com.blogspot.jabelarminecraft.enchantmentglint.proxy.ClientProxy",
            serverSide = "com.blogspot.jabelarminecraft.enchantmentglint.proxy.ServerProxy")
    public static IProxy proxy;

    /**
     * Pre-Initialization FML Life Cycle event handling method which is automatically
     * called by Forge. It must be annotated as an event handler.
     *
     * @param event the event
     */
    @EventHandler
    // preInit "Run before anything else. Read your config, create blocks, items, etc, and register them with the GameRegistry."
    public void preInit(FMLPreInitializationEvent event)
    {
        // DEBUG
        System.out.println("preInit() " + event.getModMetadata().name);

        Utilities.setModInfo(event);
        ModConfig.initConfig(event); // load configuration before doing anything else that may be controlled by it.
        // register stuff

        proxy.preInit(event);
    }

    /**
     * Initialization FML Life Cycle event handling method which is automatically
     * called by Forge. It must be annotated as an event handler.
     *
     * @param event the event
     */
    @EventHandler
    // Do your mod setup. Build whatever data structures you care about.
    // Register network handlers
    public void init(FMLInitializationEvent event)
    {
        // DEBUG
        System.out.println("init()");
                
        proxy.init(event);
    }

    /**
     * Post-Initialization FML Life Cycle event handling method which is automatically
     * called by Forge. It must be annotated as an event handler.
     *
     * @param event the event
     */
    @EventHandler
    // postInit "Handle interaction with other mods, complete your setup based on this."
    public void postInit(FMLPostInitializationEvent event)
    {
        // DEBUG
        System.out.println("postInit()");

        proxy.postInit(event);
    }


    /**
     * Fml life cycle.
     *
     * @param event the event
     */
    @EventHandler
    public void serverStarting(FMLServerStartingEvent event)
    {
        // DEBUG
        System.out.println("Server starting");

        proxy.serverStarting(event);
    }
}

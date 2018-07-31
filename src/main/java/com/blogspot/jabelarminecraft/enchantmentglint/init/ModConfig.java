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
package com.blogspot.jabelarminecraft.enchantmentglint.init;

import java.io.File;

import com.blogspot.jabelarminecraft.enchantmentglint.MainMod;

import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

// TODO: Auto-generated Javadoc
public class ModConfig
{
    // set up configuration properties (will be read from config file in preInit)
    public static File configFile;
    public static Configuration config;
    public static boolean enableColoredGlint = true;
    public static boolean useRuneTexture = true;
    
    /**
     * Process the configuration.
     *
     * @param event
     *            the event
     */
    public static void initConfig(FMLPreInitializationEvent event)
    {
        configFile = event.getSuggestedConfigurationFile();
        // DEBUG
        System.out.println(MainMod.MODNAME + " config path = " + configFile.getAbsolutePath());
        System.out.println("Config file exists = " + configFile.canRead());

        config = new Configuration(configFile);

        syncConfig();
    }

    /**
     * Sync config.
     */
    /*
     * sync the configuration want it public so you can handle case of changes made in-game
     */
    public static void syncConfig()
    {
        config.load();
        
        enableColoredGlint = config.get(Configuration.CATEGORY_GENERAL, I18n.format("config.enable_colored_glint.name"), true,
                I18n.format("config.enable_colored_glint.tooltip")).getBoolean(true);
        // DEBUG
        System.out.println("Enable colored glint = " + enableColoredGlint);
        
        useRuneTexture = config.get(Configuration.CATEGORY_GENERAL, I18n.format("config.use_rune_texture.name"), true, 
                I18n.format("config.use_rune_texture.tooltip")).getBoolean(true);
        // DEBUG
        System.out.println("Use rune texture = " + useRuneTexture);

        // save is useful for the first run where config might not exist, and doesn't hurt
        config.save();
    }
}

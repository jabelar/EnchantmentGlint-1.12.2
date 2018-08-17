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
import com.blogspot.jabelarminecraft.enchantmentglint.client.gui.HexEntry;

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

    public static final String CATEGORY_SWORD = "config.category.sword";
    public static final String CATEGORY_BOW = "config.category.bow";
    public static final String CATEGORY_TOOL = "config.category.tool";
    public static final String CATEGORY_FISHING = "config.category.fishing";
    public static final String CATEGORY_ARMOR = "config.category.armor";
    public static final String CATEGORY_CURSE = "config.cateogry.curse";
    public static final String CATEGORY_CUSTOM = "config.category.custom";

    // Sword enchantments
    public static int BANE_OF_ARTHROPODS = 0xcc00ff;
    public static int FIRE_ASPECT = 0xff4000;
    public static int KNOCKBACK = 0x6600ff;
    public static int LOOTING = 0xffe066;
    public static int SHARPNESS = 0xff9933;
    public static int SMITE = 0x00ccff;
    public static int SWEEPING = 0xccff33;
    public static int UNBREAKING = 0x00cc66;

    // Bow enchantments
    public static int FLAME = 0xff4000;
    public static int INFINITY = 0xcc00ff;
    public static int POWER = 0xff9933;
    public static int PUNCH = 0x6600ff;

    // Tool enchantments
    public static int EFFICIENCY = 0x33ccff;
    public static int FORTUNE = 0xffe066;
    public static int SILK_TOUCH = 0xccff99;

    // Fishing rod enchantments
    public static int LUCK_OF_THE_SEA = 0xffe066;
    public static int LURE = 0x33ccff;

    // Armor enchantments
    public static int AQUA_AFFINITY = 0x3366ff;
    public static int BLAST_PROTECTION = 0xcc6699;
    public static int DEPTH_STRIDER = 0x6666ff;
    public static int FEATHER_FALLING = 0xccff99;
    public static int FIRE_PROTECTION = 0xff4000;
    public static int FROST_WALKER = 0xccffff;
    public static int MENDING = 0xffe066;
    public static int PROJECTILE_PROTECTION = 0xcc99ff;
    public static int PROTECTION = 0x00cc99;
    public static int RESPIRATION = 0x3366ff;
    public static int THORNS = 0xff9933;

    // Curses
    public static int VANISHING_CURSE = 0x6600cc;
    public static int BINDING_CURSE = 0xffffff;

    public static int DEFAULT = -8372020;

    
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

        useRuneTexture = config.get(Configuration.CATEGORY_GENERAL, I18n.format("config.use_rune_texture.name"), true, 
                I18n.format("config.use_rune_texture.tooltip")).getBoolean(true);


        // Sword enchantments
        BANE_OF_ARTHROPODS = config.get(CATEGORY_SWORD, I18n.format("enchantment.damage.arthropods"), 0xcc00ff, null, 0, 0xffffff).setConfigEntryClass(HexEntry.class).getInt(0xcc00ff);
        FIRE_ASPECT = config.get(CATEGORY_SWORD, I18n.format("enchantment.fire"),0xff4000, null, 0, 0xffffff).setConfigEntryClass(HexEntry.class).getInt(0xff4000);
        KNOCKBACK = config.get(CATEGORY_SWORD, I18n.format("enchantment.knockback"), 0x6600ff, null, 0, 0xffffff).setConfigEntryClass(HexEntry.class).getInt(0x6600ff);
        LOOTING = config.get(CATEGORY_SWORD, I18n.format("enchantment.lootBonus"), 0xffe066, null, 0, 0xffffff).setConfigEntryClass(HexEntry.class).getInt(0xffe066);
        SHARPNESS = config.get(CATEGORY_SWORD, I18n.format("enchantment.damage.all"), 0xff9933, null, 0, 0xffffff).setConfigEntryClass(HexEntry.class).getInt(0xff9933);
        SMITE = config.get(CATEGORY_SWORD, I18n.format("enchantment.damage.undead"), 0x00ccff, null, 0, 0xffffff).setConfigEntryClass(HexEntry.class).getInt(0x00ccff);
        SWEEPING = config.get(CATEGORY_SWORD, I18n.format("enchantment.sweeping"), 0xccff33, null, 0, 0xffffff).setConfigEntryClass(HexEntry.class).getInt(0xccff33);
        UNBREAKING = config.get(CATEGORY_SWORD, I18n.format("enchantment.durability"), 0x00cc66, null, 0, 0xffffff).setConfigEntryClass(HexEntry.class).getInt(0x00cc66);

        // Bow enchantments
        FLAME = config.get(CATEGORY_BOW, I18n.format("enchantment.arrowFire"), 0xff4000, null, 0, 0xffffff).setConfigEntryClass(HexEntry.class).getInt(0xff4000);
        INFINITY = config.get(CATEGORY_BOW, I18n.format("enchantment.arrowInfinite"), 0xcc00ff, null, 0, 0xffffff).setConfigEntryClass(HexEntry.class).getInt(0xcc00ff);
        POWER = config.get(CATEGORY_BOW, I18n.format("enchantment.arrowDamage"), 0xff9933, null, 0, 0xffffff).setConfigEntryClass(HexEntry.class).getInt(0xff9933);
        PUNCH = config.get(CATEGORY_BOW, I18n.format("enchantment.arrowKnockback"), 0x6600ff, null, 0, 0xffffff).setConfigEntryClass(HexEntry.class).getInt(0x6600ff);

        // Tool enchantments
        EFFICIENCY = config.get(CATEGORY_TOOL, I18n.format("enchantment.digging"), 0x33ccff, null, 0, 0xffffff).setConfigEntryClass(HexEntry.class).getInt(0x33ccff);
        FORTUNE = config.get(CATEGORY_TOOL, I18n.format("enchantment.lootBonusDigger"), 0xffe066, null, 0, 0xffffff).setConfigEntryClass(HexEntry.class).getInt(0xffe066);
        SILK_TOUCH = config.get(CATEGORY_TOOL, I18n.format("enchantment.untouching"), 0xccff99, null, 0, 0xffffff).setConfigEntryClass(HexEntry.class).getInt(0xccff99);

        // Fishing rod enchantments
        LUCK_OF_THE_SEA = config.get(CATEGORY_FISHING, I18n.format("enchantment.lootBonusFishing"), 0xffe066, null, 0, 0xffffff).setConfigEntryClass(HexEntry.class).getInt(0xffe066);
        LURE = config.get(CATEGORY_FISHING, I18n.format("enchantment.fishingSpeed"), 0x33ccff, null, 0, 0xffffff).setConfigEntryClass(HexEntry.class).getInt(0x33ccff);

        // Armor enchantments
        AQUA_AFFINITY = config.get(CATEGORY_ARMOR, I18n.format("enchantment.waterWorker"), 0x3366ff, null, 0, 0xffffff).setConfigEntryClass(HexEntry.class).getInt(0x3366ff);
        BLAST_PROTECTION = config.get(CATEGORY_ARMOR, I18n.format("enchantment.protect.explosion"), 0xcc6699, null, 0, 0xffffff).setConfigEntryClass(HexEntry.class).getInt(0xcc6699);
        DEPTH_STRIDER = config.get(CATEGORY_ARMOR, I18n.format("enchantment.waterWalker"), 0x6666ff, null, 0, 0xffffff).setConfigEntryClass(HexEntry.class).getInt(0x6666ff);
        FEATHER_FALLING = config.get(CATEGORY_ARMOR, I18n.format("enchantment.protect.fall"), 0xccff99, null, 0, 0xffffff).setConfigEntryClass(HexEntry.class).getInt(0xccff99);
        FIRE_PROTECTION = config.get(CATEGORY_ARMOR, I18n.format("enchantment.protect.fire"), 0xff4000, null, 0, 0xffffff).setConfigEntryClass(HexEntry.class).getInt(0xff4000);
        FROST_WALKER = config.get(CATEGORY_ARMOR, I18n.format("enchantment.frostWalker"), 0xccffff, null, 0, 0xffffff).setConfigEntryClass(HexEntry.class).getInt(0xccffff);
        MENDING = config.get(CATEGORY_ARMOR, I18n.format("enchantment.mending"), 0xffe066, null, 0, 0xffffff).setConfigEntryClass(HexEntry.class).getInt(0xffe066);
        PROJECTILE_PROTECTION = config.get(CATEGORY_ARMOR, I18n.format("enchantment.protect.projectile"), 0xcc99ff, null, 0, 0xffffff).setConfigEntryClass(HexEntry.class).getInt(0xcc99ff);
        PROTECTION = config.get(CATEGORY_ARMOR, I18n.format("enchantment.protect.all"), 0x00cc99, null, 0, 0xffffff).setConfigEntryClass(HexEntry.class).getInt(0x00cc99);
        RESPIRATION = config.get(CATEGORY_ARMOR, I18n.format("enchantment.oxygen"), 0x3366ff, null, 0, 0xffffff).setConfigEntryClass(HexEntry.class).getInt(0x3366ff);
        THORNS = config.get(CATEGORY_ARMOR, I18n.format("enchantment.thorns"), 0xff9933, null, 0, 0xffffff).setConfigEntryClass(HexEntry.class).getInt(0xff9933);

        // Curses
        VANISHING_CURSE = config.get(CATEGORY_CURSE, I18n.format("enchantment.vanishing_curse"), 0x6600cc, null, 0, 0xffffff).setConfigEntryClass(HexEntry.class).getInt(0x6600cc);
        BINDING_CURSE = config.get(CATEGORY_CURSE, I18n.format("enchantment.binding_curse"), 0xffffff, null, 0, 0xffffff).setConfigEntryClass(HexEntry.class).getInt(0xffffff);

        DEFAULT = config.get(CATEGORY_CUSTOM, I18n.format("enchantment.custom"), 0x8040cc, "enchantment.custom.comment", 0, 0xffffff).setConfigEntryClass(HexEntry.class).getInt(0x8040cc);

        // save is useful for the first run where config might not exist, and doesn't hurt
        config.save();
    }
}

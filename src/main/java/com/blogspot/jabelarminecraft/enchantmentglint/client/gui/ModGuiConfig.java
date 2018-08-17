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

package com.blogspot.jabelarminecraft.enchantmentglint.client.gui;

import java.util.ArrayList;
import java.util.List;

import com.blogspot.jabelarminecraft.enchantmentglint.MainMod;
import com.blogspot.jabelarminecraft.enchantmentglint.init.ModConfig;
import com.blogspot.jabelarminecraft.enchantmentglint.utilities.Utilities;

/**
 * @author jabelar
 *
 */
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.DummyConfigElement.DummyCategoryElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.GuiMessageDialog;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.PostConfigChangedEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

// TODO: Auto-generated Javadoc
@SideOnly(Side.CLIENT)
public class ModGuiConfig extends GuiConfig
{

    /**
     * Instantiates a new gui config.
     *
     * @param parent
     *            the parent
     */
    public ModGuiConfig(GuiScreen parent)
    {  
        super(  parent, 
                getConfigElements(),
                MainMod.MODID,
                false,
                false,
                Utilities.stringToRainbow(I18n.format("mod_motto"))
                );
        titleLine2 = ModConfig.configFile.getAbsolutePath();
    }

    private static List<IConfigElement> getConfigElements()
    {
        List<IConfigElement> list = new ArrayList<IConfigElement>();
        list.addAll(new ConfigElement(ModConfig.config.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements());
        list.add(new DummyCategoryElement("armor", "config.category.armor", new ModConfigElement(ModConfig.config.getCategory(ModConfig.CATEGORY_ARMOR)).getChildElements()));
        list.add(new DummyCategoryElement("bow", "config.category.bow", new ModConfigElement(ModConfig.config.getCategory(ModConfig.CATEGORY_BOW)).getChildElements()));
        list.add(new DummyCategoryElement("sword", "config.category.sword", new ModConfigElement(ModConfig.config.getCategory(ModConfig.CATEGORY_SWORD)).getChildElements()));
        list.add(new DummyCategoryElement("tool", "config.category.tool", new ModConfigElement(ModConfig.config.getCategory(ModConfig.CATEGORY_TOOL)).getChildElements()));
        list.add(new DummyCategoryElement("fishing", "config.category.fishing", new ModConfigElement(ModConfig.config.getCategory(ModConfig.CATEGORY_FISHING)).getChildElements()));
        list.add(new DummyCategoryElement("curse", "config.category.curse", new ModConfigElement(ModConfig.config.getCategory(ModConfig.CATEGORY_CURSE)).getChildElements()));
        list.add(new DummyCategoryElement("custom", "config.category.custom", new ModConfigElement(ModConfig.config.getCategory(ModConfig.CATEGORY_CUSTOM)).getChildElements()));
        return list;
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.minecraftforge.fml.client.config.GuiConfig#actionPerformed(net.minecraft.client.gui.GuiButton)
     */
    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.id == 2000)
        {
            boolean flag = true;
            try
            {
                if ((configID != null || this.parentScreen == null || !(this.parentScreen instanceof ModGuiConfig))
                        && (this.entryList.hasChangedEntry(true)))
                {
                    boolean requiresMcRestart = this.entryList.saveConfigElements();

                    if (Loader.isModLoaded(modID))
                    {
                        ConfigChangedEvent event = new OnConfigChangedEvent(modID, configID, isWorldRunning, requiresMcRestart);
                        MinecraftForge.EVENT_BUS.post(event);
                        if (!event.getResult().equals(Result.DENY))
                            MinecraftForge.EVENT_BUS.post(new PostConfigChangedEvent(modID, configID, isWorldRunning, requiresMcRestart));

                        if (requiresMcRestart)
                        {
                            flag = false;
                            mc.displayGuiScreen(new GuiMessageDialog(parentScreen, "fml.configgui.gameRestartTitle",
                                    new TextComponentString(I18n.format("fml.configgui.gameRestartRequired")), "fml.configgui.confirmRestartMessage"));
                        }

                        if (this.parentScreen instanceof ModGuiConfig)
                            ((ModGuiConfig) this.parentScreen).needsRefresh = true;
                    }
                }
            }
            catch (Throwable e)
            {
                e.printStackTrace();
            }

            if (flag)
                this.mc.displayGuiScreen(this.parentScreen);
        }
        else if (button.id == 2001)
        {
            this.entryList.setAllToDefault(this.chkApplyGlobally.isChecked());
        }
        else if (button.id == 2002)
        {
            this.entryList.undoAllChanges(this.chkApplyGlobally.isChecked());
        }
    }
}
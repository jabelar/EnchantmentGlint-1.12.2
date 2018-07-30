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

package com.blogspot.jabelarminecraft.enchantmentglint.proxy;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;

import com.blogspot.jabelarminecraft.enchantmentglint.MainMod;
import com.blogspot.jabelarminecraft.enchantmentglint.client.localization.ModLocale;
import com.blogspot.jabelarminecraft.enchantmentglint.client.renderers.ModRenderArmorStand;
import com.blogspot.jabelarminecraft.enchantmentglint.client.renderers.ModRenderGiantZombie;
import com.blogspot.jabelarminecraft.enchantmentglint.client.renderers.ModRenderHusk;
import com.blogspot.jabelarminecraft.enchantmentglint.client.renderers.ModRenderItem;
import com.blogspot.jabelarminecraft.enchantmentglint.client.renderers.ModRenderPigZombie;
import com.blogspot.jabelarminecraft.enchantmentglint.client.renderers.ModRenderPlayer;
import com.blogspot.jabelarminecraft.enchantmentglint.client.renderers.ModRenderSkeleton;
import com.blogspot.jabelarminecraft.enchantmentglint.client.renderers.ModRenderStray;
import com.blogspot.jabelarminecraft.enchantmentglint.client.renderers.ModRenderWitherSkeleton;
import com.blogspot.jabelarminecraft.enchantmentglint.client.renderers.ModRenderZombie;
import com.blogspot.jabelarminecraft.enchantmentglint.client.renderers.ModRenderZombieVillager;
import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.resources.LanguageManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityGiantZombie;
import net.minecraft.entity.monster.EntityHusk;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityStray;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.util.text.translation.LanguageMap;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;


// TODO: Auto-generated Javadoc
@EventBusSubscriber(value = Side.CLIENT, modid = MainMod.MODID)
public class ClientProxy implements IProxy
{
    public static ModRenderItem modRenderItem; // used to provide custom enchantment glint color
    public static Field modelManager = ReflectionHelper.findField(Minecraft.class, "modelManager", "modelManager");
    public static Field renderItem = ReflectionHelper.findField(Minecraft.class, "renderItem", "renderItem");
    public static Field itemRenderer = ReflectionHelper.findField(ItemRenderer.class, "itemRenderer", "itemRenderer");
    public static Field playerRenderer = ReflectionHelper.findField(RenderManager.class, "playerRender", "playerRenderer");
    public static Field skinMap = ReflectionHelper.findField(RenderManager.class, "skinMap", "skinMap");
    
    public static Field locale = ReflectionHelper.findField(LanguageManager.class, "CURRENT_LOCALE", "CURRENT_LOCALE");
    public static ModLocale MOD_LOCALE = new ModLocale();

    /* (non-Javadoc)
     * @see com.blogspot.jabelarminecraft.examplemod.proxy.IProxy#preInit(net.minecraftforge.fml.common.event.FMLPreInitializationEvent)
     */
    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        // DEBUG
        System.out.println("on Client side");
        
        fixLocaleClass();
    }
    
    /**
     * Fix locale class.
     */
    public void fixLocaleClass()
    {
        // DEBUG
        System.out.println("Swapping in custom locale class");
        Field modifiersField;
        try
        {
            modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            try
            {
                modifiersField.setInt(locale, locale.getModifiers() & ~Modifier.FINAL);
            }
            catch (IllegalArgumentException | IllegalAccessException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        catch (NoSuchFieldException | SecurityException e1)
        {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        try
        {
            locale.set(null, MOD_LOCALE);
        }
        catch (IllegalArgumentException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /* (non-Javadoc)
     * @see com.blogspot.jabelarminecraft.examplemod.proxy.IProxy#init(net.minecraftforge.fml.common.event.FMLInitializationEvent)
     */
    @SuppressWarnings("unchecked")
    @Override
    public void init(FMLInitializationEvent event)
    {
        // DEBUG
        System.out.println("on Client side");
        
        Minecraft mc = Minecraft.getMinecraft();
        
        // Replace render item with custom version
        modelManager.setAccessible(true);
        renderItem.setAccessible(true);
        playerRenderer.setAccessible(true);
        try
        {
            modRenderItem = new ModRenderItem(mc.getTextureManager(), (ModelManager) modelManager.get(mc), mc.getItemColors(), ((RenderItem)renderItem.get(mc)).getItemModelMesher());
            renderItem.set(mc, modRenderItem);
            itemRenderer.set(mc.getItemRenderer(), modRenderItem);
            // DEBUG
            System.out.println("playerRenderer before reflection is "+playerRenderer.get(mc.getRenderManager()));
            playerRenderer.set(mc.getRenderManager(), new ModRenderPlayer(mc.getRenderManager()));
            // DEBUG
            System.out.println("playerRenderer after reflection is "+playerRenderer.get(mc.getRenderManager()));
            ((Map<String, RenderPlayer>)skinMap.get(mc.getRenderManager())).put("default", new ModRenderPlayer(mc.getRenderManager()));
            ((Map<String, RenderPlayer>)skinMap.get(mc.getRenderManager())).put("slim", new ModRenderPlayer(mc.getRenderManager(), true));
        }
        catch (IllegalArgumentException | IllegalAccessException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        mc.getRenderManager().entityRenderMap.put(EntityItem.class, new RenderEntityItem(mc.getRenderManager(), modRenderItem));
        mc.getRenderManager().entityRenderMap.put(EntitySkeleton.class, new ModRenderSkeleton(mc.getRenderManager()));
        mc.getRenderManager().entityRenderMap.put(EntityWitherSkeleton.class, new ModRenderWitherSkeleton(mc.getRenderManager()));
        mc.getRenderManager().entityRenderMap.put(EntityStray.class, new ModRenderStray(mc.getRenderManager()));
        mc.getRenderManager().entityRenderMap.put(EntityZombie.class, new ModRenderZombie(mc.getRenderManager()));
        mc.getRenderManager().entityRenderMap.put(EntityHusk.class, new ModRenderHusk(mc.getRenderManager()));
        mc.getRenderManager().entityRenderMap.put(EntityZombieVillager.class, new ModRenderZombieVillager(mc.getRenderManager()));
        mc.getRenderManager().entityRenderMap.put(EntityGiantZombie.class, new ModRenderGiantZombie(mc.getRenderManager(), 6.0F));
        mc.getRenderManager().entityRenderMap.put(EntityPigZombie.class, new ModRenderPigZombie(mc.getRenderManager()));
        mc.getRenderManager().entityRenderMap.put(EntityArmorStand.class, new ModRenderArmorStand(mc.getRenderManager()));
    }

    /* (non-Javadoc)
     * @see com.blogspot.jabelarminecraft.examplemod.proxy.IProxy#postInit(net.minecraftforge.fml.common.event.FMLPostInitializationEvent)
     */
    @Override
    public void postInit(FMLPostInitializationEvent event)
    {
        // DEBUG
        System.out.println("on Client side");

        refreshLangResources();
    }

    /**
     * Refresh lang resources.
     */
    @SuppressWarnings("unlikely-arg-type")
    public void refreshLangResources()
    {
        // DEBUG
        System.out.println("Refreshing lang files with proper precedence");
//        Minecraft.getMinecraft().refreshResources();
        List<String> list = Lists.newArrayList("en_us");

        if (!"en_us".equals(Minecraft.getMinecraft().getLanguageManager().getCurrentLanguage()))
        {
            list.add(Minecraft.getMinecraft().getLanguageManager().getCurrentLanguage().toString());
        }

        // This is a fix for problem where lang files are not properly replaced by resource packs
        MOD_LOCALE.loadLocaleDataFiles(Minecraft.getMinecraft().getResourceManager(), list);
        LanguageMap.replaceWith(MOD_LOCALE.properties);
    }

    /* (non-Javadoc)
     * @see com.blogspot.jabelarminecraft.examplemod.proxy.IProxy#getPlayerEntityFromContext(net.minecraftforge.fml.common.network.simpleimpl.MessageContext)
     */
    @Override
    public EntityPlayer getPlayerEntityFromContext(MessageContext ctx)
    {
        // Note that if you simply return 'Minecraft.getMinecraft().thePlayer',
        // your packets will not work because you will be getting a client
        // player even when you are on the server! Sounds absurd, but it's true.

        // Solution is to double-check side before returning the player:
        return (ctx.side.isClient() ? Minecraft.getMinecraft().player : ctx.getServerHandler().player);
    }


    /* (non-Javadoc)
     * @see com.blogspot.jabelarminecraft.examplemod.proxy.IProxy#serverStarting(net.minecraftforge.fml.common.event.FMLServerStartingEvent)
     */
    @Override
    public void serverStarting(FMLServerStartingEvent event)
    {
        // This will never get called on client side
    }
        
    public static int getColorForEnchantment(Map<Enchantment, Integer> enchMap)
    {
        int alpha = 0x66000000;
        
        // Sword enchantments
        if (enchMap.containsKey(Enchantments.BANE_OF_ARTHROPODS)) return alpha | 0xcc00ff;
        if (enchMap.containsKey(Enchantments.FIRE_ASPECT)) return alpha | 0xff4000;
        if (enchMap.containsKey(Enchantments.KNOCKBACK)) return alpha | 0x6600ff;
        if (enchMap.containsKey(Enchantments.LOOTING)) return alpha | 0xffe066;
        if (enchMap.containsKey(Enchantments.SHARPNESS)) return alpha | 0xff9933;
        if (enchMap.containsKey(Enchantments.SMITE)) return alpha | 0x00ccff;
        if (enchMap.containsKey(Enchantments.SWEEPING)) return alpha | 0xccff33;
        if (enchMap.containsKey(Enchantments.UNBREAKING)) return alpha | 0x00cc66;

        // Bow enchantments
        if (enchMap.containsKey(Enchantments.FLAME)) return alpha | 0xff4000;
        if (enchMap.containsKey(Enchantments.INFINITY)) return alpha | 0xcc00ff;
        if (enchMap.containsKey(Enchantments.POWER)) return alpha | 0xff9933;
        if (enchMap.containsKey(Enchantments.PUNCH)) return alpha | 0x6600ff;

        // Tool enchantments
        if (enchMap.containsKey(Enchantments.EFFICIENCY)) return alpha | 0x33ccff;
        if (enchMap.containsKey(Enchantments.FORTUNE)) return alpha | 0xffe066;
        if (enchMap.containsKey(Enchantments.SILK_TOUCH)) return alpha | 0xccff99;

        // Fishing rod enchantments
        if (enchMap.containsKey(Enchantments.LUCK_OF_THE_SEA)) return alpha | 0xffe066;
        if (enchMap.containsKey(Enchantments.LURE)) return alpha | 0x33ccff;

        // Armor enchantments
        if (enchMap.containsKey(Enchantments.AQUA_AFFINITY)) return alpha | 0x3366ff;
        if (enchMap.containsKey(Enchantments.BLAST_PROTECTION)) return alpha | 0xcc6699;
        if (enchMap.containsKey(Enchantments.DEPTH_STRIDER)) return alpha | 0x6666ff;
        if (enchMap.containsKey(Enchantments.FEATHER_FALLING)) return alpha | 0xccff99;
        if (enchMap.containsKey(Enchantments.FIRE_PROTECTION)) return alpha | 0xff4000;
        if (enchMap.containsKey(Enchantments.FROST_WALKER)) return alpha | 0xccffff;
        if (enchMap.containsKey(Enchantments.MENDING)) return alpha | 0xffe066;
        if (enchMap.containsKey(Enchantments.PROJECTILE_PROTECTION)) return alpha | 0xcc99ff;
        if (enchMap.containsKey(Enchantments.PROTECTION)) return alpha | 0x00cc99;
        if (enchMap.containsKey(Enchantments.RESPIRATION)) return alpha | 0x3366ff;
        if (enchMap.containsKey(Enchantments.THORNS)) return alpha | 0xff9933;

        // Curses
        if (enchMap.containsKey(Enchantments.VANISHING_CURSE)) return alpha | 0x6600cc;
        if (enchMap.containsKey(Enchantments.BINDING_CURSE)) return alpha | 0xffffff;
        
        return -8372020;
    }
        
    public static float alphaFromColor(int parColor)
    {
        return 0.32F;
//        return (parColor >> 24 & 255) / 255.0F;
    }
    
    public static float redFromColor(int parColor)
    {
        return (parColor >> 16 & 255) / 255.0F;
    }
    
    public static float greenFromColor(int parColor)
    {
        return (parColor >> 8 & 255) / 255.0F;
    }
    
    public static float blueFromColor(int parColor)
    {
        return (parColor & 255) / 255.0F;
    }
}
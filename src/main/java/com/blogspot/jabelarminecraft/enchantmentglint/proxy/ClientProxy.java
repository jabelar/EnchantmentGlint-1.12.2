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
import java.util.Map;

import com.blogspot.jabelarminecraft.enchantmentglint.MainMod;
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
import com.blogspot.jabelarminecraft.enchantmentglint.init.ModConfig;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
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
import net.minecraft.init.Enchantments;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;


// TODO: Auto-generated Javadoc
@EventBusSubscriber(value = Side.CLIENT, modid = MainMod.MODID)
public class ClientProxy implements IProxy
{
    public static ModRenderItem modRenderItem; // used to provide custom enchantment glint color
    public static Field modelManager = ReflectionHelper.findField(Minecraft.class, "modelManager", "field_175617_aL");
    public static Field renderItem = ReflectionHelper.findField(Minecraft.class, "renderItem", "field_175621_X");
    public static Field itemRenderer = ReflectionHelper.findField(ItemRenderer.class, "itemRenderer", "field_178112_h");
    public static Field playerRenderer = ReflectionHelper.findField(RenderManager.class, "playerRenderer", "field_178637_m");
    public static Field skinMap = ReflectionHelper.findField(RenderManager.class, "skinMap", "field_178636_l");


    /* (non-Javadoc)
     * @see com.blogspot.jabelarminecraft.examplemod.proxy.IProxy#init(net.minecraftforge.fml.common.event.FMLInitializationEvent)
     */
    @Override
    public void init(FMLInitializationEvent event)
    {
        replaceRenderers();       
    }
    
    @SuppressWarnings("unchecked")
    public void replaceRenderers()
    {
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
            playerRenderer.set(mc.getRenderManager(), new ModRenderPlayer(mc.getRenderManager()));
            ((Map<String, RenderPlayer>)skinMap.get(mc.getRenderManager())).put("default", new ModRenderPlayer(mc.getRenderManager()));
            ((Map<String, RenderPlayer>)skinMap.get(mc.getRenderManager())).put("slim", new ModRenderPlayer(mc.getRenderManager(), true));
        }
        catch (IllegalArgumentException | IllegalAccessException e)
        {
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
     
    public static int getColorForEnchantment(Map<Enchantment, Integer> enchMap)
    {
        if (!ModConfig.enableColoredGlint)
        {
            return -8372020;
        }
        
        int alpha = 0x66000000;
        
        // Sword enchantments
        if (enchMap.containsKey(Enchantments.BANE_OF_ARTHROPODS)) return alpha | ModConfig.BANE_OF_ARTHROPODS;
        if (enchMap.containsKey(Enchantments.FIRE_ASPECT)) return alpha | ModConfig.FIRE_ASPECT;
        if (enchMap.containsKey(Enchantments.KNOCKBACK)) return alpha | ModConfig.KNOCKBACK;
        if (enchMap.containsKey(Enchantments.LOOTING)) return alpha | ModConfig.LOOTING;
        if (enchMap.containsKey(Enchantments.SHARPNESS)) return alpha | ModConfig.SHARPNESS;
        if (enchMap.containsKey(Enchantments.SMITE)) return alpha | ModConfig.SMITE;
        if (enchMap.containsKey(Enchantments.SWEEPING)) return alpha | ModConfig.SWEEPING;
        if (enchMap.containsKey(Enchantments.UNBREAKING)) return alpha | ModConfig.UNBREAKING;

        // Bow enchantments
        if (enchMap.containsKey(Enchantments.FLAME)) return alpha | ModConfig.FLAME;
        if (enchMap.containsKey(Enchantments.INFINITY)) return alpha | ModConfig.INFINITY;
        if (enchMap.containsKey(Enchantments.POWER)) return alpha | ModConfig.POWER;
        if (enchMap.containsKey(Enchantments.PUNCH)) return alpha | ModConfig.PUNCH;

        // Tool enchantments
        if (enchMap.containsKey(Enchantments.EFFICIENCY)) return alpha | ModConfig.EFFICIENCY;
        if (enchMap.containsKey(Enchantments.FORTUNE)) return alpha | ModConfig.FORTUNE;
        if (enchMap.containsKey(Enchantments.SILK_TOUCH)) return alpha | ModConfig.SILK_TOUCH;

        // Fishing rod enchantments
        if (enchMap.containsKey(Enchantments.LUCK_OF_THE_SEA)) return alpha | ModConfig.LUCK_OF_THE_SEA;
        if (enchMap.containsKey(Enchantments.LURE)) return alpha | ModConfig.LURE;

        // Armor enchantments
        if (enchMap.containsKey(Enchantments.AQUA_AFFINITY)) return alpha | ModConfig.AQUA_AFFINITY;
        if (enchMap.containsKey(Enchantments.BLAST_PROTECTION)) return alpha | ModConfig.BLAST_PROTECTION;
        if (enchMap.containsKey(Enchantments.DEPTH_STRIDER)) return alpha | ModConfig.DEPTH_STRIDER;
        if (enchMap.containsKey(Enchantments.FEATHER_FALLING)) return alpha | ModConfig.FEATHER_FALLING;
        if (enchMap.containsKey(Enchantments.FIRE_PROTECTION)) return alpha | ModConfig.FIRE_PROTECTION;
        if (enchMap.containsKey(Enchantments.FROST_WALKER)) return alpha | ModConfig.FROST_WALKER;
        if (enchMap.containsKey(Enchantments.MENDING)) return alpha | ModConfig.MENDING;
        if (enchMap.containsKey(Enchantments.PROJECTILE_PROTECTION)) return alpha | ModConfig.PROJECTILE_PROTECTION;
        if (enchMap.containsKey(Enchantments.PROTECTION)) return alpha | ModConfig.PROTECTION;
        if (enchMap.containsKey(Enchantments.RESPIRATION)) return alpha | ModConfig.RESPIRATION;
        if (enchMap.containsKey(Enchantments.THORNS)) return alpha | ModConfig.THORNS;

        // Curses
        if (enchMap.containsKey(Enchantments.VANISHING_CURSE)) return alpha | ModConfig.VANISHING_CURSE;
        if (enchMap.containsKey(Enchantments.BINDING_CURSE)) return alpha | ModConfig.BINDING_CURSE;
        
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
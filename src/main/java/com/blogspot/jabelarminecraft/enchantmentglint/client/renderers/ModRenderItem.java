package com.blogspot.jabelarminecraft.enchantmentglint.client.renderers;

import java.util.List;

import com.blogspot.jabelarminecraft.enchantmentglint.MainMod;
import com.blogspot.jabelarminecraft.enchantmentglint.init.ModConfig;
import com.blogspot.jabelarminecraft.enchantmentglint.proxy.ClientProxy;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockHugeMushroom;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockPrismarine;
import net.minecraft.block.BlockQuartz;
import net.minecraft.block.BlockRedSandstone;
import net.minecraft.block.BlockSand;
import net.minecraft.block.BlockSandStone;
import net.minecraft.block.BlockSilverfish;
import net.minecraft.block.BlockStone;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.BlockStoneSlab;
import net.minecraft.block.BlockStoneSlabNew;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.BlockWall;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityStructure;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModRenderItem extends RenderItem
{
    private static final ResourceLocation RES_ITEM_GLINT_RUNE = new ResourceLocation(MainMod.MODID, "textures/misc/enchanted_item_glint_rune.png");
    private static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation(MainMod.MODID, "textures/misc/enchanted_item_glint.png");
    private final TextureManager textureManager;
    private final ItemColors itemColors;
    private final ItemModelMesher itemModelMesher;

    public ModRenderItem(TextureManager parTextureManager, ModelManager parModelManager, ItemColors parItemColors, ItemModelMesher parItemModelMesher)
    {
        super(parTextureManager, parModelManager, parItemColors);
        textureManager = parTextureManager;
        itemColors = parItemColors;
        itemModelMesher = parItemModelMesher;
        registerItems();
    }

    @Override
    public void renderItem(ItemStack stack, IBakedModel model)
    {
        if (!stack.isEmpty())
        {
            GlStateManager.pushMatrix();
            GlStateManager.translate(-0.5F, -0.5F, -0.5F);

            if (model.isBuiltInRenderer())
            {
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                GlStateManager.enableRescaleNormal();
                stack.getItem().getTileEntityItemStackRenderer().renderByItem(stack);
            }
            else
            {
                renderModel(model, stack);

                if (stack.hasEffect())
                {
                    renderEffect(model, ClientProxy.getColorForEnchantment(EnchantmentHelper.getEnchantments(stack)));
                }
            }

            GlStateManager.popMatrix();
        }
    }
    
    private void renderEffect(IBakedModel model, int color)
    {
        GlStateManager.depthMask(false);
        GlStateManager.depthFunc(514);
        GlStateManager.disableLighting();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
        if (ModConfig.useRuneTexture) 
        {
            textureManager.bindTexture(RES_ITEM_GLINT_RUNE);
        }
        else
        {
            textureManager.bindTexture(RES_ITEM_GLINT);
        }
        GlStateManager.matrixMode(5890);
        GlStateManager.pushMatrix();
        GlStateManager.scale(16.0F, 16.0F, 16.0F);
        float f = Minecraft.getSystemTime() % 3000L / 3000.0F / 8.0F;
        GlStateManager.translate(f, 0.0F, 0.0F);
        GlStateManager.rotate(-50.0F, 0.0F, 0.0F, 1.0F);
        renderModel(model, color); // original was -8372020);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.scale(16.0F, 16.0F, 16.0F);
        float f1 = Minecraft.getSystemTime() % 4873L / 4873.0F / 8.0F;
        GlStateManager.translate(-f1, 0.0F, 0.0F);
        GlStateManager.rotate(10.0F, 0.0F, 0.0F, 1.0F);
        renderModel(model, color); // original was -8372020);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(5888);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.enableLighting();
        GlStateManager.depthFunc(515);
        GlStateManager.depthMask(true);
        textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
    }
    
    private void renderModel(IBakedModel model, ItemStack stack)
    {
        renderModel(model, -1, stack);
    }

    private void renderModel(IBakedModel model, int color)
    {
        renderModel(model, color, ItemStack.EMPTY);
    }

    private void renderModel(IBakedModel model, int color, ItemStack stack)
    {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.ITEM);

        for (EnumFacing enumfacing : EnumFacing.values())
        {
            renderQuads(bufferbuilder, model.getQuads((IBlockState)null, enumfacing, 0L), color, stack);
        }

        renderQuads(bufferbuilder, model.getQuads((IBlockState)null, (EnumFacing)null, 0L), color, stack);
        tessellator.draw();
    }
    
    @Override
    public void renderQuads(BufferBuilder renderer, List<BakedQuad> quads, int color, ItemStack stack)
    {
        boolean flag = color == -1 && !stack.isEmpty();
        int i = 0;

        for (int j = quads.size(); i < j; ++i)
        {
            BakedQuad bakedquad = quads.get(i);
            int k = color;

            if (flag && bakedquad.hasTintIndex())
            {
                k = itemColors.colorMultiplier(stack, bakedquad.getTintIndex());

                if (EntityRenderer.anaglyphEnable)
                {
                    k = TextureUtil.anaglyphColor(k);
                }

                k = k | -16777216;
            }

            net.minecraftforge.client.model.pipeline.LightUtil.renderQuadColor(renderer, bakedquad, k);
        }
    }

    public static ResourceLocation getResItemGlint()
    {
        return RES_ITEM_GLINT;
    }

    public TextureManager getTextureManager()
    {
        return textureManager;
    }
    
    private  void modRegisterItem(Item itm, int subType, String identifier)
    {
        this.itemModelMesher.register(itm, subType, new ModelResourceLocation(identifier, "inventory"));
    }

    private  void modRegisterBlock(Block blk, int subType, String identifier)
    {
        modRegisterItem(Item.getItemFromBlock(blk), subType, identifier);
    }

    private void modRegisterBlock(Block blk, String identifier)
    {
        modRegisterBlock(blk, 0, identifier);
    }

    private void modRegisterItem(Item itm, String identifier)
    {
        modRegisterItem(itm, 0, identifier);
    }

    private void registerItems()
    {
        modRegisterBlock(Blocks.ANVIL, "anvil_intact");
        modRegisterBlock(Blocks.ANVIL, 1, "anvil_slightly_damaged");
        modRegisterBlock(Blocks.ANVIL, 2, "anvil_very_damaged");
        modRegisterBlock(Blocks.CARPET, EnumDyeColor.BLACK.getMetadata(), "black_carpet");
        modRegisterBlock(Blocks.CARPET, EnumDyeColor.BLUE.getMetadata(), "blue_carpet");
        modRegisterBlock(Blocks.CARPET, EnumDyeColor.BROWN.getMetadata(), "brown_carpet");
        modRegisterBlock(Blocks.CARPET, EnumDyeColor.CYAN.getMetadata(), "cyan_carpet");
        modRegisterBlock(Blocks.CARPET, EnumDyeColor.GRAY.getMetadata(), "gray_carpet");
        modRegisterBlock(Blocks.CARPET, EnumDyeColor.GREEN.getMetadata(), "green_carpet");
        modRegisterBlock(Blocks.CARPET, EnumDyeColor.LIGHT_BLUE.getMetadata(), "light_blue_carpet");
        modRegisterBlock(Blocks.CARPET, EnumDyeColor.LIME.getMetadata(), "lime_carpet");
        modRegisterBlock(Blocks.CARPET, EnumDyeColor.MAGENTA.getMetadata(), "magenta_carpet");
        modRegisterBlock(Blocks.CARPET, EnumDyeColor.ORANGE.getMetadata(), "orange_carpet");
        modRegisterBlock(Blocks.CARPET, EnumDyeColor.PINK.getMetadata(), "pink_carpet");
        modRegisterBlock(Blocks.CARPET, EnumDyeColor.PURPLE.getMetadata(), "purple_carpet");
        modRegisterBlock(Blocks.CARPET, EnumDyeColor.RED.getMetadata(), "red_carpet");
        modRegisterBlock(Blocks.CARPET, EnumDyeColor.SILVER.getMetadata(), "silver_carpet");
        modRegisterBlock(Blocks.CARPET, EnumDyeColor.WHITE.getMetadata(), "white_carpet");
        modRegisterBlock(Blocks.CARPET, EnumDyeColor.YELLOW.getMetadata(), "yellow_carpet");
        modRegisterBlock(Blocks.COBBLESTONE_WALL, BlockWall.EnumType.MOSSY.getMetadata(), "mossy_cobblestone_wall");
        modRegisterBlock(Blocks.COBBLESTONE_WALL, BlockWall.EnumType.NORMAL.getMetadata(), "cobblestone_wall");
        modRegisterBlock(Blocks.DIRT, BlockDirt.DirtType.COARSE_DIRT.getMetadata(), "coarse_dirt");
        modRegisterBlock(Blocks.DIRT, BlockDirt.DirtType.DIRT.getMetadata(), "dirt");
        modRegisterBlock(Blocks.DIRT, BlockDirt.DirtType.PODZOL.getMetadata(), "podzol");
        modRegisterBlock(Blocks.DOUBLE_PLANT, BlockDoublePlant.EnumPlantType.FERN.getMeta(), "double_fern");
        modRegisterBlock(Blocks.DOUBLE_PLANT, BlockDoublePlant.EnumPlantType.GRASS.getMeta(), "double_grass");
        modRegisterBlock(Blocks.DOUBLE_PLANT, BlockDoublePlant.EnumPlantType.PAEONIA.getMeta(), "paeonia");
        modRegisterBlock(Blocks.DOUBLE_PLANT, BlockDoublePlant.EnumPlantType.ROSE.getMeta(), "double_rose");
        modRegisterBlock(Blocks.DOUBLE_PLANT, BlockDoublePlant.EnumPlantType.SUNFLOWER.getMeta(), "sunflower");
        modRegisterBlock(Blocks.DOUBLE_PLANT, BlockDoublePlant.EnumPlantType.SYRINGA.getMeta(), "syringa");
        modRegisterBlock(Blocks.LEAVES, BlockPlanks.EnumType.BIRCH.getMetadata(), "birch_leaves");
        modRegisterBlock(Blocks.LEAVES, BlockPlanks.EnumType.JUNGLE.getMetadata(), "jungle_leaves");
        modRegisterBlock(Blocks.LEAVES, BlockPlanks.EnumType.OAK.getMetadata(), "oak_leaves");
        modRegisterBlock(Blocks.LEAVES, BlockPlanks.EnumType.SPRUCE.getMetadata(), "spruce_leaves");
        modRegisterBlock(Blocks.LEAVES2, BlockPlanks.EnumType.ACACIA.getMetadata() - 4, "acacia_leaves");
        modRegisterBlock(Blocks.LEAVES2, BlockPlanks.EnumType.DARK_OAK.getMetadata() - 4, "dark_oak_leaves");
        modRegisterBlock(Blocks.LOG, BlockPlanks.EnumType.BIRCH.getMetadata(), "birch_log");
        modRegisterBlock(Blocks.LOG, BlockPlanks.EnumType.JUNGLE.getMetadata(), "jungle_log");
        modRegisterBlock(Blocks.LOG, BlockPlanks.EnumType.OAK.getMetadata(), "oak_log");
        modRegisterBlock(Blocks.LOG, BlockPlanks.EnumType.SPRUCE.getMetadata(), "spruce_log");
        modRegisterBlock(Blocks.LOG2, BlockPlanks.EnumType.ACACIA.getMetadata() - 4, "acacia_log");
        modRegisterBlock(Blocks.LOG2, BlockPlanks.EnumType.DARK_OAK.getMetadata() - 4, "dark_oak_log");
        modRegisterBlock(Blocks.MONSTER_EGG, BlockSilverfish.EnumType.CHISELED_STONEBRICK.getMetadata(), "chiseled_brick_monster_egg");
        modRegisterBlock(Blocks.MONSTER_EGG, BlockSilverfish.EnumType.COBBLESTONE.getMetadata(), "cobblestone_monster_egg");
        modRegisterBlock(Blocks.MONSTER_EGG, BlockSilverfish.EnumType.CRACKED_STONEBRICK.getMetadata(), "cracked_brick_monster_egg");
        modRegisterBlock(Blocks.MONSTER_EGG, BlockSilverfish.EnumType.MOSSY_STONEBRICK.getMetadata(), "mossy_brick_monster_egg");
        modRegisterBlock(Blocks.MONSTER_EGG, BlockSilverfish.EnumType.STONE.getMetadata(), "stone_monster_egg");
        modRegisterBlock(Blocks.MONSTER_EGG, BlockSilverfish.EnumType.STONEBRICK.getMetadata(), "stone_brick_monster_egg");
        modRegisterBlock(Blocks.PLANKS, BlockPlanks.EnumType.ACACIA.getMetadata(), "acacia_planks");
        modRegisterBlock(Blocks.PLANKS, BlockPlanks.EnumType.BIRCH.getMetadata(), "birch_planks");
        modRegisterBlock(Blocks.PLANKS, BlockPlanks.EnumType.DARK_OAK.getMetadata(), "dark_oak_planks");
        modRegisterBlock(Blocks.PLANKS, BlockPlanks.EnumType.JUNGLE.getMetadata(), "jungle_planks");
        modRegisterBlock(Blocks.PLANKS, BlockPlanks.EnumType.OAK.getMetadata(), "oak_planks");
        modRegisterBlock(Blocks.PLANKS, BlockPlanks.EnumType.SPRUCE.getMetadata(), "spruce_planks");
        modRegisterBlock(Blocks.PRISMARINE, BlockPrismarine.EnumType.BRICKS.getMetadata(), "prismarine_bricks");
        modRegisterBlock(Blocks.PRISMARINE, BlockPrismarine.EnumType.DARK.getMetadata(), "dark_prismarine");
        modRegisterBlock(Blocks.PRISMARINE, BlockPrismarine.EnumType.ROUGH.getMetadata(), "prismarine");
        modRegisterBlock(Blocks.QUARTZ_BLOCK, BlockQuartz.EnumType.CHISELED.getMetadata(), "chiseled_quartz_block");
        modRegisterBlock(Blocks.QUARTZ_BLOCK, BlockQuartz.EnumType.DEFAULT.getMetadata(), "quartz_block");
        modRegisterBlock(Blocks.QUARTZ_BLOCK, BlockQuartz.EnumType.LINES_Y.getMetadata(), "quartz_column");
        modRegisterBlock(Blocks.RED_FLOWER, BlockFlower.EnumFlowerType.ALLIUM.getMeta(), "allium");
        modRegisterBlock(Blocks.RED_FLOWER, BlockFlower.EnumFlowerType.BLUE_ORCHID.getMeta(), "blue_orchid");
        modRegisterBlock(Blocks.RED_FLOWER, BlockFlower.EnumFlowerType.HOUSTONIA.getMeta(), "houstonia");
        modRegisterBlock(Blocks.RED_FLOWER, BlockFlower.EnumFlowerType.ORANGE_TULIP.getMeta(), "orange_tulip");
        modRegisterBlock(Blocks.RED_FLOWER, BlockFlower.EnumFlowerType.OXEYE_DAISY.getMeta(), "oxeye_daisy");
        modRegisterBlock(Blocks.RED_FLOWER, BlockFlower.EnumFlowerType.PINK_TULIP.getMeta(), "pink_tulip");
        modRegisterBlock(Blocks.RED_FLOWER, BlockFlower.EnumFlowerType.POPPY.getMeta(), "poppy");
        modRegisterBlock(Blocks.RED_FLOWER, BlockFlower.EnumFlowerType.RED_TULIP.getMeta(), "red_tulip");
        modRegisterBlock(Blocks.RED_FLOWER, BlockFlower.EnumFlowerType.WHITE_TULIP.getMeta(), "white_tulip");
        modRegisterBlock(Blocks.SAND, BlockSand.EnumType.RED_SAND.getMetadata(), "red_sand");
        modRegisterBlock(Blocks.SAND, BlockSand.EnumType.SAND.getMetadata(), "sand");
        modRegisterBlock(Blocks.SANDSTONE, BlockSandStone.EnumType.CHISELED.getMetadata(), "chiseled_sandstone");
        modRegisterBlock(Blocks.SANDSTONE, BlockSandStone.EnumType.DEFAULT.getMetadata(), "sandstone");
        modRegisterBlock(Blocks.SANDSTONE, BlockSandStone.EnumType.SMOOTH.getMetadata(), "smooth_sandstone");
        modRegisterBlock(Blocks.RED_SANDSTONE, BlockRedSandstone.EnumType.CHISELED.getMetadata(), "chiseled_red_sandstone");
        modRegisterBlock(Blocks.RED_SANDSTONE, BlockRedSandstone.EnumType.DEFAULT.getMetadata(), "red_sandstone");
        modRegisterBlock(Blocks.RED_SANDSTONE, BlockRedSandstone.EnumType.SMOOTH.getMetadata(), "smooth_red_sandstone");
        modRegisterBlock(Blocks.SAPLING, BlockPlanks.EnumType.ACACIA.getMetadata(), "acacia_sapling");
        modRegisterBlock(Blocks.SAPLING, BlockPlanks.EnumType.BIRCH.getMetadata(), "birch_sapling");
        modRegisterBlock(Blocks.SAPLING, BlockPlanks.EnumType.DARK_OAK.getMetadata(), "dark_oak_sapling");
        modRegisterBlock(Blocks.SAPLING, BlockPlanks.EnumType.JUNGLE.getMetadata(), "jungle_sapling");
        modRegisterBlock(Blocks.SAPLING, BlockPlanks.EnumType.OAK.getMetadata(), "oak_sapling");
        modRegisterBlock(Blocks.SAPLING, BlockPlanks.EnumType.SPRUCE.getMetadata(), "spruce_sapling");
        modRegisterBlock(Blocks.SPONGE, 0, "sponge");
        modRegisterBlock(Blocks.SPONGE, 1, "sponge_wet");
        modRegisterBlock(Blocks.STAINED_GLASS, EnumDyeColor.BLACK.getMetadata(), "black_stained_glass");
        modRegisterBlock(Blocks.STAINED_GLASS, EnumDyeColor.BLUE.getMetadata(), "blue_stained_glass");
        modRegisterBlock(Blocks.STAINED_GLASS, EnumDyeColor.BROWN.getMetadata(), "brown_stained_glass");
        modRegisterBlock(Blocks.STAINED_GLASS, EnumDyeColor.CYAN.getMetadata(), "cyan_stained_glass");
        modRegisterBlock(Blocks.STAINED_GLASS, EnumDyeColor.GRAY.getMetadata(), "gray_stained_glass");
        modRegisterBlock(Blocks.STAINED_GLASS, EnumDyeColor.GREEN.getMetadata(), "green_stained_glass");
        modRegisterBlock(Blocks.STAINED_GLASS, EnumDyeColor.LIGHT_BLUE.getMetadata(), "light_blue_stained_glass");
        modRegisterBlock(Blocks.STAINED_GLASS, EnumDyeColor.LIME.getMetadata(), "lime_stained_glass");
        modRegisterBlock(Blocks.STAINED_GLASS, EnumDyeColor.MAGENTA.getMetadata(), "magenta_stained_glass");
        modRegisterBlock(Blocks.STAINED_GLASS, EnumDyeColor.ORANGE.getMetadata(), "orange_stained_glass");
        modRegisterBlock(Blocks.STAINED_GLASS, EnumDyeColor.PINK.getMetadata(), "pink_stained_glass");
        modRegisterBlock(Blocks.STAINED_GLASS, EnumDyeColor.PURPLE.getMetadata(), "purple_stained_glass");
        modRegisterBlock(Blocks.STAINED_GLASS, EnumDyeColor.RED.getMetadata(), "red_stained_glass");
        modRegisterBlock(Blocks.STAINED_GLASS, EnumDyeColor.SILVER.getMetadata(), "silver_stained_glass");
        modRegisterBlock(Blocks.STAINED_GLASS, EnumDyeColor.WHITE.getMetadata(), "white_stained_glass");
        modRegisterBlock(Blocks.STAINED_GLASS, EnumDyeColor.YELLOW.getMetadata(), "yellow_stained_glass");
        modRegisterBlock(Blocks.STAINED_GLASS_PANE, EnumDyeColor.BLACK.getMetadata(), "black_stained_glass_pane");
        modRegisterBlock(Blocks.STAINED_GLASS_PANE, EnumDyeColor.BLUE.getMetadata(), "blue_stained_glass_pane");
        modRegisterBlock(Blocks.STAINED_GLASS_PANE, EnumDyeColor.BROWN.getMetadata(), "brown_stained_glass_pane");
        modRegisterBlock(Blocks.STAINED_GLASS_PANE, EnumDyeColor.CYAN.getMetadata(), "cyan_stained_glass_pane");
        modRegisterBlock(Blocks.STAINED_GLASS_PANE, EnumDyeColor.GRAY.getMetadata(), "gray_stained_glass_pane");
        modRegisterBlock(Blocks.STAINED_GLASS_PANE, EnumDyeColor.GREEN.getMetadata(), "green_stained_glass_pane");
        modRegisterBlock(Blocks.STAINED_GLASS_PANE, EnumDyeColor.LIGHT_BLUE.getMetadata(), "light_blue_stained_glass_pane");
        modRegisterBlock(Blocks.STAINED_GLASS_PANE, EnumDyeColor.LIME.getMetadata(), "lime_stained_glass_pane");
        modRegisterBlock(Blocks.STAINED_GLASS_PANE, EnumDyeColor.MAGENTA.getMetadata(), "magenta_stained_glass_pane");
        modRegisterBlock(Blocks.STAINED_GLASS_PANE, EnumDyeColor.ORANGE.getMetadata(), "orange_stained_glass_pane");
        modRegisterBlock(Blocks.STAINED_GLASS_PANE, EnumDyeColor.PINK.getMetadata(), "pink_stained_glass_pane");
        modRegisterBlock(Blocks.STAINED_GLASS_PANE, EnumDyeColor.PURPLE.getMetadata(), "purple_stained_glass_pane");
        modRegisterBlock(Blocks.STAINED_GLASS_PANE, EnumDyeColor.RED.getMetadata(), "red_stained_glass_pane");
        modRegisterBlock(Blocks.STAINED_GLASS_PANE, EnumDyeColor.SILVER.getMetadata(), "silver_stained_glass_pane");
        modRegisterBlock(Blocks.STAINED_GLASS_PANE, EnumDyeColor.WHITE.getMetadata(), "white_stained_glass_pane");
        modRegisterBlock(Blocks.STAINED_GLASS_PANE, EnumDyeColor.YELLOW.getMetadata(), "yellow_stained_glass_pane");
        modRegisterBlock(Blocks.STAINED_HARDENED_CLAY, EnumDyeColor.BLACK.getMetadata(), "black_stained_hardened_clay");
        modRegisterBlock(Blocks.STAINED_HARDENED_CLAY, EnumDyeColor.BLUE.getMetadata(), "blue_stained_hardened_clay");
        modRegisterBlock(Blocks.STAINED_HARDENED_CLAY, EnumDyeColor.BROWN.getMetadata(), "brown_stained_hardened_clay");
        modRegisterBlock(Blocks.STAINED_HARDENED_CLAY, EnumDyeColor.CYAN.getMetadata(), "cyan_stained_hardened_clay");
        modRegisterBlock(Blocks.STAINED_HARDENED_CLAY, EnumDyeColor.GRAY.getMetadata(), "gray_stained_hardened_clay");
        modRegisterBlock(Blocks.STAINED_HARDENED_CLAY, EnumDyeColor.GREEN.getMetadata(), "green_stained_hardened_clay");
        modRegisterBlock(Blocks.STAINED_HARDENED_CLAY, EnumDyeColor.LIGHT_BLUE.getMetadata(), "light_blue_stained_hardened_clay");
        modRegisterBlock(Blocks.STAINED_HARDENED_CLAY, EnumDyeColor.LIME.getMetadata(), "lime_stained_hardened_clay");
        modRegisterBlock(Blocks.STAINED_HARDENED_CLAY, EnumDyeColor.MAGENTA.getMetadata(), "magenta_stained_hardened_clay");
        modRegisterBlock(Blocks.STAINED_HARDENED_CLAY, EnumDyeColor.ORANGE.getMetadata(), "orange_stained_hardened_clay");
        modRegisterBlock(Blocks.STAINED_HARDENED_CLAY, EnumDyeColor.PINK.getMetadata(), "pink_stained_hardened_clay");
        modRegisterBlock(Blocks.STAINED_HARDENED_CLAY, EnumDyeColor.PURPLE.getMetadata(), "purple_stained_hardened_clay");
        modRegisterBlock(Blocks.STAINED_HARDENED_CLAY, EnumDyeColor.RED.getMetadata(), "red_stained_hardened_clay");
        modRegisterBlock(Blocks.STAINED_HARDENED_CLAY, EnumDyeColor.SILVER.getMetadata(), "silver_stained_hardened_clay");
        modRegisterBlock(Blocks.STAINED_HARDENED_CLAY, EnumDyeColor.WHITE.getMetadata(), "white_stained_hardened_clay");
        modRegisterBlock(Blocks.STAINED_HARDENED_CLAY, EnumDyeColor.YELLOW.getMetadata(), "yellow_stained_hardened_clay");
        modRegisterBlock(Blocks.STONE, BlockStone.EnumType.ANDESITE.getMetadata(), "andesite");
        modRegisterBlock(Blocks.STONE, BlockStone.EnumType.ANDESITE_SMOOTH.getMetadata(), "andesite_smooth");
        modRegisterBlock(Blocks.STONE, BlockStone.EnumType.DIORITE.getMetadata(), "diorite");
        modRegisterBlock(Blocks.STONE, BlockStone.EnumType.DIORITE_SMOOTH.getMetadata(), "diorite_smooth");
        modRegisterBlock(Blocks.STONE, BlockStone.EnumType.GRANITE.getMetadata(), "granite");
        modRegisterBlock(Blocks.STONE, BlockStone.EnumType.GRANITE_SMOOTH.getMetadata(), "granite_smooth");
        modRegisterBlock(Blocks.STONE, BlockStone.EnumType.STONE.getMetadata(), "stone");
        modRegisterBlock(Blocks.STONEBRICK, BlockStoneBrick.EnumType.CRACKED.getMetadata(), "cracked_stonebrick");
        modRegisterBlock(Blocks.STONEBRICK, BlockStoneBrick.EnumType.DEFAULT.getMetadata(), "stonebrick");
        modRegisterBlock(Blocks.STONEBRICK, BlockStoneBrick.EnumType.CHISELED.getMetadata(), "chiseled_stonebrick");
        modRegisterBlock(Blocks.STONEBRICK, BlockStoneBrick.EnumType.MOSSY.getMetadata(), "mossy_stonebrick");
        modRegisterBlock(Blocks.STONE_SLAB, BlockStoneSlab.EnumType.BRICK.getMetadata(), "brick_slab");
        modRegisterBlock(Blocks.STONE_SLAB, BlockStoneSlab.EnumType.COBBLESTONE.getMetadata(), "cobblestone_slab");
        modRegisterBlock(Blocks.STONE_SLAB, BlockStoneSlab.EnumType.WOOD.getMetadata(), "old_wood_slab");
        modRegisterBlock(Blocks.STONE_SLAB, BlockStoneSlab.EnumType.NETHERBRICK.getMetadata(), "nether_brick_slab");
        modRegisterBlock(Blocks.STONE_SLAB, BlockStoneSlab.EnumType.QUARTZ.getMetadata(), "quartz_slab");
        modRegisterBlock(Blocks.STONE_SLAB, BlockStoneSlab.EnumType.SAND.getMetadata(), "sandstone_slab");
        modRegisterBlock(Blocks.STONE_SLAB, BlockStoneSlab.EnumType.SMOOTHBRICK.getMetadata(), "stone_brick_slab");
        modRegisterBlock(Blocks.STONE_SLAB, BlockStoneSlab.EnumType.STONE.getMetadata(), "stone_slab");
        modRegisterBlock(Blocks.STONE_SLAB2, BlockStoneSlabNew.EnumType.RED_SANDSTONE.getMetadata(), "red_sandstone_slab");
        modRegisterBlock(Blocks.TALLGRASS, BlockTallGrass.EnumType.DEAD_BUSH.getMeta(), "dead_bush");
        modRegisterBlock(Blocks.TALLGRASS, BlockTallGrass.EnumType.FERN.getMeta(), "fern");
        modRegisterBlock(Blocks.TALLGRASS, BlockTallGrass.EnumType.GRASS.getMeta(), "tall_grass");
        modRegisterBlock(Blocks.WOODEN_SLAB, BlockPlanks.EnumType.ACACIA.getMetadata(), "acacia_slab");
        modRegisterBlock(Blocks.WOODEN_SLAB, BlockPlanks.EnumType.BIRCH.getMetadata(), "birch_slab");
        modRegisterBlock(Blocks.WOODEN_SLAB, BlockPlanks.EnumType.DARK_OAK.getMetadata(), "dark_oak_slab");
        modRegisterBlock(Blocks.WOODEN_SLAB, BlockPlanks.EnumType.JUNGLE.getMetadata(), "jungle_slab");
        modRegisterBlock(Blocks.WOODEN_SLAB, BlockPlanks.EnumType.OAK.getMetadata(), "oak_slab");
        modRegisterBlock(Blocks.WOODEN_SLAB, BlockPlanks.EnumType.SPRUCE.getMetadata(), "spruce_slab");
        modRegisterBlock(Blocks.WOOL, EnumDyeColor.BLACK.getMetadata(), "black_wool");
        modRegisterBlock(Blocks.WOOL, EnumDyeColor.BLUE.getMetadata(), "blue_wool");
        modRegisterBlock(Blocks.WOOL, EnumDyeColor.BROWN.getMetadata(), "brown_wool");
        modRegisterBlock(Blocks.WOOL, EnumDyeColor.CYAN.getMetadata(), "cyan_wool");
        modRegisterBlock(Blocks.WOOL, EnumDyeColor.GRAY.getMetadata(), "gray_wool");
        modRegisterBlock(Blocks.WOOL, EnumDyeColor.GREEN.getMetadata(), "green_wool");
        modRegisterBlock(Blocks.WOOL, EnumDyeColor.LIGHT_BLUE.getMetadata(), "light_blue_wool");
        modRegisterBlock(Blocks.WOOL, EnumDyeColor.LIME.getMetadata(), "lime_wool");
        modRegisterBlock(Blocks.WOOL, EnumDyeColor.MAGENTA.getMetadata(), "magenta_wool");
        modRegisterBlock(Blocks.WOOL, EnumDyeColor.ORANGE.getMetadata(), "orange_wool");
        modRegisterBlock(Blocks.WOOL, EnumDyeColor.PINK.getMetadata(), "pink_wool");
        modRegisterBlock(Blocks.WOOL, EnumDyeColor.PURPLE.getMetadata(), "purple_wool");
        modRegisterBlock(Blocks.WOOL, EnumDyeColor.RED.getMetadata(), "red_wool");
        modRegisterBlock(Blocks.WOOL, EnumDyeColor.SILVER.getMetadata(), "silver_wool");
        modRegisterBlock(Blocks.WOOL, EnumDyeColor.WHITE.getMetadata(), "white_wool");
        modRegisterBlock(Blocks.WOOL, EnumDyeColor.YELLOW.getMetadata(), "yellow_wool");
        modRegisterBlock(Blocks.FARMLAND, "farmland");
        modRegisterBlock(Blocks.ACACIA_STAIRS, "acacia_stairs");
        modRegisterBlock(Blocks.ACTIVATOR_RAIL, "activator_rail");
        modRegisterBlock(Blocks.BEACON, "beacon");
        modRegisterBlock(Blocks.BEDROCK, "bedrock");
        modRegisterBlock(Blocks.BIRCH_STAIRS, "birch_stairs");
        modRegisterBlock(Blocks.BOOKSHELF, "bookshelf");
        modRegisterBlock(Blocks.BRICK_BLOCK, "brick_block");
        modRegisterBlock(Blocks.BRICK_BLOCK, "brick_block");
        modRegisterBlock(Blocks.BRICK_STAIRS, "brick_stairs");
        modRegisterBlock(Blocks.BROWN_MUSHROOM, "brown_mushroom");
        modRegisterBlock(Blocks.CACTUS, "cactus");
        modRegisterBlock(Blocks.CLAY, "clay");
        modRegisterBlock(Blocks.COAL_BLOCK, "coal_block");
        modRegisterBlock(Blocks.COAL_ORE, "coal_ore");
        modRegisterBlock(Blocks.COBBLESTONE, "cobblestone");
        modRegisterBlock(Blocks.CRAFTING_TABLE, "crafting_table");
        modRegisterBlock(Blocks.DARK_OAK_STAIRS, "dark_oak_stairs");
        modRegisterBlock(Blocks.DAYLIGHT_DETECTOR, "daylight_detector");
        modRegisterBlock(Blocks.DEADBUSH, "dead_bush");
        modRegisterBlock(Blocks.DETECTOR_RAIL, "detector_rail");
        modRegisterBlock(Blocks.DIAMOND_BLOCK, "diamond_block");
        modRegisterBlock(Blocks.DIAMOND_ORE, "diamond_ore");
        modRegisterBlock(Blocks.DISPENSER, "dispenser");
        modRegisterBlock(Blocks.DROPPER, "dropper");
        modRegisterBlock(Blocks.EMERALD_BLOCK, "emerald_block");
        modRegisterBlock(Blocks.EMERALD_ORE, "emerald_ore");
        modRegisterBlock(Blocks.ENCHANTING_TABLE, "enchanting_table");
        modRegisterBlock(Blocks.END_PORTAL_FRAME, "end_portal_frame");
        modRegisterBlock(Blocks.END_STONE, "end_stone");
        modRegisterBlock(Blocks.OAK_FENCE, "oak_fence");
        modRegisterBlock(Blocks.SPRUCE_FENCE, "spruce_fence");
        modRegisterBlock(Blocks.BIRCH_FENCE, "birch_fence");
        modRegisterBlock(Blocks.JUNGLE_FENCE, "jungle_fence");
        modRegisterBlock(Blocks.DARK_OAK_FENCE, "dark_oak_fence");
        modRegisterBlock(Blocks.ACACIA_FENCE, "acacia_fence");
        modRegisterBlock(Blocks.OAK_FENCE_GATE, "oak_fence_gate");
        modRegisterBlock(Blocks.SPRUCE_FENCE_GATE, "spruce_fence_gate");
        modRegisterBlock(Blocks.BIRCH_FENCE_GATE, "birch_fence_gate");
        modRegisterBlock(Blocks.JUNGLE_FENCE_GATE, "jungle_fence_gate");
        modRegisterBlock(Blocks.DARK_OAK_FENCE_GATE, "dark_oak_fence_gate");
        modRegisterBlock(Blocks.ACACIA_FENCE_GATE, "acacia_fence_gate");
        modRegisterBlock(Blocks.FURNACE, "furnace");
        modRegisterBlock(Blocks.GLASS, "glass");
        modRegisterBlock(Blocks.GLASS_PANE, "glass_pane");
        modRegisterBlock(Blocks.GLOWSTONE, "glowstone");
        modRegisterBlock(Blocks.GOLDEN_RAIL, "golden_rail");
        modRegisterBlock(Blocks.GOLD_BLOCK, "gold_block");
        modRegisterBlock(Blocks.GOLD_ORE, "gold_ore");
        modRegisterBlock(Blocks.GRASS, "grass");
        modRegisterBlock(Blocks.GRASS_PATH, "grass_path");
        modRegisterBlock(Blocks.GRAVEL, "gravel");
        modRegisterBlock(Blocks.HARDENED_CLAY, "hardened_clay");
        modRegisterBlock(Blocks.HAY_BLOCK, "hay_block");
        modRegisterBlock(Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE, "heavy_weighted_pressure_plate");
        modRegisterBlock(Blocks.HOPPER, "hopper");
        modRegisterBlock(Blocks.ICE, "ice");
        modRegisterBlock(Blocks.IRON_BARS, "iron_bars");
        modRegisterBlock(Blocks.IRON_BLOCK, "iron_block");
        modRegisterBlock(Blocks.IRON_ORE, "iron_ore");
        modRegisterBlock(Blocks.IRON_TRAPDOOR, "iron_trapdoor");
        modRegisterBlock(Blocks.JUKEBOX, "jukebox");
        modRegisterBlock(Blocks.JUNGLE_STAIRS, "jungle_stairs");
        modRegisterBlock(Blocks.LADDER, "ladder");
        modRegisterBlock(Blocks.LAPIS_BLOCK, "lapis_block");
        modRegisterBlock(Blocks.LAPIS_ORE, "lapis_ore");
        modRegisterBlock(Blocks.LEVER, "lever");
        modRegisterBlock(Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE, "light_weighted_pressure_plate");
        modRegisterBlock(Blocks.LIT_PUMPKIN, "lit_pumpkin");
        modRegisterBlock(Blocks.MELON_BLOCK, "melon_block");
        modRegisterBlock(Blocks.MOSSY_COBBLESTONE, "mossy_cobblestone");
        modRegisterBlock(Blocks.MYCELIUM, "mycelium");
        modRegisterBlock(Blocks.NETHERRACK, "netherrack");
        modRegisterBlock(Blocks.NETHER_BRICK, "nether_brick");
        modRegisterBlock(Blocks.NETHER_BRICK_FENCE, "nether_brick_fence");
        modRegisterBlock(Blocks.NETHER_BRICK_STAIRS, "nether_brick_stairs");
        modRegisterBlock(Blocks.NOTEBLOCK, "noteblock");
        modRegisterBlock(Blocks.OAK_STAIRS, "oak_stairs");
        modRegisterBlock(Blocks.OBSIDIAN, "obsidian");
        modRegisterBlock(Blocks.PACKED_ICE, "packed_ice");
        modRegisterBlock(Blocks.PISTON, "piston");
        modRegisterBlock(Blocks.PUMPKIN, "pumpkin");
        modRegisterBlock(Blocks.QUARTZ_ORE, "quartz_ore");
        modRegisterBlock(Blocks.QUARTZ_STAIRS, "quartz_stairs");
        modRegisterBlock(Blocks.RAIL, "rail");
        modRegisterBlock(Blocks.REDSTONE_BLOCK, "redstone_block");
        modRegisterBlock(Blocks.REDSTONE_LAMP, "redstone_lamp");
        modRegisterBlock(Blocks.REDSTONE_ORE, "redstone_ore");
        modRegisterBlock(Blocks.REDSTONE_TORCH, "redstone_torch");
        modRegisterBlock(Blocks.RED_MUSHROOM, "red_mushroom");
        modRegisterBlock(Blocks.SANDSTONE_STAIRS, "sandstone_stairs");
        modRegisterBlock(Blocks.RED_SANDSTONE_STAIRS, "red_sandstone_stairs");
        modRegisterBlock(Blocks.SEA_LANTERN, "sea_lantern");
        modRegisterBlock(Blocks.SLIME_BLOCK, "slime");
        modRegisterBlock(Blocks.SNOW, "snow");
        modRegisterBlock(Blocks.SNOW_LAYER, "snow_layer");
        modRegisterBlock(Blocks.SOUL_SAND, "soul_sand");
        modRegisterBlock(Blocks.SPRUCE_STAIRS, "spruce_stairs");
        modRegisterBlock(Blocks.STICKY_PISTON, "sticky_piston");
        modRegisterBlock(Blocks.STONE_BRICK_STAIRS, "stone_brick_stairs");
        modRegisterBlock(Blocks.STONE_BUTTON, "stone_button");
        modRegisterBlock(Blocks.STONE_PRESSURE_PLATE, "stone_pressure_plate");
        modRegisterBlock(Blocks.STONE_STAIRS, "stone_stairs");
        modRegisterBlock(Blocks.TNT, "tnt");
        modRegisterBlock(Blocks.TORCH, "torch");
        modRegisterBlock(Blocks.TRAPDOOR, "trapdoor");
        modRegisterBlock(Blocks.TRIPWIRE_HOOK, "tripwire_hook");
        modRegisterBlock(Blocks.VINE, "vine");
        modRegisterBlock(Blocks.WATERLILY, "waterlily");
        modRegisterBlock(Blocks.WEB, "web");
        modRegisterBlock(Blocks.WOODEN_BUTTON, "wooden_button");
        modRegisterBlock(Blocks.WOODEN_PRESSURE_PLATE, "wooden_pressure_plate");
        modRegisterBlock(Blocks.YELLOW_FLOWER, BlockFlower.EnumFlowerType.DANDELION.getMeta(), "dandelion");
        modRegisterBlock(Blocks.END_ROD, "end_rod");
        modRegisterBlock(Blocks.CHORUS_PLANT, "chorus_plant");
        modRegisterBlock(Blocks.CHORUS_FLOWER, "chorus_flower");
        modRegisterBlock(Blocks.PURPUR_BLOCK, "purpur_block");
        modRegisterBlock(Blocks.PURPUR_PILLAR, "purpur_pillar");
        modRegisterBlock(Blocks.PURPUR_STAIRS, "purpur_stairs");
        modRegisterBlock(Blocks.PURPUR_SLAB, "purpur_slab");
        modRegisterBlock(Blocks.PURPUR_DOUBLE_SLAB, "purpur_double_slab");
        modRegisterBlock(Blocks.END_BRICKS, "end_bricks");
        modRegisterBlock(Blocks.MAGMA, "magma");
        modRegisterBlock(Blocks.NETHER_WART_BLOCK, "nether_wart_block");
        modRegisterBlock(Blocks.RED_NETHER_BRICK, "red_nether_brick");
        modRegisterBlock(Blocks.BONE_BLOCK, "bone_block");
        modRegisterBlock(Blocks.STRUCTURE_VOID, "structure_void");
        modRegisterBlock(Blocks.OBSERVER, "observer");
        modRegisterBlock(Blocks.WHITE_SHULKER_BOX, "white_shulker_box");
        modRegisterBlock(Blocks.ORANGE_SHULKER_BOX, "orange_shulker_box");
        modRegisterBlock(Blocks.MAGENTA_SHULKER_BOX, "magenta_shulker_box");
        modRegisterBlock(Blocks.LIGHT_BLUE_SHULKER_BOX, "light_blue_shulker_box");
        modRegisterBlock(Blocks.YELLOW_SHULKER_BOX, "yellow_shulker_box");
        modRegisterBlock(Blocks.LIME_SHULKER_BOX, "lime_shulker_box");
        modRegisterBlock(Blocks.PINK_SHULKER_BOX, "pink_shulker_box");
        modRegisterBlock(Blocks.GRAY_SHULKER_BOX, "gray_shulker_box");
        modRegisterBlock(Blocks.SILVER_SHULKER_BOX, "silver_shulker_box");
        modRegisterBlock(Blocks.CYAN_SHULKER_BOX, "cyan_shulker_box");
        modRegisterBlock(Blocks.PURPLE_SHULKER_BOX, "purple_shulker_box");
        modRegisterBlock(Blocks.BLUE_SHULKER_BOX, "blue_shulker_box");
        modRegisterBlock(Blocks.BROWN_SHULKER_BOX, "brown_shulker_box");
        modRegisterBlock(Blocks.GREEN_SHULKER_BOX, "green_shulker_box");
        modRegisterBlock(Blocks.RED_SHULKER_BOX, "red_shulker_box");
        modRegisterBlock(Blocks.BLACK_SHULKER_BOX, "black_shulker_box");
        modRegisterBlock(Blocks.WHITE_GLAZED_TERRACOTTA, "white_glazed_terracotta");
        modRegisterBlock(Blocks.ORANGE_GLAZED_TERRACOTTA, "orange_glazed_terracotta");
        modRegisterBlock(Blocks.MAGENTA_GLAZED_TERRACOTTA, "magenta_glazed_terracotta");
        modRegisterBlock(Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA, "light_blue_glazed_terracotta");
        modRegisterBlock(Blocks.YELLOW_GLAZED_TERRACOTTA, "yellow_glazed_terracotta");
        modRegisterBlock(Blocks.LIME_GLAZED_TERRACOTTA, "lime_glazed_terracotta");
        modRegisterBlock(Blocks.PINK_GLAZED_TERRACOTTA, "pink_glazed_terracotta");
        modRegisterBlock(Blocks.GRAY_GLAZED_TERRACOTTA, "gray_glazed_terracotta");
        modRegisterBlock(Blocks.SILVER_GLAZED_TERRACOTTA, "silver_glazed_terracotta");
        modRegisterBlock(Blocks.CYAN_GLAZED_TERRACOTTA, "cyan_glazed_terracotta");
        modRegisterBlock(Blocks.PURPLE_GLAZED_TERRACOTTA, "purple_glazed_terracotta");
        modRegisterBlock(Blocks.BLUE_GLAZED_TERRACOTTA, "blue_glazed_terracotta");
        modRegisterBlock(Blocks.BROWN_GLAZED_TERRACOTTA, "brown_glazed_terracotta");
        modRegisterBlock(Blocks.GREEN_GLAZED_TERRACOTTA, "green_glazed_terracotta");
        modRegisterBlock(Blocks.RED_GLAZED_TERRACOTTA, "red_glazed_terracotta");
        modRegisterBlock(Blocks.BLACK_GLAZED_TERRACOTTA, "black_glazed_terracotta");

        for (EnumDyeColor enumdyecolor : EnumDyeColor.values())
        {
            modRegisterBlock(Blocks.CONCRETE, enumdyecolor.getMetadata(), enumdyecolor.getDyeColorName() + "_concrete");
            modRegisterBlock(Blocks.CONCRETE_POWDER, enumdyecolor.getMetadata(), enumdyecolor.getDyeColorName() + "_concrete_powder");
        }

        modRegisterBlock(Blocks.CHEST, "chest");
        modRegisterBlock(Blocks.TRAPPED_CHEST, "trapped_chest");
        modRegisterBlock(Blocks.ENDER_CHEST, "ender_chest");
        modRegisterItem(Items.IRON_SHOVEL, "iron_shovel");
        modRegisterItem(Items.IRON_PICKAXE, "iron_pickaxe");
        modRegisterItem(Items.IRON_AXE, "iron_axe");
        modRegisterItem(Items.FLINT_AND_STEEL, "flint_and_steel");
        modRegisterItem(Items.APPLE, "apple");
        modRegisterItem(Items.BOW, "bow");
        modRegisterItem(Items.ARROW, "arrow");
        modRegisterItem(Items.SPECTRAL_ARROW, "spectral_arrow");
        modRegisterItem(Items.TIPPED_ARROW, "tipped_arrow");
        modRegisterItem(Items.COAL, 0, "coal");
        modRegisterItem(Items.COAL, 1, "charcoal");
        modRegisterItem(Items.DIAMOND, "diamond");
        modRegisterItem(Items.IRON_INGOT, "iron_ingot");
        modRegisterItem(Items.GOLD_INGOT, "gold_ingot");
        modRegisterItem(Items.IRON_SWORD, "iron_sword");
        modRegisterItem(Items.WOODEN_SWORD, "wooden_sword");
        modRegisterItem(Items.WOODEN_SHOVEL, "wooden_shovel");
        modRegisterItem(Items.WOODEN_PICKAXE, "wooden_pickaxe");
        modRegisterItem(Items.WOODEN_AXE, "wooden_axe");
        modRegisterItem(Items.STONE_SWORD, "stone_sword");
        modRegisterItem(Items.STONE_SHOVEL, "stone_shovel");
        modRegisterItem(Items.STONE_PICKAXE, "stone_pickaxe");
        modRegisterItem(Items.STONE_AXE, "stone_axe");
        modRegisterItem(Items.DIAMOND_SWORD, "diamond_sword");
        modRegisterItem(Items.DIAMOND_SHOVEL, "diamond_shovel");
        modRegisterItem(Items.DIAMOND_PICKAXE, "diamond_pickaxe");
        modRegisterItem(Items.DIAMOND_AXE, "diamond_axe");
        modRegisterItem(Items.STICK, "stick");
        modRegisterItem(Items.BOWL, "bowl");
        modRegisterItem(Items.MUSHROOM_STEW, "mushroom_stew");
        modRegisterItem(Items.GOLDEN_SWORD, "golden_sword");
        modRegisterItem(Items.GOLDEN_SHOVEL, "golden_shovel");
        modRegisterItem(Items.GOLDEN_PICKAXE, "golden_pickaxe");
        modRegisterItem(Items.GOLDEN_AXE, "golden_axe");
        modRegisterItem(Items.STRING, "string");
        modRegisterItem(Items.FEATHER, "feather");
        modRegisterItem(Items.GUNPOWDER, "gunpowder");
        modRegisterItem(Items.WOODEN_HOE, "wooden_hoe");
        modRegisterItem(Items.STONE_HOE, "stone_hoe");
        modRegisterItem(Items.IRON_HOE, "iron_hoe");
        modRegisterItem(Items.DIAMOND_HOE, "diamond_hoe");
        modRegisterItem(Items.GOLDEN_HOE, "golden_hoe");
        modRegisterItem(Items.WHEAT_SEEDS, "wheat_seeds");
        modRegisterItem(Items.WHEAT, "wheat");
        modRegisterItem(Items.BREAD, "bread");
        modRegisterItem(Items.LEATHER_HELMET, "leather_helmet");
        modRegisterItem(Items.LEATHER_CHESTPLATE, "leather_chestplate");
        modRegisterItem(Items.LEATHER_LEGGINGS, "leather_leggings");
        modRegisterItem(Items.LEATHER_BOOTS, "leather_boots");
        modRegisterItem(Items.CHAINMAIL_HELMET, "chainmail_helmet");
        modRegisterItem(Items.CHAINMAIL_CHESTPLATE, "chainmail_chestplate");
        modRegisterItem(Items.CHAINMAIL_LEGGINGS, "chainmail_leggings");
        modRegisterItem(Items.CHAINMAIL_BOOTS, "chainmail_boots");
        modRegisterItem(Items.IRON_HELMET, "iron_helmet");
        modRegisterItem(Items.IRON_CHESTPLATE, "iron_chestplate");
        modRegisterItem(Items.IRON_LEGGINGS, "iron_leggings");
        modRegisterItem(Items.IRON_BOOTS, "iron_boots");
        modRegisterItem(Items.DIAMOND_HELMET, "diamond_helmet");
        modRegisterItem(Items.DIAMOND_CHESTPLATE, "diamond_chestplate");
        modRegisterItem(Items.DIAMOND_LEGGINGS, "diamond_leggings");
        modRegisterItem(Items.DIAMOND_BOOTS, "diamond_boots");
        modRegisterItem(Items.GOLDEN_HELMET, "golden_helmet");
        modRegisterItem(Items.GOLDEN_CHESTPLATE, "golden_chestplate");
        modRegisterItem(Items.GOLDEN_LEGGINGS, "golden_leggings");
        modRegisterItem(Items.GOLDEN_BOOTS, "golden_boots");
        modRegisterItem(Items.FLINT, "flint");
        modRegisterItem(Items.PORKCHOP, "porkchop");
        modRegisterItem(Items.COOKED_PORKCHOP, "cooked_porkchop");
        modRegisterItem(Items.PAINTING, "painting");
        modRegisterItem(Items.GOLDEN_APPLE, "golden_apple");
        modRegisterItem(Items.GOLDEN_APPLE, 1, "golden_apple");
        modRegisterItem(Items.SIGN, "sign");
        modRegisterItem(Items.OAK_DOOR, "oak_door");
        modRegisterItem(Items.SPRUCE_DOOR, "spruce_door");
        modRegisterItem(Items.BIRCH_DOOR, "birch_door");
        modRegisterItem(Items.JUNGLE_DOOR, "jungle_door");
        modRegisterItem(Items.ACACIA_DOOR, "acacia_door");
        modRegisterItem(Items.DARK_OAK_DOOR, "dark_oak_door");
        modRegisterItem(Items.BUCKET, "bucket");
        modRegisterItem(Items.WATER_BUCKET, "water_bucket");
        modRegisterItem(Items.LAVA_BUCKET, "lava_bucket");
        modRegisterItem(Items.MINECART, "minecart");
        modRegisterItem(Items.SADDLE, "saddle");
        modRegisterItem(Items.IRON_DOOR, "iron_door");
        modRegisterItem(Items.REDSTONE, "redstone");
        modRegisterItem(Items.SNOWBALL, "snowball");
        modRegisterItem(Items.BOAT, "oak_boat");
        modRegisterItem(Items.SPRUCE_BOAT, "spruce_boat");
        modRegisterItem(Items.BIRCH_BOAT, "birch_boat");
        modRegisterItem(Items.JUNGLE_BOAT, "jungle_boat");
        modRegisterItem(Items.ACACIA_BOAT, "acacia_boat");
        modRegisterItem(Items.DARK_OAK_BOAT, "dark_oak_boat");
        modRegisterItem(Items.LEATHER, "leather");
        modRegisterItem(Items.MILK_BUCKET, "milk_bucket");
        modRegisterItem(Items.BRICK, "brick");
        modRegisterItem(Items.CLAY_BALL, "clay_ball");
        modRegisterItem(Items.REEDS, "reeds");
        modRegisterItem(Items.PAPER, "paper");
        modRegisterItem(Items.BOOK, "book");
        modRegisterItem(Items.SLIME_BALL, "slime_ball");
        modRegisterItem(Items.CHEST_MINECART, "chest_minecart");
        modRegisterItem(Items.FURNACE_MINECART, "furnace_minecart");
        modRegisterItem(Items.EGG, "egg");
        modRegisterItem(Items.COMPASS, "compass");
        modRegisterItem(Items.FISHING_ROD, "fishing_rod");
        modRegisterItem(Items.CLOCK, "clock");
        modRegisterItem(Items.GLOWSTONE_DUST, "glowstone_dust");
        modRegisterItem(Items.FISH, ItemFishFood.FishType.COD.getMetadata(), "cod");
        modRegisterItem(Items.FISH, ItemFishFood.FishType.SALMON.getMetadata(), "salmon");
        modRegisterItem(Items.FISH, ItemFishFood.FishType.CLOWNFISH.getMetadata(), "clownfish");
        modRegisterItem(Items.FISH, ItemFishFood.FishType.PUFFERFISH.getMetadata(), "pufferfish");
        modRegisterItem(Items.COOKED_FISH, ItemFishFood.FishType.COD.getMetadata(), "cooked_cod");
        modRegisterItem(Items.COOKED_FISH, ItemFishFood.FishType.SALMON.getMetadata(), "cooked_salmon");
        modRegisterItem(Items.DYE, EnumDyeColor.BLACK.getDyeDamage(), "dye_black");
        modRegisterItem(Items.DYE, EnumDyeColor.RED.getDyeDamage(), "dye_red");
        modRegisterItem(Items.DYE, EnumDyeColor.GREEN.getDyeDamage(), "dye_green");
        modRegisterItem(Items.DYE, EnumDyeColor.BROWN.getDyeDamage(), "dye_brown");
        modRegisterItem(Items.DYE, EnumDyeColor.BLUE.getDyeDamage(), "dye_blue");
        modRegisterItem(Items.DYE, EnumDyeColor.PURPLE.getDyeDamage(), "dye_purple");
        modRegisterItem(Items.DYE, EnumDyeColor.CYAN.getDyeDamage(), "dye_cyan");
        modRegisterItem(Items.DYE, EnumDyeColor.SILVER.getDyeDamage(), "dye_silver");
        modRegisterItem(Items.DYE, EnumDyeColor.GRAY.getDyeDamage(), "dye_gray");
        modRegisterItem(Items.DYE, EnumDyeColor.PINK.getDyeDamage(), "dye_pink");
        modRegisterItem(Items.DYE, EnumDyeColor.LIME.getDyeDamage(), "dye_lime");
        modRegisterItem(Items.DYE, EnumDyeColor.YELLOW.getDyeDamage(), "dye_yellow");
        modRegisterItem(Items.DYE, EnumDyeColor.LIGHT_BLUE.getDyeDamage(), "dye_light_blue");
        modRegisterItem(Items.DYE, EnumDyeColor.MAGENTA.getDyeDamage(), "dye_magenta");
        modRegisterItem(Items.DYE, EnumDyeColor.ORANGE.getDyeDamage(), "dye_orange");
        modRegisterItem(Items.DYE, EnumDyeColor.WHITE.getDyeDamage(), "dye_white");
        modRegisterItem(Items.BONE, "bone");
        modRegisterItem(Items.SUGAR, "sugar");
        modRegisterItem(Items.CAKE, "cake");
        modRegisterItem(Items.REPEATER, "repeater");
        modRegisterItem(Items.COOKIE, "cookie");
        modRegisterItem(Items.SHEARS, "shears");
        modRegisterItem(Items.MELON, "melon");
        modRegisterItem(Items.PUMPKIN_SEEDS, "pumpkin_seeds");
        modRegisterItem(Items.MELON_SEEDS, "melon_seeds");
        modRegisterItem(Items.BEEF, "beef");
        modRegisterItem(Items.COOKED_BEEF, "cooked_beef");
        modRegisterItem(Items.CHICKEN, "chicken");
        modRegisterItem(Items.COOKED_CHICKEN, "cooked_chicken");
        modRegisterItem(Items.RABBIT, "rabbit");
        modRegisterItem(Items.COOKED_RABBIT, "cooked_rabbit");
        modRegisterItem(Items.MUTTON, "mutton");
        modRegisterItem(Items.COOKED_MUTTON, "cooked_mutton");
        modRegisterItem(Items.RABBIT_FOOT, "rabbit_foot");
        modRegisterItem(Items.RABBIT_HIDE, "rabbit_hide");
        modRegisterItem(Items.RABBIT_STEW, "rabbit_stew");
        modRegisterItem(Items.ROTTEN_FLESH, "rotten_flesh");
        modRegisterItem(Items.ENDER_PEARL, "ender_pearl");
        modRegisterItem(Items.BLAZE_ROD, "blaze_rod");
        modRegisterItem(Items.GHAST_TEAR, "ghast_tear");
        modRegisterItem(Items.GOLD_NUGGET, "gold_nugget");
        modRegisterItem(Items.NETHER_WART, "nether_wart");
        modRegisterItem(Items.BEETROOT, "beetroot");
        modRegisterItem(Items.BEETROOT_SEEDS, "beetroot_seeds");
        modRegisterItem(Items.BEETROOT_SOUP, "beetroot_soup");
        modRegisterItem(Items.TOTEM_OF_UNDYING, "totem");
        modRegisterItem(Items.POTIONITEM, "bottle_drinkable");
        modRegisterItem(Items.SPLASH_POTION, "bottle_splash");
        modRegisterItem(Items.LINGERING_POTION, "bottle_lingering");
        modRegisterItem(Items.GLASS_BOTTLE, "glass_bottle");
        modRegisterItem(Items.DRAGON_BREATH, "dragon_breath");
        modRegisterItem(Items.SPIDER_EYE, "spider_eye");
        modRegisterItem(Items.FERMENTED_SPIDER_EYE, "fermented_spider_eye");
        modRegisterItem(Items.BLAZE_POWDER, "blaze_powder");
        modRegisterItem(Items.MAGMA_CREAM, "magma_cream");
        modRegisterItem(Items.BREWING_STAND, "brewing_stand");
        modRegisterItem(Items.CAULDRON, "cauldron");
        modRegisterItem(Items.ENDER_EYE, "ender_eye");
        modRegisterItem(Items.SPECKLED_MELON, "speckled_melon");
        this.itemModelMesher.register(Items.SPAWN_EGG, new ItemMeshDefinition()
        {
            @Override
            public ModelResourceLocation getModelLocation(ItemStack stack)
            {
                return new ModelResourceLocation("spawn_egg", "inventory");
            }
        });
        modRegisterItem(Items.EXPERIENCE_BOTTLE, "experience_bottle");
        modRegisterItem(Items.FIRE_CHARGE, "fire_charge");
        modRegisterItem(Items.WRITABLE_BOOK, "writable_book");
        modRegisterItem(Items.EMERALD, "emerald");
        modRegisterItem(Items.ITEM_FRAME, "item_frame");
        modRegisterItem(Items.FLOWER_POT, "flower_pot");
        modRegisterItem(Items.CARROT, "carrot");
        modRegisterItem(Items.POTATO, "potato");
        modRegisterItem(Items.BAKED_POTATO, "baked_potato");
        modRegisterItem(Items.POISONOUS_POTATO, "poisonous_potato");
        modRegisterItem(Items.MAP, "map");
        modRegisterItem(Items.GOLDEN_CARROT, "golden_carrot");
        modRegisterItem(Items.SKULL, 0, "skull_skeleton");
        modRegisterItem(Items.SKULL, 1, "skull_wither");
        modRegisterItem(Items.SKULL, 2, "skull_zombie");
        modRegisterItem(Items.SKULL, 3, "skull_char");
        modRegisterItem(Items.SKULL, 4, "skull_creeper");
        modRegisterItem(Items.SKULL, 5, "skull_dragon");
        modRegisterItem(Items.CARROT_ON_A_STICK, "carrot_on_a_stick");
        modRegisterItem(Items.NETHER_STAR, "nether_star");
        modRegisterItem(Items.END_CRYSTAL, "end_crystal");
        modRegisterItem(Items.PUMPKIN_PIE, "pumpkin_pie");
        modRegisterItem(Items.FIREWORK_CHARGE, "firework_charge");
        modRegisterItem(Items.COMPARATOR, "comparator");
        modRegisterItem(Items.NETHERBRICK, "netherbrick");
        modRegisterItem(Items.QUARTZ, "quartz");
        modRegisterItem(Items.TNT_MINECART, "tnt_minecart");
        modRegisterItem(Items.HOPPER_MINECART, "hopper_minecart");
        modRegisterItem(Items.ARMOR_STAND, "armor_stand");
        modRegisterItem(Items.IRON_HORSE_ARMOR, "iron_horse_armor");
        modRegisterItem(Items.GOLDEN_HORSE_ARMOR, "golden_horse_armor");
        modRegisterItem(Items.DIAMOND_HORSE_ARMOR, "diamond_horse_armor");
        modRegisterItem(Items.LEAD, "lead");
        modRegisterItem(Items.NAME_TAG, "name_tag");
        this.itemModelMesher.register(Items.BANNER, new ItemMeshDefinition()
        {
            @Override
            public ModelResourceLocation getModelLocation(ItemStack stack)
            {
                return new ModelResourceLocation("banner", "inventory");
            }
        });
        this.itemModelMesher.register(Items.BED, new ItemMeshDefinition()
        {
            @Override
            public ModelResourceLocation getModelLocation(ItemStack stack)
            {
                return new ModelResourceLocation("bed", "inventory");
            }
        });
        this.itemModelMesher.register(Items.SHIELD, new ItemMeshDefinition()
        {
            @Override
            public ModelResourceLocation getModelLocation(ItemStack stack)
            {
                return new ModelResourceLocation("shield", "inventory");
            }
        });
        modRegisterItem(Items.ELYTRA, "elytra");
        modRegisterItem(Items.CHORUS_FRUIT, "chorus_fruit");
        modRegisterItem(Items.CHORUS_FRUIT_POPPED, "chorus_fruit_popped");
        modRegisterItem(Items.SHULKER_SHELL, "shulker_shell");
        modRegisterItem(Items.IRON_NUGGET, "iron_nugget");
        modRegisterItem(Items.RECORD_13, "record_13");
        modRegisterItem(Items.RECORD_CAT, "record_cat");
        modRegisterItem(Items.RECORD_BLOCKS, "record_blocks");
        modRegisterItem(Items.RECORD_CHIRP, "record_chirp");
        modRegisterItem(Items.RECORD_FAR, "record_far");
        modRegisterItem(Items.RECORD_MALL, "record_mall");
        modRegisterItem(Items.RECORD_MELLOHI, "record_mellohi");
        modRegisterItem(Items.RECORD_STAL, "record_stal");
        modRegisterItem(Items.RECORD_STRAD, "record_strad");
        modRegisterItem(Items.RECORD_WARD, "record_ward");
        modRegisterItem(Items.RECORD_11, "record_11");
        modRegisterItem(Items.RECORD_WAIT, "record_wait");
        modRegisterItem(Items.PRISMARINE_SHARD, "prismarine_shard");
        modRegisterItem(Items.PRISMARINE_CRYSTALS, "prismarine_crystals");
        modRegisterItem(Items.KNOWLEDGE_BOOK, "knowledge_book");
        this.itemModelMesher.register(Items.ENCHANTED_BOOK, new ItemMeshDefinition()
        {
            @Override
            public ModelResourceLocation getModelLocation(ItemStack stack)
            {
                return new ModelResourceLocation("enchanted_book", "inventory");
            }
        });
        this.itemModelMesher.register(Items.FILLED_MAP, new ItemMeshDefinition()
        {
            @Override
            public ModelResourceLocation getModelLocation(ItemStack stack)
            {
                return new ModelResourceLocation("filled_map", "inventory");
            }
        });
        modRegisterBlock(Blocks.COMMAND_BLOCK, "command_block");
        modRegisterItem(Items.FIREWORKS, "fireworks");
        modRegisterItem(Items.COMMAND_BLOCK_MINECART, "command_block_minecart");
        modRegisterBlock(Blocks.BARRIER, "barrier");
        modRegisterBlock(Blocks.MOB_SPAWNER, "mob_spawner");
        modRegisterItem(Items.WRITTEN_BOOK, "written_book");
        modRegisterBlock(Blocks.BROWN_MUSHROOM_BLOCK, BlockHugeMushroom.EnumType.ALL_INSIDE.getMetadata(), "brown_mushroom_block");
        modRegisterBlock(Blocks.RED_MUSHROOM_BLOCK, BlockHugeMushroom.EnumType.ALL_INSIDE.getMetadata(), "red_mushroom_block");
        modRegisterBlock(Blocks.DRAGON_EGG, "dragon_egg");
        modRegisterBlock(Blocks.REPEATING_COMMAND_BLOCK, "repeating_command_block");
        modRegisterBlock(Blocks.CHAIN_COMMAND_BLOCK, "chain_command_block");
        modRegisterBlock(Blocks.STRUCTURE_BLOCK, TileEntityStructure.Mode.SAVE.getModeId(), "structure_block");
        modRegisterBlock(Blocks.STRUCTURE_BLOCK, TileEntityStructure.Mode.LOAD.getModeId(), "structure_block");
        modRegisterBlock(Blocks.STRUCTURE_BLOCK, TileEntityStructure.Mode.CORNER.getModeId(), "structure_block");
        modRegisterBlock(Blocks.STRUCTURE_BLOCK, TileEntityStructure.Mode.DATA.getModeId(), "structure_block");
        net.minecraftforge.client.model.ModelLoader.onRegisterItems(this.itemModelMesher);
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager)
    {
        this.itemModelMesher.rebuildCache();
    }
}

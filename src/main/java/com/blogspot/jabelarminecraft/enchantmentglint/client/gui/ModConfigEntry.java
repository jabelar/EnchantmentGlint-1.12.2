package com.blogspot.jabelarminecraft.enchantmentglint.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.GuiConfigEntries;
import net.minecraftforge.fml.client.config.GuiConfigEntries.IntegerEntry;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModConfigEntry extends IntegerEntry
{

    public ModConfigEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement)
    {
        super(owningScreen, owningEntryList, configElement);
        // TODO Auto-generated constructor stub
    }
    
    @Override
    public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected, float partial)
    {
        super.drawEntry(slotIndex, x, y, listWidth, slotHeight, mouseX, mouseY, isSelected, partial);
        this.textFieldValue.x = this.owningEntryList.controlX + 2;
        this.textFieldValue.y = y + 1;
        this.textFieldValue.width = this.owningEntryList.controlWidth - 4;
        this.textFieldValue.setEnabled(enabled());
        this.textFieldValue.drawTextBox();
    }
    
    /**
     * Draws the textbox
     */
    public void drawTextBox(GuiTextField textFieldValue)
    {
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
        
        if (textFieldValue.getVisible())
        {
            if (textFieldValue.getEnableBackgroundDrawing())
            {
                Gui.drawRect(textFieldValue.x - 1, textFieldValue.y - 1, textFieldValue.x + textFieldValue.width + 1, textFieldValue.y + textFieldValue.height + 1, -6250336);
                Gui.drawRect(textFieldValue.x, textFieldValue.y, textFieldValue.x + textFieldValue.width, textFieldValue.y + textFieldValue.height, -16777216);
            }

            int i = textFieldValue.isEnabled ? textFieldValue.enabledColor : textFieldValue.disabledColor;
            int j = textFieldValue.getCursorPosition() - textFieldValue.lineScrollOffset;
            int k = textFieldValue.getSelectionEnd() - textFieldValue.lineScrollOffset;
            String s = fontRenderer.trimStringToWidth(textFieldValue.getText().substring(textFieldValue.lineScrollOffset), textFieldValue.getWidth());
            boolean flag = j >= 0 && j <= s.length();
            boolean flag1 = textFieldValue.isFocused() && textFieldValue.cursorCounter / 6 % 2 == 0 && flag;
            int l = textFieldValue.getEnableBackgroundDrawing() ? textFieldValue.x + 4 : textFieldValue.x;
            int i1 = textFieldValue.getEnableBackgroundDrawing() ? textFieldValue.y + (textFieldValue.height - 8) / 2 : textFieldValue.y;
            int j1 = l;

            if (k > s.length())
            {
                k = s.length();
            }

            if (!s.isEmpty())
            {
                String s1 = flag ? s.substring(0, j) : s;
                j1 = fontRenderer.drawStringWithShadow(s1, l, i1, i);
            }

            boolean flag2 = textFieldValue.getCursorPosition() < textFieldValue.getText().length() || textFieldValue.getText().length() >= textFieldValue.getMaxStringLength();
            int k1 = j1;

            if (!flag)
            {
                k1 = j > 0 ? l + textFieldValue.width : l;
            }
            else if (flag2)
            {
                k1 = j1 - 1;
                --j1;
            }

            if (!s.isEmpty() && flag && j < s.length())
            {
                j1 = fontRenderer.drawStringWithShadow(s.substring(j), j1, i1, i);
            }

            if (flag1)
            {
                if (flag2)
                {
                    Gui.drawRect(k1, i1 - 1, k1 + 1, i1 + 1 + fontRenderer.FONT_HEIGHT, -3092272);
                }
                else
                {
                    fontRenderer.drawStringWithShadow("_", k1, i1, i);
                }
            }

            if (k != j)
            {
                int l1 = l + fontRenderer.getStringWidth(s.substring(0, k));
                drawSelectionBox(textFieldValue, k1, i1 - 1, l1 - 1, i1 + 1 + fontRenderer.FONT_HEIGHT);
            }
        }
    }
    
    /**
     * Draws the blue selection box.
     */
    private void drawSelectionBox(GuiTextField textFieldValue, int startX, int startY, int endX, int endY)
    {
        if (startX < endX)
        {
            int i = startX;
            startX = endX;
            endX = i;
        }

        if (startY < endY)
        {
            int j = startY;
            startY = endY;
            endY = j;
        }

        if (endX > textFieldValue.x + textFieldValue.width)
        {
            endX = textFieldValue.x + textFieldValue.width;
        }

        if (startX > textFieldValue.x + textFieldValue.width)
        {
            startX = textFieldValue.x + textFieldValue.width;
        }

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.color(0.0F, 0.0F, 255.0F, 255.0F);
        GlStateManager.disableTexture2D();
        GlStateManager.enableColorLogic();
        GlStateManager.colorLogicOp(GlStateManager.LogicOp.OR_REVERSE);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
        bufferbuilder.pos(startX, endY, 0.0D).endVertex();
        bufferbuilder.pos(endX, endY, 0.0D).endVertex();
        bufferbuilder.pos(endX, startY, 0.0D).endVertex();
        bufferbuilder.pos(startX, startY, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.disableColorLogic();
        GlStateManager.enableTexture2D();
    }
}

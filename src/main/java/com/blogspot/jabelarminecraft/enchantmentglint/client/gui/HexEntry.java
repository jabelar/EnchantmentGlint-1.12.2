package com.blogspot.jabelarminecraft.enchantmentglint.client.gui;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.GuiConfigEntries;
import net.minecraftforge.fml.client.config.GuiConfigEntries.IntegerEntry;
import net.minecraftforge.fml.client.config.HoverChecker;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class HexEntry extends IntegerEntry
{

    public HexEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement)
    {
        super(owningScreen, owningEntryList, configElement);
        // TODO Auto-generated constructor stub
    }
    
    @Override
    public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected, float partial)
    {
        boolean isChanged = isChanged();

        if (drawLabel)
        {
            String label = (!isValidValue ? TextFormatting.RED.toString() :
                    (isChanged ? TextFormatting.WHITE.toString() : TextFormatting.GRAY.toString()))
                    + (isChanged ? TextFormatting.ITALIC.toString() : "") + name;
            mc.fontRenderer.drawString(
                    label,
                    owningScreen.entryList.labelX,
                    y + slotHeight / 2 - mc.fontRenderer.FONT_HEIGHT / 2,
                    16777215);
        }

        btnUndoChanges.x = owningEntryList.scrollBarX - 44;
        btnUndoChanges.y = y;
        btnUndoChanges.enabled = enabled() && isChanged;
        btnUndoChanges.drawButton(mc, mouseX, mouseY, partial);

        btnDefault.x = owningEntryList.scrollBarX - 22;
        btnDefault.y = y;
        btnDefault.enabled = enabled() && !isDefault();
        btnDefault.drawButton(mc, mouseX, mouseY, partial);

        if (tooltipHoverChecker == null)
            tooltipHoverChecker = new HoverChecker(y, y + slotHeight, x, owningScreen.entryList.controlX - 8, 800);
        else
            tooltipHoverChecker.updateBounds(y, y + slotHeight, x, owningScreen.entryList.controlX - 8);

        textFieldValue.x = owningEntryList.controlX + 2;
        textFieldValue.y = y + 1;
        textFieldValue.width = owningEntryList.controlWidth - 4;
        textFieldValue.setEnabled(enabled());
        textFieldValue.drawTextBox();
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

            int textColor = 0x6666ff ;
            int cursorMinusOffset = textFieldValue.getCursorPosition(); // - textFieldValue.lineScrollOffset;
            int selectionMinusOffset = textFieldValue.getSelectionEnd(); // - textFieldValue.lineScrollOffset;
            String stringTrimmed = fontRenderer.trimStringToWidth(textFieldValue.getText().substring(0), textFieldValue.getWidth()); //textFieldValue.lineScrollOffset), textFieldValue.getWidth());
            boolean cursorWithinTrimmedString = cursorMinusOffset >= 0 && cursorMinusOffset <= stringTrimmed.length();
            boolean flag1 = textFieldValue.isFocused() && cursorWithinTrimmedString; // textFieldValue.cursorCounter / 6 % 2 == 0 && cursorWithinTrimmedString;
            int posX = textFieldValue.getEnableBackgroundDrawing() ? textFieldValue.x + 4 : textFieldValue.x;
            int posY = textFieldValue.getEnableBackgroundDrawing() ? textFieldValue.y + (textFieldValue.height - 8) / 2 : textFieldValue.y;
            int posXAfterRender = posX;

            if (selectionMinusOffset > stringTrimmed.length())
            {
                selectionMinusOffset = stringTrimmed.length();
            }

            if (!stringTrimmed.isEmpty())
            {
                String s1 = cursorWithinTrimmedString ? stringTrimmed.substring(0, cursorMinusOffset) : stringTrimmed;
                posXAfterRender = fontRenderer.drawStringWithShadow(s1, posX, posY, textColor);
            }

            boolean flag2 = textFieldValue.getCursorPosition() < textFieldValue.getText().length() || textFieldValue.getText().length() >= textFieldValue.getMaxStringLength();
            int k1 = posXAfterRender;

            if (!cursorWithinTrimmedString)
            {
                k1 = cursorMinusOffset > 0 ? posX + textFieldValue.width : posX;
            }
            else if (flag2)
            {
                k1 = posXAfterRender - 1;
                --posXAfterRender;
            }

            if (!stringTrimmed.isEmpty() && cursorWithinTrimmedString && cursorMinusOffset < stringTrimmed.length())
            {
                posXAfterRender = fontRenderer.drawStringWithShadow(stringTrimmed.substring(cursorMinusOffset), posXAfterRender, posY, textColor);
            }

            if (flag1)
            {
                if (flag2)
                {
                    Gui.drawRect(k1, posY - 1, k1 + 1, posY + 1 + fontRenderer.FONT_HEIGHT, -3092272);
                }
                else
                {
                    fontRenderer.drawStringWithShadow("_", k1, posY, textColor);
                }
            }

            if (selectionMinusOffset != cursorMinusOffset)
            {
                int l1 = posX + fontRenderer.getStringWidth(stringTrimmed.substring(0, selectionMinusOffset));
                drawSelectionBox(textFieldValue, k1, posY - 1, l1 - 1, posY + 1 + fontRenderer.FONT_HEIGHT);
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
    

    @Override
    public void keyTyped(char eventChar, int eventKey)
    {
        if (enabled() || eventKey == Keyboard.KEY_LEFT || eventKey == Keyboard.KEY_RIGHT || eventKey == Keyboard.KEY_HOME || eventKey == Keyboard.KEY_END)
        {
            String validChars = "0123456789ABCDEFabcdef";
            String before = textFieldValue.getText();
            if (validChars.contains(String.valueOf(eventChar))
                    || (!before.startsWith("-") && textFieldValue.getCursorPosition() == 0 && eventChar == '-')
                    || eventKey == Keyboard.KEY_BACK || eventKey == Keyboard.KEY_DELETE
                    || eventKey == Keyboard.KEY_LEFT || eventKey == Keyboard.KEY_RIGHT || eventKey == Keyboard.KEY_HOME || eventKey == Keyboard.KEY_END)
                textFieldValue.textboxKeyTyped((enabled() ? eventChar : Keyboard.CHAR_NONE), eventKey);

            if (!textFieldValue.getText().trim().isEmpty() && !textFieldValue.getText().trim().equals("-"))
            {
                try
                {
                    long value = Long.parseLong(textFieldValue.getText().trim(), 16);
                    if (value < Integer.valueOf(configElement.getMinValue().toString()) || value > Integer.valueOf(configElement.getMaxValue().toString()))
                        isValidValue = false;
                    else
                        isValidValue = true;
                }
                catch (Throwable e)
                {
                    isValidValue = false;
                }
            }
            else
                isValidValue = false;
        }
    }

    @Override
    public boolean saveConfigElement()
    {
        if (enabled())
        {
            if (isChanged() && isValidValue)
                try
                {
                    int value = Integer.parseInt(textFieldValue.getText().trim(), 16);
                    configElement.set(value);
                    return configElement.requiresMcRestart();
                }
                catch (Throwable e)
                {
                    configElement.setToDefault();
                }
            else if (isChanged() && !isValidValue)
                try
                {
                    int value = Integer.parseInt(textFieldValue.getText().trim(), 16);
                    if (value < Integer.valueOf(configElement.getMinValue().toString()))
                        configElement.set(configElement.getMinValue());
                    else
                        configElement.set(configElement.getMaxValue());

                }
                catch (Throwable e)
                {
                    configElement.setToDefault();
                }

            return configElement.requiresMcRestart() && beforeValue != Integer.parseInt(configElement.get().toString());
        }
        return false;
    }
}


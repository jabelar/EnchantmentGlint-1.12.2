package com.blogspot.jabelarminecraft.enchantmentglint.client.gui;

import java.util.Collections;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
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
    private int colorSampleX;
    private int colorSampleY;
    
    public HexEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement)
    {
        super(owningScreen, owningEntryList, configElement);
        textFieldValue.setText(padHexString(Integer.toHexString(Integer.parseInt(textFieldValue.getText()))));
        
        initToolTip();
     }
    
    private void initToolTip()
    {
        toolTip.clear();

        String comment = I18n.format(configElement.getLanguageKey() + ".tooltip").replace("\\n", "\n");

        if (!comment.equals(configElement.getLanguageKey() + ".tooltip"))
            Collections.addAll(toolTip, (TextFormatting.GREEN + name + "\n" + TextFormatting.YELLOW + removeTag(comment, "[default:", "]")).split("\n"));
        else if (configElement.getComment() != null && !configElement.getComment().trim().isEmpty())
            Collections.addAll(toolTip, (TextFormatting.GREEN + name + "\n" + TextFormatting.YELLOW + I18n.format(removeTag(configElement.getComment(), "[default:", "]"))).split("\n"));
        else
            Collections.addAll(toolTip, (TextFormatting.GREEN + name + "\n" + TextFormatting.AQUA + I18n.format("config.enter_hex")).split("\n"));

        Collections.addAll(toolTip, (TextFormatting.AQUA + I18n.format("fml.configgui.tooltip.defaultNumeric", "0", "ffffff", Integer.toHexString(Integer.parseInt((String) configElement.getDefault())))).split("\n"));

        if (configElement.requiresMcRestart() || owningScreen.allRequireMcRestart)
            toolTip.add(TextFormatting.RED + "[" + I18n.format("fml.configgui.gameRestartTitle") + "]");
    }
    
    /**
     * Get string surrounding tagged area.
     */
    private String removeTag(String target, String tagStart, String tagEnd)
    {
        int tagStartPosition = target.indexOf(tagStart);
        int tagEndPosition = target.indexOf(tagEnd, tagStartPosition + tagStart.length());

        if (-1 == tagStartPosition || -1 == tagEndPosition)
            return target;

        String taglessResult = target.substring(0, tagStartPosition);
        taglessResult += target.substring(tagEndPosition + 1, target.length());

        return taglessResult;
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
                    0xFFFFFF);
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

        textFieldValue.width = 50;
        textFieldValue.x = owningEntryList.controlX + + owningEntryList.controlWidth - 19 - 20 - textFieldValue.width;
        textFieldValue.y = y + 1;
        textFieldValue.setEnabled(enabled());
        textFieldValue.drawTextBox();
        
        colorSampleX = owningEntryList.controlX + owningEntryList.controlWidth - 30;
        colorSampleY = y;
        GuiScreen.drawRect(colorSampleX - 1, colorSampleY - 1, colorSampleX + 18 + 1, colorSampleY + 18 + 1, 0xFFFFFF);
        GuiScreen.drawRect(colorSampleX, colorSampleY, colorSampleX + 18, colorSampleY + 18, 0xFF000000 | (isValidValue ? Integer.parseInt(textFieldValue.getText(), 16) : 0xE0E0E0));
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

            textFieldValue.setText(padHexString(textFieldValue.getText()));
            
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
    public void setToDefault()
    {
        if (enabled())
        {
            textFieldValue.setText(padHexString(Integer.toHexString(Integer.parseInt(configElement.getDefault().toString()))));
            keyTyped((char) Keyboard.CHAR_NONE, Keyboard.KEY_HOME);
        }
    }

    @Override
    public void undoChanges()
    {
        if (enabled())
            this.textFieldValue.setText(padHexString(Integer.toHexString(beforeValue)));
    }
    
    private String padHexString(String theString)
    {
        theString = "000000" + theString;
        return theString.substring(theString.length()-6, theString.length()).toLowerCase();
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

            return configElement.requiresMcRestart() && beforeValue != Integer.parseInt(configElement.get().toString(), 16);
        }
        return false;
    }
}


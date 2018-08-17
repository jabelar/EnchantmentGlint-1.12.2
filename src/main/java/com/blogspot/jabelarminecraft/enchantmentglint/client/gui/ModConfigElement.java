package com.blogspot.jabelarminecraft.enchantmentglint.client.gui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.config.ConfigGuiType;
import net.minecraftforge.fml.client.config.GuiConfigEntries.IConfigEntry;
import net.minecraftforge.fml.client.config.GuiEditArrayEntries.IArrayEntry;
import net.minecraftforge.fml.client.config.IConfigElement;

public class ModConfigElement implements IConfigElement
{
    private Property prop;
    private Property.Type type;
    private boolean isProperty;
    private ConfigCategory category;
    private boolean categoriesFirst = true;

    public ModConfigElement(ConfigCategory category)
    {
        this.category = category;
        isProperty = false;
    }

    public ModConfigElement(Property prop)
    {
        this.prop = prop;
        this.type = prop.getType();
        this.isProperty = true;
    }

    public ModConfigElement listCategoriesFirst(boolean categoriesFirst)
    {
        this.categoriesFirst = categoriesFirst;
        return this;
    }

    @Override
    public List<IConfigElement> getChildElements()
    {
        if (!isProperty)
        {
            List<IConfigElement> elements = new ArrayList<IConfigElement>();
            Iterator<ConfigCategory> ccI = category.getChildren().iterator();
            Iterator<Property> pI = category.getOrderedValues().iterator();
            @SuppressWarnings("unused")
            int index = 0;

            if (categoriesFirst)
                while (ccI.hasNext())
                {
                    ModConfigElement temp = new ModConfigElement(ccI.next());
                    if (temp.showInGui()) // don't bother adding elements that shouldn't show
                        elements.add(temp);
                }

            while (pI.hasNext())
            {
                ModConfigElement temp = new ModConfigElement(pI.next());
                if (temp.showInGui())
                    elements.add(temp);
            }

            if (!categoriesFirst)
                while (ccI.hasNext())
                {
                    ModConfigElement temp = new ModConfigElement(ccI.next());
                    if (temp.showInGui())
                        elements.add(temp);
                }

            return elements;
        }
        return null;
    }

    @Override
    public String getName()
    {
        return isProperty ? prop.getName() : category.getName();
    }

    @Override
    public boolean isProperty()
    {
        return isProperty;
    }

    @Override
    public Class<? extends IConfigEntry> getConfigEntryClass()
    {
        return isProperty ? HexEntry.class : category.getConfigEntryClass();
    }

    @Override
    public Class<? extends IArrayEntry> getArrayEntryClass()
    {
        return isProperty ? prop.getArrayEntryClass() : null;
    }

    @Override
    public String getQualifiedName()
    {
        return isProperty ? prop.getName() : category.getQualifiedName();
    }

    @Override
    public ConfigGuiType getType()
    {
        return isProperty ? getType(this.prop) : ConfigGuiType.CONFIG_CATEGORY;
    }

    public static ConfigGuiType getType(Property prop)
    {
        return prop.getType() == Property.Type.BOOLEAN ? ConfigGuiType.BOOLEAN : prop.getType() == Property.Type.DOUBLE ? ConfigGuiType.DOUBLE :
            prop.getType() == Property.Type.INTEGER ? ConfigGuiType.INTEGER : prop.getType() == Property.Type.COLOR ? ConfigGuiType.COLOR :
            prop.getType() == Property.Type.MOD_ID ? ConfigGuiType.MOD_ID : ConfigGuiType.STRING;
    }

    @Override
    public boolean isList()
    {
        return isProperty && prop.isList();
    }

    @Override
    public boolean isListLengthFixed()
    {
        return isProperty && prop.isListLengthFixed();
    }

    @Override
    public int getMaxListLength()
    {
        return isProperty ? prop.getMaxListLength() : -1;
    }

    @Override
    public String getComment()
    {
        return isProperty ? prop.getComment() : category.getComment();
    }

    @Override
    public boolean isDefault()
    {
        return !isProperty || prop.isDefault();
    }

    @Override
    public void setToDefault()
    {
        if (isProperty)
            prop.setToDefault();
    }

    @Override
    public boolean requiresWorldRestart()
    {
        return isProperty ? prop.requiresWorldRestart() : category.requiresWorldRestart();
    }

    @Override
    public boolean showInGui()
    {
        return isProperty ? prop.showInGui() : category.showInGui();
    }

    @Override
    public boolean requiresMcRestart()
    {
        return isProperty ? prop.requiresMcRestart() : category.requiresMcRestart();
    }

    @Override
    public String[] getValidValues()
    {
        return isProperty ? prop.getValidValues() : null;
    }

    @Override
    public String getLanguageKey()
    {
        return isProperty ? prop.getLanguageKey() : category.getLanguagekey();
    }

    @Override
    public Object getDefault()
    {
        return isProperty ? prop.getDefault() : null;
    }

    @Override
    public Object[] getDefaults()
    {
        if (isProperty)
        {
            String[] aVal = prop.getDefaults();
            if (type == Property.Type.BOOLEAN)
            {
                Boolean[] ba = new Boolean[aVal.length];
                for(int i = 0; i < aVal.length; i++)
                    ba[i] = Boolean.valueOf(aVal[i]);
                return ba;
            }
            else if (type == Property.Type.DOUBLE)
            {
                Double[] da = new Double[aVal.length];
                for(int i = 0; i < aVal.length; i++)
                    da[i] = Double.valueOf(aVal[i].toString());
                return da;
            }
            else if (type == Property.Type.INTEGER)
            {
                Integer[] ia = new Integer[aVal.length];
                for(int i = 0; i < aVal.length; i++)
                    ia[i] = Integer.valueOf(aVal[i].toString());
                return ia;
            }
            else
                return aVal;
        }
        return null;
    }

    @Override
    public Pattern getValidationPattern()
    {
        return isProperty ? prop.getValidationPattern() : null;
    }

    @Override
    public Object get()
    {
        return isProperty ? prop.getString() : null;
    }

    @Override
    public Object[] getList()
    {
        if (isProperty)
        {
            String[] aVal = prop.getStringList();
            if (type == Property.Type.BOOLEAN)
            {
                Boolean[] ba = new Boolean[aVal.length];
                for(int i = 0; i < aVal.length; i++)
                    ba[i] = Boolean.valueOf(aVal[i]);
                return ba;
            }
            else if (type == Property.Type.DOUBLE)
            {
                Double[] da = new Double[aVal.length];
                for(int i = 0; i < aVal.length; i++)
                    da[i] = Double.valueOf(aVal[i].toString());
                return da;
            }
            else if (type == Property.Type.INTEGER)
            {
                Integer[] ia = new Integer[aVal.length];
                for(int i = 0; i < aVal.length; i++)
                    ia[i] = Integer.valueOf(aVal[i].toString());
                return ia;
            }
            else
                return aVal;
        }
        return null;
    }

    @Override
    public void set(Object value)
    {
        if (isProperty)
        {
            if (type == Property.Type.BOOLEAN)
                prop.set(Boolean.parseBoolean(value.toString()));
            else if (type == Property.Type.DOUBLE)
                prop.set(Double.parseDouble(value.toString()));
            else if (type == Property.Type.INTEGER)
                prop.set(Integer.parseInt(value.toString()));
            else
                prop.set(value.toString());
        }
    }

    @Override
    public void set(Object[] aVal)
    {
        if (isProperty)
        {
            if (type == Property.Type.BOOLEAN)
            {
                boolean[] ba = new boolean[aVal.length];
                for(int i = 0; i < aVal.length; i++)
                    ba[i] = Boolean.valueOf(aVal[i].toString());
                prop.set(ba);
            }
            else if (type == Property.Type.DOUBLE)
            {
                double[] da = new double[aVal.length];
                for(int i = 0; i < aVal.length; i++)
                    da[i] = Double.valueOf(aVal[i].toString());
                prop.set(da);
            }
            else if (type == Property.Type.INTEGER)
            {
                int[] ia = new int[aVal.length];
                for(int i = 0; i < aVal.length; i++)
                    ia[i] = Integer.valueOf(aVal[i].toString());
                prop.set(ia);
            }
            else
            {
                String[] is = new String[aVal.length];
                for(int i = 0; i < aVal.length; i++)
                    is[i] = aVal[i].toString();
                prop.set(is);
            }
        }
    }

    @Override
    public Object getMinValue()
    {
        return isProperty ? prop.getMinValue() : null;
    }

    @Override
    public Object getMaxValue()
    {
        return isProperty ? prop.getMaxValue() : null;
    }
    
//    /**
//     * Provides a ModConfigElement derived from the annotation-based config system
//     * @param configClass the class which contains the configuration
//     * @return A ModConfigElement based on the described category.
//     */
//    public static IConfigElement from(Class<?> configClass)
//    {
//        Config annotation = configClass.getAnnotation(Config.class);
//        if (annotation == null)
//            throw new RuntimeException(String.format("The class '%s' has no @Config annotation!", configClass.getName()));
//        
//        Configuration config = ConfigManager.getConfiguration(annotation.modid(), annotation.name());
//        if (config == null)
//        {
//            String error = String.format("The configuration '%s' of mod '%s' isn't loaded with the ConfigManager!", annotation.name(), annotation.modid());
//            throw new RuntimeException(error);
//        }
//        
//        String name = Strings.isNullOrEmpty(annotation.name()) ? annotation.modid() : annotation.name();
//        String langKey = name;
//        Config.LangKey langKeyAnnotation = configClass.getAnnotation(Config.LangKey.class);
//        if (langKeyAnnotation != null)
//        {
//            langKey = langKeyAnnotation.value();
//        }
//         
//        if (annotation.category().isEmpty())
//        {
//            List<IConfigElement> elements = Lists.newArrayList();
//            Set<String> catNames = config.getCategoryNames();
//            for (String catName : catNames)
//            {
//                if (catName.isEmpty())
//                    continue;
//                ConfigCategory category = config.getCategory(catName);
//                if (category.isChild())
//                    continue;
//                DummyCategoryElement element = new DummyCategoryElement(category.getName(), category.getLanguagekey(), new ModConfigElement(category).getChildElements());
//                element.setRequiresMcRestart(category.requiresMcRestart());
//                element.setRequiresWorldRestart(category.requiresWorldRestart());
//                elements.add(element);
//            }
//                
//            return new DummyCategoryElement(name, langKey, elements);
//        }
//        else
//        {
//            ConfigCategory category = config.getCategory(annotation.category());
//            DummyCategoryElement element = new DummyCategoryElement(name, langKey, new ModConfigElement(category).getChildElements());   
//            element.setRequiresMcRestart(category.requiresMcRestart());
//            element.setRequiresWorldRestart(category.requiresWorldRestart());
//            return element;
//        } 
//    }
}
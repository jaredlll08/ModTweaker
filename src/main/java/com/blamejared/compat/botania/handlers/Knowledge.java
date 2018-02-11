package com.blamejared.compat.botania.handlers;

import com.blamejared.compat.botania.LocalizationHelper;
import crafttweaker.*;
import crafttweaker.annotations.*;
import net.minecraft.util.text.TextFormatting;
import stanhebben.zenscript.annotations.*;
import vazkii.botania.api.BotaniaAPI;

@ZenClass("mods.botania.Knowledge")
@ModOnly("botania")
@ZenRegister
public class Knowledge {
    
    private static final String typeName = "Botania Knowledge Variable:";
    
    @ZenMethod
    public static void registerKnowledgeType(String unlocalized, String localized, String color, boolean autoUnlock) {
        if(unlocalized == null || unlocalized.isEmpty()) {
            CraftTweakerAPI.logError("Found null String in " + typeName + " name");
        } else if(color == null || color.isEmpty()) {
            CraftTweakerAPI.logError("Found null String in " + typeName + " color");
        } else {
            LocalizationHelper.setLocale("botania.knowledge." + unlocalized, localized);
            CraftTweakerAPI.apply(new Add(unlocalized, color, autoUnlock));
        }
    }
    
    private static class Add implements IAction {
        
        String unlocalized;
        String color;
        boolean autoUnlock;
        
        public Add(String name, String color, boolean autoUnlock) {
            this.unlocalized = name;
            this.color = color;
            this.autoUnlock = autoUnlock;
        }
        
        @Override
        public void apply() {
            BotaniaAPI.registerKnowledgeType(unlocalized, TextFormatting.getValueByName(color.toUpperCase()), autoUnlock);
        }
        
        @Override
        public String describe() {
            String Output;
            if(autoUnlock) {
                Output = "Enabled";
            } else {
                Output = "Disabled";
            }
            return "Adding Knowledge Type: " + unlocalized + " With Auto-Unlock: " + Output;
        }
    }
}
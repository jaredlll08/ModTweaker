package com.blamejared.compat.botania.lexicon;

import com.blamejared.compat.botania.BotaniaHelper;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.LexiconEntry;

public class RemoveEntry implements IAction {
    
    private LexiconEntry lexEntry;
    private final String entry;
    
    public RemoveEntry(String entry) {
        this.entry = entry;
    }
    
    @Override
    public void apply() {
        lexEntry = BotaniaHelper.findEntry(entry);
        if(lexEntry == null) {
            CraftTweakerAPI.getLogger().logError("Cannot find lexicon entry " + entry);
            return;
        }
        lexEntry.category.entries.remove(lexEntry);
        BotaniaAPI.getAllEntries().remove(lexEntry);
        CraftTweakerAPI.getLogger().logInfo("Removing Lexicon entry: " + lexEntry.getUnlocalizedName());
    }
    
    @Override
    public String describe() {
        return "Attempting to remove Lexicon entry " + entry;
    }
    
}

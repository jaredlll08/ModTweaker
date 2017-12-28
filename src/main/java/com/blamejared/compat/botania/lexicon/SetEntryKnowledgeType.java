package com.blamejared.compat.botania.lexicon;

import com.blamejared.compat.botania.BotaniaHelper;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import vazkii.botania.api.lexicon.KnowledgeType;
import vazkii.botania.api.lexicon.LexiconEntry;

public class SetEntryKnowledgeType implements IAction {
    
    LexiconEntry lexEntry;
    KnowledgeType newType;
    KnowledgeType oldType;
    final String entry;
    final String knowledgeType;
    
    public SetEntryKnowledgeType(String entry, String knowledgeType) {
        this.entry = entry;
        this.knowledgeType = knowledgeType;
    }
    
    @Override
    public void apply() {
        lexEntry = BotaniaHelper.findEntry(entry);
        newType = BotaniaHelper.findKnowledgeType(knowledgeType);
        if(lexEntry == null) {
            CraftTweakerAPI.getLogger().logError("Cannot find lexicon entry " + entry);
            return;
        }
        if(newType == null) {
            CraftTweakerAPI.getLogger().logError("Cannot find knowledge type " + knowledgeType);
            return;
        }
        oldType = lexEntry.getKnowledgeType();
        lexEntry.setKnowledgeType(newType);
        CraftTweakerAPI.getLogger().logInfo("Setting Knowledge type for: " + lexEntry.getUnlocalizedName());
    }
    
    @Override
    public String describe() {
        return "Attempting to set the knowledge type for Lexicon entry " + entry + " to " + knowledgeType;
    }
}

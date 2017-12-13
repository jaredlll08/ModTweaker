package com.blamejared.compat.botania;

import java.util.List;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.KnowledgeType;
import vazkii.botania.api.lexicon.LexiconCategory;
import vazkii.botania.api.lexicon.LexiconEntry;

public class BotaniaHelper {
    
    public static LexiconCategory findCatagory(String name) {
        List<LexiconCategory> catagories = BotaniaAPI.getAllCategories();
        for(LexiconCategory catagory : catagories) {
            if(catagory.getUnlocalizedName().equalsIgnoreCase(name))
                return catagory;
        }
        return null;
    }
    
    public static LexiconEntry findEntry(String name) {
        List<LexiconEntry> entries = BotaniaAPI.getAllEntries();
        for(LexiconEntry entry : entries) {
            if(entry.getUnlocalizedName().equalsIgnoreCase(name))
                return entry;
        }
        return null;
    }
    
    public static KnowledgeType findKnowledgeType(String name) {
        if(BotaniaAPI.knowledgeTypes.containsKey(name))
            return BotaniaAPI.knowledgeTypes.get(name);
        return null;
    }
}

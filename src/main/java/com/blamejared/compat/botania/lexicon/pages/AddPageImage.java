package com.blamejared.compat.botania.lexicon.pages;

import com.blamejared.compat.botania.BotaniaHelper;
import crafttweaker.*;
import net.minecraft.entity.EntityList;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.api.lexicon.*;
import vazkii.botania.common.lexicon.page.*;

public class AddPageImage implements IAction {
    
    private String name;
    private String entry;
    private int page_number;
    private String resource;
    
    public AddPageImage(String name, String entry, int page_number, String resource) {
        this.name = name;
        this.entry = entry;
        this.page_number = page_number;
        this.resource = resource;
    }
    
    @Override
    public void apply() {
        LexiconEntry lexiconEntry = BotaniaHelper.findEntry(entry);
        if(lexiconEntry == null) {
            CraftTweakerAPI.getLogger().logError("Cannot find lexicon entry " + entry);
            return;
        }
        if(page_number > lexiconEntry.pages.size()) {
            CraftTweakerAPI.getLogger().logError("Page Number " + page_number + " out of bounds for " + entry);
            return;
        }
        LexiconPage page = new PageImage(name, resource);
        lexiconEntry.pages.add(page_number, page);
    }
    
    @Override
    public String describe() {
        return "Adding Lexicon Page: " + name;
    }
    
}

package com.blamejared.compat.botania.lexicon.pages;

import com.blamejared.compat.botania.BotaniaHelper;
import crafttweaker.*;
import crafttweaker.api.item.*;
import vazkii.botania.api.lexicon.*;
import vazkii.botania.api.recipe.RecipeManaInfusion;
import vazkii.botania.common.lexicon.page.*;

import java.util.*;

import static com.blamejared.mtlib.helpers.InputHelper.*;

public class AddPageText implements IAction {
    
    private String name;
    private String entry;
    private int page_number;
    
    public AddPageText(String name, String entry, int page_number) {
        this.name = name;
        this.entry = entry;
        this.page_number = page_number;
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
        LexiconPage page = new PageText(name);
        lexiconEntry.pages.add(page_number, page);
    }
    
    @Override
    public String describe() {
        return "Adding Lexicon Page: " + name;
    }
    
}

package com.blamejared.compat.botania.lexicon.pages;

import com.blamejared.compat.botania.BotaniaHelper;
import crafttweaker.*;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.api.lexicon.*;
import vazkii.botania.common.lexicon.page.*;

import java.util.*;

public class AddPageCrafting implements IAction {
    
    private String name;
    private String entry;
    private int page_number;
    private String[] recipeNames;
    
    
    public AddPageCrafting(String name, String entry, int page_number, String[] recipeNames) {
        this.name = name;
        this.entry = entry;
        this.page_number = page_number;
        this.recipeNames = recipeNames;
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
        List<ResourceLocation> recipes = new ArrayList<>();
        for(String s : recipeNames) {
            recipes.add(new ResourceLocation(s));
        }
        LexiconPage page = new PageCraftingRecipe(name, recipes);
        lexiconEntry.pages.add(page_number, page);
    }
    
    @Override
    public String describe() {
        return "Adding Lexicon Page: " + name;
    }
    
}

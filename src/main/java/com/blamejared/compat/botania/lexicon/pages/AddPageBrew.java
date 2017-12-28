package com.blamejared.compat.botania.lexicon.pages;

import com.blamejared.compat.botania.BotaniaHelper;
import crafttweaker.*;
import crafttweaker.api.item.IIngredient;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.lexicon.LexiconPage;
import vazkii.botania.api.recipe.RecipeBrew;
import vazkii.botania.common.lexicon.page.PageBrew;

import static com.blamejared.mtlib.helpers.InputHelper.toObjects;

public class AddPageBrew implements IAction {
    
    private String name;
    private String entry;
    private int page_number;
    private String brew;
    private IIngredient[] recipe;
    private String bottomText;
    
    public AddPageBrew(String name, String entry, int page_number, String brew, IIngredient[] recipe, String bottomText) {
        this.name = name;
        this.entry = entry;
        this.page_number = page_number;
        this.brew = brew;
        this.recipe = recipe;
        this.bottomText = bottomText;
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
        if(BotaniaAPI.getBrewFromKey(brew) == null) {
            CraftTweakerAPI.getLogger().logError("Cannot find brew " + brew);
            return;
        }
        RecipeBrew page_recipe = new RecipeBrew(BotaniaAPI.getBrewFromKey(brew), toObjects(recipe));
        LexiconPage page = new PageBrew(page_recipe, name, bottomText);
        lexiconEntry.pages.add(page_number, page);
    }
    
    @Override
    public String describe() {
        return "Adding Lexicon Page: " + name;
    }
    
}

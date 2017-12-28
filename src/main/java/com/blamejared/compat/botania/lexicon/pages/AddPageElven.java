package com.blamejared.compat.botania.lexicon.pages;

import com.blamejared.compat.botania.BotaniaHelper;
import crafttweaker.*;
import crafttweaker.api.item.*;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.*;
import vazkii.botania.api.recipe.*;
import vazkii.botania.common.lexicon.page.*;

import java.util.*;

import static com.blamejared.mtlib.helpers.InputHelper.toObjects;
import static com.blamejared.mtlib.helpers.InputHelper.toStacks;

public class AddPageElven implements IAction {
    
    private String name;
    private String entry;
    private int page_number;
    private IItemStack[] outputs;
    private IIngredient[][] inputs;
    
    public AddPageElven(String name, String entry, int page_number, IItemStack[] outputs, IIngredient[][] inputs) {
        this.name = name;
        this.entry = entry;
        this.page_number = page_number;
        this.outputs = outputs;
        this.inputs = inputs;
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
        if(outputs.length != inputs.length) {
            CraftTweakerAPI.getLogger().logError("Length of input and output must match");
            return;
        }
        List<RecipeElvenTrade> recipes = new ArrayList<>();
        for(int i = 0; i < outputs.length; i++) {
            //TODO test
            recipes.add(new RecipeElvenTrade(toStacks(outputs), toObjects(inputs[i])));
        }
    
        LexiconPage page = new PageElvenRecipe(name, recipes);
        lexiconEntry.pages.add(page_number, page);
    }
    
    @Override
    public String describe() {
        return "Adding Lexicon Page: " + name;
    }
    
}

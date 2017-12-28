package com.blamejared.compat.botania.lexicon.pages;

import com.blamejared.compat.botania.BotaniaHelper;
import crafttweaker.*;
import crafttweaker.api.item.*;
import vazkii.botania.api.lexicon.*;
import vazkii.botania.api.recipe.RecipeManaInfusion;
import vazkii.botania.common.lexicon.page.PageManaInfusionRecipe;

import java.util.*;

import static com.blamejared.mtlib.helpers.InputHelper.*;

public class AddPageConjuration implements IAction {
    
    private String name;
    private String entry;
    private int page_number;
    private IItemStack[] outputs;
    private IIngredient[] inputs;
    private int[] mana;
    
    
    public AddPageConjuration(String name, String entry, int page_number, IItemStack[] outputs, IIngredient[] inputs, int[] mana) {
        this.name = name;
        this.entry = entry;
        this.page_number = page_number;
        this.outputs = outputs;
        this.inputs = inputs;
        this.mana = mana;
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
        if(outputs.length != inputs.length || outputs.length != mana.length) {
            CraftTweakerAPI.getLogger().logError("Length of input and output must match");
            return;
        }
        List<RecipeManaInfusion> recipes = new ArrayList<>();
        for(int i = 0; i < outputs.length; i++) {
            RecipeManaInfusion current_recipe = new RecipeManaInfusion(toStack(outputs[i]), toObject(inputs[i]), mana[i]);
            current_recipe.setCatalyst(RecipeManaInfusion.conjurationState);
            recipes.add(current_recipe);
        }
        LexiconPage page = new PageManaInfusionRecipe(name, recipes);
        lexiconEntry.pages.add(page_number, page);
    }
    
    @Override
    public String describe() {
        return "Adding Lexicon Page: " + name;
    }
    
}

package com.blamejared.compat.botania.lexicon;

import com.blamejared.compat.botania.BotaniaHelper;
import com.blamejared.mtlib.helpers.*;
import crafttweaker.*;
import crafttweaker.api.item.IItemStack;
import vazkii.botania.api.lexicon.*;

import static com.blamejared.mtlib.helpers.InputHelper.toStack;

public class AddRecipeMapping implements IAction {
    
    private IItemStack stack;
    private String entry;
    private int page;
    
    public AddRecipeMapping(IItemStack stack, String entry, int page) {
        this.stack = stack;
        this.entry = entry;
        this.page = page;
    }
    
    @Override
    public void apply() {
        
        LexiconEntry lexiconentry = BotaniaHelper.findEntry(entry);
        if(LexiconRecipeMappings.getDataForStack(toStack(stack)) != null) {
            CraftTweakerAPI.getLogger().logError("There is already a recipe mapping for " + stack);
            return;
        }
        if(lexiconentry == null) {
            CraftTweakerAPI.getLogger().logError("Cannot find lexicon entry " + entry);
            return;
        }
        if(lexiconentry.pages.size() < page) {
            CraftTweakerAPI.getLogger().logError("Not enough pages in " + entry);
            return;
        }
        
        LexiconRecipeMappings.map(InputHelper.toStack(stack), lexiconentry, page);
    }
    
    @Override
    public String describe() {
        return "Adding Lexicon Recipe Lookup: " + LogHelper.getStackDescription(stack);
    }
    
}

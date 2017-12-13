package com.blamejared.compat.botania.lexicon;

import crafttweaker.IAction;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.LexiconCategory;

public class AddCategory implements IAction {
    
    LexiconCategory category;
    
    public AddCategory(LexiconCategory category) {
        this.category = category;
    }
    
    @Override
    public void apply() {
        BotaniaAPI.addCategory(category);
    }
    
    @Override
    public String describe() {
        return "Adding Lexicon Category: " + category.getUnlocalizedName();
    }
    
}

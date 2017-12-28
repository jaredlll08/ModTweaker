package com.blamejared.compat.botania.lexicon;

import com.blamejared.compat.botania.BotaniaHelper;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.mc1120.brackets.BracketHandlerItem;
import vazkii.botania.api.lexicon.LexiconCategory;

public class SetCategoryPriority implements IAction {
    
    private LexiconCategory category;
    private int oldPriority;
    private int newPriority;
    private final String name;
    
    public SetCategoryPriority(String name, int priority) {
        this.name = name;
        this.newPriority = priority;
    }
    
    @Override
    public void apply() {
        category = BotaniaHelper.findCatagory(name);
        if(category == null) {
            CraftTweakerAPI.getLogger().logError("Cannot find lexicon category " + name);
            return;
        }
        oldPriority = category.getSortingPriority();
        category.setPriority(newPriority);
        CraftTweakerAPI.getLogger().logInfo("Setting Lexicon Category priority: " + category.getUnlocalizedName());
    }
    
    @Override
    public String describe() {
        return "Attempting to set the priority for Lexicon Category " + name + " to " + newPriority;
    }
    
}

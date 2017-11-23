package com.blamejared.compat.botania.lexicon;

import crafttweaker.IAction;
import vazkii.botania.api.lexicon.LexiconCategory;

public class SetCategoryPriority implements IAction {
	
	LexiconCategory category;
	int oldPriority;
	int newPriority;

    public SetCategoryPriority(LexiconCategory category, int priority) {
        this.category=category;
        this.newPriority=priority;
    }

    @Override
	public void apply() {
    	oldPriority=category.getSortingPriority();
    	category.setPriority(newPriority);
	}
	
	@Override
	public String describe() {
        return "Setting Lexicon Category priority: " + category.getUnlocalizedName();
	}
	
}

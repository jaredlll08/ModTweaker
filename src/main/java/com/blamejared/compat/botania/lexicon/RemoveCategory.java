package com.blamejared.compat.botania.lexicon;

import crafttweaker.IAction;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.LexiconCategory;

public class RemoveCategory implements IAction {
	
	LexiconCategory category;

    public RemoveCategory(LexiconCategory category) {
        this.category=category;
    }

    @Override
	public void apply() {
		BotaniaAPI.getAllCategories().remove(category);
	}


	@Override
	public String describe() {
        return "Removing Lexicon Category: " + category.getUnlocalizedName();
	}

}

package com.blamejared.compat.botania.lexicon;

import com.blamejared.compat.botania.BotaniaHelper;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.LexiconCategory;

public class RemoveCategory implements IAction {

	final String name;
	LexiconCategory category;
	
    public RemoveCategory(String name) {
        this.name=name;
    }

    @Override
	public void apply() {
		category = BotaniaHelper.findCatagory(name);
		if (category == null) {
			CraftTweakerAPI.getLogger().logError("Cannot find lexicon category " + name);
			return;
		}
		BotaniaAPI.getAllCategories().remove(category);
		CraftTweakerAPI.getLogger().logInfo("Removing Lexicon Category: " + category.getUnlocalizedName());
	}


	@Override
	public String describe() {
        return "";
	}

}

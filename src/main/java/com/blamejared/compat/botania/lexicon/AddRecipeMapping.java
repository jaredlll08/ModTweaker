package com.blamejared.compat.botania.lexicon;

import crafttweaker.IAction;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.lexicon.LexiconRecipeMappings;

public class AddRecipeMapping implements IAction {
	
	ItemStack stack;
	LexiconEntry entry;
	int page;

    public AddRecipeMapping(ItemStack stack, LexiconEntry entry, int page) {
        this.stack =stack;
        this.entry=entry;
        this.page=page;
    }

    @Override
	public void apply() {
    	LexiconRecipeMappings.map(stack, entry, page);
	}

	@Override
	public String describe() {
        return "Adding Lexicon Recipe Lookup: " + stack.getUnlocalizedName();
	}

}

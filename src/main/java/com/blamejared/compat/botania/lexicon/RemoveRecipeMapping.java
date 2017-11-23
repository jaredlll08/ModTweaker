package com.blamejared.compat.botania.lexicon;

import crafttweaker.IAction;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.lexicon.LexiconRecipeMappings;
import vazkii.botania.api.lexicon.LexiconRecipeMappings.EntryData;

public class RemoveRecipeMapping implements IAction {
	
	ItemStack stack;
	LexiconEntry entry;
	int page;

    public RemoveRecipeMapping(ItemStack stack) {
        this.stack =stack;
    }

    @Override
	public void apply() {
    	EntryData data=LexiconRecipeMappings.getDataForStack(stack);
        this.entry=data.entry;
        this.page=data.page;
    	LexiconRecipeMappings.remove(stack);
	}
	
	@Override
	public String describe() {
        return "Removing Lexicon Recipe Lookup: " + stack.getUnlocalizedName();
	}

}

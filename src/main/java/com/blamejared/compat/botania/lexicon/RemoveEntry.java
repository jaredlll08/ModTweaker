package com.blamejared.compat.botania.lexicon;

import crafttweaker.IAction;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.LexiconEntry;

public class RemoveEntry implements IAction {
	
	LexiconEntry Entry;

    public RemoveEntry(LexiconEntry Entry) {
        this.Entry=Entry;
    }

    @Override
	public void apply() {
		Entry.category.entries.remove(Entry);
		BotaniaAPI.getAllEntries().remove(Entry);
	}
	
	@Override
	public String describe() {
        return "Removing Lexicon Entry: " + Entry.getUnlocalizedName();
	}
	
}

package com.blamejared.compat.botania.lexicon;

import crafttweaker.IAction;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.lexicon.LexiconPage;

public class RemovePage implements IAction {
	
	int page_number;
	LexiconEntry Entry;
	LexiconPage page;

    public RemovePage(LexiconEntry Entry, int page_number) {
        this.Entry=Entry;
        this.page_number=page_number;
    }

    @Override
	public void apply() {
        this.page=Entry.pages.get(page_number);
    	Entry.pages.remove(page);
	}
	
	@Override
	public String describe() {
        return "Removing Lexicon Page: " + Entry.pages.get(page_number).getUnlocalizedName();
	}

}

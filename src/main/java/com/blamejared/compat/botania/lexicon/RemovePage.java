package com.blamejared.compat.botania.lexicon;

import com.blamejared.compat.botania.BotaniaHelper;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.lexicon.LexiconPage;

public class RemovePage implements IAction {

	int page_number;
	LexiconEntry lexEntry;
	LexiconPage page;
	final String entry;

	public RemovePage(String entry, int page_number) {
		this.entry = entry;
		this.page_number = page_number;
	}

	@Override
	public void apply() {
		this.lexEntry = BotaniaHelper.findEntry(entry);
		if (lexEntry == null) {
			CraftTweakerAPI.getLogger().logError("Cannot find lexicon entry " + entry);
			return;
		}
		if (page_number >= lexEntry.pages.size()) {
			CraftTweakerAPI.getLogger().logError("Page Number " + page_number + " out of bounds for " + entry);
			return;
		}

		this.page = lexEntry.pages.get(page_number);
		lexEntry.pages.remove(page);
		CraftTweakerAPI.getLogger().logInfo("Removing Lexicon Page: " + lexEntry.pages.get(page_number).getUnlocalizedName());
	}

	@Override
	public String describe() {
		return "";
	}

}

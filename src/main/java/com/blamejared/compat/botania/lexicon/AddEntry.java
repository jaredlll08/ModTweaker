package com.blamejared.compat.botania.lexicon;

import crafttweaker.IAction;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.LexiconEntry;

public class AddEntry implements IAction {
    
    LexiconEntry Entry;
    
    public AddEntry(LexiconEntry Entry) {
        this.Entry = Entry;
    }
    
    @Override
    public void apply() {
        BotaniaAPI.addEntry(Entry, Entry.category);
    }
    
    @Override
    public String describe() {
        return "Adding Lexicon Entry: " + Entry.getUnlocalizedName();
    }
    
}

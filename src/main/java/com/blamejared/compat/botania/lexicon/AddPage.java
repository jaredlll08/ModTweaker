package com.blamejared.compat.botania.lexicon;

import crafttweaker.IAction;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.lexicon.LexiconPage;

public class AddPage implements IAction {
    
    String Name;
    int page_number;
    LexiconEntry Entry;
    LexiconPage page;
    
    public AddPage(String Name, LexiconEntry Entry, LexiconPage page, int page_number) {
        this.Name = Name;
        this.Entry = Entry;
        this.page = page;
        this.page_number = page_number;
    }
    
    @Override
    public void apply() {
        Entry.pages.add(page_number, page);
    }
    
    @Override
    public String describe() {
        return "Adding Lexicon Page: " + Name;
    }
    
}

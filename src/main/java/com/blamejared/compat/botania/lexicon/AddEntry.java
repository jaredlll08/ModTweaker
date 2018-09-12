package com.blamejared.compat.botania.lexicon;

import com.blamejared.compat.botania.BotaniaHelper;
import com.blamejared.mtlib.helpers.LogHelper;
import crafttweaker.*;
import crafttweaker.api.item.IItemStack;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.*;

import static com.blamejared.mtlib.helpers.InputHelper.toStack;

public class AddEntry implements IAction {
    
    private String entry;
    private String catagory;
    private IItemStack stack;
    
    public AddEntry(String entry, String catagory, IItemStack stack) {
        this.entry = entry;
        this.catagory = catagory;
        this.stack = stack;
    }
    
    @Override
    public void apply() {
        LexiconCategory lexiconCategory = BotaniaHelper.findCatagory(catagory);
        if(lexiconCategory == null) {
            CraftTweakerAPI.getLogger().logError("Cannot find lexicon category " + catagory);
            return;
        }
        LexiconEntry lexiconEntry = new UsefulEntry(entry, lexiconCategory);
        lexiconEntry.setIcon(toStack(stack));
        BotaniaAPI.addEntry(lexiconEntry, lexiconEntry.category);
    }
    
    @Override
    public String describe() {
        return "Adding Lexicon entry: " + LogHelper.getStackDescription(entry);
    }
    
}

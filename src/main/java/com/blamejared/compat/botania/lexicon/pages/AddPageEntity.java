package com.blamejared.compat.botania.lexicon.pages;

import com.blamejared.compat.botania.BotaniaHelper;
import crafttweaker.*;
import crafttweaker.api.item.*;
import net.minecraft.entity.EntityList;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.api.lexicon.*;
import vazkii.botania.api.recipe.RecipeElvenTrade;
import vazkii.botania.common.lexicon.page.*;

import java.util.*;

import static com.blamejared.mtlib.helpers.InputHelper.*;

public class AddPageEntity implements IAction {
    
    private String name;
    private String entry;
    private int page_number;
    private String entity;
    private int size;
    
    public AddPageEntity(String name, String entry, int page_number, String entity, int size) {
        this.name = name;
        this.entry = entry;
        this.page_number = page_number;
        this.entity = entity;
        this.size = size;
    }
    
    @Override
    public void apply() {
        LexiconEntry lexiconEntry = BotaniaHelper.findEntry(entry);
        if(lexiconEntry == null) {
            CraftTweakerAPI.getLogger().logError("Cannot find lexicon entry " + entry);
            return;
        }
        if(page_number > lexiconEntry.pages.size()) {
            CraftTweakerAPI.getLogger().logError("Page Number " + page_number + " out of bounds for " + entry);
            return;
        }
        if(!EntityList.isRegistered(new ResourceLocation(entity))) {
            CraftTweakerAPI.getLogger().logError("No such entity " + entity);
            return;
        }
        LexiconPage page = new PageEntity(entity, entity, size);
        lexiconEntry.pages.add(page_number, page);
    }
    
    @Override
    public String describe() {
        return "Adding Lexicon Page: " + name;
    }
    
}

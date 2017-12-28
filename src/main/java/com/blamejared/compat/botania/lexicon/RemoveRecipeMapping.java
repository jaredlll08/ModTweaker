package com.blamejared.compat.botania.lexicon;

import static com.blamejared.mtlib.helpers.InputHelper.toStack;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.lexicon.LexiconRecipeMappings;
import vazkii.botania.api.lexicon.LexiconRecipeMappings.EntryData;

public class RemoveRecipeMapping implements IAction {
    
    private ItemStack stack;
    private IItemStack iStack;
    private LexiconEntry entry;
    private int page;
    
    public RemoveRecipeMapping(IItemStack stack) {
        this.iStack = stack;
    }
    
    @Override
    public void apply() {
        if(LexiconRecipeMappings.getDataForStack(stack = toStack(iStack)) == null) {
            CraftTweakerAPI.getLogger().logError("There isn't a recipe mapping for " + iStack);
            return;
        }
        EntryData data = LexiconRecipeMappings.getDataForStack(stack);
        this.entry = data.entry;
        this.page = data.page;
        LexiconRecipeMappings.remove(stack);
        CraftTweakerAPI.getLogger().logInfo("Removing Lexicon Recipe Lookup: " + stack.getUnlocalizedName());
    }
    
    @Override
    public String describe() {
        return "Attempting to remove recipe mapping for " + toStack(iStack);
    }
    
}

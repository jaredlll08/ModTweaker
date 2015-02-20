package modtweaker2.mods.botania.lexicon;

import minetweaker.IUndoableAction;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.lexicon.LexiconRecipeMappings;
import vazkii.botania.api.lexicon.LexiconRecipeMappings.EntryData;

public class RemoveRecipeMapping implements IUndoableAction {
	
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
	public boolean canUndo() {
        return true;
	}
	
	@Override
	public String describe() {
        return "Removing Lexicon Recipe Lookup: " + stack.getUnlocalizedName();
	}
	
	@Override
	public String describeUndo() {
        return "Adding Lexicon Recipe Lookup: " + stack.getUnlocalizedName();
	}
	
	@Override
	public void undo() {
		LexiconRecipeMappings.map(stack,entry,page);
	}
	
	@Override
	public Object getOverrideKey() {
		return null;
	}
}

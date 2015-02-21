package modtweaker2.mods.botania.lexicon;

import minetweaker.IUndoableAction;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.lexicon.LexiconRecipeMappings;

public class AddRecipeMapping implements IUndoableAction {
	
	ItemStack stack;
	LexiconEntry entry;
	int page;

    public AddRecipeMapping(ItemStack stack, LexiconEntry entry, int page) {
        this.stack =stack;
        this.entry=entry;
        this.page=page;
    }

    @Override
	public void apply() {
    	LexiconRecipeMappings.map(stack, entry, page);
	}

	@Override
	public boolean canUndo() {
        return true;
	}
	
	@Override
	public String describe() {
        return "Adding Lexicon Recipe Lookup: " + stack.getUnlocalizedName();
	}
	
	@Override
	public String describeUndo() {
        return "Removing Lexicon Recipe Lookup: " + stack.getUnlocalizedName();
	}
	
	@Override
	public void undo() {
		LexiconRecipeMappings.remove(stack);
	}
	
	@Override
	public Object getOverrideKey() {
		return null;
	}
}

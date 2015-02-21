package modtweaker2.mods.botania.lexicon;

import minetweaker.IUndoableAction;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.LexiconEntry;

public class RemoveEntry implements IUndoableAction {
	
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
	public boolean canUndo() {
        return Entry != null;
	}
	
	@Override
	public String describe() {
        return "Removing Lexicon Entry: " + Entry.getUnlocalizedName();
	}
	
	@Override
	public String describeUndo() {
        return "Adding Lexicon Entry: " + Entry.getUnlocalizedName();
	}
	
	@Override
	public void undo() {
    	BotaniaAPI.addEntry(Entry, Entry.category);
	}
	
	@Override
	public Object getOverrideKey() {
		return null;
	}
}

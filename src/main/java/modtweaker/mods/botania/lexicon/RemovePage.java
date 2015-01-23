package modtweaker.mods.botania.lexicon;

import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.lexicon.LexiconPage;
import minetweaker.IUndoableAction;

public class RemovePage implements IUndoableAction {
	
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
	public boolean canUndo() {
        return Entry != null && page != null;
	}
	
	@Override
	public String describe() {
        return "Removing Lexicon Page: " + Entry.pages.get(page_number).getUnlocalizedName();
	}
	
	@Override
	public String describeUndo() {
        return "Adding Lexicon Page: " + page.getUnlocalizedName();
	}
	
	@Override
	public void undo() {
		Entry.pages.add(page_number,page);
	}
	
	@Override
	public Object getOverrideKey() {
		return null;
	}
}

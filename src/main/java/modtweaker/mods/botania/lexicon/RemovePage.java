package modtweaker.mods.botania.lexicon;

import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.lexicon.LexiconPage;
import minetweaker.IUndoableAction;

public class RemovePage implements IUndoableAction {
	
	String Name;
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
        this.Name=this.page.getUnlocalizedName();
    	Entry.pages.remove(page_number);
	}

	@Override
	public boolean canUndo() {
        return Name != null && Entry != null && page != null;
	}
	
	@Override
	public String describe() {
        return "Adding Lexicon Page: " + Name;
	}
	
	@Override
	public String describeUndo() {
        return "Removing Lexicon Page: " + Name;
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

package modtweaker.mods.botania.lexicon;

import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.lexicon.LexiconPage;
import minetweaker.IUndoableAction;

public class AddPage implements IUndoableAction {
	
	String Name;
	int page_number;
	LexiconEntry Entry;
	LexiconPage page;

    public AddPage(String Name, LexiconEntry Entry, LexiconPage page, int page_number) {
        this.Name=Name;
        this.Entry=Entry;
        this.page=page;
        this.page_number=page_number;
    }

    @Override
	public void apply() {
    	Entry.pages.add(page_number, page);
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
		Entry.pages.remove(page_number);
	}
	
	@Override
	public Object getOverrideKey() {
		return null;
	}
}

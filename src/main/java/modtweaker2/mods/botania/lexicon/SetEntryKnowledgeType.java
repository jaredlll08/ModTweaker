package modtweaker2.mods.botania.lexicon;

import minetweaker.IUndoableAction;
import vazkii.botania.api.lexicon.KnowledgeType;
import vazkii.botania.api.lexicon.LexiconEntry;

public class SetEntryKnowledgeType implements IUndoableAction {
	
	LexiconEntry Entry;
	KnowledgeType newType;
	KnowledgeType oldType;

    public SetEntryKnowledgeType(LexiconEntry Entry, KnowledgeType type) {
        this.Entry=Entry;
        this.newType=type;
    }

    @Override
	public void apply() {
    	oldType=Entry.getKnowledgeType();
    	Entry.setKnowledgeType(newType);
	}

	@Override
	public boolean canUndo() {
        return Entry != null && oldType != null;
	}
	
	@Override
	public String describe() {
        return "Setting Knowledge type for: " + Entry.getUnlocalizedName();
	}
	
	@Override
	public String describeUndo() {
        return "Unsetting Knowledge type for: " + Entry.getUnlocalizedName();
	}
	
	@Override
	public void undo() {
    	Entry.setKnowledgeType(oldType);
	}
	
	@Override
	public Object getOverrideKey() {
		return null;
	}
}

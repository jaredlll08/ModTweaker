package com.blamejared.compat.botania.lexicon;

import crafttweaker.IAction;
import vazkii.botania.api.lexicon.KnowledgeType;
import vazkii.botania.api.lexicon.LexiconEntry;

public class SetEntryKnowledgeType implements IAction {
	
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
	public String describe() {
        return "Setting Knowledge type for: " + Entry.getUnlocalizedName();
	}
}

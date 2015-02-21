package modtweaker2.mods.thaumcraft.research;

import minetweaker.IUndoableAction;
import modtweaker2.mods.thaumcraft.ThaumcraftHelper;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchItem;

public class RemoveResearch implements IUndoableAction {
    String key;
    String tab;
    ResearchItem removed;

    public RemoveResearch(String victim) {
        key = victim;
    }

    @Override
    public void apply() {
        tab = ThaumcraftHelper.getResearchTab(key);
        if (tab != null) {
            removed = ResearchCategories.researchCategories.get(tab).research.get(key);
            ResearchCategories.researchCategories.get(tab).research.remove(key);
        }
    }

    @Override
    public String describe() {
        return "Removing Research: " + key;
    }

    @Override
    public boolean canUndo() {
        return tab != null && removed != null;
    }

    @Override
    public void undo() {
        ResearchCategories.researchCategories.get(tab).research.put(key, removed);
    }

    @Override
    public String describeUndo() {
        return "Restoring Research: " + key;
    }

    @Override
    public String getOverrideKey() {
        return null;
    }

}
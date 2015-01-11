package modtweaker.mods.thaumcraft.research;

import minetweaker.IUndoableAction;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchCategoryList;

public class RemoveTab implements IUndoableAction {
    String tab;
    ResearchCategoryList list;

    public RemoveTab(String victim) {
        tab = victim;
    }

    @Override
    public void apply() {
        list = ResearchCategories.getResearchList(tab);
        ResearchCategories.researchCategories.remove(tab);
    }

    @Override
    public String describe() {
        return "Removing Research Tab: " + tab;
    }

    @Override
    public boolean canUndo() {
        return list != null;
    }

    @Override
    public void undo() {
        ResearchCategories.researchCategories.put(tab, list);
    }

    @Override
    public String describeUndo() {
        return "Restoring Research Tab: " + tab;
    }

    @Override
    public String getOverrideKey() {
        return null;
    }

}
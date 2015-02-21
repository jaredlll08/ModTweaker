package modtweaker2.mods.thaumcraft.research;

import minetweaker.IUndoableAction;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchItem;

public class AddResearch implements IUndoableAction {
    String key;
    String tab;
    ResearchItem research;

    public AddResearch(ResearchItem res) {
        research = res;
        tab = research.category;
        key = research.key;
    }

    @Override
    public void apply() {
        research.registerResearchItem();
    }

    @Override
    public String describe() {
        return "Registering Research: " + key;
    }

    @Override
    public boolean canUndo() {
        return tab != null && key != null;
    }

    @Override
    public void undo() {
        ResearchCategories.researchCategories.get(tab).research.remove(key);
    }

    @Override
    public String describeUndo() {
        return "Removing Research: " + key;
    }

    @Override
    public String getOverrideKey() {
        return null;
    }

}
package modtweaker2.mods.thaumcraft.research;

import minetweaker.IUndoableAction;
import modtweaker2.mods.thaumcraft.ThaumcraftHelper;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchPage;

public class ClearPages implements IUndoableAction {
    String key;
    String tab;
    ResearchPage[] oldPages;

    public ClearPages(String res) {
        key = res;
        tab = ThaumcraftHelper.getResearchTab(key);
    }

    @Override
    public void apply() {
        oldPages = ResearchCategories.researchCategories.get(tab).research.get(key).getPages();
        ResearchCategories.researchCategories.get(tab).research.get(key).setPages(new ResearchPage[0]);
    }

    @Override
    public String describe() {
        return "Clearing Research Pages from " + key;
    }

    @Override
    public boolean canUndo() {
        return oldPages != null;
    }

    @Override
    public void undo() {
        ResearchCategories.researchCategories.get(tab).research.get(key).setPages(oldPages);
    }

    @Override
    public String describeUndo() {
        return "Restoring Pages to " + key;
    }

    @Override
    public String getOverrideKey() {
        return null;
    }

}
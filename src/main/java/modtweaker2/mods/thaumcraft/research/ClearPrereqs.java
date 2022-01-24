package modtweaker2.mods.thaumcraft.research;

import minetweaker.IUndoableAction;
import modtweaker2.mods.thaumcraft.ThaumcraftHelper;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchItem;

import static modtweaker2.mods.thaumcraft.ThaumcraftHelper.getResearchSafe;

public class ClearPrereqs implements IUndoableAction {
    String key;
    String tab;
    String[] prereqs;
    String[] secretPrereqs;

    public ClearPrereqs(String res) {
        key = res;
        tab = ThaumcraftHelper.getResearchTab(key);
    }

    @Override
    public void apply() {
        prereqs = ResearchCategories.researchCategories.get(tab).research.get(key).parents;
        secretPrereqs = ResearchCategories.researchCategories.get(tab).research.get(key).parentsHidden;
        ResearchCategories.researchCategories.get(tab).research.get(key).setParents(new String[0]);
        ResearchCategories.researchCategories.get(tab).research.get(key).setParentsHidden(new String[0]);
    }

    @Override
    public String describe() {
        return "Clearing Prerequisites for " + key;
    }

    @Override
    public boolean canUndo() {
        return prereqs != null || secretPrereqs != null;
    }

    @Override
    public void undo() {
        final ResearchItem research = getResearchSafe(tab, key);
        if(research == null) {
            return;
        }

        if (prereqs != null) research.setParents(prereqs);
        if (secretPrereqs != null) research.setParentsHidden(secretPrereqs);
    }

    @Override
    public String describeUndo() {
        return "Restoring Prerequisites for " + key;
    }

    @Override
    public String getOverrideKey() {
        return null;
    }

}
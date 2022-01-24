package modtweaker2.mods.thaumcraft.research;

import minetweaker.IUndoableAction;
import modtweaker2.mods.thaumcraft.ThaumcraftHelper;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchItem;

import static modtweaker2.mods.thaumcraft.ThaumcraftHelper.getResearchSafe;

public class AddSibling implements IUndoableAction {
    String key;
    String tab;
    String sibling;
    String[] oldSiblings;

    public AddSibling(String res, String sib) {
        key = res;
        tab = ThaumcraftHelper.getResearchTab(key);
        sibling = sib;
    }

    @Override
    public void apply() {
        oldSiblings = ResearchCategories.researchCategories.get(tab).research.get(key).siblings;
        if (oldSiblings == null) oldSiblings = new String[0];
        String[] newSiblings = new String[oldSiblings.length + 1];
        for (int x = 0; x < oldSiblings.length; x++) {
            newSiblings[x] = oldSiblings[x];
        }
        newSiblings[oldSiblings.length] = sibling;
        ResearchCategories.researchCategories.get(tab).research.get(key).setSiblings(sibling);
    }

    @Override
    public String describe() {
        return "Adding Sibling to " + key;
    }

    @Override
    public boolean canUndo() {
        return oldSiblings != null;
    }

    @Override
    public void undo() {
        final ResearchItem research = getResearchSafe(tab, key);
        if(research == null) {
            return;
        }

        research.setSiblings(oldSiblings);
    }

    @Override
    public String describeUndo() {
        return "Restoring Siblings for " + key;
    }

    @Override
    public String getOverrideKey() {
        return null;
    }

}
package modtweaker.mods.thaumcraft.research;

import minetweaker.IUndoableAction;
import modtweaker.mods.thaumcraft.ThaumcraftHelper;
import thaumcraft.api.research.ResearchCategories;

public class AddPrereq implements IUndoableAction {
    String key;
    String tab;
    String prereq;
    String[] oldPrereqs;
    boolean hidden;

    public AddPrereq(String res, String req, boolean secret) {
        key = res;
        tab = ThaumcraftHelper.getResearchTab(key);
        prereq = req;
        hidden = secret;
    }

    @Override
    public void apply() {
        if (!hidden) {
            oldPrereqs = ResearchCategories.researchCategories.get(tab).research.get(key).parents;
            if (oldPrereqs == null) oldPrereqs = new String[0];
            String[] newPrereqs = new String[oldPrereqs.length + 1];
            for (int x = 0; x < oldPrereqs.length; x++) {
                newPrereqs[x] = oldPrereqs[x];
            }
            newPrereqs[oldPrereqs.length] = prereq;
            ResearchCategories.researchCategories.get(tab).research.get(key).setParents(newPrereqs);
        } else {
            oldPrereqs = ResearchCategories.researchCategories.get(tab).research.get(key).parentsHidden;
            if (oldPrereqs == null) oldPrereqs = new String[0];
            String[] newPrereqs = new String[oldPrereqs.length + 1];
            for (int x = 0; x < oldPrereqs.length; x++) {
                newPrereqs[x] = oldPrereqs[x];
            }
            newPrereqs[oldPrereqs.length] = prereq;
            ResearchCategories.researchCategories.get(tab).research.get(key).setParentsHidden(newPrereqs);
        }
    }

    @Override
    public String describe() {
        return "Adding Prerequisites to " + key;
    }

    @Override
    public boolean canUndo() {
        return oldPrereqs != null;
    }

    @Override
    public void undo() {
        if (!hidden) ResearchCategories.researchCategories.get(tab).research.get(key).setParents(oldPrereqs);
        else ResearchCategories.researchCategories.get(tab).research.get(key).setParentsHidden(oldPrereqs);
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

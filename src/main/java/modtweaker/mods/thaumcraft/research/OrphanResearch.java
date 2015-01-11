package modtweaker.mods.thaumcraft.research;

import java.util.ArrayList;
import java.util.HashMap;

import minetweaker.IUndoableAction;
import thaumcraft.api.research.ResearchCategories;

public class OrphanResearch implements IUndoableAction {
    String key;
    HashMap<String, String> children = new HashMap();
    HashMap<String, String> secretChildren = new HashMap();
    HashMap<String, String> siblings = new HashMap();

    public OrphanResearch(String victim) {
        key = victim;
    }

    @Override
    public void apply() {
        for (String tab : ResearchCategories.researchCategories.keySet()) {
            for (String research : ResearchCategories.researchCategories.get(tab).research.keySet()) {
                String[] prereqs = ResearchCategories.researchCategories.get(tab).research.get(research).parents;
                if (prereqs != null) {
                    for (int x = 0; x < prereqs.length; x++) {
                        if (prereqs[x] != null && prereqs[x].equals(key)) {
                            children.put(research, tab);
                            ArrayList<String> newReqs = new ArrayList();
                            for (int y = 0; y < prereqs.length; y++) {
                                if (y != x) newReqs.add(prereqs[y]);
                            }
                            ResearchCategories.researchCategories.get(tab).research.get(research).setParents(newReqs.toArray(new String[prereqs.length - 1]));
                            break;
                        }
                    }
                }
                prereqs = ResearchCategories.researchCategories.get(tab).research.get(research).parentsHidden;
                if (prereqs != null) {
                    for (int x = 0; x < prereqs.length; x++) {
                        if (prereqs[x] != null && prereqs[x].equals(key)) {
                            secretChildren.put(research, tab);
                            ArrayList<String> newReqs = new ArrayList();
                            for (int y = 0; y < prereqs.length; y++) {
                                if (y != x) newReqs.add(prereqs[y]);
                            }
                            ResearchCategories.researchCategories.get(tab).research.get(research).setParentsHidden(newReqs.toArray(new String[prereqs.length - 1]));
                            break;
                        }
                    }
                }
                prereqs = ResearchCategories.researchCategories.get(tab).research.get(research).siblings;
                if (prereqs != null) {
                    for (int x = 0; x < prereqs.length; x++) {
                        if (prereqs[x] != null && prereqs[x].equals(key)) {
                            siblings.put(research, tab);
                            ArrayList<String> newReqs = new ArrayList();
                            for (int y = 0; y < prereqs.length; y++) {
                                if (y != x) newReqs.add(prereqs[y]);
                            }
                            ResearchCategories.researchCategories.get(tab).research.get(research).setSiblings(newReqs.toArray(new String[prereqs.length - 1]));
                            break;
                        }
                    }
                }
            }
        }
    }

    @Override
    public String describe() {
        return "Orphaning Research: " + key;
    }

    @Override
    public boolean canUndo() {
        return children.size() > 0 || secretChildren.size() > 0 || siblings.size() > 0;
    }

    @Override
    public void undo() {
        if (children.size() > 0) {
            for (String research : children.keySet()) {
                String[] oldPrereqs = ResearchCategories.researchCategories.get(children.get(research)).research.get(research).parents;
                String[] newReqs = new String[oldPrereqs.length + 1];
                for (int x = 0; x < oldPrereqs.length; x++) {
                    newReqs[x] = oldPrereqs[x];
                }
                newReqs[oldPrereqs.length] = key;
                ResearchCategories.researchCategories.get(children.get(research)).research.get(research).setParents(newReqs);
            }
        }
        if (secretChildren.size() > 0) {
            for (String research : secretChildren.keySet()) {
                String[] oldPrereqs = ResearchCategories.researchCategories.get(secretChildren.get(research)).research.get(research).parentsHidden;
                String[] newReqs = new String[oldPrereqs.length + 1];
                for (int x = 0; x < oldPrereqs.length; x++) {
                    newReqs[x] = oldPrereqs[x];
                }
                newReqs[oldPrereqs.length] = key;
                ResearchCategories.researchCategories.get(secretChildren.get(research)).research.get(research).setParentsHidden(newReqs);
            }
        }
        if (siblings.size() > 0) {
            for (String research : siblings.keySet()) {
                String[] oldPrereqs = ResearchCategories.researchCategories.get(siblings.get(research)).research.get(research).siblings;
                String[] newReqs = new String[oldPrereqs.length + 1];
                for (int x = 0; x < oldPrereqs.length; x++) {
                    newReqs[x] = oldPrereqs[x];
                }
                newReqs[oldPrereqs.length] = key;
                ResearchCategories.researchCategories.get(siblings.get(research)).research.get(research).setSiblings(newReqs);
            }
        }
    }

    @Override
    public String describeUndo() {
        return "Reattaching Research: " + key;
    }

    @Override
    public String getOverrideKey() {
        return null;
    }

}
package modtweaker.mods.thaumcraft.research;

import java.lang.reflect.Field;

import minetweaker.IUndoableAction;
import modtweaker.mods.thaumcraft.ThaumcraftHelper;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchItem;

public class SetAspects implements IUndoableAction {
    String key;
    String tab;
    AspectList oldList;
    AspectList list;
    boolean applied = false;

    public SetAspects(String res, AspectList asp) {
        key = res;
        tab = ThaumcraftHelper.getResearchTab(key);
        list = asp;
    }

    @Override
    public void apply() {
        ResearchItem research = ResearchCategories.researchCategories.get(tab).research.get(key);
        oldList = research.tags;
        try {
            Field tags = Class.forName("thaumcraft.api.research.ResearchItem").getField("tags");
            tags.setAccessible(true);
            tags.set(research, list);
            applied = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String describe() {
        return "Changing Aspects for " + key;
    }

    @Override
    public boolean canUndo() {
        return applied;
    }

    @Override
    public void undo() {
        try {
            ResearchItem research = ResearchCategories.researchCategories.get(tab).research.get(key);
            Field tags = Class.forName("thaumcraft.api.research.ResearchItem").getField("tags");
            tags.setAccessible(true);
            tags.set(research, oldList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String describeUndo() {
        return "Restoring Aspects for " + key;
    }

    @Override
    public String getOverrideKey() {
        return null;
    }

}
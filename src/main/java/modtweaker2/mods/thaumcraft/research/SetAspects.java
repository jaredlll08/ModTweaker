package modtweaker2.mods.thaumcraft.research;

import minetweaker.IUndoableAction;
import modtweaker2.mods.thaumcraft.ThaumcraftHelper;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchItem;

import java.lang.reflect.Field;

import static modtweaker2.mods.thaumcraft.ThaumcraftHelper.getResearchSafe;

public class SetAspects implements IUndoableAction {
    String key;
    String tab;
    AspectList oldList;
    AspectList list;
    boolean applied = false;
    final static Field tags;
    
    static {
        Field tags1;
        try {
            tags1 = Class.forName("thaumcraft.api.research.ResearchItem").getField("tags");
        } catch (NoSuchFieldException | ClassNotFoundException e) {
            e.printStackTrace();
            tags1 = null;
        }
        tags = tags1;
    }

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
            final ResearchItem research = getResearchSafe(tab, key);
            if(research == null || tags == null) {
                return;
            }
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
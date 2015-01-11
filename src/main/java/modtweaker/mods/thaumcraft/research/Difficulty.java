package modtweaker.mods.thaumcraft.research;

import java.lang.reflect.Field;

import minetweaker.IUndoableAction;
import modtweaker.mods.thaumcraft.ThaumcraftHelper;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchItem;

public class Difficulty implements IUndoableAction {
    String key;
    String tab;
    int difficulty;
    int oldDif;
    boolean applied = false;

    public Difficulty(String res, int dif) {
        key = res;
        tab = ThaumcraftHelper.getResearchTab(key);
        difficulty = dif;
    }

    @Override
    public void apply() {
        ResearchItem research = ResearchCategories.researchCategories.get(tab).research.get(key);
        oldDif = research.getComplexity();
        try {
            Field complexity = Class.forName("thaumcraft.api.research.ResearchItem").getDeclaredField("complexity");
            complexity.setAccessible(true);
            complexity.set(research, difficulty);
            applied = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String describe() {
        return "Changing Complexity for " + key;
    }

    @Override
    public boolean canUndo() {
        return applied;
    }

    @Override
    public void undo() {
        try {
            ResearchItem research = ResearchCategories.researchCategories.get(tab).research.get(key);
            Field complexity = Class.forName("thaumcraft.api.research.ResearchItem").getDeclaredField("complexity");
            complexity.setAccessible(true);
            complexity.set(research, oldDif);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String describeUndo() {
        return "Restoring Complexity for " + key;
    }

    @Override
    public String getOverrideKey() {
        return null;
    }

}
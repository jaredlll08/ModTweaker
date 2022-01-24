package modtweaker2.mods.thaumcraft.research;

import minetweaker.IUndoableAction;
import modtweaker2.mods.thaumcraft.ThaumcraftHelper;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchItem;

import java.lang.reflect.Field;

import static modtweaker2.mods.thaumcraft.ThaumcraftHelper.getResearchSafe;

public class Difficulty implements IUndoableAction {
    String key;
    String tab;
    int difficulty;
    int oldDif;
    boolean applied = false;
    final static Field complexity;
    
    static {
        Field complexity1;
        try {
            complexity1 = Class.forName("thaumcraft.api.research.ResearchItem").getDeclaredField("complexity");
        } catch (NoSuchFieldException | ClassNotFoundException e) {
            complexity1 = null;
            e.printStackTrace();
        }
        complexity = complexity1;
    }
    


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
            final ResearchItem research = getResearchSafe(tab, key);
            if(research == null || complexity == null) {
                return;
            }
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
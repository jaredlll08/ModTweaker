package modtweaker2.mods.thaumcraft.research;

import java.lang.reflect.Field;

import minetweaker.IUndoableAction;
import modtweaker2.mods.thaumcraft.ThaumcraftHelper;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchItem;

public class MoveResearch implements IUndoableAction {
    String key;
    String newTab;
    int x;
    int y;
    String oldTab;
    int oldX;
    int oldY;
    boolean moved = false;

    public MoveResearch(String research, String destination, int ex, int wy) {
        key = research;
        oldTab = ThaumcraftHelper.getResearchTab(key);
        newTab = destination;
        x = ex;
        y = wy;
    }

    @Override
    public void apply() {
        if (oldTab != null) {
            ResearchItem research = ResearchCategories.researchCategories.get(oldTab).research.get(key);
            oldX = research.displayColumn;
            oldY = research.displayRow;
            try {
                Class res = Class.forName("thaumcraft.api.research.ResearchItem");
                Field ex = res.getField("displayColumn");
                ex.setAccessible(true);
                ex.setInt(research, x);
                Field wy = res.getField("displayRow");
                wy.setAccessible(true);
                wy.setInt(research, y);
                Field cat = res.getField("category");
                cat.setAccessible(true);
                cat.set(research, newTab);
                ResearchCategories.researchCategories.get(oldTab).research.remove(key);
                research.registerResearchItem();
                moved = true;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String describe() {
        return "Moving Research " + key + " to " + newTab;
    }

    @Override
    public boolean canUndo() {
        return moved;
    }

    @Override
    public void undo() {
        ResearchItem research = ResearchCategories.researchCategories.get(oldTab).research.get(key);
        try {
            Class res = Class.forName("thaumcraft.api.research.ResearchItem");
            Field ex = res.getField("displayColumn");
            ex.setAccessible(true);
            ex.setInt(research, oldX);
            Field wy = res.getField("displayRow");
            wy.setAccessible(true);
            wy.setInt(research, oldY);
            Field cat = res.getField("category");
            cat.setAccessible(true);
            cat.set(research, oldTab);
            ResearchCategories.researchCategories.get(newTab).research.remove(key);
            research.registerResearchItem();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String describeUndo() {
        return "Moving Research " + key + " back to " + oldTab;
    }

    @Override
    public String getOverrideKey() {
        return null;
    }

}
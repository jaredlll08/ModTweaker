package modtweaker2.mods.thaumcraft.research;

import minetweaker.IUndoableAction;
import modtweaker2.mods.thaumcraft.ThaumcraftHelper;
import modtweaker2.mods.thaumcraft.handlers.Research.SetType;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchItem;

import java.lang.reflect.Field;

import static modtweaker2.mods.thaumcraft.ThaumcraftHelper.getResearchSafe;

public class SetResearch implements IUndoableAction {
    String key;
    String tab;
    SetType type;
    boolean flag;
    boolean applied = false;

    public SetResearch(String res, boolean f, SetType typ) {
        key = res;
        tab = ThaumcraftHelper.getResearchTab(key);
        type = typ;
        flag = f;
    }

    @Override
    public void apply() {
        ResearchItem research = ResearchCategories.researchCategories.get(tab).research.get(key);
        if (flag) {
            if (type == SetType.AUTO) research.setAutoUnlock();
            else if (type == SetType.ROUND) research.setRound();
            else if (type == SetType.SPIKE) research.setSpecial();
            else if (type == SetType.SECONDARY) research.setSecondary();
            else if (type == SetType.STUB) research.setStub();
            else if (type == SetType.VIRTUAL) research.setVirtual();
            else if (type == SetType.CONCEAL) research.setConcealed();
            else if (type == SetType.HIDDEN) research.setHidden();
            applied = true;
        } else {
            try {
                Field target = null;
                if (type == SetType.AUTO) target = Class.forName("thaumcraft.api.research.ResearchItem").getDeclaredField("isAutoUnlock");
                else if (type == SetType.ROUND) target = Class.forName("thaumcraft.api.research.ResearchItem").getDeclaredField("isRound");
                else if (type == SetType.SPIKE) target = Class.forName("thaumcraft.api.research.ResearchItem").getDeclaredField("isSpecial");
                else if (type == SetType.SECONDARY) target = Class.forName("thaumcraft.api.research.ResearchItem").getDeclaredField("isSecondary");
                else if (type == SetType.STUB) target = Class.forName("thaumcraft.api.research.ResearchItem").getDeclaredField("isStub");
                else if (type == SetType.VIRTUAL) target = Class.forName("thaumcraft.api.research.ResearchItem").getDeclaredField("isVirtual");
                else if (type == SetType.CONCEAL) target = Class.forName("thaumcraft.api.research.ResearchItem").getDeclaredField("isConcealed");
                else if (type == SetType.HIDDEN) target = Class.forName("thaumcraft.api.research.ResearchItem").getDeclaredField("isHidden");

                if (target != null) {
                    target.setAccessible(true);
                    target.setBoolean(research, false);
                    applied = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String describe() {
        return "Setting tag for " + key;
    }

    @Override
    public boolean canUndo() {
        return applied;
    }

    @Override
    public void undo() {
        final ResearchItem research = getResearchSafe(tab, key);
        if(research == null) {
            return;
        }

        if (!flag) {
            if (type == SetType.AUTO) research.setAutoUnlock();
            else if (type == SetType.ROUND) research.setRound();
            else if (type == SetType.SPIKE) research.setSpecial();
            else if (type == SetType.SECONDARY) research.setSecondary();
            else if (type == SetType.STUB) research.setStub();
            else if (type == SetType.VIRTUAL) research.setVirtual();
            else if (type == SetType.CONCEAL) research.setConcealed();
            else if (type == SetType.HIDDEN) research.setHidden();
        } else {
            try {
                Field target = null;
                if (type == SetType.AUTO) target = Class.forName("thaumcraft.api.research.ResearchItem").getDeclaredField("isAutoUnlock");
                else if (type == SetType.ROUND) target = Class.forName("thaumcraft.api.research.ResearchItem").getDeclaredField("isRound");
                else if (type == SetType.SPIKE) target = Class.forName("thaumcraft.api.research.ResearchItem").getDeclaredField("isSpecial");
                else if (type == SetType.SECONDARY) target = Class.forName("thaumcraft.api.research.ResearchItem").getDeclaredField("isSecondary");
                else if (type == SetType.STUB) target = Class.forName("thaumcraft.api.research.ResearchItem").getDeclaredField("isStub");
                else if (type == SetType.VIRTUAL) target = Class.forName("thaumcraft.api.research.ResearchItem").getDeclaredField("isVirtual");
                else if (type == SetType.CONCEAL) target = Class.forName("thaumcraft.api.research.ResearchItem").getDeclaredField("isConcealed");
                else if (type == SetType.HIDDEN) target = Class.forName("thaumcraft.api.research.ResearchItem").getDeclaredField("isHidden");

                if (target != null) {
                    target.setAccessible(true);
                    target.setBoolean(research, false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String describeUndo() {
        return "Reversing tag for " + key;
    }

    @Override
    public String getOverrideKey() {
        return null;
    }

}
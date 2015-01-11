package modtweaker.mods.thaumcraft.research;

import minetweaker.IUndoableAction;
import net.minecraft.util.ResourceLocation;
import thaumcraft.api.research.ResearchCategories;

public class AddTab implements IUndoableAction {
    String tab;
    ResourceLocation icon;
    ResourceLocation background;

    public AddTab(String research, ResourceLocation pic, ResourceLocation back) {
        icon = pic;
        background = back;
        tab = research;
    }

    @Override
    public void apply() {
        ResearchCategories.registerCategory(tab, icon, background);
    }

    @Override
    public String describe() {
        return "Registering Research Tab: " + tab;
    }

    @Override
    public boolean canUndo() {
        return true;
    }

    @Override
    public void undo() {
        ResearchCategories.researchCategories.remove(tab);
    }

    @Override
    public String describeUndo() {
        return "Removing Research Tab: " + tab;
    }

    @Override
    public String getOverrideKey() {
        return null;
    }

}
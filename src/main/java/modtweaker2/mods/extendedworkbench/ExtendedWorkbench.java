package modtweaker2.mods.extendedworkbench;

import minetweaker.MineTweakerAPI;
import modtweaker2.mods.extendedworkbench.handlers.ExtendedCrafting;

public class ExtendedWorkbench {
    public ExtendedWorkbench() {
        MineTweakerAPI.registerClass(ExtendedCrafting.class);
    }
}

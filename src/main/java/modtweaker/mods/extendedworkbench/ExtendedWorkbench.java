package modtweaker.mods.extendedworkbench;

import minetweaker.MineTweakerAPI;
import modtweaker.mods.extendedworkbench.handlers.ExtendedCrafting;

public class ExtendedWorkbench {
    public ExtendedWorkbench() {
        MineTweakerAPI.registerClass(ExtendedCrafting.class);
    }
}

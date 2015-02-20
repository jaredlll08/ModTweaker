package modtweaker2.mods.pneumaticcraft;

import minetweaker.MineTweakerAPI;
import modtweaker2.mods.pneumaticcraft.handlers.Assembly;
import modtweaker2.mods.pneumaticcraft.handlers.Pressure;

public class PneumaticCraft {
    public PneumaticCraft() {
        MineTweakerAPI.registerClass(Assembly.class);
        MineTweakerAPI.registerClass(Pressure.class);
    }
}

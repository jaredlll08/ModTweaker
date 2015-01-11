package modtweaker.mods.pneumaticcraft;

import minetweaker.MineTweakerAPI;
import modtweaker.mods.pneumaticcraft.handlers.Assembly;
import modtweaker.mods.pneumaticcraft.handlers.Pressure;

public class PneumaticCraft {
    public PneumaticCraft() {
        MineTweakerAPI.registerClass(Assembly.class);
        MineTweakerAPI.registerClass(Pressure.class);
    }
}

package modtweaker.mods.tconstruct;

import minetweaker.MineTweakerAPI;
import modtweaker.mods.tconstruct.handlers.Casting;
import modtweaker.mods.tconstruct.handlers.Drying;
import modtweaker.mods.tconstruct.handlers.Smeltery;
import modtweaker.mods.tconstruct.handlers.TiCTweaks;
import modtweaker.mods.tconstruct.handlers.ToolStats;

public class TConstruct {
    public TConstruct() {
        MineTweakerAPI.registerClass(Casting.class);
        MineTweakerAPI.registerClass(Drying.class);
        MineTweakerAPI.registerClass(Smeltery.class);
        MineTweakerAPI.registerClass(TiCTweaks.class);
        MineTweakerAPI.registerClass(ToolStats.class);
    }
}

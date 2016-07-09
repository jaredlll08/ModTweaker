package modtweaker2.mods.tconstruct;

import minetweaker.MineTweakerAPI;
import modtweaker2.brackets.MaterialBracketHandler;

public class TConstruct {
    public TConstruct() {
//        MineTweakerAPI.registerClass(Casting.class);
//        MineTweakerAPI.registerClass(Drying.class);
//        MineTweakerAPI.registerClass(Smeltery.class);
//        MineTweakerAPI.registerClass(Modifiers.class);
//        MineTweakerAPI.registerClass(TiCTweaks.class);
//        MineTweakerAPI.registerClass(ToolStats.class);
        MineTweakerAPI.registerBracketHandler(new MaterialBracketHandler());
    }
}

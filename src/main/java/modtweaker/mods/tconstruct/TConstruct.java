package modtweaker.mods.tconstruct;

import minetweaker.MineTweakerAPI;
import modtweaker.brackets.MaterialBracketHandler;
import modtweaker.mods.tconstruct.handlers.Casting;
import modtweaker.mods.tconstruct.handlers.Drying;
import modtweaker.mods.tconstruct.handlers.Modifiers;
import modtweaker.mods.tconstruct.handlers.Smeltery;

public class TConstruct {
    public TConstruct() {
        MineTweakerAPI.registerClass(Casting.class);
        MineTweakerAPI.registerClass(Drying.class);
        MineTweakerAPI.registerClass(Smeltery.class);
        MineTweakerAPI.registerClass(Modifiers.class);
        MineTweakerAPI.registerBracketHandler(new MaterialBracketHandler());
    }
}

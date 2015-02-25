package modtweaker2.mods.tconstruct;

import net.minecraft.item.ItemStack;
import tconstruct.library.crafting.PatternBuilder;
import tconstruct.library.crafting.PatternBuilder.MaterialSet;
import tconstruct.library.util.IPattern;
import tconstruct.tools.logic.PartBuilderLogic;
import minetweaker.MineTweakerAPI;
import modtweaker2.mods.tconstruct.handlers.Casting;
import modtweaker2.mods.tconstruct.handlers.Drying;
import modtweaker2.mods.tconstruct.handlers.Smeltery;
import modtweaker2.mods.tconstruct.handlers.TiCTweaks;
import modtweaker2.mods.tconstruct.handlers.ToolStats;

public class TConstruct {
    public TConstruct() {
        MineTweakerAPI.registerClass(Casting.class);
        MineTweakerAPI.registerClass(Drying.class);
        MineTweakerAPI.registerClass(Smeltery.class);
        MineTweakerAPI.registerClass(TiCTweaks.class);
        MineTweakerAPI.registerClass(ToolStats.class);
    }
}

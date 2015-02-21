package modtweaker2.mods.mariculture;

import minetweaker.MineTweakerAPI;
import modtweaker2.mods.mariculture.handlers.Anvil;
import modtweaker2.mods.mariculture.handlers.Casting;
import modtweaker2.mods.mariculture.handlers.Crucible;
import modtweaker2.mods.mariculture.handlers.Fishing;
import modtweaker2.mods.mariculture.handlers.Vat;

public class Mariculture {
    public Mariculture() {
        MineTweakerAPI.registerClass(Anvil.class);
        MineTweakerAPI.registerClass(Casting.class);
        MineTweakerAPI.registerClass(Crucible.class);
        MineTweakerAPI.registerClass(Fishing.class);
        MineTweakerAPI.registerClass(Vat.class);
    }
}

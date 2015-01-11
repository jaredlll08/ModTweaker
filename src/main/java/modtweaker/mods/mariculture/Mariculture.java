package modtweaker.mods.mariculture;

import minetweaker.MineTweakerAPI;
import modtweaker.mods.mariculture.handlers.Anvil;
import modtweaker.mods.mariculture.handlers.Casting;
import modtweaker.mods.mariculture.handlers.Crucible;
import modtweaker.mods.mariculture.handlers.Fishing;
import modtweaker.mods.mariculture.handlers.Vat;

public class Mariculture {
    public Mariculture() {
        MineTweakerAPI.registerClass(Anvil.class);
        MineTweakerAPI.registerClass(Casting.class);
        MineTweakerAPI.registerClass(Crucible.class);
        MineTweakerAPI.registerClass(Fishing.class);
        MineTweakerAPI.registerClass(Vat.class);
    }
}

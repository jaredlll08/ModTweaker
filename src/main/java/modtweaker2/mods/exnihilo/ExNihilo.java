package modtweaker2.mods.exnihilo;

import minetweaker.MineTweakerAPI;
import modtweaker2.mods.exnihilo.handlers.Compost;
import modtweaker2.mods.exnihilo.handlers.Crucible;
import modtweaker2.mods.exnihilo.handlers.Hammer;
import modtweaker2.mods.exnihilo.handlers.Sieve;

public class ExNihilo {
    public ExNihilo() {
        MineTweakerAPI.registerClass(Compost.class);
        MineTweakerAPI.registerClass(Crucible.class);
        MineTweakerAPI.registerClass(Hammer.class);
        MineTweakerAPI.registerClass(Sieve.class);
    }
}

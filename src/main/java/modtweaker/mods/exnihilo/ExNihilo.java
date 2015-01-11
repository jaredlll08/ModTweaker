package modtweaker.mods.exnihilo;

import minetweaker.MineTweakerAPI;
import modtweaker.mods.exnihilo.handlers.Compost;
import modtweaker.mods.exnihilo.handlers.Crucible;
import modtweaker.mods.exnihilo.handlers.Hammer;
import modtweaker.mods.exnihilo.handlers.Sieve;

public class ExNihilo {
    public ExNihilo() {
        MineTweakerAPI.registerClass(Compost.class);
        MineTweakerAPI.registerClass(Crucible.class);
        MineTweakerAPI.registerClass(Hammer.class);
        MineTweakerAPI.registerClass(Sieve.class);
    }
}

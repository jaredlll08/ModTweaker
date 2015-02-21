package modtweaker2.mods.metallurgy;

import minetweaker.MineTweakerAPI;
import modtweaker2.mods.metallurgy.handlers.Alloyer;
import modtweaker2.mods.metallurgy.handlers.Crusher;

public class Metallurgy {
    public Metallurgy() {
        MineTweakerAPI.registerClass(Alloyer.class);
        MineTweakerAPI.registerClass(Crusher.class);
    }
}

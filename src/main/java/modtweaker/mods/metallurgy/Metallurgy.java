package modtweaker.mods.metallurgy;

import minetweaker.MineTweakerAPI;
import modtweaker.mods.metallurgy.handlers.Alloyer;
import modtweaker.mods.metallurgy.handlers.Crusher;

public class Metallurgy {
    public Metallurgy() {
        MineTweakerAPI.registerClass(Alloyer.class);
        MineTweakerAPI.registerClass(Crusher.class);
    }
}

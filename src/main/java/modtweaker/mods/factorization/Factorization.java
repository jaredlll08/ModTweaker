package modtweaker.mods.factorization;

import minetweaker.MineTweakerAPI;
import modtweaker.mods.factorization.handlers.Crystallizer;
import modtweaker.mods.factorization.handlers.Lacerator;
import modtweaker.mods.factorization.handlers.SlagFurnace;

public class Factorization {
    public Factorization() {
        MineTweakerAPI.registerClass(Crystallizer.class);
        MineTweakerAPI.registerClass(Lacerator.class);
        MineTweakerAPI.registerClass(SlagFurnace.class);
    }
}

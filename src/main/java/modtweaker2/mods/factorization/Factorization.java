package modtweaker2.mods.factorization;

import minetweaker.MineTweakerAPI;
import modtweaker2.mods.factorization.handlers.Crystallizer;
import modtweaker2.mods.factorization.handlers.Lacerator;
import modtweaker2.mods.factorization.handlers.SlagFurnace;

public class Factorization {
    public Factorization() {
        MineTweakerAPI.registerClass(Crystallizer.class);
        MineTweakerAPI.registerClass(Lacerator.class);
        MineTweakerAPI.registerClass(SlagFurnace.class);
    }
}

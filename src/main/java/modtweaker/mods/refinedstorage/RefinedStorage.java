package modtweaker.mods.refinedstorage;

import minetweaker.MineTweakerAPI;
import modtweaker.mods.refinedstorage.handlers.Solderer;

public class RefinedStorage {

    public RefinedStorage() {
        MineTweakerAPI.registerClass(Solderer.class);
    }
}

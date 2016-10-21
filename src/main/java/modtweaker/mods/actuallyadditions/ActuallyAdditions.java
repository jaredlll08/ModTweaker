package modtweaker.mods.actuallyadditions;

import minetweaker.MineTweakerAPI;
import modtweaker.mods.actuallyadditions.handlers.AtomicReconstructor;
import modtweaker.mods.actuallyadditions.handlers.CoffeeMaker;
import modtweaker.mods.actuallyadditions.handlers.Compost;
import modtweaker.mods.actuallyadditions.handlers.Crusher;
import modtweaker.mods.actuallyadditions.handlers.Empowerer;

public class ActuallyAdditions {
    public ActuallyAdditions() {
    	MineTweakerAPI.registerClass(Crusher.class);
        MineTweakerAPI.registerClass(AtomicReconstructor.class);
        MineTweakerAPI.registerClass(Empowerer.class);
        MineTweakerAPI.registerClass(Compost.class);
        MineTweakerAPI.registerClass(CoffeeMaker.class);
    }
}

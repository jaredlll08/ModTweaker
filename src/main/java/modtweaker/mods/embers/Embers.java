package modtweaker.mods.embers;

import minetweaker.MineTweakerAPI;
import modtweaker.mods.actuallyadditions.handlers.AtomicReconstructor;
import modtweaker.mods.actuallyadditions.handlers.CoffeeMaker;
import modtweaker.mods.actuallyadditions.handlers.Compost;
import modtweaker.mods.actuallyadditions.handlers.Crusher;
import modtweaker.mods.actuallyadditions.handlers.Empowerer;
import modtweaker.mods.embers.handlers.Melter;
import modtweaker.mods.embers.handlers.Mixer;
import modtweaker.mods.embers.handlers.Stamper;

public class Embers {
    public Embers() {
    	MineTweakerAPI.registerClass(Melter.class);
    	MineTweakerAPI.registerClass(Stamper.class);
    	MineTweakerAPI.registerClass(Mixer.class);
    }
}

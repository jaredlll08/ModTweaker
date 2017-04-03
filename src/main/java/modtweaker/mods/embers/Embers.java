package modtweaker.mods.embers;

import minetweaker.MineTweakerAPI;
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

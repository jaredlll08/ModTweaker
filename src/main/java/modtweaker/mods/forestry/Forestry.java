package modtweaker.mods.forestry;

import minetweaker.MineTweakerAPI;
import modtweaker.mods.forestry.handlers.Bees;
import modtweaker.mods.forestry.handlers.Carpenter;
import modtweaker.mods.forestry.handlers.Fermenter;
import modtweaker.mods.forestry.handlers.Moistener;
import modtweaker.mods.forestry.handlers.Squeezer;
import modtweaker.mods.forestry.handlers.Still;

public class Forestry {
	public Forestry() {
		MineTweakerAPI.registerClass(Fermenter.class);
		MineTweakerAPI.registerClass(Bees.class);
		MineTweakerAPI.registerClass(Still.class);
		MineTweakerAPI.registerClass(Moistener.class);
		MineTweakerAPI.registerClass(Carpenter.class);
		MineTweakerAPI.registerClass(Squeezer.class);
		
		
	}
}

package modtweaker.mods.forestry;

import minetweaker.MineTweakerAPI;
import modtweaker.mods.forestry.handlers.Carpenter;
import modtweaker.mods.forestry.handlers.Centrifuge;
import modtweaker.mods.forestry.handlers.Fermenter;
import modtweaker.mods.forestry.handlers.Moistener;
import modtweaker.mods.forestry.handlers.Squeezer;
import modtweaker.mods.forestry.handlers.Still;
import modtweaker.mods.forestry.handlers.ThermionicFabricator;

public class Forestry {
	public Forestry() {
		MineTweakerAPI.registerClass(Fermenter.class);
		MineTweakerAPI.registerClass(Still.class);
		MineTweakerAPI.registerClass(Moistener.class);
		MineTweakerAPI.registerClass(Carpenter.class);
		MineTweakerAPI.registerClass(Squeezer.class);
		MineTweakerAPI.registerClass(Centrifuge.class);
		MineTweakerAPI.registerClass(ThermionicFabricator.class);
	}
}

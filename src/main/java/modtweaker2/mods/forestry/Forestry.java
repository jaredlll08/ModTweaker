package modtweaker2.mods.forestry;

import minetweaker.MineTweakerAPI;
import modtweaker2.mods.forestry.handlers.Carpenter;
import modtweaker2.mods.forestry.handlers.Centrifuge;
import modtweaker2.mods.forestry.handlers.Fermenter;
import modtweaker2.mods.forestry.handlers.Moistener;
import modtweaker2.mods.forestry.handlers.Squeezer;
import modtweaker2.mods.forestry.handlers.Still;
import modtweaker2.mods.forestry.handlers.ThermionicFabricator;

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

package modtweaker2.mods.auracascade;

import minetweaker.MineTweakerAPI;
import modtweaker2.mods.auracascade.aura.AuraBracketHandler;
import modtweaker2.mods.auracascade.handlers.Pylon;
import modtweaker2.mods.mekanism.gas.GasBracketHandler;
import pixlepix.auracascade.block.tile.AngelSteelTile;
import pixlepix.auracascade.data.recipe.PylonRecipeRegistry;

public class AuraCascade {
	public AuraCascade(){
		MineTweakerAPI.registerBracketHandler(new AuraBracketHandler());
		MineTweakerAPI.registerClass(Pylon.class);
	}

}

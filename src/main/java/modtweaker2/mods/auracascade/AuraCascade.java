package modtweaker2.mods.auracascade;

import minetweaker.MineTweakerAPI;
import modtweaker2.mods.auracascade.aura.AuraBracketHandler;
import modtweaker2.mods.auracascade.handlers.Pylon;

public class AuraCascade {
	public AuraCascade(){
		MineTweakerAPI.registerBracketHandler(new AuraBracketHandler());
		MineTweakerAPI.registerClass(Pylon.class);
	}

}

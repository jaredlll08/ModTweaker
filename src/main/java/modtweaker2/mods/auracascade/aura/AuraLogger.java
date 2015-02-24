package modtweaker2.mods.auracascade.aura;

import java.util.Set;

import pixlepix.auracascade.data.EnumAura;
import minetweaker.MineTweakerAPI;
import minetweaker.MineTweakerImplementationAPI;
import minetweaker.api.player.IPlayer;
import minetweaker.api.server.ICommandFunction;
import modtweaker2.ModTweaker2;
import net.minecraft.entity.EntityList;

public class AuraLogger implements ICommandFunction {

	@Override
	public void execute(String[] arguments, IPlayer player) {

		
		System.out.println("Aura Types: " + EnumAura.values().length);
		MineTweakerAPI.logCommand("Aura Types: " + EnumAura.values().length);
		for (EnumAura aura : EnumAura.values()) {
			ModTweaker2.logger.info("Aura Type: " + aura.name() + ".");
			MineTweakerAPI.logCommand("Aura Type: " + aura.name() + ".");

		}

		if (player != null) {
			player.sendChat(MineTweakerImplementationAPI.platform.getMessage("List generated; see minetweaker.log in your minecraft dir"));
		}
	}
}

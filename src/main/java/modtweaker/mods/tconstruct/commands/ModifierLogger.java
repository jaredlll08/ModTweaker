package modtweaker.mods.tconstruct.commands;

import minetweaker.MineTweakerAPI;
import minetweaker.api.player.IPlayer;
import minetweaker.api.server.ICommandFunction;
import slimeknights.tconstruct.library.modifiers.IModifier;

import java.util.Map;

import static modtweaker.helpers.LogHelper.logPrinted;
import static modtweaker.mods.tconstruct.TConstructHelper.modifiers;

public class ModifierLogger implements ICommandFunction{
	@Override
	public void execute(String[] arguments, IPlayer player) {
		MineTweakerAPI.logCommand(modifiers.size() + " Tinker's Construct modifiers:");
		for (Map.Entry<String, IModifier> entry : modifiers.entrySet()) {
			if (!entry.getKey().equals(""))
				MineTweakerAPI.logCommand(entry.getKey());
		}
		logPrinted(player);
	}
}

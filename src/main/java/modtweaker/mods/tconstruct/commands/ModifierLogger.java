package modtweaker.mods.tconstruct.commands;

import minetweaker.MineTweakerAPI;
import minetweaker.api.player.IPlayer;
import minetweaker.api.server.ICommandFunction;
import modtweaker.mods.tconstruct.TConstructHelper;
import slimeknights.tconstruct.library.modifiers.IModifier;

import static modtweaker.helpers.LogHelper.logPrinted;

public class ModifierLogger implements ICommandFunction{
	@Override
	public void execute(String[] arguments, IPlayer player) {
		MineTweakerAPI.logCommand(TConstructHelper.modifiers.size() + " Tinker's Construct modifiers:");
		for (IModifier modifier : TConstructHelper.modifiers) {
			if (!modifier.getIdentifier().equals(""))
				MineTweakerAPI.logCommand(modifier.getIdentifier());
		}
		logPrinted(player);
	}
}

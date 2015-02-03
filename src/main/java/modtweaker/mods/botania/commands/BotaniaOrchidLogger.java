package modtweaker.mods.botania.commands;

import java.util.Set;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.Botania;
import minetweaker.MineTweakerAPI;
import minetweaker.MineTweakerImplementationAPI;
import minetweaker.api.player.IPlayer;
import minetweaker.api.server.ICommandFunction;
import modtweaker.mods.botania.BotaniaHelper;
import net.minecraft.entity.EntityList;

public class BotaniaOrchidLogger implements ICommandFunction {

	@Override
	public void execute(String[] arguments, IPlayer player) {

		Set<String> keys = BotaniaAPI.oreWeights.keySet();
		System.out.println("Orchid Keys: " + keys.size());
		for (String str : BotaniaAPI.oreWeights.keySet()) {
			System.out.println("Orchid Key: " + str);
			MineTweakerAPI.logCommand(str + ": " + BotaniaAPI.oreWeights.get(str) + "\n");
		}

		if (player != null) {
			player.sendChat(MineTweakerImplementationAPI.platform.getMessage("List generated; see minetweaker.log in your minecraft dir"));
		}
	}
}

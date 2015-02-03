package modtweaker.commands;

import java.util.Set;

import cpw.mods.fml.common.registry.EntityRegistry;
import minetweaker.MineTweakerAPI;
import minetweaker.MineTweakerImplementationAPI;
import minetweaker.api.player.IPlayer;
import minetweaker.api.server.ICommandFunction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;

public class EntityMappingLogger implements ICommandFunction {

	@Override
	public void execute(String[] arguments, IPlayer player) {

		Set<Integer> keys = EntityList.stringToIDMapping.keySet();
		System.out.println("Mob Keys: " + keys.size());
		for (Integer key : keys) {
			System.out.println("Mob Key " + EntityList.getStringFromID(key) + " : " + key);
			MineTweakerAPI.logCommand("Mob Key " + EntityList.getStringFromID(key) + " : " + key);

		}

		if (player != null) {
			player.sendChat(MineTweakerImplementationAPI.platform.getMessage("List generated; see minetweaker.log in your minecraft dir"));
		}
	}
}

package modtweaker2;

import minetweaker.MineTweakerAPI;
import minetweaker.api.player.IPlayer;
import minetweaker.api.server.ICommandFunction;
import modtweaker2.commands.EntityMappingLogger;
import modtweaker2.mods.thaumcraft.commands.AspectLogger;
import modtweaker2.mods.thaumcraft.research.commands.ResearchLogger;
import modtweaker2.utils.TweakerPlugin;

public class Commands {

	public static void registerCommands() {
		if (MineTweakerAPI.server != null) {

			MineTweakerAPI.server.addMineTweakerCommand("tooltips", new String[] { "/minetweaker tooltips", "    Adds tooltips to all items ingame with their mt script name, press ctrl on an item to print to the log" }, new ICommandFunction() {
				@Override
				public void execute(String[] arguments, IPlayer player) {
					ClientEvents.active = !ClientEvents.active;
				}
			});

			MineTweakerAPI.server.addMineTweakerCommand("entities", new String[] { "/minetweaker entities", "    Outputs a list of entities class mapping keys and the entity IDs" }, new EntityMappingLogger());
			

			if (TweakerPlugin.isLoaded("Thaumcraft")) {
				MineTweakerAPI.server.addMineTweakerCommand("research", new String[] { "/minetweaker research", "/minetweaker research [CATEGORY]", "    Outputs a list of all category names in the game to the minetweaker log," + " or outputs a list of all research keys in a category to the log." }, new ResearchLogger());
				MineTweakerAPI.server.addMineTweakerCommand("aspectList", new String[] { "/minetweaker aspectList", "    Outputs a list of all aspects registered to entities and items" }, new AspectLogger());
			}
			
		}
	}

	public static void registerMineTweakerCommand(String commandName, String[] usages, ICommandFunction commandFunction) {
		MineTweakerAPI.server.addMineTweakerCommand(commandName, usages, commandFunction);
		ModTweaker2.logger.info("\nRegistering command " + commandName + ".");
	}
}

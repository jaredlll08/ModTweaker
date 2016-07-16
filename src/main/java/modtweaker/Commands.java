package modtweaker;

import minetweaker.MineTweakerAPI;
import minetweaker.api.player.IPlayer;
import minetweaker.api.server.ICommandFunction;
import modtweaker.commands.EntityMappingLogger;
import modtweaker.mods.randomthings.commands.RandomThingsLogger;
import modtweaker.utils.TweakerPlugin;

public class Commands {

    public static void registerCommands() {
        if (MineTweakerAPI.server != null) {

            MineTweakerAPI.server.addMineTweakerCommand("tooltips", new String[]{"/minetweaker tooltips", "    Adds tooltips to all items ingame with their mt script name, press ctrl on an item to print to the log"}, new ICommandFunction() {
                @Override
                public void execute(String[] arguments, IPlayer player) {
                    ClientEvents.active = !ClientEvents.active;
                }
            });

            MineTweakerAPI.server.addMineTweakerCommand("entities", new String[]{"/minetweaker entities", "    Outputs a list of entities class mapping keys and the entity IDs"}, new EntityMappingLogger());

            if (TweakerPlugin.isLoaded("randomthings")) {
                MineTweakerAPI.server.addMineTweakerCommand("randomthings", new String[]{"/minetweaker randomthings [HANDLER]", "    Outputs a list of all Random Things recipes."}, new RandomThingsLogger());
            }
        }
    }

    public static void registerMineTweakerCommand(String commandName, String[] usages, ICommandFunction commandFunction) {
        MineTweakerAPI.server.addMineTweakerCommand(commandName, usages, commandFunction);
        ModTweaker.logger.info("\nRegistering command " + commandName + ".");
    }
}

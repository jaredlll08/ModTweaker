package modtweaker;

import minetweaker.MineTweakerAPI;
import minetweaker.api.player.IPlayer;
import minetweaker.api.server.ICommandFunction;
import modtweaker.commands.EntityMappingLogger;
import modtweaker.mods.botania.commands.*;
import modtweaker.mods.botania.lexicon.commands.*;
import modtweaker.mods.chisel.commands.ChiselGroupLogger;
import modtweaker.mods.chisel.commands.ChiselVariationLogger;
import modtweaker.mods.randomthings.commands.RandomThingsLogger;
import modtweaker.mods.tconstruct.commands.MaterialLogger;
import modtweaker.mods.tconstruct.commands.ModifierLogger;
import modtweaker.mods.tconstruct.commands.TConstructLogger;

public class Commands {

    public static void registerCommands() {
        if(MineTweakerAPI.server != null) {

            MineTweakerAPI.server.addMineTweakerCommand("tooltips", new String[]{"/minetweaker tooltips", "    Adds tooltips to all items ingame with their mt script name, press ctrl on an item to print to the log"}, new ICommandFunction() {
                @Override
                public void execute(String[] arguments, IPlayer player) {
                    ClientEvents.active = !ClientEvents.active;
                }
            });

            MineTweakerAPI.server.addMineTweakerCommand("entities", new String[]{"/minetweaker entities", "    Outputs a list of entities class mapping keys and the entity IDs"}, new EntityMappingLogger());

            if(TweakerPlugin.isLoaded("randomthings")) {
                MineTweakerAPI.server.addMineTweakerCommand("randomthings", new String[]{"/minetweaker randomthings [HANDLER]", "    Outputs a list of all Random Things recipes."}, new RandomThingsLogger());
            }
            if(TweakerPlugin.isLoaded("tconstruct")) {
                MineTweakerAPI.server.addMineTweakerCommand("materials", new String[]{"/minetweaker materials", "    Outputs a list of all Tinker's Construct material names in the game to the minetweaker log"}, new MaterialLogger());
                MineTweakerAPI.server.addMineTweakerCommand("modifiers", new String[]{"/minetweaker modifiers", "    Outputs a list of all Tinker's Construct modifier names in the game to the minetweaker log"}, new ModifierLogger());
                MineTweakerAPI.server.addMineTweakerCommand("tconstruct", new String[]{"/minetweaker tconstruct [HANDLER]", "    Outputs a list of all Tinkers Construct recipes."}, new TConstructLogger());
            }
            if(TweakerPlugin.isLoaded("chisel")) {
                MineTweakerAPI.server.addMineTweakerCommand("chiselGroups", new String[]{"/minetweaker chiselGroups", "    Outputs a list of chisel groups"}, new ChiselGroupLogger());
                MineTweakerAPI.server.addMineTweakerCommand("chiselVariations", new String[]{"/minetweaker chiselVariations", "    Outputs a list of chisel variations"}, new ChiselVariationLogger());
            }
            if (TweakerPlugin.isLoaded("Botania")) {
                MineTweakerAPI.server.addMineTweakerCommand("lexiconCategories", new String[] { "/minetweaker lexiconCategories", "    Outputs a list of lexicon categories" }, new LexiconCategoryLogger());
                MineTweakerAPI.server.addMineTweakerCommand("lexiconPages", new String[] { "/minetweaker lexiconPages", "/minetweaker lexiconPages [ENTRY]", "    Outputs a list of lexicon pages for the entry" }, new LexiconPageLogger());
                MineTweakerAPI.server.addMineTweakerCommand("botaniaBrews", new String[] { "/minetweaker botaniaBrews", "    Outputs a list of keys for botania brews" }, new BotaniaBrewLogger());
                MineTweakerAPI.server.addMineTweakerCommand("lexiconKnowledgeTypes", new String[] { "/minetweaker lexiconKnowledgeTypes", "    Outputs a list of keys for lexicon knowledge types" }, new LexiconKnowledgeTypesLogger());
                MineTweakerAPI.server.addMineTweakerCommand("botaniaOrechid", new String[] { "/minetweaker botaniaOrechid", "    Outputs a list of keys for botania orechid weights" }, new BotaniaOrechidLogger());
                MineTweakerAPI.server.addMineTweakerCommand("botania", new String[] { "/minetweaker botania [HANDLER]", "    Outputs a list of all Botania recipes." }, new BotaniaLogger());
            }
        }
    }

    public static void registerMineTweakerCommand(String commandName, String[] usages, ICommandFunction commandFunction) {
        MineTweakerAPI.server.addMineTweakerCommand(commandName, usages, commandFunction);
        ModTweaker.logger.info("\nRegistering command " + commandName + ".");
    }
}

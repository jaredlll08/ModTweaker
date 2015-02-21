package modtweaker2;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import minetweaker.MineTweakerAPI;
import minetweaker.api.player.IPlayer;
import minetweaker.api.server.ICommandFunction;
import minetweaker.mc1710.MineTweakerMod;
import modtweaker2.commands.EntityMappingLogger;
import modtweaker2.mods.botania.commands.BotaniaBrewLogger;
import modtweaker2.mods.botania.commands.BotaniaOrechidLogger;
import modtweaker2.mods.botania.lexicon.commands.LexiconCategoryLogger;
import modtweaker2.mods.botania.lexicon.commands.LexiconEntryLogger;
import modtweaker2.mods.botania.lexicon.commands.LexiconKnowledgeTypesLogger;
import modtweaker2.mods.botania.lexicon.commands.LexiconPageLogger;
import modtweaker2.mods.chisel.commands.ChiselGroupLogger;
import modtweaker2.mods.chisel.commands.ChiselVariationLogger;
import modtweaker2.mods.mekanism.gas.GasLogger;
import modtweaker2.mods.tconstruct.MaterialLogger;
import modtweaker2.mods.thaumcraft.commands.AspectLogger;
import modtweaker2.mods.thaumcraft.research.commands.ResearchLogger;
import modtweaker2.util.TweakerPlugin;

public class Commands {
	
	public static void registerCommands() {
		
		registerMineTweakerCommand("tooltips", new String[] { "/minetweaker tooltips", "    Adds tooltips to all items ingame with their mt script name, press ctrl on an item to print to the log" }, new ICommandFunction() {
			@Override
			public void execute(String[] arguments, IPlayer player) {
				ClientEvents.active = !ClientEvents.active;
			}
		});

		registerMineTweakerCommand("entities", new String[] { "/minetweaker entities", "    Outputs a list of entities class mapping keys and the entity IDs" }, new EntityMappingLogger());

		if (TweakerPlugin.isLoaded("Mekanism")) {
			registerMineTweakerCommand("gases", new String[] { "/minetweaker gases", "    Outputs a list of all gas names in the game to the minetweaker log" }, new GasLogger());
		}

		if (TweakerPlugin.isLoaded("Thaumcraft")) {
			registerMineTweakerCommand("research", new String[] { "/minetweaker research", "/minetweaker research [CATEGORY]", "    Outputs a list of all category names in the game to the minetweaker log," + " or outputs a list of all research keys in a category to the log." }, new ResearchLogger());
			registerMineTweakerCommand("aspectList", new String[] { "/minetweaker aspectList", "    Outputs a list of all aspects registered to entities and items" }, new AspectLogger());
		}

		if (TweakerPlugin.isLoaded("TConstruct")) {
			registerMineTweakerCommand("materials", new String[] { "/minetweaker materials", "    Outputs a list of all Tinker's Construct material names in the game to the minetweaker log" }, new MaterialLogger());
		}

		if (TweakerPlugin.isLoaded("Botania")) {
			registerMineTweakerCommand("lexiconCategories", new String[] { "/minetweaker lexiconCategories", "    Outputs a list of lexicon categories" }, new LexiconCategoryLogger());
			registerMineTweakerCommand("lexiconEntries", new String[] { "/minetweaker lexiconEntries", "/minetweaker lexiconEntries [CATEGORY]", "    Outputs a list of lexicon entries" }, new LexiconEntryLogger());
			registerMineTweakerCommand("lexiconPages", new String[] { "/minetweaker lexiconPages", "/minetweaker lexiconPages [ENTRY]", "    Outputs a list of lexicon pages for the entry" }, new LexiconPageLogger());
			registerMineTweakerCommand("botaniaBrews", new String[] { "/minetweaker botaniaBrews", "    Outputs a list of keys for botania brews" }, new BotaniaBrewLogger());
			registerMineTweakerCommand("lexiconKnowledgeTypes", new String[] { "/minetweaker lexiconKnowledgeTypes", "    Outputs a list of keys for lexicon knowledge types" }, new LexiconKnowledgeTypesLogger());
			registerMineTweakerCommand("botaniaOrchid", new String[] { "/minetweaker botaniaOrechid", "    Outputs a list of keys for botania orechid weights" }, new BotaniaOrechidLogger());
		}

		if (TweakerPlugin.isLoaded("chisel")) {
			registerMineTweakerCommand("chiselGroups", new String[] { "/minetweaker chiselGroups", "    Outputs a list of chisel groups" }, new ChiselGroupLogger());
			registerMineTweakerCommand("chiselVariations", new String[] { "/minetweaker chiselVariations", "/minetweaker chiselVariations [GROUP]", "    Outputs a list of chisel variations" }, new ChiselVariationLogger());
		}

	}

	private static void registerMineTweakerCommand(String commandName, String[] usages, ICommandFunction commandFunction) {
		MineTweakerAPI.server.addMineTweakerCommand(commandName, usages, commandFunction);
		ModTweaker2.logger.info("Registering command " + commandName + ".");
	}
}

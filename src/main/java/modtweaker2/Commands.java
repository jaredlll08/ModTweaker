package modtweaker2;

import minetweaker.MineTweakerAPI;
import minetweaker.api.player.IPlayer;
import minetweaker.api.server.ICommandFunction;
import modtweaker2.commands.EntityMappingLogger;
import modtweaker2.mods.appeng.commands.AppliedEnergisticsLogger;
import modtweaker2.mods.auracascade.commands.AuraLogger;
import modtweaker2.mods.botania.commands.BotaniaBrewLogger;
import modtweaker2.mods.botania.commands.BotaniaLogger;
import modtweaker2.mods.botania.commands.BotaniaOrechidLogger;
import modtweaker2.mods.botania.lexicon.commands.LexiconCategoryLogger;
import modtweaker2.mods.botania.lexicon.commands.LexiconKnowledgeTypesLogger;
import modtweaker2.mods.botania.lexicon.commands.LexiconPageLogger;
import modtweaker2.mods.botanicaladdons.commands.BotanicalAddonsDendricLogger;
import modtweaker2.mods.chisel.commands.ChiselGroupLogger;
import modtweaker2.mods.chisel.commands.ChiselVariationLogger;
import modtweaker2.mods.exnihilo.commands.ExNihiloLogger;
import modtweaker2.mods.factorization.commands.FactorizationLogger;
import modtweaker2.mods.mekanism.commands.GasLogger;
import modtweaker2.mods.mekanism.commands.MekanismLogger;
import modtweaker2.mods.railcraft.commands.RailcraftLogger;
import modtweaker2.mods.tconstruct.commands.MaterialLogger;
import modtweaker2.mods.tconstruct.commands.ModifierLogger;
import modtweaker2.mods.tconstruct.commands.TConstructLogger;
import modtweaker2.mods.thaumcraft.commands.AspectLogger;
import modtweaker2.mods.thaumcraft.research.commands.ResearchLogger;
import modtweaker2.mods.thermalexpansion.commands.ThermalExpansionLogger;
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
			
			if (TweakerPlugin.isLoaded("appliedenergistics2-core")) {
				MineTweakerAPI.server.addMineTweakerCommand("appeng", new String[] {"/minetweaker appeng [HANDLER]", "    Outputs a list of all Applied Energistics 2 recipes."}, new AppliedEnergisticsLogger());
			}

			if (TweakerPlugin.isLoaded("aura")) {
				MineTweakerAPI.server.addMineTweakerCommand("auras", new String[] { "/minetweaker auras", "Outputs a list of Aura Types" }, new AuraLogger());
			}			

			if (TweakerPlugin.isLoaded("Botania")) {
				MineTweakerAPI.server.addMineTweakerCommand("lexiconCategories", new String[] { "/minetweaker lexiconCategories", "    Outputs a list of lexicon categories" }, new LexiconCategoryLogger());
				MineTweakerAPI.server.addMineTweakerCommand("lexiconPages", new String[] { "/minetweaker lexiconPages", "/minetweaker lexiconPages [ENTRY]", "    Outputs a list of lexicon pages for the entry" }, new LexiconPageLogger());
				MineTweakerAPI.server.addMineTweakerCommand("botaniaBrews", new String[] { "/minetweaker botaniaBrews", "    Outputs a list of keys for botania brews" }, new BotaniaBrewLogger());
				MineTweakerAPI.server.addMineTweakerCommand("lexiconKnowledgeTypes", new String[] { "/minetweaker lexiconKnowledgeTypes", "    Outputs a list of keys for lexicon knowledge types" }, new LexiconKnowledgeTypesLogger());
				MineTweakerAPI.server.addMineTweakerCommand("botaniaOrechid", new String[] { "/minetweaker botaniaOrechid", "    Outputs a list of keys for botania orechid weights" }, new BotaniaOrechidLogger());
				MineTweakerAPI.server.addMineTweakerCommand("botania", new String[] { "/minetweaker botania [HANDLER]", "    Outputs a list of all Botania recipes." }, new BotaniaLogger());
			}

			if (TweakerPlugin.isLoaded("shadowfox_botany")) {
				MineTweakerAPI.server.addMineTweakerCommand("treeCrafting", new String[] { "/minetweaker treeCrafting", "    Outputs a list of all Botanical Addons Tree Crafting recipes." }, new BotanicalAddonsDendricLogger());
			}

			if (TweakerPlugin.isLoaded("chisel")) {
				MineTweakerAPI.server.addMineTweakerCommand("chiselGroups", new String[] { "/minetweaker chiselGroups", "    Outputs a list of chisel groups" }, new ChiselGroupLogger());
				MineTweakerAPI.server.addMineTweakerCommand("chiselVariations", new String[] { "/minetweaker chiselVariations", "/minetweaker chiselVariations [GROUP]", "    Outputs a list of chisel variations" }, new ChiselVariationLogger());
			}
			
			if (TweakerPlugin.isLoaded("exnihilo")) {
			    MineTweakerAPI.server.addMineTweakerCommand("exnihilo", new String[] {"/minetweaker exnihilo [HANDLER]", "    Outputs a list of all ExNihilo recipes."}, new ExNihiloLogger());
			}			
			
			if (TweakerPlugin.isLoaded("factorization")) {
			    MineTweakerAPI.server.addMineTweakerCommand("factorization", new String[] {"/minetweaker factorization [HANDLER]", "    Outputs a list of all Factorization recipes."}, new FactorizationLogger());
			}

			if (TweakerPlugin.isLoaded("Mekanism")) {
				MineTweakerAPI.server.addMineTweakerCommand("gases", new String[] { "/minetweaker gases", "    Outputs a list of all gas names in the game to the minetweaker log" }, new GasLogger());
				MineTweakerAPI.server.addMineTweakerCommand("mekanism", new String[] { "/minetweaker mekanism [HANDLER]", "    Outputs a list of all Mekanism recipes." }, new MekanismLogger());
			}
			
			if (TweakerPlugin.isLoaded("Railcraft")) {
			    MineTweakerAPI.server.addMineTweakerCommand("railcraft", new String[] {"/minetweaker railcraft [HANDLER]", "    Outputs a list of all Railcraft recipes."}, new RailcraftLogger());
			}			

			if (TweakerPlugin.isLoaded("TConstruct")) {
				MineTweakerAPI.server.addMineTweakerCommand("materials", new String[] { "/minetweaker materials", "    Outputs a list of all Tinker's Construct material names in the game to the minetweaker log" }, new MaterialLogger());
				MineTweakerAPI.server.addMineTweakerCommand("modifiers", new String[] { "/minetweaker modifiers", "    Outputs a list of all Tinker's Construct modifier names in the game to the minetweaker log" }, new ModifierLogger());
				MineTweakerAPI.server.addMineTweakerCommand("tconstruct", new String[]{ "/minetweaker tconstruct [HANDLER]", "    Outputs a list of all Tinkers Construct recipes."}, new TConstructLogger());
			}

			if (TweakerPlugin.isLoaded("Thaumcraft")) {
				MineTweakerAPI.server.addMineTweakerCommand("research", new String[] { "/minetweaker research", "/minetweaker research [CATEGORY]", "    Outputs a list of all category names in the game to the minetweaker log," + " or outputs a list of all research keys in a category to the log." }, new ResearchLogger());
				MineTweakerAPI.server.addMineTweakerCommand("aspectList", new String[] { "/minetweaker aspectList", "    Outputs a list of all aspects registered to entities and items" }, new AspectLogger());
			}
			
			if (TweakerPlugin.isLoaded("ThermalExpansion")) {
			    MineTweakerAPI.server.addMineTweakerCommand("thermalexpansion", new String[] {"/minetweaker thermalexpansion [HANDLER]", "    Outputs a list of all Thermal Expansion recipes."}, new ThermalExpansionLogger());
			}
		}
	}

	public static void registerMineTweakerCommand(String commandName, String[] usages, ICommandFunction commandFunction) {
		MineTweakerAPI.server.addMineTweakerCommand(commandName, usages, commandFunction);
		ModTweaker2.logger.info("\nRegistering command " + commandName + ".");
	}
}

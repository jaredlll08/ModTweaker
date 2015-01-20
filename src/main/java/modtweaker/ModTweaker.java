package modtweaker;

import java.io.File;

import minetweaker.MineTweakerAPI;
import minetweaker.api.minecraft.MineTweakerMC;
import minetweaker.api.player.IPlayer;
import minetweaker.api.server.ICommandFunction;
import minetweaker.mc1710.MineTweakerMod;
import modtweaker.mods.botania.Botania;
import modtweaker.mods.botania.lexicon.commands.LexiconCategoryLogger;
import modtweaker.mods.botania.lexicon.commands.LexiconEntryLogger;
import modtweaker.mods.botania.lexicon.commands.LexiconPageLogger;
import modtweaker.mods.exnihilo.ExNihilo;
import modtweaker.mods.extendedworkbench.ExtendedWorkbench;
import modtweaker.mods.factorization.Factorization;
import modtweaker.mods.forestry.Forestry;
import modtweaker.mods.fsp.Steamcraft;
import modtweaker.mods.hee.HardcoreEnderExpansion;
import modtweaker.mods.imc.handler.Message;
import modtweaker.mods.mariculture.Mariculture;
import modtweaker.mods.mekanism.Mekanism;
import modtweaker.mods.mekanism.gas.GasLogger;
import modtweaker.mods.metallurgy.Metallurgy;
import modtweaker.mods.pneumaticcraft.PneumaticCraft;
import modtweaker.mods.railcraft.Railcraft;
import modtweaker.mods.tconstruct.MaterialLogger;
import modtweaker.mods.tconstruct.TConstruct;
import modtweaker.mods.thaumcraft.Thaumcraft;
import modtweaker.mods.thaumcraft.research.ResearchLogger;
import modtweaker.mods.thermalexpansion.ThermalExpansion;
import modtweaker.util.TweakerPlugin;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms.IMCEvent;
import cpw.mods.fml.common.event.FMLInterModComms.IMCMessage;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = ModProps.modid, name = ModProps.name, dependencies = ModProps.dependencies)
public class ModTweaker {

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		File file = new File("scripts");

		if (!file.exists()) {
			file.mkdir();
		}
		new Message(file.getPath());
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		TweakerPlugin.register("Botania", Botania.class);
		TweakerPlugin.register("exnihilo", ExNihilo.class);
		TweakerPlugin.register("extendedWorkbench", ExtendedWorkbench.class);
		TweakerPlugin.register("factorization", Factorization.class);
		TweakerPlugin.register("HardcoreEnderExpansion", HardcoreEnderExpansion.class);
		TweakerPlugin.register("Mariculture", Mariculture.class);
		TweakerPlugin.register("Mekanism", Mekanism.class);
		TweakerPlugin.register("Metallurgy", Metallurgy.class);
		TweakerPlugin.register("PneumaticCraft", PneumaticCraft.class);
		TweakerPlugin.register("Railcraft", Railcraft.class);
		TweakerPlugin.register("Steamcraft", Steamcraft.class);
		TweakerPlugin.register("TConstruct", TConstruct.class);
		TweakerPlugin.register("Thaumcraft", Thaumcraft.class);
		TweakerPlugin.register("ThermalExpansion", ThermalExpansion.class);
		TweakerPlugin.register("Forestry", Forestry.class);

		if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
			MinecraftForge.EVENT_BUS.register(new ClientEvents());
		}
	}

	@EventHandler
	public void onServerStart(FMLServerStartingEvent event) {
		MineTweakerAPI.server.addMineTweakerCommand("tooltips", new String[] { "/minetweaker tooltips", "    Adds tooltips to all items ingame with their mt script name, press ctrl on an item to print to the log" }, new ICommandFunction() {
			@Override
			public void execute(String[] arguments, IPlayer player) {
				ClientEvents.active = !ClientEvents.active;
			}
		});

		if (TweakerPlugin.isLoaded("Mekanism")) {
			MineTweakerAPI.server.addMineTweakerCommand("gases", new String[] { "/minetweaker gases", "    Outputs a list of all gas names in the game to the minetweaker log" }, new GasLogger());
		}

		if (TweakerPlugin.isLoaded("Thaumcraft")) {
			MineTweakerAPI.server.addMineTweakerCommand("research", new String[] { "/minetweaker research", "/minetweaker research [CATEGORY]", "    Outputs a list of all category names in the game to the minetweaker log," + " or outputs a list of all research keys in a category to the log." }, new ResearchLogger());
		}

		if (TweakerPlugin.isLoaded("TConstruct")) {
			MineTweakerAPI.server.addMineTweakerCommand("materials", new String[] { "/minetweaker materials", "    Outputs a list of all Tinker's Construct material names in the game to the minetweaker log" }, new MaterialLogger());
		}

		if (TweakerPlugin.isLoaded("Botania")) {
			MineTweakerAPI.server.addMineTweakerCommand("lexiconCategories", new String[] { "/minetweaker lexiconCategories", "    Outputs a list of lexicon categories" }, new LexiconCategoryLogger());
			MineTweakerAPI.server.addMineTweakerCommand("lexiconEntries", new String[] { "/minetweaker lexiconEntries", "/minetweaker lexiconEntries [CATEGORY]", "    Outputs a list of lexicon entries" }, new LexiconEntryLogger());
			MineTweakerAPI.server.addMineTweakerCommand("lexiconPages", new String[] { "/minetweaker lexiconPages", "/minetweaker lexiconPages [ENTRY]", "    Outputs a list of lexicon pages for the entry" }, new LexiconPageLogger());
		}

	}
}

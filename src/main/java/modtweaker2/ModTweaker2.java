package modtweaker2;

import java.io.File;

import minetweaker.MineTweakerImplementationAPI;
import minetweaker.MineTweakerImplementationAPI.ReloadEvent;
import minetweaker.util.IEventHandler;
import modtweaker2.mods.appeng.AppliedEnergistics;
import modtweaker2.mods.auracascade.AuraCascade;
import modtweaker2.mods.botania.Botania;
import modtweaker2.mods.chisel.Chisel;
import modtweaker2.mods.exnihilo.ExNihilo;
import modtweaker2.mods.extendedworkbench.ExtendedWorkbench;
import modtweaker2.mods.factorization.Factorization;
import modtweaker2.mods.forestry.Forestry;
import modtweaker2.mods.fsp.Steamcraft;
import modtweaker2.mods.hee.HardcoreEnderExpansion;
import modtweaker2.mods.imc.handler.Message;
import modtweaker2.mods.mariculture.Mariculture;
import modtweaker2.mods.mekanism.Mekanism;
import modtweaker2.mods.metallurgy.Metallurgy;
import modtweaker2.mods.pneumaticcraft.PneumaticCraft;
import modtweaker2.mods.railcraft.Railcraft;
import modtweaker2.mods.tconstruct.TConstruct;
import modtweaker2.mods.thaumcraft.Thaumcraft;
import modtweaker2.mods.thermalexpansion.ThermalExpansion;
import modtweaker2.utils.TweakerPlugin;
import net.minecraftforge.common.MinecraftForge;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = ModProps.modid, version = ModProps.version, dependencies = ModProps.dependencies)
public class ModTweaker2 {

	public static Logger logger = LogManager.getLogger(ModProps.modid);

	@Instance(ModProps.modid)
	public ModTweaker2 instance;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger.info("Starting PreInitialization for " + ModProps.modid);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		logger.info("Starting Initialization for " + ModProps.modid);
		TweakerPlugin.register("appliedenergistics2-core", AppliedEnergistics.class);
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
		TweakerPlugin.register("chisel", Chisel.class);
		TweakerPlugin.register("aura", AuraCascade.class);

		if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
			MinecraftForge.EVENT_BUS.register(new ClientEvents());
		}
//		MineTweakerImplementationAPI.onReloadEvent(new IEventHandler<MineTweakerImplementationAPI.ReloadEvent>() {
//
//			@Override
//			public void handle(ReloadEvent event) {
//				Commands.registerCommands();
//			}
//		});
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		logger.info("Starting PostInitialization for " + ModProps.modid);

	}

	@EventHandler
	public void serverStart(FMLServerStartingEvent event) {
		logger.info("Starting ServerStart for " + ModProps.modid);
		Commands.registerCommands();

	}
}

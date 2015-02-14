package modtweaker;

import java.io.File;

import modtweaker.mods.appeng.AppliedEnergistics;
import modtweaker.mods.botania.Botania;
import modtweaker.mods.chisel.Chisel;
import modtweaker.mods.exnihilo.ExNihilo;
import modtweaker.mods.extendedworkbench.ExtendedWorkbench;
import modtweaker.mods.factorization.Factorization;
import modtweaker.mods.forestry.Forestry;
import modtweaker.mods.fsp.Steamcraft;
import modtweaker.mods.hee.HardcoreEnderExpansion;
import modtweaker.mods.imc.handler.Message;
import modtweaker.mods.mariculture.Mariculture;
import modtweaker.mods.mekanism.Mekanism;
import modtweaker.mods.metallurgy.Metallurgy;
import modtweaker.mods.pneumaticcraft.PneumaticCraft;
import modtweaker.mods.railcraft.Railcraft;
import modtweaker.mods.tconstruct.TConstruct;
import modtweaker.mods.thaumcraft.Thaumcraft;
import modtweaker.mods.thermalexpansion.ThermalExpansion;
import modtweaker.util.TweakerPlugin;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
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
		TweakerPlugin.register("chisel", Chisel.class);
		TweakerPlugin.register("appliedenergistics2-core", AppliedEnergistics.class);
		

		if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
			MinecraftForge.EVENT_BUS.register(new ClientEvents());
		}
	}

	@EventHandler
	public void onServerStart(FMLServerStartingEvent event) {
		Commands.registerCommands();
	}
}

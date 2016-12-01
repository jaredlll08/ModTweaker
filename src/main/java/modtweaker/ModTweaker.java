package modtweaker;

import com.blamejared.ctgui.api.GuiRegistry;
import minetweaker.MineTweakerImplementationAPI;
import minetweaker.runtime.providers.ScriptProviderDirectory;
import modtweaker.mods.actuallyadditions.ActuallyAdditions;
import modtweaker.mods.appeng.AppliedEnergistics;
import modtweaker.mods.bloodmagic.BloodMagic;
import modtweaker.mods.botania.Botania;
import modtweaker.mods.chisel.Chisel;
import modtweaker.mods.extrautils.ExtraUtilities;
import modtweaker.mods.forestry.Forestry;
import modtweaker.mods.randomthings.RandomThings;
import modtweaker.mods.refinedstorage.RefinedStorage;
import modtweaker.mods.tconstruct.TConstruct;
import modtweaker.mods.embers.Embers;
import modtweaker.proxy.CommonProxy;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.Mod.*;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.*;

import java.io.File;
import java.util.Arrays;

@Mod(modid = ModProps.modid, version = ModProps.version, dependencies = ModProps.dependencies)
public class ModTweaker {

    public static Logger logger = LogManager.getLogger(ModProps.modid);

    @SidedProxy(clientSide = "modtweaker.proxy.ClientProxy", serverSide = "modtweaker.proxy.CommonProxy")
    public static CommonProxy proxy;

    @Instance(ModProps.modid)
    public ModTweaker instance;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger.info("Starting PreInitialization for " + ModProps.modid);
        GuiRegistry.registerGui(Arrays.asList(3), new GuiHandlerMods());
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        logger.info("Starting Initialization for " + ModProps.modid);
        TweakerPlugin.register("forestry", Forestry.class);
        TweakerPlugin.register("tconstruct", TConstruct.class);
        TweakerPlugin.register("randomthings", RandomThings.class);
        TweakerPlugin.register("Botania", Botania.class);
        TweakerPlugin.register("chisel", Chisel.class);
        TweakerPlugin.register("BloodMagic", BloodMagic.class);
        TweakerPlugin.register("actuallyadditions", ActuallyAdditions.class);
        TweakerPlugin.register("refinedstorage", RefinedStorage.class);
        TweakerPlugin.register("embers", Embers.class);
	
		TweakerPlugin.register("ExtraUtils2", ExtraUtilities.class);
		TweakerPlugin.register("appliedenergistics2", AppliedEnergistics.class);
		
        if(FMLCommonHandler.instance().getSide() == Side.CLIENT) {
            MinecraftForge.EVENT_BUS.register(new ClientEvents());
        }

        MineTweakerImplementationAPI.onReloadEvent(event1 -> proxy.registerCommands());

        File scripts = new File("scripts");
        if(!scripts.exists()) {
            scripts.mkdir();
        }
        MineTweakerImplementationAPI.setScriptProvider(new ScriptProviderDirectory(scripts));
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        logger.info("Starting PostInitialization for " + ModProps.modid);
    }

    @EventHandler
    public void serverStart(FMLServerStartingEvent event) {
        logger.info("Starting ServerStart for " + ModProps.modid);
        proxy.registerCommands();

    }
}
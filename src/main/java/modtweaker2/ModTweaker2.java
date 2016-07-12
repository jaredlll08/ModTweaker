package modtweaker2;

import minetweaker.MineTweakerImplementationAPI;
import minetweaker.MineTweakerImplementationAPI.ReloadEvent;
import minetweaker.runtime.providers.ScriptProviderDirectory;
import minetweaker.util.IEventHandler;
import modtweaker2.mods.forestry.Forestry;
import modtweaker2.mods.tconstruct.TConstruct;
import modtweaker2.proxy.CommonProxy;
import modtweaker2.utils.TweakerPlugin;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

@Mod(modid = ModProps.modid, version = ModProps.version, dependencies = ModProps.dependencies)
public class ModTweaker2 {

    public static Logger logger = LogManager.getLogger(ModProps.modid);

    @SidedProxy(clientSide = "modtweaker2.proxy.ClientProxy", serverSide = "modtweaker2.proxy.CommonProxy")
    public static CommonProxy proxy;

    @Instance(ModProps.modid)
    public ModTweaker2 instance;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger.info("Starting PreInitialization for " + ModProps.modid);
    }


    @EventHandler
    public void init(FMLInitializationEvent event) {
        logger.info("Starting Initialization for " + ModProps.modid);
        TweakerPlugin.register("forestry", Forestry.class);
        TweakerPlugin.register("tconstruct", TConstruct.class);

        if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
            MinecraftForge.EVENT_BUS.register(new ClientEvents());
        }

        MineTweakerImplementationAPI.onReloadEvent(new IEventHandler<MineTweakerImplementationAPI.ReloadEvent>() {

            @Override
            public void handle(ReloadEvent event) {
                proxy.registerCommands();

            }
        });

        File scripts = new File("scripts");
        if (!scripts.exists()) {
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

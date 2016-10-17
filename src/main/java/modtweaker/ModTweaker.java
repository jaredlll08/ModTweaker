package modtweaker;

import minetweaker.MineTweakerImplementationAPI;
import minetweaker.MineTweakerImplementationAPI.ReloadEvent;
import minetweaker.runtime.providers.ScriptProviderDirectory;
import minetweaker.util.IEventHandler;
import modtweaker.mods.bloodmagic.BloodMagic;
import modtweaker.mods.botania.Botania;
import modtweaker.mods.chisel.Chisel;
import modtweaker.mods.forestry.Forestry;
import modtweaker.mods.randomthings.RandomThings;
import modtweaker.mods.tconstruct.TConstruct;
import modtweaker.proxy.CommonProxy;
import modtweaker.utils.TweakerPlugin;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

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

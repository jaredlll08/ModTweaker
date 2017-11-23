package com.blamejared;

import static com.blamejared.reference.Reference.DEPENDENCIES;
import static com.blamejared.reference.Reference.MODID;
import static com.blamejared.reference.Reference.NAME;
import static com.blamejared.reference.Reference.VERSION;

import java.util.LinkedList;
import java.util.List;

import com.blamejared.compat.botania.Botania;
import com.blamejared.compat.tconstruct.TConstruct;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = MODID, name = NAME, version = VERSION, dependencies = DEPENDENCIES)
public class ModTweaker {
    
    public static final List<IAction> LATE_REMOVALS = new LinkedList<>();
    public static final List<IAction> LATE_ADDITIONS = new LinkedList<>();
    
    
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
    }
    
    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
    }
    
    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
    
    }
    
    @Mod.EventHandler
    public void loadComplete(FMLLoadCompleteEvent event) {
        try {
            LATE_REMOVALS.forEach(CraftTweakerAPI::apply);
            LATE_ADDITIONS.forEach(CraftTweakerAPI::apply);
        } catch(Exception e) {
            e.printStackTrace();
            CraftTweakerAPI.logError("Error while applying actions", e);
        }
    }
    
    @Mod.EventHandler
    public void onFMLServerStarting(FMLServerStartingEvent event) {
        if(Loader.isModLoaded("tconstruct")) {
            TConstruct.registerCommands();
        }
        if(Loader.isModLoaded("botania")) {
        	Botania.registerCommands();
        }
    }
}

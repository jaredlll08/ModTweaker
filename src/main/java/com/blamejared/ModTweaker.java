package com.blamejared;

import com.blamejared.compat.botania.Botania;
import com.blamejared.compat.chisel.Chisel;
import com.blamejared.compat.inspirations.Inspirations;
import com.blamejared.compat.tconstruct.TConstruct;
import com.blamejared.compat.thaumcraft.handlers.ThaumCraft;
import crafttweaker.*;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.event.*;

import java.util.*;

import static com.blamejared.reference.Reference.*;

@Mod(modid = MODID, name = NAME, version = VERSION, dependencies = DEPENDENCIES)
public class ModTweaker {
    
    public static final List<IAction> LATE_REMOVALS = new LinkedList<>();
    public static final List<IAction> LATE_ADDITIONS = new LinkedList<>();
    
    
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        if(Loader.isModLoaded("thaumcraft")) {
            ThaumCraft.preInit();
        }
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
        LATE_REMOVALS.clear();
        LATE_ADDITIONS.clear();
    }
    
    @Mod.EventHandler
    public void onFMLServerStarting(FMLServerStartingEvent event) {
        if(Loader.isModLoaded("tconstruct")) {
            TConstruct.registerCommands();
        }
        if(Loader.isModLoaded("botania")) {
            Botania.registerCommands();
        }
        if(Loader.isModLoaded("chisel")) {
            Chisel.registerCommands();
        }
        if(Loader.isModLoaded("inspirations")) {
            Inspirations.registerCommands();
        }
        
        if(Loader.isModLoaded("thaumcraft")) {
            ThaumCraft.registerCommands();
        }
    }
}

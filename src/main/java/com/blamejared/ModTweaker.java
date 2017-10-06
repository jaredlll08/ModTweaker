package com.blamejared;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static com.blamejared.reference.Reference.*;

@Mod(modid = MODID, name = NAME, version = VERSION, dependencies = DEPENDENCIES)
public class ModTweaker {
    
    public static final List<IAction> LATE_REMOVALS = new LinkedList<>();
    public static final List<IAction> LATE_ADDITIONS = new LinkedList<>();

    public static final List<IAction> SEMI_LATE_STUFF = new LinkedList<>();

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) throws IOException {
    }
    
    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        try {
            SEMI_LATE_STUFF.forEach(CraftTweakerAPI::apply);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
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
        }
    }
}

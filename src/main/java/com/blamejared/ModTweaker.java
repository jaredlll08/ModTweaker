package com.blamejared;

import com.blamejared.api.annotations.Handler;
import crafttweaker.*;
import de.ellpeck.actuallyadditions.mod.ActuallyAdditions;
import mezz.jei.JustEnoughItems;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.event.*;

import java.io.IOException;
import java.util.*;

import static com.blamejared.reference.Reference.*;

@Mod(modid = MODID, name = NAME, version = VERSION, dependencies = DEPENDENCIES)
public class ModTweaker {
    
    public static final List<IAction> LATE_REMOVALS = new LinkedList<>();
    public static final List<IAction> LATE_ADDITIONS = new LinkedList<>();
    
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) throws IOException {
        e.getAsmData().getAll(Handler.class.getCanonicalName()).forEach(data -> {
            try {
                Class clazz = Class.forName(data.getClassName(), false, getClass().getClassLoader());
                if(Loader.isModLoaded(((Handler) clazz.getAnnotation(Handler.class)).value())) {
                    CraftTweakerAPI.registerClass(clazz);
                }
            } catch(ClassNotFoundException e1) {
                e1.printStackTrace();
            }
        });
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
        }
    }
}

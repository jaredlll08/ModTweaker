package com.blamejared;

import com.blamejared.api.annotations.Handler;
import minetweaker.MineTweakerAPI;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.event.*;

import static com.blamejared.reference.Reference.*;

@Mod(modid = MODID, name = NAME, version = VERSION, dependencies = DEPENDENCIES)
public class ModTweaker {
    
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        e.getAsmData().getAll(Handler.class.getCanonicalName()).forEach(data -> {
            try {
                Class clazz = Class.forName(data.getClassName());
                Handler handl = (Handler) clazz.getAnnotation(Handler.class);
                if(Loader.isModLoaded(handl.modid())) {
                    MineTweakerAPI.registerClass(clazz);
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
}

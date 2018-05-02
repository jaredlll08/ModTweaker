package com.blamejared.compat.thaumcraft.handlers;

import com.blamejared.compat.thaumcraft.handlers.brackets.BracketHandlerAspect;
import crafttweaker.zenscript.GlobalRegistry;

public class ThaumCraft {
    
    
    public static void preInit(){
        GlobalRegistry.registerBracketHandler(new BracketHandlerAspect());
    }
}

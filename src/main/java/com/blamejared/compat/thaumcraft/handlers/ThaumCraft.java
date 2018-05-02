package com.blamejared.compat.thaumcraft.handlers;

import com.blamejared.compat.thaumcraft.handlers.brackets.BracketHandlerAspect;
import crafttweaker.annotations.ModOnly;
import crafttweaker.zenscript.GlobalRegistry;

public class ThaumCraft {

	@ModOnly("thaumcraft")
    public static void preInit(){
        GlobalRegistry.registerBracketHandler(new BracketHandlerAspect());
    }
}

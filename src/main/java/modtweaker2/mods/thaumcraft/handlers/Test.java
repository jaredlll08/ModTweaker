package modtweaker2.mods.thaumcraft.handlers;

import minetweaker.api.item.IIngredient;
import modtweaker2.mods.thaumcraft.aspect.IAspectStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.thaumcraft.Test")
public class Test {
    @ZenMethod
    public static void getAspect(IAspectStack aspectStack, IIngredient ingredient) {
        System.out.println(aspectStack.getName());
        
    }
}

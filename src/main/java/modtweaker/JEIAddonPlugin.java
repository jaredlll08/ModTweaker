package modtweaker;

import mezz.jei.api.*;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.ingredients.*;
import minetweaker.MineTweakerAPI;
import minetweaker.api.compat.DummyJEIRecipeRegistry;
import minetweaker.mods.jei.JEIRecipeRegistry;
import net.minecraft.util.ResourceLocation;


@mezz.jei.api.JEIPlugin
public class JEIAddonPlugin implements IModPlugin {

    public static IJeiHelpers jeiHelpers;
    public static IIngredientRegistry itemRegistry;
    public static IRecipeRegistry recipeRegistry;
    
    public static IDrawable castingTable;
    public static IDrawable castingBasin;

    @Override
    public void registerItemSubtypes(ISubtypeRegistry subtypeRegistry) {

    }

    @Override
    public void registerIngredients(IModIngredientRegistration registry) {

    }

    @Override
    public void register(IModRegistry registry) {
        jeiHelpers = registry.getJeiHelpers();
        itemRegistry = registry.getIngredientRegistry();
        ResourceLocation rec =  new ResourceLocation("tconstruct", "textures/gui/jei/casting.png");
        this.castingTable = jeiHelpers.getGuiHelper().createDrawable(rec, 141, 0, 16, 16);
        this.castingBasin = jeiHelpers.getGuiHelper().createDrawable(rec, 141, 16, 16, 16);
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime iJeiRuntime) {
        recipeRegistry = iJeiRuntime.getRecipeRegistry();
        
    }


}

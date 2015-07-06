package modtweaker2.mods.thermalexpansion;

import static modtweaker2.helpers.ReflectionHelper.getConstructor;

import java.lang.reflect.Constructor;

import modtweaker2.helpers.LogHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import cofh.thermalexpansion.util.crafting.CrucibleManager.RecipeCrucible;
import cofh.thermalexpansion.util.crafting.FurnaceManager.RecipeFurnace;
import cofh.thermalexpansion.util.crafting.PulverizerManager.RecipePulverizer;
import cofh.thermalexpansion.util.crafting.SawmillManager.RecipeSawmill;
import cofh.thermalexpansion.util.crafting.SmelterManager.RecipeSmelter;
import cofh.thermalexpansion.util.crafting.TransposerManager.RecipeTransposer;


@SuppressWarnings("unchecked")
public class ThermalHelper {
    public static Constructor<RecipeCrucible> crucibleRecipe;
    public static Constructor<RecipeFurnace> furanceRecipe;
    public static Constructor<RecipePulverizer> pulverizerRecipe;
    public static Constructor<RecipeSawmill> sawmillRecipe;
    public static Constructor<RecipeSmelter> smelterRecipe;
    public static Constructor<RecipeTransposer> transposerRecipe;
    
    static {
    	try {
    	    crucibleRecipe   = getConstructor("cofh.thermalexpansion.util.crafting.CrucibleManager$RecipeCrucible", ItemStack.class, FluidStack.class, int.class);
    	    furanceRecipe    = getConstructor("cofh.thermalexpansion.util.crafting.FurnaceManager$RecipeFurnace", ItemStack.class, ItemStack.class, int.class);
    	    pulverizerRecipe = getConstructor("cofh.thermalexpansion.util.crafting.PulverizerManager$RecipePulverizer", ItemStack.class, ItemStack.class, ItemStack.class, int.class, int.class);
    	    sawmillRecipe    = getConstructor("cofh.thermalexpansion.util.crafting.SawmillManager$RecipeSawmill", ItemStack.class, ItemStack.class, ItemStack.class, int.class, int.class);
            smelterRecipe    = getConstructor("cofh.thermalexpansion.util.crafting.SmelterManager$RecipeSmelter", ItemStack.class, ItemStack.class, ItemStack.class, ItemStack.class, int.class, int.class);
            transposerRecipe = getConstructor("cofh.thermalexpansion.util.crafting.TransposerManager$RecipeTransposer", ItemStack.class, ItemStack.class, FluidStack.class, int.class, int.class);
        } catch (Exception e) { LogHelper.logError("Exception getting constructor for Thermal Expansion recipes!", e); }
    }

    public static <T> T getTERecipe(Constructor<T> constructor, Object... items) {
        try {
            return constructor.newInstance(items);
        } catch (Exception e) { LogHelper.logError("Exception creating instance of Thermal Expansion recipe!", e); }

        return null;
    }
}

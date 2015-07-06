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

public class ThermalHelper {
    public static Constructor<RecipeCrucible> crucibleRecipe;
    public static Constructor<RecipeFurnace> furanceRecipe;
    public static Constructor<RecipePulverizer> pulverizerRecipe;
    public static Constructor<RecipeSawmill> sawmillRecipe;
    public static Constructor<RecipeSmelter> smelterRecipe;
    public static Constructor<RecipeTransposer> transposerRecipe;
    
    static {
    	try {
    	    crucibleRecipe   = getConstructor(RecipeCrucible.class, ItemStack.class, FluidStack.class, int.class);
    	    furanceRecipe    = getConstructor(RecipeFurnace.class, ItemStack.class, ItemStack.class, int.class);
    	    pulverizerRecipe = getConstructor(RecipePulverizer.class, ItemStack.class, ItemStack.class, ItemStack.class, int.class, int.class);
    	    sawmillRecipe    = getConstructor(RecipeSawmill.class, ItemStack.class, ItemStack.class, ItemStack.class, int.class, int.class);
            smelterRecipe    = getConstructor(RecipeSmelter.class, ItemStack.class, ItemStack.class, ItemStack.class, ItemStack.class, int.class, int.class);
            transposerRecipe = getConstructor(RecipeTransposer.class, ItemStack.class, ItemStack.class, FluidStack.class, int.class, int.class);
        } catch (Exception e) { LogHelper.logError("Exception getting constructor for Thermal Expansion recipes!", e); }
    }
}

package modtweaker2.mods.thermalexpansion;

import static modtweaker2.helpers.ReflectionHelper.getConstructor;
import static modtweaker2.helpers.StackHelper.areEqualOrNull;

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

    public static boolean equals(RecipeCrucible r1, RecipeCrucible r2) {
        if(r1 == r2) {
            return true;
        }
        
        if(r1 == null || r2 == null) {
            return false;
        }
        
        if(!areEqualOrNull(r1.getInput(), r2.getInput())) {
            return false;
        }
        
        if(r1.getEnergy() != r2.getEnergy()) {
            return false;
        }
        
        if(!areEqualOrNull(r1.getOutput(), r2.getOutput())) {
            return false;
        }

        return true;
    }
    
    public static boolean equals(RecipeFurnace r1, RecipeFurnace r2) {
        if(r1 == r2) {
            return true;
        }
        
        if(r1 == null || r2 == null) {
            return false;
        }
        
        if(!areEqualOrNull(r1.getInput(), r2.getInput())) {
            return false;
        }        
        
        if(r1.getEnergy() != r2.getEnergy()) {
            return false;
        }
        
        if(!areEqualOrNull(r1.getOutput(), r2.getOutput())) {
            return false;
        }
        
        return true;
    }
    
    public static boolean equals(RecipePulverizer r1, RecipePulverizer r2) {
        if(r1 == r2) {
            return true;
        }
        
        if(r1 == null || r2 == null) {
            return false;
        }
        
        if(!areEqualOrNull(r1.getInput(), r2.getInput())) {
            return false;
        }        
        
        if(r1.getEnergy() != r2.getEnergy()) {
            return false;
        }
        
        if(!areEqualOrNull(r1.getPrimaryOutput(), r2.getPrimaryOutput())) {
            return false;
        }
        
        if(!areEqualOrNull(r1.getSecondaryOutput(), r2.getSecondaryOutput())) {
            return false;
        }
        
        if(r1.getSecondaryOutput() != null && r2.getSecondaryOutput() != null &&
                r1.getSecondaryOutputChance() != r2.getSecondaryOutputChance()) {
            return false;
        }
        
        return true;
    }
    
    public static boolean equals(RecipeSawmill r1, RecipeSawmill r2) {
        if(r1 == r2) {
            return true;
        }
        
        if(r1 == null || r2 == null) {
            return false;
        }
        
        if(!areEqualOrNull(r1.getInput(), r2.getInput())) {
            return false;
        }        
        
        if(r1.getEnergy() != r2.getEnergy()) {
            return false;
        }
        
        if(!areEqualOrNull(r1.getPrimaryOutput(), r2.getPrimaryOutput())) {
            return false;
        }
        
        if(!areEqualOrNull(r1.getSecondaryOutput(), r2.getSecondaryOutput())) {
            return false;
        }
        
        if(r1.getSecondaryOutput() != null && r2.getSecondaryOutput() != null &&
                r1.getSecondaryOutputChance() != r2.getSecondaryOutputChance()) {
            return false;
        }
        
        return true;
    }
    
    public static boolean equals(RecipeSmelter r1, RecipeSmelter r2) {
        if(r1 == r2) {
            return true;
        }
        
        if(r1 == null || r2 == null) {
            return false;
        }
        
        if(!areEqualOrNull(r1.getPrimaryInput(), r2.getPrimaryInput())) {
            return false;
        }
        
        if(!areEqualOrNull(r1.getSecondaryInput(), r2.getSecondaryInput())) {
            return false;
        }
        
        if(r1.getEnergy() != r2.getEnergy()) {
            return false;
        }
        
        if(!areEqualOrNull(r1.getPrimaryOutput(), r2.getPrimaryOutput())) {
            return false;
        }
        
        if(!areEqualOrNull(r1.getSecondaryOutput(), r2.getSecondaryOutput())) {
            return false;
        }
        
        if(r1.getSecondaryOutput() != null && r2.getSecondaryOutput() != null &&
                r1.getSecondaryOutputChance() != r2.getSecondaryOutputChance()) {
            return false;
        }
        
        return true;
    }
    
    public static boolean equals(RecipeTransposer r1, RecipeTransposer r2) {
        if(r1 == r2) {
            return true;
        }
        
        if(r1 == null || r2 == null) {
            return false;
        }
        
        if(!areEqualOrNull(r1.getInput(), r2.getInput())) {
            return false;
        }        
        
        if(r1.getEnergy() != r2.getEnergy()) {
            return false;
        }
        
        if(!areEqualOrNull(r1.getOutput(), r2.getOutput())) {
            return false;
        }
        
        if(!areEqualOrNull(r1.getFluid(), r2.getFluid())) {
            return false;
        }
        
        if(r1.getChance() != r2.getChance()) {
            return false;
        }
        
        return true;
    }
}

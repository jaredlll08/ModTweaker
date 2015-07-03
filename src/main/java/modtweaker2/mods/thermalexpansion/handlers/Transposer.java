package modtweaker2.mods.thermalexpansion.handlers;

import static modtweaker2.helpers.InputHelper.toFluid;
import static modtweaker2.helpers.InputHelper.toIItemStack;
import static modtweaker2.helpers.InputHelper.toILiquidStack;
import static modtweaker2.helpers.InputHelper.toStack;
import static modtweaker2.helpers.StackHelper.matches;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import minetweaker.api.liquid.ILiquidStack;
import modtweaker2.helpers.InputHelper;
import modtweaker2.helpers.LogHelper;
import modtweaker2.mods.thermalexpansion.ThermalHelper;
import modtweaker2.utils.BaseMapAddition;
import modtweaker2.utils.BaseMapRemoval;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import cofh.lib.inventory.ComparableItemStackSafe;
import cofh.thermalexpansion.util.crafting.TransposerManager;
import cofh.thermalexpansion.util.crafting.TransposerManager.RecipeTransposer;

@ZenClass("mods.thermalexpansion.Transposer")
public class Transposer {
    
    public static final String nameFill = "Thermal Expansion Transposer (Fill)";
    public static final String nameExtract = "Thermal Expansion Transposer (Extract)";
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
	@ZenMethod
	public static void addFillRecipe(int energy, IItemStack input, IItemStack output, ILiquidStack liquid) {
	    Map<List<Integer>, RecipeTransposer> recipes = new HashMap<List<Integer>, RecipeTransposer>();
	    ItemStack in = toStack(input);
	    ItemStack out = toStack(output);
	    FluidStack fluid = toFluid(liquid);
	    List<Integer> key = Arrays.asList(new Integer[] { Integer.valueOf(new ComparableItemStackSafe(in).hashCode()), Integer.valueOf(fluid.fluidID) });
	    RecipeTransposer value = (RecipeTransposer) ThermalHelper.getTERecipe(ThermalHelper.transposerRecipe, in, out, fluid, energy, 100);
	    
	    recipes.put(key, value);
	    
	    MineTweakerAPI.apply(new AddFill(recipes));
	}

	@ZenMethod
	public static void addExtractRecipe(int energy, IItemStack input, IItemStack output, ILiquidStack liquid, int chance) {
        Map<ComparableItemStackSafe, RecipeTransposer> recipes = new HashMap<ComparableItemStackSafe, RecipeTransposer>();
        ItemStack in = toStack(input);
        ItemStack out = toStack(output);
        FluidStack fluid = toFluid(liquid);
        ComparableItemStackSafe key = new ComparableItemStackSafe(in);
        RecipeTransposer value = (RecipeTransposer) ThermalHelper.getTERecipe(ThermalHelper.transposerRecipe, in, out, fluid, energy, 100);
        
        recipes.put(key, value);
        
        MineTweakerAPI.apply(new AddExtract(recipes));
	}
	
	private static class AddFill extends BaseMapAddition<List<Integer>, RecipeTransposer> {

        protected AddFill(Map<List<Integer>, RecipeTransposer> recipes) {
            super(Transposer.nameFill, ThermalHelper.getFillMap());
            recipes.putAll(recipes);
        }

        @Override
        public void apply() {
            map = ThermalHelper.getFillMap();
            super.apply();
            TransposerManager.refreshRecipes();
        }
        
        @Override
        public void undo() {
            map = ThermalHelper.getFillMap();
            super.undo();
            TransposerManager.refreshRecipes();
        }
        
        @Override
        protected String getRecipeInfo(Entry<List<Integer>, RecipeTransposer> recipe) {
            return InputHelper.getStackDescription(recipe.getValue().getOutput());
        }
	}

    private static class AddExtract extends BaseMapAddition<ComparableItemStackSafe, RecipeTransposer> {

        protected AddExtract(Map<ComparableItemStackSafe, RecipeTransposer> recipes) {
            super(Transposer.nameFill, ThermalHelper.getExtractMap());
            recipes.putAll(recipes);
        }

        @Override
        public void apply() {
            map = ThermalHelper.getExtractMap();
            super.apply();
            TransposerManager.refreshRecipes();
        }
        
        @Override
        public void undo() {
            map = ThermalHelper.getExtractMap();
            super.undo();
            TransposerManager.refreshRecipes();
        }
        
        @Override
        protected String getRecipeInfo(Entry<ComparableItemStackSafe, RecipeTransposer> recipe) {
            return InputHelper.getStackDescription(recipe.getValue().getInput());
        }
    }

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@ZenMethod
	public static void removeFillRecipe(IIngredient input, IIngredient liquid) {
	    Map<List<Integer>, RecipeTransposer> recipes = new HashMap<List<Integer>, RecipeTransposer>();
	    for(Entry<List<Integer>, RecipeTransposer> recipe : ThermalHelper.getFillMap().entrySet()) {
	        if(recipe != null && recipe.getValue() != null && matches(input, toIItemStack(recipe.getValue().getInput())) && matches(liquid, toILiquidStack(recipe.getValue().getFluid()))) {
	            recipes.put(recipe.getKey(), recipe.getValue());
	        }
	    }
	    
	    if(!recipes.isEmpty()) {
	        MineTweakerAPI.apply(new RemoveFill(recipes));
	    } else {
	        LogHelper.logWarning(String.format("No %s Recipe found for %s and %s. Command ignored!", nameFill, input.toString(), liquid.toString()));
	    }
	}

	@ZenMethod
	public static void removeExtractRecipe(IIngredient input) {
	    Map<ComparableItemStackSafe, RecipeTransposer> recipes = new HashMap<ComparableItemStackSafe, RecipeTransposer>();
	    for(Entry<ComparableItemStackSafe, RecipeTransposer> recipe : ThermalHelper.getExtractMap().entrySet()) {
	        if(recipe != null && recipe.getValue() != null && matches(input, toIItemStack(recipe.getValue().getInput()))) {
	            recipes.put(recipe.getKey(), recipe.getValue());
	        }
	    }
	    
        if(!recipes.isEmpty()) {
            MineTweakerAPI.apply(new RemoveExtract(recipes));
        } else {
            LogHelper.logWarning(String.format("No %s Recipe found for %s. Command ignored!", nameFill, input.toString()));
        }

	}

	private static class RemoveFill extends BaseMapRemoval<List<Integer>, RecipeTransposer> {

        protected RemoveFill(Map<List<Integer>, RecipeTransposer> recipes) {
            super(nameFill, ThermalHelper.getFillMap(), recipes);
        }
        
        @Override
        public void apply() {
            map = ThermalHelper.getFillMap();
            super.apply();
            TransposerManager.refreshRecipes();
        }
        
        @Override
        public void undo() {
            map = ThermalHelper.getFillMap();
            super.undo();
            TransposerManager.refreshRecipes();
        }

        @Override
        protected String getRecipeInfo(Entry<List<Integer>, RecipeTransposer> recipe) {
            return InputHelper.getStackDescription(recipe.getValue().getOutput());
        }
	}
	
    private static class RemoveExtract extends BaseMapRemoval<ComparableItemStackSafe, RecipeTransposer> {

        protected RemoveExtract(Map<ComparableItemStackSafe, RecipeTransposer> recipes) {
            super(nameExtract, ThermalHelper.getExtractMap(), recipes);
        }
        
        @Override
        public void apply() {
            map = ThermalHelper.getExtractMap();
            super.apply();
            TransposerManager.refreshRecipes();
        }
        
        @Override
        public void undo() {
            map = ThermalHelper.getExtractMap();
            super.undo();
            TransposerManager.refreshRecipes();
        }

        @Override
        protected String getRecipeInfo(Entry<ComparableItemStackSafe, RecipeTransposer> recipe) {
            return InputHelper.getStackDescription(recipe.getValue().getInput());
        }
    }
}

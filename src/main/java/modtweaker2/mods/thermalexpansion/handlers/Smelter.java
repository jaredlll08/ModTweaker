package modtweaker2.mods.thermalexpansion.handlers;

import static modtweaker2.helpers.InputHelper.toIItemStack;
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
import modtweaker2.helpers.InputHelper;
import modtweaker2.helpers.LogHelper;
import modtweaker2.mods.thermalexpansion.ThermalHelper;
import modtweaker2.utils.BaseMapAddition;
import modtweaker2.utils.BaseMapRemoval;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import cofh.thermalexpansion.util.crafting.SmelterManager;
import cofh.thermalexpansion.util.crafting.SmelterManager.ComparableItemStackSmelter;
import cofh.thermalexpansion.util.crafting.SmelterManager.RecipeSmelter;

@ZenClass("mods.thermalexpansion.Smelter")
public class Smelter {
    
    public static final String name = "Thermal Expansion Smelter";

	@ZenMethod
	public static void addRecipe(int energy, IItemStack input, IItemStack input2, IItemStack output) {
		addRecipe(energy, input, input2, output, null, 0);
	}

	@ZenMethod
	public static void addRecipe(int energy, IItemStack input, IItemStack input2, IItemStack output, IItemStack output2, int chance) {
		ItemStack in1 = toStack(input);
		ItemStack in2 = toStack(input2);
		ItemStack out1 = toStack(output);
		ItemStack out2 = toStack(output2);
		
		List<ComparableItemStackSmelter> key = Arrays.asList(new ComparableItemStackSmelter[] { new ComparableItemStackSmelter(in1), new ComparableItemStackSmelter(in2) });
		RecipeSmelter value = (RecipeSmelter) ThermalHelper.getTERecipe(ThermalHelper.smelterRecipe, in1, in2, out1, out2, chance, energy);
		
		Map<List<ComparableItemStackSmelter>, RecipeSmelter> recipes = new HashMap<List<ComparableItemStackSmelter>, RecipeSmelter>();
		
		recipes.put(key, value);
		
		MineTweakerAPI.apply(new Add(recipes));
	}

	private static class Add extends BaseMapAddition<List<ComparableItemStackSmelter>, RecipeSmelter> {
		public Add(Map<List<ComparableItemStackSmelter>, RecipeSmelter> recipes) {
			super(Smelter.name, ThermalHelper.getSmelterMap(), recipes);
		}

		@Override
		public void apply() {
            map = ThermalHelper.getSmelterMap();
		    super.apply();
		    SmelterManager.refreshRecipes();
		}

		@Override
		public void undo() {
            map = ThermalHelper.getSmelterMap();
		    super.undo();
		    SmelterManager.refreshRecipes();
		}

		@Override
		public String getRecipeInfo(Entry<List<ComparableItemStackSmelter>, RecipeSmelter> recipe) {
		    return InputHelper.getStackDescription(recipe.getValue().getPrimaryOutput());
		}
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@ZenMethod
	public static void removeRecipe(IIngredient input1, IIngredient input2) {
	    Map<List<ComparableItemStackSmelter>, RecipeSmelter> recipes = new HashMap<List<ComparableItemStackSmelter>, RecipeSmelter>();
	    
        for(Entry<List<ComparableItemStackSmelter>, RecipeSmelter> entry : ThermalHelper.getSmelterMap().entrySet()) {
            RecipeSmelter recipe = entry.getValue();
            
            if(recipe != null && matches(input1, toIItemStack(recipe.getPrimaryInput())) && matches(input2, toIItemStack(recipe.getSecondaryInput()))) {
                recipes.put(entry.getKey(), entry.getValue());
            }
        }
        
        if(!recipes.isEmpty()) {
            MineTweakerAPI.apply(new Remove(recipes));
        } else {
            LogHelper.logWarning(String.format("No %s Recipes found for %s and %s. Command ignored!", Smelter.name, input1.toString(), input2.toString()));
        }
	}

	private static class Remove extends BaseMapRemoval<List<ComparableItemStackSmelter>, RecipeSmelter> {

		public Remove(Map<List<ComparableItemStackSmelter>, RecipeSmelter> recipes) {
			super(Smelter.name, ThermalHelper.getSmelterMap(), recipes);
		}

		@Override
		public void apply() {
		    map = ThermalHelper.getSmelterMap();
		    super.apply();
		    SmelterManager.refreshRecipes();
		}
		
		@Override
		public void undo() {
            map = ThermalHelper.getSmelterMap();
            super.undo();
            SmelterManager.refreshRecipes();
		}

        @Override
        public String getRecipeInfo(Entry<List<ComparableItemStackSmelter>, RecipeSmelter> recipe) {
            return InputHelper.getStackDescription(recipe.getValue().getPrimaryOutput());
        }
	}
}

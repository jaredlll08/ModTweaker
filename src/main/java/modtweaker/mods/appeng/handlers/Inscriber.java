package modtweaker.mods.appeng.handlers;

import appeng.api.AEApi;
import appeng.api.features.*;
import appeng.core.features.registries.entries.InscriberRecipe;
import com.blamejared.mtlib.helpers.LogHelper;
import com.blamejared.mtlib.utils.*;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.*;
import modtweaker.mods.appeng.AppliedEnergisticsHelper;
import stanhebben.zenscript.annotations.*;

import java.util.LinkedList;

import static com.blamejared.mtlib.helpers.InputHelper.*;
import static com.blamejared.mtlib.helpers.StackHelper.matches;

@ZenClass("mods.appeng.Inscriber")
public class Inscriber {
	
	protected static final String name = "Applied Energistics 2 Inscriber";
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Adds a shaped recipe for the Carpenter
	 *
	 * @param outputStack      - Product of the Recipe
	 * @param inputArray       - Ingredients of the Recipe
	 * @param inputStackPlateA - Ingredient of the Recipe for Plate Slot A
	 * @param inputStackPlateB - Ingredient of the Recipe for Plate Slot B
	 * @param typeString       - Type that decides whether to consume the ItemStack in Plate Slot A/B
	 **/
	@ZenMethod
	public static void addRecipe(IItemStack outputStack, IItemStack[] inputArray, IItemStack inputStackPlateA, IItemStack inputStackPlateB, String typeString) {
		if(inputArray == null || outputStack == null || (!typeString.equals("Press") && !typeString.equals("Inscribe"))) {
			LogHelper.logError(String.format("Required parameters missing for %s Recipe.", Inscriber.name));
			return;
		}
		
		// Create recipe
		IInscriberRecipe recipe = new InscriberRecipe(ArrayUtils.toArrayList(toStacks(inputArray)), toStack(outputStack), toStack(inputStackPlateA), toStack(inputStackPlateB), InscriberProcessType.valueOf(typeString));
		
		// Check if the recipe is already present, we don't want to add duplicates
		for(IInscriberRecipe r : AEApi.instance().registries().inscriber().getRecipes()) {
			if(r != null && AppliedEnergisticsHelper.equals(r, recipe)) {
				LogHelper.logWarning(String.format("Duplicate %s Recipe found for %s. Command ignored!", Inscriber.name, LogHelper.getStackDescription(toStack(outputStack))));
				return;
			}
		}
		
		MineTweakerAPI.apply(new Add(recipe));
	}
	
	@Deprecated
	@ZenMethod
	public static void addRecipe(IItemStack[] imprintable, IItemStack plateA, IItemStack plateB, IItemStack out, String type) {
		if(imprintable == null || out == null || (!type.equals("Press") && !type.equals("Inscribe"))) {
			LogHelper.logError(String.format("Required parameters missing for %s Recipe.", Inscriber.name));
			return;
		}
		
		// Create recipe
		IInscriberRecipe recipe = new InscriberRecipe(ArrayUtils.toArrayList(toStacks(imprintable)), toStack(out), toStack(plateA), toStack(plateB), InscriberProcessType.valueOf(type));
		
		// Check if the recipe is already present, we don't want to add duplicates
		for(IInscriberRecipe r : AEApi.instance().registries().inscriber().getRecipes()) {
			if(r != null && AppliedEnergisticsHelper.equals(r, recipe)) {
				LogHelper.logWarning(String.format("Duplicate %s Recipe found for %s. Command ignored!", Inscriber.name, LogHelper.getStackDescription(toStack(out))));
				return;
			}
		}
		
		MineTweakerAPI.apply(new Add(recipe));
	}
	
	public static class Add extends BaseListAddition<IInscriberRecipe> {
		
		public Add(IInscriberRecipe recipe) {
			super(Inscriber.name, AppliedEnergisticsHelper.inscriber);
			recipes.add(recipe);
		}
		
		@Override
		public void apply() {
			if(recipes.isEmpty()) {
				return;
			}
			
			for(IInscriberRecipe recipe : recipes) {
				try {
					AEApi.instance().registries().inscriber().addRecipe(recipe);
					successful.add(recipe);
				} catch(Exception ex) {
					LogHelper.logError("Error adding inscriber recipe.", ex);
				}
			}
		}
		
		@Override
		public void undo() {
			if(successful.isEmpty()) {
				return;
			}
			
			for(IInscriberRecipe recipe : successful) {
				try {
					AEApi.instance().registries().inscriber().removeRecipe(recipe);
				} catch(Exception ex) {
					LogHelper.logError("Error removing inscriber recipe.", ex);
				}
			}
		}
		
		@Override
		public String getRecipeInfo(IInscriberRecipe recipe) {
			return LogHelper.getStackDescription(recipe.getOutput());
		}
		
		@Override
		protected boolean equals(IInscriberRecipe recipe, IInscriberRecipe otherRecipe) {
			return AppliedEnergisticsHelper.equals(recipe, otherRecipe);
		}
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Adds a shaped recipe for the Carpenter
	 *
	 * @param outputStack - Product of the Recipe
	 **/
	@ZenMethod
	public static void removeRecipe(IIngredient outputStack) {
		if(outputStack == null) {
			LogHelper.logError(String.format("Required parameters missing for %s Recipe.", Inscriber.name));
			return;
		}
		
		// Get list of existing recipes, matching with parameter
		LinkedList<IInscriberRecipe> result = new LinkedList<IInscriberRecipe>();
		
		for(IInscriberRecipe entry : AEApi.instance().registries().inscriber().getRecipes()) {
			if(entry != null && entry.getOutput() != null && matches(outputStack, toIItemStack(entry.getOutput()))) {
				result.add(entry);
			}
		}
		
		// Check if we found the recipes and apply the action
		if(!result.isEmpty()) {
			MineTweakerAPI.apply(new Remove(result));
		} else {
			LogHelper.logWarning(String.format("No %s Recipe found for %s. Command ignored!", Inscriber.name, outputStack.toString()));
		}
	}
	
	public static class Remove extends BaseListRemoval<IInscriberRecipe> {
		
		public Remove(LinkedList<IInscriberRecipe> recipes) {
			super(Inscriber.name, AppliedEnergisticsHelper.inscriber, recipes);
		}
		
		@Override
		public void apply() {
			if(this.recipes.isEmpty()) {
				return;
			}
			
			for(IInscriberRecipe recipe : recipes) {
				try {
					AEApi.instance().registries().inscriber().removeRecipe(recipe);
					successful.add(recipe);
				} catch(Exception ex) {
					LogHelper.logError("Error removing inscriber recipe.", ex);
				}
			}
		}
		
		@Override
		public void undo() {
			if(successful.isEmpty()) {
				return;
			}
			
			for(IInscriberRecipe recipe : successful) {
				try {
					AEApi.instance().registries().inscriber().addRecipe(recipe);
				} catch(Exception ex) {
					LogHelper.logError("Error restoring inscriber recipe.", ex);
				}
			}
		}
		
		@Override
		public String getRecipeInfo(IInscriberRecipe recipe) {
			return LogHelper.getStackDescription(recipe.getOutput());
		}
	}
}

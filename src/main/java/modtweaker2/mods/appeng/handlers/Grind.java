package modtweaker2.mods.appeng.handlers;

import appeng.api.AEApi;
import appeng.api.features.IGrinderEntry;
import appeng.core.features.registries.entries.AppEngGrinderRecipe;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import minetweaker.api.item.WeightedItemStack;
import modtweaker2.helpers.InputHelper;
import modtweaker2.helpers.LogHelper;
import modtweaker2.mods.appeng.AppliedEnergisticsHelper;
import modtweaker2.utils.BaseListAddition;
import modtweaker2.utils.BaseListRemoval;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.LinkedList;

import static modtweaker2.helpers.InputHelper.toIItemStack;
import static modtweaker2.helpers.InputHelper.toStack;
import static modtweaker2.helpers.StackHelper.matches;

@ZenClass("mods.appeng.Grinder")
public class Grind {

	protected static final String name = "Applied Energistics 2 Grindstone";

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Adds a shaped recipe for the Grindstone
	 * @param outputs
	 * @param inputStack
	 * @param turns
	 */
	@ZenMethod
	public static void  addRecipe(WeightedItemStack[] outputs, IItemStack inputStack, int turns) {
		IGrinderEntry recipe;
		if (outputs.length == 1)
			recipe = new AppEngGrinderRecipe(InputHelper.toStack(inputStack), InputHelper.toStack(outputs[0].getStack()), turns);
		else if (outputs.length == 2)
			recipe = new AppEngGrinderRecipe(InputHelper.toStack(inputStack), InputHelper.toStack(outputs[0].getStack()), InputHelper.toStack(outputs[1].getStack()), outputs[1].getChance(), turns);
		else if (outputs.length == 3)
			recipe = new AppEngGrinderRecipe(InputHelper.toStack(inputStack), InputHelper.toStack(outputs[0].getStack()), InputHelper.toStack(outputs[1].getStack()), InputHelper.toStack(outputs[2].getStack()), outputs[1].getChance(), outputs[2].getChance(), turns);
		else {
			LogHelper.logWarning(String.format("No more then 3 output stacks are allowed in %s. Command ignored!", name, LogHelper.getStackDescription(toStack(inputStack))));
			return;
		}

		// Check if the recipe is already present, we don't want to add duplicates
		for (IGrinderEntry r : AEApi.instance().registries().grinder().getRecipes()) {
			if (r != null && AppliedEnergisticsHelper.equals(r, recipe)) {
				LogHelper.logWarning(String.format("Duplicate %s Recipe found for %s. Command ignored!", name, LogHelper.getStackDescription(toStack(inputStack))));
				return;
			}
		}

		MineTweakerAPI.apply(new Add(recipe));
	}

	/**
	 * Adds a shaped recipe for the Grindstone
	 *
	 * @param outputStack - Product of the Recipe
	 * @param inputStack  - Ingredient of the Recipe
	 * @param inputEnergy - Energy requirement of the Recipe
	 * @optionalParam outputStack2 - Second product of the Recipe
	 * @optionalParam outputStack2Chance - Chance for the acquirement of the second product
	 * @optionalParam outputStack3 - Third product of the Recipe
	 * @optionalParam outputStack3Chance - Chance for the acquirement of the third product
	 **/
	@Deprecated
	@ZenMethod
	public static void addRecipe(IItemStack outputStack, IItemStack inputStack, @Optional IItemStack outputStack2, @Optional float outputStack2Chance, @Optional IItemStack outputStack3, @Optional float outputStack3Chance, int inputEnergy) {
		if (inputStack == null || outputStack == null) {
			LogHelper.logError(String.format("Required parameters missing for %s Recipe.", name));
			return;
		}

		// Create recipe
		IGrinderEntry recipe;

		if (outputStack2 != null && outputStack3 != null)
			recipe = new AppEngGrinderRecipe(InputHelper.toStack(inputStack), InputHelper.toStack(outputStack), InputHelper.toStack(outputStack2), InputHelper.toStack(outputStack3), outputStack2Chance, outputStack3Chance, inputEnergy);
		else if (outputStack2 != null)
			recipe = new AppEngGrinderRecipe(InputHelper.toStack(inputStack), InputHelper.toStack(outputStack), InputHelper.toStack(outputStack2), outputStack2Chance, inputEnergy);
		else
			recipe = new AppEngGrinderRecipe(InputHelper.toStack(inputStack), InputHelper.toStack(outputStack), inputEnergy);

		// Check if the recipe is already present, we don't want to add duplicates
		for (IGrinderEntry r : AEApi.instance().registries().grinder().getRecipes()) {
			if (r != null && AppliedEnergisticsHelper.equals(r, recipe)) {
				LogHelper.logWarning(String.format("Duplicate %s Recipe found for %s. Command ignored!", name, LogHelper.getStackDescription(toStack(inputStack))));
				return;
			}
		}

		MineTweakerAPI.apply(new Add(recipe));
	}

	@Deprecated
	@ZenMethod
	public static void addRecipe(IItemStack input, IItemStack output, int energy, @Optional IItemStack output2, @Optional float chance2, @Optional IItemStack output3, @Optional float chance3) {
		if (input == null || output == null) {
			LogHelper.logError(String.format("Required parameters missing for %s Recipe.", name));
			return;
		}

		// Create recipe
		IGrinderEntry recipe;

		if (output2 != null && output3 != null)
			recipe = new AppEngGrinderRecipe(InputHelper.toStack(input), InputHelper.toStack(output), InputHelper.toStack(output2), InputHelper.toStack(output3), chance2, chance3, energy);
		else if (output2 != null)
			recipe = new AppEngGrinderRecipe(InputHelper.toStack(input), InputHelper.toStack(output), InputHelper.toStack(output2), chance2, energy);
		else
			recipe = new AppEngGrinderRecipe(InputHelper.toStack(input), InputHelper.toStack(output), energy);

		// Check if the recipe is already present, we don't want to add duplicates
		for (IGrinderEntry r : AEApi.instance().registries().grinder().getRecipes()) {
			if (r != null && AppliedEnergisticsHelper.equals(r, recipe)) {
				LogHelper.logWarning(String.format("Duplicate %s Recipe found for %s. Command ignored!", name, LogHelper.getStackDescription(toStack(input))));
				return;
			}
		}

		MineTweakerAPI.apply(new Add(recipe));
	}

	private static class Add extends BaseListAddition<IGrinderEntry> {

		public Add(IGrinderEntry recipe) {
			super(Grind.name, AEApi.instance().registries().grinder().getRecipes());
			recipes.add(recipe);
		}

		@Override
		public String getRecipeInfo(IGrinderEntry recipe) {
			return LogHelper.getStackDescription(recipe.getInput());
		}

		@Override
		protected boolean equals(IGrinderEntry recipe, IGrinderEntry otherRecipe) {
			return AppliedEnergisticsHelper.equals(recipe, otherRecipe);
		}
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Removes a recipe for the Grindstone
	 *
	 * @param input - Product of the Recipe
	 **/
	@ZenMethod
	public static void removeRecipe(IIngredient input) {
		if (input == null) {
			LogHelper.logError(String.format("Required parameters missing for %s Recipe.", name));
			return;
		}

		// Get list of existing recipes, matching with parameter
		LinkedList<IGrinderEntry> result = new LinkedList<IGrinderEntry>();

		for (IGrinderEntry entry : AEApi.instance().registries().grinder().getRecipes()) {
			if (entry != null && entry.getOutput() != null && matches(input, toIItemStack(entry.getOutput()))) {
				result.add(entry);
			}
		}

		// Check if we found the recipes and apply the action
		if (!result.isEmpty()) {
			MineTweakerAPI.apply(new Remove(result));
		} else {
			LogHelper.logWarning(String.format("No %s Recipe found for %s. Command ignored!", name, input.toString()));
		}
	}

	private static class Remove extends BaseListRemoval<IGrinderEntry> {
		public Remove(LinkedList<IGrinderEntry> recipes) {
			super(Grind.name, AEApi.instance().registries().grinder().getRecipes(), recipes);
		}

		@Override
		public String getRecipeInfo(IGrinderEntry recipe) {
			return LogHelper.getStackDescription(recipe.getInput());
		}
	}

}
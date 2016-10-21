package modtweaker.mods.actuallyadditions.handlers;

import static modtweaker.helpers.InputHelper.toIItemStack;
import static modtweaker.helpers.InputHelper.toStack;
import static modtweaker.helpers.StackHelper.matches;

import java.util.LinkedList;
import java.util.List;

import de.ellpeck.actuallyadditions.api.ActuallyAdditionsAPI;
import de.ellpeck.actuallyadditions.api.recipe.CrusherRecipe;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import modtweaker.helpers.LogHelper;
import modtweaker.utils.BaseListAddition;
import modtweaker.utils.BaseListRemoval;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.actuallyadditions.Crusher")
public class Crusher {
	protected static final String name = "Actually Additions Crusher";

	@ZenMethod
	public static void addRecipe(IItemStack input, IItemStack outputOne, @Optional IItemStack outputTwo, @Optional int outputTwoChance) {
		MineTweakerAPI.apply(new Add(new CrusherRecipe(toStack(input), toStack(outputOne), toStack(outputTwo), outputTwoChance)));
	}


	private static class Add extends BaseListAddition<CrusherRecipe> {
		public Add(CrusherRecipe recipe) {
			super(Crusher.name, ActuallyAdditionsAPI.CRUSHER_RECIPES);
			
			this.recipes.add(recipe);
		}

		@Override
		public String getRecipeInfo(CrusherRecipe recipe) {
			return LogHelper.getStackDescription(recipe.inputStack);
		}
	}

	@ZenMethod
	public static void remove(IIngredient input) {
		List<CrusherRecipe> recipes = new LinkedList<CrusherRecipe>();

		if (input == null) {
			LogHelper.logError(String.format("Required parameters missing for %s Recipe.", name));
			return;
		}

		for (CrusherRecipe recipe : ActuallyAdditionsAPI.CRUSHER_RECIPES) {
			if (matches(input, toIItemStack(recipe.inputStack)))
				recipes.add(recipe);
		}

		if (!recipes.isEmpty()) {
			MineTweakerAPI.apply(new Remove(recipes));
		} else {
			LogHelper.logWarning(String.format("No %s Recipe found for output %s. Command ignored!", Crusher.name,
					input.toString()));
		}

	}

	private static class Remove extends BaseListRemoval<CrusherRecipe> {
		public Remove(List<CrusherRecipe> recipes) {
			super(Crusher.name, ActuallyAdditionsAPI.CRUSHER_RECIPES, recipes);
		}

		@Override
		protected String getRecipeInfo(CrusherRecipe recipe) {
			return LogHelper.getStackDescription(recipe.inputStack);
		}
	}
}

package modtweaker.mods.actuallyadditions.handlers;

import static modtweaker.helpers.InputHelper.toIItemStack;
import static modtweaker.helpers.InputHelper.toStack;
import static modtweaker.helpers.StackHelper.matches;

import java.util.LinkedList;
import java.util.List;

import de.ellpeck.actuallyadditions.api.ActuallyAdditionsAPI;
import de.ellpeck.actuallyadditions.api.recipe.CompostRecipe;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import modtweaker.helpers.InputHelper;
import modtweaker.helpers.LogHelper;
import modtweaker.utils.BaseListAddition;
import modtweaker.utils.BaseListRemoval;
import net.minecraft.block.Block;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.actuallyadditions.Compost")
public class Compost {
	protected static final String name = "Actually Additions Compost";

	@ZenMethod
	public static void addRecipe(IItemStack input,IItemStack output) {
		if(!InputHelper.isABlock(input) || !InputHelper.isABlock(output)){
			LogHelper.logWarning(String.format("Invalid %s Recipe found for %s. Input and output must be blocks.", Empowerer.name,
					output.toString()));
			return;
		}
		MineTweakerAPI.apply(new Add(new CompostRecipe(toStack(input), Block.getBlockFromItem(toStack(input).getItem()), toStack(output), Block.getBlockFromItem(toStack(output).getItem()))));
	}
	
	@ZenMethod
	public static void addRecipe(IItemStack input, IItemStack displayInput, IItemStack output, IItemStack displayOutput) {
		if(!InputHelper.isABlock(displayInput) || !InputHelper.isABlock(displayOutput)){
			LogHelper.logWarning(String.format("Invalid %s Recipe found for %s. Input and output must be blocks.", Empowerer.name,
					output.toString()));
			return;
		}
		MineTweakerAPI.apply(new Add(new CompostRecipe(toStack(input), Block.getBlockFromItem(toStack(displayInput).getItem()), toStack(output), Block.getBlockFromItem(toStack(displayOutput).getItem()))));
	}


	private static class Add extends BaseListAddition<CompostRecipe> {
		public Add(CompostRecipe recipe) {
			super(Compost.name, ActuallyAdditionsAPI.COMPOST_RECIPES);

			this.recipes.add(recipe);
		}

		@Override
		public String getRecipeInfo(CompostRecipe recipe) {
			return LogHelper.getStackDescription(recipe.output);
		}
	}

	@ZenMethod
	public static void remove(IIngredient input) {
		List<CompostRecipe> recipes = new LinkedList<CompostRecipe>();

		if (input == null) {
			LogHelper.logError(String.format("Required parameters missing for %s Recipe.", name));
			return;
		}

		for (CompostRecipe recipe : ActuallyAdditionsAPI.COMPOST_RECIPES) {
			if (matches(input, toIItemStack(recipe.input)))
				recipes.add(recipe);
		}

		if (!recipes.isEmpty()) {
			MineTweakerAPI.apply(new Remove(recipes));
		} else {
			LogHelper.logWarning(String.format("No %s Recipe found for output %s. Command ignored!", Compost.name,
					input.toString()));
		}

	}

	private static class Remove extends BaseListRemoval<CompostRecipe> {
		public Remove(List<CompostRecipe> recipes) {
			super(Compost.name, ActuallyAdditionsAPI.COMPOST_RECIPES, recipes);
		}

		@Override
		protected String getRecipeInfo(CompostRecipe recipe) {
			return LogHelper.getStackDescription(recipe.output);
		}
	}
}

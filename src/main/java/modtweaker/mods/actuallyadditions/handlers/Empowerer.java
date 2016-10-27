package modtweaker.mods.actuallyadditions.handlers;

import com.blamejared.mtlib.helpers.LogHelper;
import com.blamejared.mtlib.utils.BaseListAddition;
import com.blamejared.mtlib.utils.BaseListRemoval;
import de.ellpeck.actuallyadditions.api.ActuallyAdditionsAPI;
import de.ellpeck.actuallyadditions.api.recipe.EmpowererRecipe;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.LinkedList;
import java.util.List;

import static com.blamejared.mtlib.helpers.InputHelper.toIItemStack;
import static com.blamejared.mtlib.helpers.InputHelper.toStack;
import static com.blamejared.mtlib.helpers.StackHelper.matches;

@ZenClass("mods.actuallyadditions.Empowerer")
public class Empowerer {
	protected static final String name = "Actually Additions Empowerer";

	@ZenMethod
	public static void addRecipe(IItemStack input, IItemStack output, IItemStack modifier1,
			IItemStack modifier2, IItemStack modifier3, IItemStack modifier4, int energyPerStand,
			int time, @Optional float redValue, @Optional float greenValue, @Optional float blueValue) {


		float[] particleColor = new float[]{redValue,greenValue,blueValue};
		MineTweakerAPI.apply(new Add(
				new EmpowererRecipe(toStack(input), toStack(output), toStack(modifier1), toStack(modifier2), toStack(modifier3), toStack(modifier4), energyPerStand, time, particleColor)));
	}

	private static class Add extends BaseListAddition<EmpowererRecipe> {
		public Add(EmpowererRecipe recipe) {
			super(Empowerer.name, ActuallyAdditionsAPI.EMPOWERER_RECIPES);

			this.recipes.add(recipe);
		}

		@Override
		public String getRecipeInfo(EmpowererRecipe recipe) {
			return LogHelper.getStackDescription(recipe.output);
		}
	}

	@ZenMethod
	public static void remove(IIngredient input, IIngredient output) {
		List<EmpowererRecipe> recipes = new LinkedList<EmpowererRecipe>();

		if (output == null || input == null) {
			LogHelper.logError(String.format("Required parameters missing for %s Recipe.", name));
			return;
		}

		for (EmpowererRecipe recipe : ActuallyAdditionsAPI.EMPOWERER_RECIPES) {
			if (matches(output, toIItemStack(recipe.output))){
				if(matches(input, toIItemStack(recipe.input))){
					recipes.add(recipe);
				}
			}
		}

		if (!recipes.isEmpty()) {
			MineTweakerAPI.apply(new Remove(recipes));
		} else {
			LogHelper.logWarning(String.format("No %s Recipe found for output %s. Command ignored!", Empowerer.name,
					output.toString()));
		}

	}

	private static class Remove extends BaseListRemoval<EmpowererRecipe> {
		public Remove(List<EmpowererRecipe> recipes) {
			super(Empowerer.name, ActuallyAdditionsAPI.EMPOWERER_RECIPES, recipes);
		}

		@Override
		protected String getRecipeInfo(EmpowererRecipe recipe) {
			return LogHelper.getStackDescription(recipe.output);
		}
	}
}

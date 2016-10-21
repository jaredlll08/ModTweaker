package modtweaker.mods.actuallyadditions.handlers;

import static modtweaker.helpers.InputHelper.toIItemStack;
import static modtweaker.helpers.InputHelper.toStack;
import static modtweaker.helpers.StackHelper.matches;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.ellpeck.actuallyadditions.api.ActuallyAdditionsAPI;
import de.ellpeck.actuallyadditions.api.recipe.CoffeeIngredient;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import modtweaker.helpers.LogHelper;
import modtweaker.utils.BaseListAddition;
import modtweaker.utils.BaseListRemoval;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.actuallyadditions.Coffee")
public class CoffeeMaker {
	protected static final String name = "Actually Additions Coffee Maker";
	
	@ZenMethod
	public static void addRecipe(IItemStack ingredient, int maxAmplifier, String[] effectStrings, int[] effectDurations) {

		ArrayList<PotionEffect> potionTypeArrayList = new ArrayList<PotionEffect>();
		for(int i=0;i<effectStrings.length;i++){
			potionTypeArrayList.add(new PotionEffect(Potion.getPotionFromResourceLocation(effectStrings[i]),effectDurations[i]));
		}
		
		PotionEffect[] potionTypeArray = new PotionEffect[potionTypeArrayList.size()];
		for(int i=0;i<potionTypeArrayList.size();i++){
			potionTypeArray[i] = potionTypeArrayList.get(i);
		}

		MineTweakerAPI.apply(new Add(new CoffeeIngredient(toStack(ingredient), potionTypeArray, maxAmplifier)));
	}


	private static class Add extends BaseListAddition<CoffeeIngredient> {
		public Add(CoffeeIngredient recipe) {
			super(CoffeeMaker.name, ActuallyAdditionsAPI.COFFEE_MACHINE_INGREDIENTS);

			this.recipes.add(recipe);
		}

		@Override
		public String getRecipeInfo(CoffeeIngredient recipe) {
			return LogHelper.getStackDescription(recipe.ingredient);
		}
	}

	@ZenMethod
	public static void remove(IIngredient input) {
		List<CoffeeIngredient> recipes = new LinkedList<CoffeeIngredient>();

		if (input == null) {
			LogHelper.logError(String.format("Required parameters missing for %s Recipe.", name));
			return;
		}

		for (CoffeeIngredient recipe : ActuallyAdditionsAPI.COFFEE_MACHINE_INGREDIENTS) {
			if (matches(input, toIItemStack(recipe.ingredient)))
				recipes.add(recipe);
		}

		if (!recipes.isEmpty()) {
			MineTweakerAPI.apply(new Remove(recipes));
		} else {
			LogHelper.logWarning(String.format("No %s Recipe found for output %s. Command ignored!", CoffeeMaker.name,
					input.toString()));
		}

	}

	private static class Remove extends BaseListRemoval<CoffeeIngredient> {
		public Remove(List<CoffeeIngredient> recipes) {
			super(CoffeeMaker.name, ActuallyAdditionsAPI.COFFEE_MACHINE_INGREDIENTS, recipes);
		}

		@Override
		protected String getRecipeInfo(CoffeeIngredient recipe) {
			return LogHelper.getStackDescription(recipe.ingredient);
		}
	}
}

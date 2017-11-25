package com.blamejared.compat.botania.handlers;

import static com.blamejared.mtlib.helpers.InputHelper.toIItemStack;
import static com.blamejared.mtlib.helpers.InputHelper.toObject;
import static com.blamejared.mtlib.helpers.InputHelper.toStack;
import static com.blamejared.mtlib.helpers.StackHelper.matches;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.blamejared.ModTweaker;
import com.blamejared.mtlib.helpers.LogHelper;
import com.blamejared.mtlib.utils.BaseListAddition;
import com.blamejared.mtlib.utils.BaseListRemoval;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipeManaInfusion;

@ZenClass("mods.botania.ManaInfusion")
@ModOnly("botania")
@ZenRegister
public class ManaInfusion {

	protected static final String name = "Botania Mana Infusion";

	@ZenMethod
	public static void addInfusion(IItemStack output, IIngredient input, int mana) {
		CraftTweakerAPI.apply(new Add(new RecipeManaInfusion(toStack(output), toObject(input), mana)));
	}

	@ZenMethod
	public static void addAlchemy(IItemStack output, IIngredient input, int mana) {
		RecipeManaInfusion recipe = new RecipeManaInfusion(toStack(output), toObject(input), mana);
		recipe.setCatalyst(RecipeManaInfusion.alchemyState);
		CraftTweakerAPI.apply(new Add(recipe));
	}

	@ZenMethod
	public static void addConjuration(IItemStack output, IIngredient input, int mana) {
		RecipeManaInfusion recipe = new RecipeManaInfusion(toStack(output), toObject(input), mana);
		recipe.setCatalyst(RecipeManaInfusion.conjurationState);
		CraftTweakerAPI.apply(new Add(recipe));
	}

	private static class Add extends BaseListAddition<RecipeManaInfusion> {
		public Add(RecipeManaInfusion recipe) {
			super(ManaInfusion.name, BotaniaAPI.manaInfusionRecipes);
			recipes.add(recipe);
		}

		@Override
		public String getRecipeInfo(RecipeManaInfusion recipe) {
			return LogHelper.getStackDescription(recipe.getOutput());
		}
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@ZenMethod
	public static void removeRecipe(IIngredient output) {
		ModTweaker.LATE_REMOVALS.add(new Remove(output));
	}

	private static class Remove extends BaseListRemoval<RecipeManaInfusion> {

		final IIngredient output;

		public Remove(IIngredient output) {
			super(ManaInfusion.name, BotaniaAPI.manaInfusionRecipes, Collections.emptyList());
			this.output = output;
		}

		@Override
		public String getRecipeInfo(RecipeManaInfusion recipe) {
			return LogHelper.getStackDescription(recipe.getOutput());
		}

		@Override
		public void apply() {
			// Get list of existing recipes, matching with parameter
			List<RecipeManaInfusion> recipes = new LinkedList<RecipeManaInfusion>();

			for (RecipeManaInfusion r : BotaniaAPI.manaInfusionRecipes) {
				if (r.getOutput() != null && matches(output, toIItemStack(r.getOutput()))) {
					recipes.add(r);
				}
			}

			// Check if we found the recipes and apply the action
			if (!recipes.isEmpty()) {
				this.recipes.addAll(recipes);
				super.apply();
			} else {
				LogHelper.logWarning(String.format("No %s Recipe found for %s. Command ignored!", ManaInfusion.name, output.toString()));
			}
			CraftTweakerAPI.getLogger().logInfo(super.describe());
		}

		@Override
		public String describe() {
			return "";
		}
	}
}

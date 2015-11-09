package modtweaker2.mods.railcraft.handlers;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import minetweaker.api.item.WeightedItemStack;
import mods.railcraft.api.crafting.IRockCrusherRecipe;
import modtweaker2.helpers.LogHelper;
import modtweaker2.mods.railcraft.RailcraftHelper;
import modtweaker2.utils.BaseListAddition;
import modtweaker2.utils.BaseListRemoval;
import net.minecraftforge.oredict.OreDictionary;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.LinkedList;
import java.util.List;

import static modtweaker2.helpers.InputHelper.toIItemStack;
import static modtweaker2.helpers.InputHelper.toStack;
import static modtweaker2.helpers.StackHelper.matches;

@ZenClass("mods.railcraft.RockCrusher")
public class RockCrusher {

	public static final String name = "Railcraft Rock Crusher";

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Add a recipe for the Rock Crusher
	 *
	 * @param output        WeightedItemStack Array of outputs
	 * @param input         ItemStack input
	 * @param matchEmptyNBT Set this to true, if you want to match only input items strictly without NBT Data)
	 */
	@ZenMethod
	public static void addRecipe(WeightedItemStack[] output, IItemStack input, @Optional Boolean matchEmptyNBT) {
		if (matchEmptyNBT == null)
			if (toStack(input).stackTagCompound == null)
				matchEmptyNBT = false;
			else
				matchEmptyNBT = true;

		IRockCrusherRecipe recipe;
		if (input.getDamage() == OreDictionary.WILDCARD_VALUE)
			recipe = RailcraftHelper.getRockCrusherRecipe(toStack(input), false, matchEmptyNBT);
		else
			recipe = RailcraftHelper.getRockCrusherRecipe(toStack(input), true, matchEmptyNBT);

		for (int i = 0; i < output.length; i++) {
			recipe.addOutput(toStack(output[i].getStack()), output[i].getChance());
		}

		MineTweakerAPI.apply(new Add(recipe));
	}

	@Deprecated
	@ZenMethod
	public static void addRecipe(IItemStack input, boolean matchDamage, boolean matchNBT, IItemStack[] output, double[] chances) {
		IRockCrusherRecipe recipe = RailcraftHelper.getRockCrusherRecipe(toStack(input), matchDamage, matchNBT);

		for (int i = 0; i < output.length; i++) {
			recipe.addOutput(toStack(output[i]), (float) chances[i]);
		}

		MineTweakerAPI.apply(new Add(recipe));
	}

	private static class Add extends BaseListAddition<IRockCrusherRecipe> {

		@SuppressWarnings("unchecked")
		public Add(IRockCrusherRecipe recipe) {
			super(RockCrusher.name, (List<IRockCrusherRecipe>) RailcraftHelper.crusher);
			recipes.add(recipe);
		}

		@Override
		public String getRecipeInfo(IRockCrusherRecipe recipe) {
			return LogHelper.getStackDescription(recipe.getInput());
		}
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@ZenMethod
	public static void removeRecipe(IIngredient input) {
		List<IRockCrusherRecipe> recipes = new LinkedList<IRockCrusherRecipe>();

		for (IRockCrusherRecipe r : RailcraftHelper.crusher) {
			if (r.getInput() != null && matches(input, toIItemStack(r.getInput()))) {
				recipes.add(r);
			}
		}

		if (!recipes.isEmpty()) {
			MineTweakerAPI.apply(new Remove(recipes));
		} else {
			LogHelper.logWarning(String.format("No %s Recipe found for %s. Command ignored!", RockCrusher.name, input.toString()));
		}
	}

	private static class Remove extends BaseListRemoval<IRockCrusherRecipe> {

		@SuppressWarnings("unchecked")
		public Remove(List<IRockCrusherRecipe> recipes) {
			super("Rock Crusher", (List<IRockCrusherRecipe>) RailcraftHelper.crusher, recipes);
		}

		@Override
		public String getRecipeInfo(IRockCrusherRecipe recipe) {
			return LogHelper.getStackDescription(recipe.getInput());
		}
	}
}

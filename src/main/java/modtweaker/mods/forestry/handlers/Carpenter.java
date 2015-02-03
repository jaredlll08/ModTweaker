package modtweaker.mods.forestry.handlers;

import static modtweaker.helpers.InputHelper.toStack;
import static modtweaker.helpers.InputHelper.toFluid;
import static modtweaker.helpers.InputHelper.toStacks;

import java.util.ArrayList;
import java.util.List;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import minetweaker.api.liquid.ILiquidStack;
import minetweaker.api.recipes.ShapedRecipe;
import minetweaker.mc1710.recipes.RecipeConverter;
import modtweaker.util.BaseListAddition;
import modtweaker.util.BaseListRemoval;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import forestry.Forestry;
import forestry.api.core.ForestryAPI;
import forestry.api.recipes.RecipeManagers;
import forestry.core.utils.RecipeUtil;
import forestry.core.utils.ShapedRecipeCustom;
import forestry.factory.gadgets.MachineCarpenter;
import forestry.factory.gadgets.MachineCarpenter.Recipe;
import forestry.factory.gadgets.MachineCarpenter.RecipeManager;
import modtweaker.mods.forestry.ForestryHelper;

@ZenClass("mods.forestry.Carpenter")
public class Carpenter {

	@ZenMethod
	public static void addRecipe(int packagingTime, ILiquidStack liquid, IItemStack[] ingredients, IItemStack ingredient, IItemStack product) {
		ArrayList<ItemStack> stacks = new ArrayList<ItemStack>();
		for (ItemStack stack : toStacks(ingredients)) {
			if (stack != null) {
				stacks.add(stack);
			}
			if (stack == null) {
				stacks.add(new ItemStack(Blocks.air));
			}

		}
		MineTweakerAPI.apply(new Add(new Recipe(packagingTime, toFluid(liquid), toStack(ingredient), new ShapedRecipeCustom(3, 3, toStacks(ingredients), toStack(product)))));
	}

	public ShapedRecipeCustom convertToRecipeCustom() {

		return null;
	}

	private static class Add extends BaseListAddition {

		public Add(Recipe recipe) {
			super("Forestry Carpenter", MachineCarpenter.RecipeManager.recipes, recipe);

			// The Carpenter has a list of valid Fluids, access them via
			// Relfection because of private
			if (recipe.getLiquid() != null)
				ForestryHelper.addCarpenterRecipeFluids(recipe.getLiquid().getFluid());

			if(!RecipeManager.isBox(recipe.getBox())){
				ForestryHelper.addCarpenterRecipeBox(recipe.getBox());
			}
		}

		public void apply() {
			Recipe r = (MachineCarpenter.Recipe)recipe;
		}

	}

	@ZenMethod
	public static void removeRecipe(IItemStack output) {
		MineTweakerAPI.apply(new Remove(MachineCarpenter.RecipeManager.recipes, toStack(output)));
	}

	private static class Remove extends BaseListRemoval {

		public Remove(List list, ItemStack stack) {
			super("Forestry Carpenter", list, stack);

		}

		@Override
		public void apply() {
			for (Recipe r : RecipeManager.recipes) {
				if (r.getCraftingResult() != null && r.getCraftingResult().isItemEqual(stack)) {
					recipe = r;
					break;
				}
			}
			RecipeManager.recipes.remove(recipe);
		}

	}
}
